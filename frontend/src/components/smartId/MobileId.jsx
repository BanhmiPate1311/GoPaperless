import { Alert, Box, Stack } from "@mui/material";
import axios from "axios";
import React, { Fragment, memo, useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { v4 as uuidv4 } from "uuid";
import "../../assets/styles/fieldSmartId.css";
import { api } from "../../constants/api";
import { smartIdService } from "../../services/smartid";
import {
  apiControllerManagerActions,
  useApiControllerManager,
} from "../../store/apiControllerManager";
import ButtonField from "../form/button_field";
import ComboBoxField from "../form/combo_box_field";
import InputField from "../form/input_field";
import PhoneInputField from "../form/phone_input_field";
import Notice from "./Notice";
import ModalField from "./modal_field";
import { getSignature, getSignerId } from "../../ultis/commonFunction";

const MobileId = ({ isCardChecked, connectorName, workFlow }) => {
  const { t } = useTranslation();
  const dispatch = useDispatch();

  const signerId = getSignerId(workFlow);

  const { signaturePrepare } = useApiControllerManager();
  const signature = getSignature(
    signaturePrepare,
    signerId,
    workFlow.workFlowId
  );
  console.log("signature: ", signature);

  const [isFetching, setIsFecthing] = useState(false);
  const [prefix, setPrefix] = useState([]);

  // const getPrefix = async () => {
  //   const response = await api.post("/getPrefix", { lang: lang });
  //   if (response.data) {
  //     setPrefix(response.data);
  //   }
  // };
  // const getPrefixPhone = async () => {
  //   const response = await api.post("/getPrefixPhone", { lang: lang });
  //   if (response.data) {
  //     setPrefixPhone(response.data);
  //   }
  // };

  const [showPersonalCode, setShowPersonalCode] = useState(false);
  const [phoneNumber, setPhoneNumber] = useState("");
  const [personalCode, setPersonalCode] = useState("");
  const [checkLength, setCheckLength] = useState(false);
  //combobox
  const [value, setValue] = useState("PHONE");

  const checkParam = (value, condition) => {
    if (value.length >= condition) {
      setCheckLength(true);
    } else {
      setCheckLength(false);
    }
  };

  const handleInputChange = (value) => {
    setPersonalCode(value);
    checkParam(value, 12);
  };

  const enterToSubmit = (event) => {
    if (checkLength && event.key === "Enter") {
      console.log("abc");
      handleClickOpen("paper");
    }
  };

  useEffect(() => {
    setPersonalCode("");
    setPhoneNumber("");
  }, [isCardChecked, connectorName]);

  let lang = localStorage.getItem("language");
  switch (lang) {
    case "Vietnamese":
      lang = "VN";
      break;
    default:
      lang = "EN";
      break;
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        const responsePrefixPhone = await api.post("/getPrefixPhone", {
          lang: lang,
        });
        const responsePrefix = await api.post("/getPrefix", { lang: lang });

        if (responsePrefix.data && responsePrefixPhone.data) {
          const merged = [...responsePrefixPhone.data, ...responsePrefix.data];
          setPrefix(merged);
        }
      } catch (error) {
        // Xử lý lỗi nếu có
        console.error("Error fetching data:", error);
      }
    };

    fetchData(); // Gọi hàm fetchData để lấy dữ liệu
  }, [lang]);

  const [signerID, setSignerID] = useState(null);
  useEffect(() => {
    const signerId = workFlow.participants.find(
      (item) => item.signerToken === workFlow.signerToken
    )?.signerId;
    setSignerID(signerId);
  }, [workFlow]);

  const phonePrefix = useRef("");
  const handlePhoneNumber = (phone, country) => {
    setPhoneNumber(phone);
    phonePrefix.current = country.dialCode;
    checkParam(phone, 11);
  };

  useEffect(() => {
    // When showPersonalCode changes, reset personalCode to empty string
    setPersonalCode("");
    setPhoneNumber("");
  }, [showPersonalCode]);

  //filed Search crietia

  const handleMenuItemClick = (event) => {
    setValue(event.target.value);
    if (event.target.value === "PHONE") {
      setShowPersonalCode(false);
    } else {
      setShowPersonalCode(true);
    }
  };

  // click on cancel
  const [signFileController, setSignFileController] = useState(
    new AbortController()
  );
  const [getVCController, setGetVCController] = useState(new AbortController());
  const handleCancelSign = () => {
    signFileController.abort();
    getVCController.abort();
    setSignFileController(new AbortController());
    setGetVCController(new AbortController());
    setIsFecthing(false);
  };

  const [codeVC, setCodeVC] = useState(null);
  const [messageError, setMessageError] = useState("");

  useEffect(() => {
    if (codeVC || isFetching) {
      const elements = document.querySelectorAll("#navTab, .cardTab");
      elements.forEach((element) => {
        element.style.pointerEvents = "none";
        element.style.cursor = "auto";
      });
    } else {
      const elements = document.querySelectorAll("#navTab, .cardTab");
      elements.forEach((element) => {
        element.style.pointerEvents = "all";
        element.style.cursor = "auto";
      });
    }
  }, [codeVC, isFetching]);

  // handle VC code
  const [VCEnabled, setVCEnabled] = useState(false);

  useEffect(() => {
    if (connectorName) {
      getVCEnabled();
    }
  }, [connectorName]);

  const getVCEnabled = async () => {
    const response = await api.post("/getVCEnabled", {
      connectorName: connectorName,
    });
    setVCEnabled(response.data);
    // setVCEnabled(false);
  };

  const currentURL = window.location.href;
  const url = new URL(currentURL);
  const urlWithoutProtocol = url.origin.replace(/^(https?:\/\/)/, "");

  const [content, setContent] = useState({});
  const [errorPG, setErrorPG] = useState(null);

  const [cerSelected, setCerSelected] = useState(0);

  const handleTokenSelected = (index) => {
    setCerSelected(index);
  };

  const getCertificate = async () => {
    setIsFecthing(true);
    try {
      let codeNumber = "";
      if (phoneNumber) {
        codeNumber =
          value + ":" + phoneNumber.replace(phonePrefix.current, "0").trim();
      }
      if (personalCode) {
        codeNumber = value + ":" + personalCode.trim();
      }
      // const formData = new FormData();
      // formData.append("lang", lang);
      // formData.append("connectorName", connectorName);
      // formData.append("codeNumber", codeNumber);
      // formData.append("enterpriseId", workFlow.enterpriseId);
      // formData.append("workFlowId", workFlow.workFlowId);
      const data = {
        lang: lang,
        connectorName: connectorName,
        codeNumber: codeNumber,
        enterpriseId: workFlow.enterpriseId,
        workFlowId: workFlow.workFlowId,
      };
      const response = await smartIdService.getCertificate(data);
      // setShowModal1(true);
      setContent(response.data);
      // setIsFecthing(false);
      if (response.data.listCertificate.length === 0) {
        setErrorPG(t("smartID.error3"));
      }
      setOpen(true);
    } catch (error) {
      // Xử lý lỗi ở đây
      console.error(
        "Lỗi khi gửi yêu cầu lấy chứng thư:",
        error.response.data.message
      );
      let title = "";

      const errorData = error?.response?.data.message;

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
          title = t("smartID.signingFail") + (errorData?.toUpperCase() || "");
          break;
      }
      setIsFecthing(false);
      setMessageError(title);
    }
  };

  const sign = () => {
    setOpen(false);
    // setSignTimeout(302);
    setMessageError("");
    setCodeVC(" ");
    dispatch(apiControllerManagerActions.clearsetMessageSuccess());

    dispatch(apiControllerManagerActions.clearsetMessageError());
    const requestID = uuidv4();
    let codeNumber = "";
    if (phoneNumber) {
      codeNumber =
        value + ":" + phoneNumber.replace(phonePrefix.current, "0").trim();
    }
    if (personalCode) {
      codeNumber = value + ":" + personalCode.trim();
    }

    const data = {
      lang,
      requestID,
      signingToken: workFlow.signingToken,
      fileName: workFlow.fileName,
      signerToken: workFlow.signerToken,
      connectorName,
      signingOption: "smartid",
      codeNumber,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
      credentialID: content.listCertificate[cerSelected].credentialID,
      signerId: signerID,
      certChain: content.listCertificate[cerSelected].cert,
      prefixCode: content.prefixCode,
      relyingParty: content.relyingParty,
      codeEnable: content.codeEnable,
      fieldName: signature ? signature.field_name : "",
      type: value,
      documentId: workFlow.documentId,
      lastFileId: workFlow.lastFileId,
    };
    // const formData = new FormData();
    // formData.append("lang", lang);
    // formData.append("requestID", requestID);
    // formData.append("signingToken", workFlow.signingToken);
    // formData.append("filename", workFlow.fileName);
    // formData.append("signerToken", workFlow.signerToken);
    // formData.append("connectorName", connectorName);
    // formData.append("signingOption", "smartid");
    // formData.append("codeNumber", codeNumber);
    // formData.append("enterpriseId", workFlow.enterpriseId);
    // formData.append("workFlowId", workFlow.workFlowId);
    // formData.append(
    //   "credentialID",
    //   content.listCertificate[cerSelected].credentialID
    // );
    // formData.append("signerId", signerID);
    // formData.append("certChain", content.listCertificate[cerSelected].cert);
    // formData.append("prefixCode", content.prefixCode);
    // formData.append("relyingParty", content.relyingParty);
    // formData.append("codeEnable", content.codeEnable);
    // formData.append(
    //   "field_name",
    //   signature?.field_name ? signature?.field_name : ""
    // );
    // formData.append("type", value);
    try {
      smartIdService
        .sign(data, signFileController.signal)
        .then((response) => {
          // const messageHome = {
          //   message: "Document signed successfully.",
          //   type: "success",
          // };
          // sessionStorage.setItem("messageHome", JSON.stringify(messageHome));

          // window.location.reload();
          window.parent.postMessage(
            { data: response.data, status: "Success" },
            "*"
          );
          dispatch(apiControllerManagerActions.setMessageSuccess());
        })
        .catch((error) => {
          setIsFecthing(false);
          console.log("error: ", error);
          setCodeVC(null);
          const errorData = error?.response?.data.message;
          const elements = document.querySelectorAll("#navTab, .cardTab");
          elements.forEach((element) => {
            element.style.pointerEvents = "all";
            element.style.cursor = "pointer";
          });

          if (axios.isCancel(error)) {
            //handle cancel
          } else {
            let title =
              t("smartID.signingFail") + (errorData?.toUpperCase() || "");
            setMessageError(title);
          }
        })

        .finally(() => {});
      api
        .get("/getVC?requestID=" + requestID, {
          signal: getVCController.signal,
        })
        .then((response) => {
          setCodeVC(response.data);
        })
        .catch((error) => {
          setCodeVC(null);
          setIsFecthing(false);
          if (error?.response?.data) {
            let title = "GET VC FAILED: " + error?.response?.data;
            setMessageError(title);
            window.parent.postMessage(
              { data: { title }, status: "Error" },
              "*"
            );
          }
        });
    } catch (error) {
      console.log(error);
    }
  };

  const [open, setOpen] = useState(false);
  const [scroll, setScroll] = useState("paper");

  const handleClickOpen = (scrollType) => {
    setMessageError("");
    setScroll(scrollType);
    getCertificate();
  };

  const handleClose = () => {
    setOpen(false);
    setIsFecthing(false);
    setContent({});
    setErrorPG(null);
  };

  return (
    <Fragment>
      {Object.keys(content).length !== 0 && (
        <ModalField
          title={t("usb.usb3")}
          subtitle={t("usb.usb4")}
          open={open}
          scroll={scroll}
          errorPG={errorPG}
          handleClose={handleClose}
          urlWithoutProtocol={urlWithoutProtocol}
          data={content?.listCertificate}
          handleModalClick={sign}
          handleModalChange={handleTokenSelected}
          disabled={content?.listCertificate?.length === 0}
          value={cerSelected}
        />
      )}

      {messageError && (
        <Alert
          style={{
            color: "black",
            background: "#F4C7C7",
          }}
          severity="error"
          // onClose={() => setMessageError("")}
        >
          {messageError}
        </Alert>
      )}

      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={2}>
        <Box width={300}>
          {showPersonalCode && (
            <InputField
              label={t("smartID.code")}
              number={personalCode}
              handleInputChange={handleInputChange}
              enterToSubmit={enterToSubmit}
              disabled={isFetching}
              maxLength={12}
            />
          )}

          {!showPersonalCode && (
            <PhoneInputField
              label={t("smartID.phoneNumber")}
              phoneNumber={phoneNumber}
              setPhoneNumber={setPhoneNumber}
              handlePhoneNumber={handlePhoneNumber}
              enterToSubmit={enterToSubmit}
              disabled={isFetching}
            />
          )}
        </Box>

        {prefix.length !== 0 && (
          <Box width={300}>
            <ComboBoxField
              data={prefix}
              value={value}
              handleMenuItemChange={handleMenuItemClick}
              disabled={isFetching}
              valueExtractorValue={(item) => item.prefix}
              valueExtractorText={(item) => item.remark}
            />
          </Box>
        )}
      </Stack>

      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={4}>
        <Box width={300}>
          <ButtonField
            handleButtonClick={() => handleClickOpen("paper")}
            disabled={
              (!phoneNumber && !personalCode) || !checkLength || isFetching
            }
            text={t("smartID.sign")}
            loading={isFetching}
          />
        </Box>
        {codeVC && (
          <Notice
            timeout={302}
            codeVC={codeVC}
            handleCancelSign={handleCancelSign}
            VCEnabled={VCEnabled}
          />
        )}
      </Stack>
    </Fragment>
  );
};

export default memo(MobileId);
