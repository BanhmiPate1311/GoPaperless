import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Typography,
} from "@mui/material";
import React from "react";
import { useState } from "react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ShowSignature from "./ShowSignature";

const SignDetail = ({ sign, signType }) => {
  console.log("sign: ", sign);
  const [expanded, setExpanded] = useState("panel");

  const handleChangeShow = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : true);
  };

  return (
    <Accordion
      expanded={expanded === "panel"}
      onChange={handleChangeShow("panel")}
      sx={{ boxShadow: "none", borderBottom: "1px solid #ccc" }}
      style={{ margin: 0 }}
    >
      <AccordionSummary
        expandIcon={<ExpandMoreIcon />}
        aria-controls="panel1bh-content"
        id="panel1bh-header"
        sx={{
          background: "rgb(232, 235, 240)",
          boxShadow: "none",
          minHeight: "unset !important",
          display: "flex",
          alignItems: "center",
        }}
      >
        <Typography sx={{ width: "90%", flexShrink: 0 }}>
          {sign.name} signatures
        </Typography>
        <Typography sx={{ color: "text.secondary" }}>{sign.length}</Typography>
      </AccordionSummary>
      {sign.value.map((val, i) => {
        return (
          <AccordionDetails key={i} sx={{ padding: "unset !important" }}>
            <ShowSignature sig={val} sign={sign} signType={signType} />
            {/* <Divider /> */}
          </AccordionDetails>
        );
      })}
    </Accordion>
  );
};

export default SignDetail;
