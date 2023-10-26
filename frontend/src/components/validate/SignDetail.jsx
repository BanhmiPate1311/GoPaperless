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
import { createValidName } from "../../ultis/commonFunction";

const SignDetail = ({ sign, signType }) => {
  let name = sign.name + " " + signType;
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
        <Typography variant="h6" sx={{ width: "90%", flexShrink: 0 }}>
          {createValidName(name)}
        </Typography>
        <Typography variant="h6" sx={{ color: "text.secondary" }}>
          {sign.value.length}
        </Typography>
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
