import React, { Fragment, useEffect, useRef, useState } from "react";
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
import { Box, Button, Typography } from "@mui/material";
import { api } from "../../constants/api";

const NonLinearStepper = ({
  type,
  code,
  connectorName,
  workFlow,
  activeStep,
  steps,
}) => {
  let lang = localStorage.getItem("language");
  switch (lang) {
    case "Vietnamese":
      lang = "VN";
      break;
    default:
      lang = "EN";
      break;
  }

  const providerSelected = useRef(null);

  return <Box sx={{ width: "100%" }}>{steps}</Box>;
};

export default NonLinearStepper;
