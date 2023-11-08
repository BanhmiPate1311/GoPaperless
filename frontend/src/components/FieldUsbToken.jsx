import { Box, Stack } from "@mui/material";
import React, { useEffect, useRef, useState } from "react";
// import { useTranslation } from "react-i18next";
import moment from "moment";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import imageLoading from "../assets/images/ajax-loader.gif";
import ISPluginClient from "../assets/js/checkid";
import "../assets/styles/usbtoken.css";
import { api } from "../constants/api";
import {
  apiControllerManagerActions,
  useApiControllerManager,
} from "../store/apiControllerManager";
import ButtonField from "./form/button_field";
import UsbModalField2 from "./usbtoken/usb_modal2_field";
import UsbModalField from "./usbtoken/usb_modal_field";
import { isPluginService } from "../services/isPluginService";

const FieldUsbToken = ({ isCardChecked, connectorName, workFlow, swError }) => {
  // console.log("workFlow: ", workFlow);
  const dispatch = useDispatch();
  const { t } = useTranslation();

  const { signaturePrepare } = useApiControllerManager();
  console.log("signaturePrepare: ", signaturePrepare);
  // const signer = workFlow?.participants?.find(
  //   (item) => item.signerToken === workFlow.signerToken
  // );

  // const signature = signaturePrepare.find(
  //   (item) => item.field_name === signer.signerId
  // );
  // console.log("signature: ", signature);

  let lang = localStorage.getItem("language");
  switch (lang) {
    case "Vietnamese":
      lang = "VN";
      break;
    default:
      lang = "EN";
      break;
  }

  const signingTokenList = [];
  const signerTokenList = [];
  const fileNameList = [];
  const workFlowIdList = [];
  const enterpriseIdList = [];
  const signerIdList = [];
  const documentIdList = [];
  const lastFileIdList = [];
  const signatureList = [];
  console.log("signatureList: ", signatureList);

  if (Array.isArray(workFlow) && workFlow.length > 0) {
    let signerId = null;
    for (let i = 0; i < workFlow.length; i++) {
      signingTokenList.push(workFlow[i].signingToken);
      signerTokenList.push(workFlow[i].signerToken);
      fileNameList.push(workFlow[i].fileName);
      workFlowIdList.push(workFlow[i].workFlowId);
      enterpriseIdList.push(workFlow[i].enterpriseId);
      documentIdList.push(workFlow[i].documentId);
      lastFileIdList.push(workFlow[i].lastFileId);

      const signer = workFlow[i].participants.find(
        (item) => item.signerToken === workFlow[i].signerToken
      );
      signerId = signer.signerId;
      signerIdList.push(signerId);

      const signature = signaturePrepare.find(
        (item) =>
          item.field_name === signer.signerId &&
          item.workFlowId === workFlow[i].workFlowId
      );
      signatureList.push(signature);
    }
  } else {
    signingTokenList.push(workFlow?.signingToken);
    signerTokenList.push(workFlow?.signerToken);
    fileNameList.push(workFlow?.fileName);
    workFlowIdList.push(workFlow.workFlowId);
    enterpriseIdList.push(workFlow.enterpriseId);
    documentIdList.push(workFlow.documentId);
    lastFileIdList.push(workFlow.lastFileId);

    const signer = workFlow?.participants.find(
      (item) => item.signerToken === workFlow?.signerToken
    );
    let signerId = signer.signerId;
    signerIdList.push(signerId);

    const signature = signaturePrepare.find(
      (item) => item.field_name === signer.signerId
    );
    signatureList.push(signature);
  }

  const currentURL = window.location.href;
  const url = new URL(currentURL);
  const urlWithoutProtocol = url.origin.replace(/^(https?:\/\/)/, "");

  const [dllUSB, setdllUSB] = useState([]);

  const getDLL = async (connectorName) => {
    try {
      const response = await api.post("/getDLLUSBToken", {
        connector: connectorName,
      });
      setdllUSB(response.data);
    } catch (error) {
      setdllUSB([]);
    }
  };

  useEffect(() => {
    if (connectorName) getDLL(connectorName);
  }, [connectorName]);

  let flagFailedConnectHTML = 0;
  let connectSuccessfully = false;
  const sdk = useRef(null);
  const [content1, setContent1] = useState([]);
  const [minLength, setMinLength] = useState(0);
  const [maxLength, setMaxLength] = useState(255);
  const [showAlert, setShowAlert] = useState(false);

  const [cerSelected, setCerSelected] = useState(0);

  const handleTokenSelected = (data) => {
    setCerSelected(data);
  };

  function getTokenCertificate() {
    console.log("getTokenCertificate");
    sdk.current.getTokenCertificate(
      60,
      dllUSB,
      urlWithoutProtocol,
      // "uat-paperless-gw.mobile-id.vn",
      connectorName,
      workFlowIdList,
      enterpriseIdList,
      lang,
      function (response) {
        const currentMoment = moment();
        // console.log("response: ", currentMoment);
        setMinLength(response.tokenDetails.minPinLength);
        setMaxLength(response.tokenDetails.maxPinLength);
        const newCertList = response.signingCertificates.filter((cer) =>
          moment(cer.validTo, "YYYY-MM-DD HH:mm:ss").isAfter(currentMoment)
        );
        if (newCertList.length === 0) {
          setErrorPG(t("smartID.error3"));
        } else {
          setContent1(newCertList);
        }
      },
      function (error, mess) {
        console.log("error: ", error);
        console.log("mess: ", mess);
        setContent1([]);
        let title = "";
        // {translate("smartID.NoCertificateFound")}

        switch (mess) {
          case "USB Token could not be initialized":
            title = "USB Token could not be initialized";
            break;
          default:
            title = mess;
            break;
        }
        setErrorPG(title);
      },
      function () {
        console.log("timeout");
        sdk.current = null;
      }
    );
  }

  function disconnectWSHTML() {
    flagFailedConnectHTML = 0;
    console.log("sdk", sdk);
    sdk.current.shutdown();
  }

  const handleClickOpen = (scrollType) => () => {
    setCerSelected(0);
    setOpenModal1(true);
    setScroll(scrollType);
    flagFailedConnectHTML = 1;
    const ipWS = "127.0.0.1";
    const portWS = "9505";
    const typeOfWS = "wss";

    sdk.current = new ISPluginClient(
      ipWS,
      portWS,
      typeOfWS,
      function () {
        console.log("connected");
        //            socket onopen => connected
        getTokenCertificate();
        flagFailedConnectHTML = 1;
      },
      function () {
        //            socket disconnected
        console.log("Connect error");
      },
      function () {
        //            socket stopped
        flagFailedConnectHTML = 0;

        connectSuccessfully = false;
      },
      function (statusCallBack) {
        console.log("connected denied");
        disconnectWSHTML();
      },
      function (cmd, id, error, data) {
        //RECEIVE
        console.log("cmd: " + cmd);
      }
    );
  };

  const handleCloseModal1 = () => {
    setContent1([]);
    setErrorPG(null);
    flagFailedConnectHTML = 0;
    setOpenModal1(false);
    if (sdk) {
      disconnectWSHTML();
    }
  };

  const handleModal1Submit = () => {
    setOpenModal1(false);
    setOpenModal2(true);
  };

  const [errorPG, setErrorPG] = useState(null);

  const pinValue = useRef(null);

  const [isChecking, setIsChecking] = useState(false);

  const handleModal2Submit = async (cer) => {
    dispatch(apiControllerManagerActions.clearsetMessageSuccess());
    setIsChecking(true);
    setErrorPG(null);
    try {
      const response = [];
      const signObjects = [];
      const signatureId = [];
      const hashList = [];
      for (let i = 0; i < signingTokenList.length; i++) {
        // const formData = new FormData(); // Tạo FormData mới trong mỗi vòng lặp
        // formData.append("signingToken", signingTokenList[i]);
        // formData.append("cerId", cer.id); // credentialID
        // formData.append("signerId", signerIdList[i]);
        // formData.append("signerToken", signerTokenList[i]);
        // formData.append("signingOption", "usbtoken");
        // formData.append("certEncode", cer.value); // certChain
        // formData.append("signName", cer.subject.commonName);
        // formData.append("connectorName", connectorName);
        // formData.append("documentId", workFlow.documentId);

        // response[i] = await api.post("/is/getHashFile2", formData, {
        //   headers: {
        //     "Content-Type": "multipart/form-data",
        //   },
        // });
        const data = {
          fieldName: signatureList[i] ? signatureList[i].field_name : "",
          signerToken: signerTokenList[i],
          connectorName,
          documentId: documentIdList[i],
          certChain: cer.value,
          signingToken: signingTokenList[i],
          credentialID: cer.id,
          signerId: signerIdList[i],
        };

        response[i] = await isPluginService.getHash(data);
        console.log("response[i]: ", response[i]);
        const temp = {};
        temp.dtbsHash = response[i].data.hashPG;
        temp.algorithm = "SHA256";
        hashList.push(response[i].data.hash);
        signatureId.push(response[i].data.signatureId);
        signObjects.push(temp);
      }

      sdk.current.signTokenCertificate(
        cer.id,
        pinValue.current,
        signObjects,
        60,
        lang,
        async function (response) {
          handleCloseModal2();
          // setShowModal3(true);

          for (let i = 0; i < signingTokenList.length; i++) {
            // const formData = new FormData(); // Tạo FormData mới trong mỗi vòng lặp
            // formData.append("signingToken", signingTokenList[i]);
            // formData.append("cerId", cer.id);
            // formData.append("signerId", signerIdList[i]);
            // formData.append("certEncode", cer.value);
            // formData.append("signatures", response.signatures[i]);
            // formData.append("signatureId", signatureId[i]);
            // formData.append("fileName", fileNameList[i]);
            // formData.append("connectorName", connectorName);
            // formData.append("serialNumber", cer.subject.serialNumber);
            // formData.append("signingOption", "usbtoken");
            // formData.append("signerToken", signerTokenList[i]);
            // formData.append("enterpriseId", enterpriseIdList[i]);
            // formData.append("workFlowId", workFlowIdList[i]);

            const data = {
              fieldName: signatureList[i] ? signatureList[i].field_name : "",
              signature: response.signatures[i],
              hashList: hashList[i],
              documentId: documentIdList[i],
              signingToken: signingTokenList[i],
              signerToken: signerTokenList[i],
              certChain: cer.value,
              enterpriseId: enterpriseIdList[i],
              workFlowId: workFlowIdList[i],
              signerId: signerIdList[i],
              fileName: fileNameList[i],
              signatureId: signatureId[i],
              lastFileId: lastFileIdList[i],
              serialNumber: cer.subject.serialNumber,
            };

            await isPluginService
              .signUsbTokenFps(data)
              .then((response) => {
                window.parent.postMessage(
                  { data: response.data, status: "Success" },
                  "*"
                );
                if (i === signingTokenList.length - 1) {
                  dispatch(apiControllerManagerActions.setMessageSuccess());
                }
              })
              .catch((error) => {
                let title = "";
                console.log("error?.response?.data: ", error?.response?.data);
                const errorData = error?.response?.data;

                switch (errorData) {
                  case "EXPIRED":
                    title = t("smartID.error1");
                    break;
                  case "Read timed out":
                    title = t("smartID.error2");
                    break;
                  case "not available":
                    title = t("smartID.error3");
                    break;
                  default:
                    title =
                      t("smartID.signingFail") +
                      (errorData?.toUpperCase() || "");
                    break;
                }
                setShowAlert(title);
                window.parent.postMessage(
                  { data: { title }, status: "Error" },
                  "*"
                );
              });
          }
          // setShowModal3(false);
          setIsChecking(false);
        },
        function (error, mess) {
          console.log("error: ", error);
          console.log("mess: ", mess);
          setIsChecking(false);
          setErrorPG(mess);
        },
        function () {
          console.log("timeout");
        }
      );
    } catch (error) {
      console.log("error: ", error);
    }
  };
  const handleCloseModal2 = () => {
    flagFailedConnectHTML = 0;
    setContent1([]);
    setErrorPG(null);
    setOpenModal2(false);
    if (sdk) {
      disconnectWSHTML();
    }
  };

  function checkError(errorPG) {
    if (errorPG === "USB Token could not be initialized") {
      return t("smartID.NoCertificateFound");
    }
  }

  const [openModal1, setOpenModal1] = useState(false);
  const [openModal2, setOpenModal2] = useState(false);
  const [scroll, setScroll] = useState("paper");

  return (
    <div>
      {isChecking && (
        <div
          className="modal backdrop fade show d-flex justify-content-center align-items-center"
          style={{ background: "#00000080" }}
        >
          <div className="loader" />
        </div>
      )}
      <div>
        {/* {showAlert && (
          <Grid item style={{ marginLeft: "-25px", marginBottom: "20px" }}>
            <Alert severity="error" onClose={handleAlertClose}>
              {t("usb.remind")}
            </Alert>
          </Grid>
        )} */}
        {openModal1 && (
          <p>
            {t("usb.usb1")}{" "}
            <img style={{ width: "20px" }} src={imageLoading} alt="loading" />
          </p>
        )}
        {openModal2 && (
          <p>
            {t("usb.usb2")}{" "}
            <img style={{ width: "20px" }} src={imageLoading} alt="loading" />
          </p>
        )}
      </div>
      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={4}>
        <Box width={300}>
          <ButtonField
            handleButtonClick={handleClickOpen("paper")}
            disabled={isCardChecked === null || swError !== null}
            text={t("usb.sign")}
          />
        </Box>
      </Stack>

      <UsbModalField
        title={t("usb.usb3")}
        subtitle={t("usb.usb4")}
        open={openModal1}
        scroll={scroll}
        errorPG={errorPG}
        handleClose={handleCloseModal1}
        urlWithoutProtocol={urlWithoutProtocol}
        data={content1}
        handleModalClick={handleModal1Submit}
        handleModalChange={handleTokenSelected}
        disabled={content1.length === 0}
        value={cerSelected}
      />

      {content1.length !== 0 && (
        <UsbModalField2
          title={t("usb.usb7")}
          subtitle={t("usb.usb8")}
          open={openModal2}
          errorPG={errorPG}
          handleClose={handleCloseModal2}
          urlWithoutProtocol={urlWithoutProtocol}
          data={content1[cerSelected]}
          handleModalClick={handleModal2Submit}
          disabled={isChecking}
          minLength={minLength}
          maxLength={maxLength}
          value={pinValue}
        />
      )}
    </div>
  );
};

export default FieldUsbToken;
