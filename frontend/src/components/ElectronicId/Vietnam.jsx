import { Backdrop, Box, Grow, Modal, Stack, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ReactComponent as ReactLogo } from "../../assets/images/finger.svg";
import { api } from "../../constants/api";
import HorizontalNonLinearStepper from "../HorizontalNonLinearStepper";
import ButtonField from "../form/button_field";
import ComboBoxField from "../form/combo_box_field";
import InputField from "../form/input_field";

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

const Vietnam = ({ swError, connectorName, workFlow }) => {
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

        {prefix.length !== 0 && (
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
        )}
      </Stack>

      <Stack spacing={6} direction="row" useFlexGap flexWrap="wrap" pt={4}>
        <Box width={300}>
          <ButtonField
            handleButtonClick={handleShowModal1}
            disabled={!checkLength || swError !== null}
            text={t("electronic.continue")}
          />
        </Box>
      </Stack>

      <Modal
        aria-labelledby="transition1-modal-title"
        aria-describedby="transition1-modal-description"
        open={showModal1}
        onClose={handleCloseModal1}
        closeAfterTransition
        slots={{ backdrop: Backdrop }}
        slotProps={{
          backdrop: {
            timeout: 500,
          },
        }}
      >
        <Grow
          in={showModal1}
          style={{
            width: "500px",
            transform: "translate(-50%, -50%)",
            transformOrigin: "0 0 0",
            // padding: "16px 0px 59px 16px",
            paddingTop: "16px",
            paddingLeft: "30px",
            border: "none",
            outline: "none",
          }}
          {...(showModal1 ? { timeout: 1000 } : {})}
        >
          <Box sx={style}>
            <Stack
              direction="row"
              justifyContent="flex-end"
              alignItems="center"
            >
              <Typography
                sx={{
                  color: "rgb(79, 78, 78)",
                  marginRight: "8px",
                  fontWeight: 500,
                  fontSize: "12px",
                  lineHeight: "14px",
                }}
              >
                Powered by
              </Typography>
              <ReactLogo />
            </Stack>
            <HorizontalNonLinearStepper
              handleCloseModal1={handleCloseModal1}
              type={type}
              code={code}
              connectorName={connectorName}
              workFlow={workFlow}
            />
          </Box>
        </Grow>
      </Modal>
    </Box>
  );
};

export default Vietnam;
