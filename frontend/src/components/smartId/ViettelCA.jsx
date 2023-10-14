import { Alert, Box, Stack } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { api } from "../../constants/api";
import { apiControllerManagerActions } from "../../store/apiControllerManager";
import ButtonField from "../form/button_field";
import InputField from "../form/input_field";
import Notice from "./Notice";
import ViettelModalField from "./viettel_modal_field";

const ViettelCA = ({ isCardChecked, connectorName, workFlow }) => {
  const { t } = useTranslation();

  const dispatch = useDispatch();

  const [isFetching, setIsFecthing] = useState(false);

  const [signTimeout, setSignTimeout] = useState(180);

  const [messageError, setMessageError] = useState("");

  useEffect(() => {
    if (!isFetching) return;
    if (signTimeout <= 0 && !isFetching) return;
    if (signTimeout <= 0) {
      // alert("Signing timeout")
      window.location.reload();
    }
    const timeout = setTimeout(() => {
      setSignTimeout(signTimeout - 1);
    }, 1000);

    return () => clearTimeout(timeout);
  }, [signTimeout, isFetching]);

  useEffect(() => {
    if (isFetching) {
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
  }, [isFetching]);

  const [signFileController, setSignFileController] = useState(
    new AbortController()
  );

  const handleCancelSign = () => {
    signFileController.abort();
    setSignFileController(new AbortController());
  };

  const currentURL = window.location.href;
  const url = new URL(currentURL);
  const urlWithoutProtocol = url.origin.replace(/^(https?:\/\/)/, "");

  const [userId, setUserId] = useState("");

  const [content, setContent] = useState([]);
  const [errorPG, setErrorPG] = useState(null);

  const [cerSelected, setCerSelected] = useState(0);

  const handleTokenSelected = (data) => {
    console.log("cerSelected: ", data);
    setCerSelected(data);
  };

  const [signerID, setSignerID] = useState(null);
  useEffect(() => {
    const signerId = workFlow.participants.find(
      (item) => item.signerToken === workFlow.signerToken
    )?.signerId;
    setSignerID(signerId);
  }, [workFlow]);

  const getCertificate = async () => {
    try {
      setMessageError("");
      setIsFecthing(true);
      dispatch(apiControllerManagerActions.clearsetMessageError());
      const formData = new FormData();
      formData.append("userId", userId);
      formData.append("connectorName", connectorName);
      formData.append("enterpriseId", workFlow.enterpriseId);
      formData.append("workFlowId", workFlow.workFlowId);
      const response = await api.post("/viettel-ca/getCertificate", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      // setIsFecthing(false);
      setOpen(true);
      setContent(response.data);
    } catch (error) {
      // Xử lý lỗi ở đây
      setIsFecthing(false);
      console.error(
        "Lỗi khi gửi yêu cầu lấy chứng thư:",
        error.response.data.message
      );
      setMessageError(error.response.data.message);
    }
  };

  const signHash = async () => {
    try {
      setSignTimeout(181);
      setOpen(false);
      setIsFecthing(true);
      const formData = new FormData();
      formData.append("credentialID", content.data[cerSelected].credential_id);
      formData.append("signingToken", workFlow.signingToken);
      formData.append("signerToken", workFlow.signerToken);
      formData.append("signerId", signerID);
      formData.append(
        "certChain",
        content.data[cerSelected].cert.certificates[0]
      );
      formData.append("connectorName", connectorName);
      formData.append("enterpriseId", workFlow.enterpriseId);
      formData.append("workFlowId", workFlow.workFlowId);
      formData.append("accessToken", content.access_token);
      formData.append("fileName", workFlow.fileName);
      formData.append(
        "serialNumber",
        content.data[cerSelected].cert.serialNumber
      );
      formData.append("signingOption", "smartid");
      const response = await api.post("/viettel-ca/signHash", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
        signal: signFileController.signal,
      });
      window.parent.postMessage(
        { data: response.data, status: "Success" },
        "*"
      );
      dispatch(apiControllerManagerActions.setMessageSuccess());
    } catch (error) {
      // Xử lý lỗi ở đây
      console.log("error: ", error?.response?.data.message);
      setIsFecthing(false);
      const elements = document.querySelectorAll("#navTab, .cardTab");
      elements.forEach((element) => {
        element.style.pointerEvents = "all";
        element.style.cursor = "pointer";
      });
      if (axios.isCancel(error)) {
        //handle cancel
      } else {
        const errorData = error?.response?.data.message;
        let title = t("smartID.signingFail") + (errorData?.toUpperCase() || "");
        setMessageError(title);
      }
    }
  };

  let lang = localStorage.getItem("language");
  switch (lang) {
    case "Vietnamese":
      lang = "VN";
      break;
    default:
      lang = "EN";
      break;
  }

  const handleInputChange = (value) => {
    setUserId(value);
  };

  const enterToSubmit = (event) => {
    if (event.key === "Enter") {
      getCertificate();
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
    setContent([]);
    setErrorPG(null);
  };
  return (
    <div>
      {messageError && (
        <Alert
          style={{
            color: "black",
            background: "#F4C7C7",
          }}
          severity="error"
          onClose={() => setMessageError("")}
        >
          {messageError.toUpperCase()}
        </Alert>
      )}
      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={2}>
        <Box width={300}>
          <InputField
            label={t("smartID.code")}
            number={userId}
            handleInputChange={handleInputChange}
            enterToSubmit={enterToSubmit}
            disabled={!isCardChecked || isFetching === true}
          />
        </Box>
      </Stack>
      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={4}>
        <Box width={300}>
          <ButtonField
            handleButtonClick={() => handleClickOpen("paper")}
            disabled={!userId || isFetching === true}
            text={t("usb.sign")}
            loading={isFetching}
          />
        </Box>
        {isFetching && (
          <Notice codeVC={isFetching} handleCancelSign={handleCancelSign} />
        )}
      </Stack>

      {content.length !== 0 && (
        <ViettelModalField
          title={t("usb.usb3")}
          subtitle={t("usb.usb4")}
          open={open}
          scroll={scroll}
          errorPG={errorPG}
          handleClose={handleClose}
          urlWithoutProtocol={urlWithoutProtocol}
          data={content?.data}
          handleModalClick={signHash}
          handleModalChange={handleTokenSelected}
          // disabled={content?.listCertificate?.length === 0}
          value={cerSelected}
        />
      )}
    </div>
  );
};

export default ViettelCA;
