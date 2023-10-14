import { Backdrop, Box, Grow, Modal, Stack, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ReactComponent as ReactLogo } from "../../assets/images/finger.svg";
import { api } from "../../constants/api";
import HorizontalNonLinearStepper from "../HorizontalNonLinearStepper";
import ButtonField from "../form/button_field";
import ComboBoxField from "../form/combo_box_field";
import InputField from "../form/input_field";
import ElecModalField from "./elec_modal_field";
import { electronicService } from "../../services/electronicService";

const style = {
  position: "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  height: 700,
  maxHeight: 760,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
  overflowY: "auto",
  fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
};

const Vietnam1 = ({ swError, connectorName, workFlow }) => {
  const { t } = useTranslation();

  const [type, setType] = useState("CITIZEN-IDENTITY-CARD");

  const [checkLength, setCheckLength] = useState(false);

  const [code, setCode] = useState("");

  const [prefix, setPrefix] = useState([]);

  const getPrefix = async () => {
    const response = await api.post("/getPrefix", { lang: lang });
    if (response.data) {
      setPrefix(response.data);
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

  useEffect(() => {
    getPrefix();
  }, [lang]);

  const [showModal1, setShowModal1] = useState(false);

  const handleShowModal1 = () => {
    setShowModal1(true);
  };

  const handleCloseModal1 = () => {
    setShowModal1(false);
  };

  const checkParam = (value, condition) => {
    if (value.length >= condition) {
      setCheckLength(true);
    } else {
      setCheckLength(false);
    }
  };

  const handleInputChange = (value) => {
    setCode(value);
    checkParam(value, 12);
  };
  const enterToSubmit = (event) => {
    if (checkLength && event.key === "Enter") {
      handleShowModal1();
    }
  };

  const handleMenuItemClick = (event) => {
    setType(event.target.value);
  };

  const [open, setOpen] = useState(false);
  const [isIdentifyRegistered, setIsIdentifyRegistered] = useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const [subject, setSubject] = useState("");
  console.log("subject: ", subject);
  const [personalInfomation, setPersonalInformation] = useState(null);
  const [image, setImage] = useState(null);

  let typeAlias = "CITIZEN_CARD";

  const checkIdentity = async () => {
    const data = {
      lang: lang,
      code: code,
      type: typeAlias,
      connectorName: connectorName,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
    };
    try {
      const response = await electronicService.checkIdentity(data);
      if (response.data) {
        setSubject(response.data);
      }
      if (response.data.status === 0) {
        setIsIdentifyRegistered(true);
        setPersonalInformation(response.data.personal_informations);
        setImage(response.data.personal_informations.dg2);
      }
    } catch (error) {
      console.log("error: ", error);
    }
  };

  const handleOpen = async () => {
    await checkIdentity();
    setOpen(true);
  };

  return (
    <Box>
      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={2}>
        <Box width={300}>
          <InputField
            label={t("smartID.code")}
            handleInputChange={handleInputChange}
            enterToSubmit={enterToSubmit}
            maxLength={12}
          />
        </Box>

        <Box width={300}>
          <ComboBoxField
            data={prefix}
            value={type}
            handleMenuItemChange={handleMenuItemClick}
            // disabled={isFetching}
            valueExtractorValue={(item) => item.prefix}
            valueExtractorText={(item) => item.remark}
          />
        </Box>
      </Stack>

      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={4}>
        <Box width={300}>
          <ButtonField
            handleButtonClick={handleOpen}
            disabled={!checkLength || swError !== null}
            text={t("electronic.continue")}
          />
        </Box>
      </Stack>

      {Object.keys(subject) !== 0 && (
        <ElecModalField
          open={open}
          handleClose={handleClose}
          code={code}
          type={type}
          connectorName={connectorName}
          workFlow={workFlow}
          isIdentifyRegistered={isIdentifyRegistered}
        />
      )}
    </Box>
  );
};

export default Vietnam1;
