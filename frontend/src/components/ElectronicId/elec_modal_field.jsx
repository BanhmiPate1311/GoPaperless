import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Fade,
  Stack,
  ToggleButton,
  Typography,
  styled,
} from "@mui/material";
import React, { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { ReactComponent as ReactLogo } from "../../assets/images/finger.svg";
import NonLinearStepper from "./NonLinearStepper";
import { api } from "../../constants/api";
import ISPluginClient from "../../assets/js/checkid";
import spinner from "../../assets/images/1488.gif";
import Step1 from "./Step1";
import Step2 from "./Step2";
import Step3 from "./Step3";
import Step4 from "./Step4";
import Step5 from "./Step5";
import Step6 from "./Step6";
import Step7 from "./Step7";
import Step8 from "./Step8";
import Step9 from "./Step9";
import Step10 from "./Step10";
import Step11 from "./Step11";
import Step11a from "./Step11a";
import Step12 from "./Step12";
import Step13 from "./Step13";

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Fade elevation={4} ref={ref} {...props} />;
});

const ElecModalField = ({
  open,
  scroll = "paper",
  handleClose,
  code,
  type,
  connectorName,
  workFlow,
  isIdentifyRegistered,
}) => {
  const { t } = useTranslation();
  const descriptionElementRef = useRef(null);
  useEffect(() => {
    if (open) {
      const { current: descriptionElement } = descriptionElementRef;
      if (descriptionElement !== null) {
        descriptionElement.focus();
      }
    }
  }, [open]);

  let lang = localStorage.getItem("language");
  switch (lang) {
    case "Vietnamese":
      lang = "VN";
      break;
    default:
      lang = "EN";
      break;
  }
  const [isFetching, setIsFetching] = useState(false);
  const [errorPG, setErrorPG] = useState(null);
  const [activeStep, setActiveStep] = useState(0);
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(true);

  const handleDisableSubmit = (disabled) => {
    setIsSubmitDisabled(disabled);
  };

  const [skipped, setSkipped] = useState(new Set());

  const isStepOptional = (step) => {
    return step === 15;
  };

  const isStepSkipped = (step) => {
    return skipped.has(step);
  };

  const handleNext = (step = 1) => {
    let newSkipped = skipped;
    if (isStepSkipped(activeStep)) {
      newSkipped = new Set(newSkipped.values());
      newSkipped.delete(activeStep);
    }

    setActiveStep((prevActiveStep) => prevActiveStep + step);
    setSkipped(newSkipped);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleSkip = () => {
    if (!isStepOptional(activeStep)) {
      // You probably want to guard against something like this,
      // it should never occur unless someone's actively trying to break something.
      throw new Error("You can't skip a step that isn't optional.");
    }

    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    setSkipped((prevSkipped) => {
      const newSkipped = new Set(prevSkipped.values());
      newSkipped.add(activeStep);
      return newSkipped;
    });
  };

  const handleSubmitClick = () => {
    switch (activeStep) {
      case 0:
        if (isIdentifyRegistered) {
          handleNext(3);
        } else {
          handleNext(1);
        }
        break;
      //   case 2:
      //     connectToWS();
      //     break;
      //   case 5:
      //     // faceAndCreate();
      //     setShouldDetectFaces(true);
      //     setErrorPG(null);
      //     break;
      //   case 6:
      //     updateSubject();
      //     break;
      //   case 7:
      //     perFormProcess(otp);
      //     break;
      //   case 8:
      //     updateSubject();
      //     break;
      //   case 9:
      //     perFormProcess(otp);
      //     break;
      //   case 10:
      //     checkCertificate();
      //     // createCertificate();
      //     break;
      //   case 11:
      //     if (certificate) {
      //       handleNext();
      //     } else {
      //       createCertificate();
      //     }
      //     // checkCertificate();
      //     break;
      //   case 12:
      //     credentialOTP();
      //     break;
      //   case 13:
      //     authorizeOTP(otp);
      //     break;
      default:
        // perFormProcess(); // chỉ để test
        handleNext();
    }
  };

  const steps = [
    <Step1 isIdentifyRegistered={isIdentifyRegistered} />,
    <Step2 onDisableSubmit={handleDisableSubmit} />,
    <Step3 />,
    <Step4 />,
    <Step5 />,
    <Step6 />,
    <Step7 />,
    <Step8 />,
    <Step9 />,
    <Step10 />,
    <Step11 />,
    <Step11a />,
    <Step12 />,
    <Step13 />,
  ];

  return (
    <Dialog
      sx={{
        "& .MuiDialog-container": {
          "& .MuiPaper-root": {
            width: "100%",
            maxWidth: "480px", // Set your width here
          },
          "& .MuiTypography-root": {
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          },
        },
      }}
      TransitionComponent={Transition}
      keepMounted
      open={open}
      onClose={handleClose}
      scroll={scroll}
      aria-labelledby="scroll-dialog-title"
      aria-describedby="scroll-dialog-description"
    >
      <DialogTitle
        component="div"
        id="scroll-dialog-title"
        sx={{ padding: "16px" }}
      >
        <Stack direction="row" justifyContent="flex-end" alignItems="center">
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
      </DialogTitle>
      <DialogContent sx={{ padding: "16px" }}>
        <DialogContentText
          id="scroll-dialog-description"
          ref={descriptionElementRef}
          tabIndex={-1}
          component="div"
        >
          {open && (
            <NonLinearStepper
              type={type}
              code={code}
              connectorName={connectorName}
              workFlow={workFlow}
              steps={steps[activeStep]}
            />
          )}
        </DialogContentText>
      </DialogContent>
      <DialogActions sx={{ justifyContent: "space-between", px: "16px" }}>
        <Button
          variant="secondary"
          onClick={
            activeStep === 1 ||
            activeStep === 2 ||
            activeStep === 11 ||
            activeStep === 12
              ? handleBack
              : handleClose
          }
          style={{
            backgroundColor: "rgb(240, 237, 234)",
            textTransform: "capitalize",
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          }}
        >
          {activeStep === 1 ||
          activeStep === 2 ||
          activeStep === 11 ||
          activeStep === 12
            ? t("electronicid.other9")
            : t("electronicid.other5")}
        </Button>

        <Button
          variant="primary"
          //   onClick={handleModalClick}
          style={{
            backgroundColor: "#013f94",
            color: "#fff",
            textTransform: "capitalize",
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          }}
          disabled={
            isFetching ||
            // activeStep === 5 ||
            activeStep === 1 ||
            activeStep === 6 ||
            activeStep === 7 ||
            activeStep === 8 ||
            activeStep === 9 ||
            activeStep === 10 ||
            activeStep === 13
          }
          onClick={handleSubmitClick}
        >
          {isFetching ? (
            <Box sx={{ background: "transparent" }}>
              <img style={{ width: "20px" }} src={spinner} alt="loading" />
            </Box>
          ) : errorPG ? (
            t("electronicid.other10")
          ) : activeStep === steps.length - 1 ? (
            t("electronicid.other7")
          ) : (
            t("electronicid.other8")
          )}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ElecModalField;
