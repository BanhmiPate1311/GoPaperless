import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Box,
  Divider,
  Stack,
  Typography,
} from "@mui/material";
import React, { useState } from "react";
import ShowSignature from "./ShowSignature";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import SignDetail from "./SignDetail";
import { ReactComponent as IconChipWhite } from "../../assets/images/icon_Chip_White.svg";
import { Error } from "@mui/icons-material";

const Signatures = ({ validFile, signType }) => {
  console.log("validFile: ", validFile);
  const [expanded, setExpanded] = useState("panel");

  const handleChangeShow = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : true);
  };

  const valueSign = [
    {
      name: "valid",
      value: validFile.filter((sig) => sig.indication === "TOTAL_PASSED"),
      icon: (
        <Box padding="7px" bgcolor="rgb(255, 240, 226)">
          <IconChipWhite />
        </Box>
      ),
      title:
        signType === "Signature"
          ? "Qualified Electronic Signature"
          : "Advanced Electronic Seal",
    },
    {
      name: "indeteminate",
      value: validFile.filter((sig) => sig.indication === "INDETERMINATE"),
      icon: (
        <Stack
          padding="7px"
          border="1px solid transparent"
          bgcolor="rgb(255, 240, 226)"
          borderRadius="50px"
          justifyContent="center"
        >
          <Error sx={{ color: "rgb(235, 106, 0)", fontSize: "18px" }} />
        </Stack>
      ),
      title: "Insufficient information to ascertain validity",
    },
    {
      name: "invalid",
      value: validFile.filter((sig) => sig.indication === "TOTAL_FAILED"),
      icon: (
        <Stack
          padding="7px"
          bgcolor="rgb(255, 233, 235)"
          borderRadius="50px"
          justifyContent="center"
        >
          <Error sx={{ color: "rgb(216, 81, 63)", fontSize: "18px" }} />
        </Stack>
      ),
      title: "Checks of the signature failed",
    },
  ];
  const newSign = valueSign.filter((sig) => sig.value.length > 0);
  console.log("newSign: ", newSign);
  return (
    <>
      <Box sx={{ p: 3 }}>
        {/* <Title>Signatures</Title> */}
        <div style={{ fontSize: "14px", fontWeight: "550" }}>Signatures</div>
      </Box>
      <Divider />
      <Box>
        <Box>
          {newSign.length > 0 &&
            newSign.map((val, i) => (
              <SignDetail sign={val} signType={signType} key={i} />
            ))}
        </Box>
      </Box>
    </>
  );
};

export default Signatures;
