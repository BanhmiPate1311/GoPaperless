import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Avatar,
  Box,
  Divider,
  Drawer,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import CloseIcon from "@mui/icons-material/Close";
import { ReactComponent as IconChipWhite } from "../../assets/images/icon_Chip_White.svg";
import { ReactComponent as DocumentDetail } from "../../assets/images/detail_document.svg";
import { formatPeriodTime, formatTime } from "../../ultis/commonFunction";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

const ShowSignature = ({ sig, sign, signType }) => {
  console.log("sig: ", sig);
  const [isOpen, setIsOpen] = useState(false);
  const toggleDrawer = () => {
    setIsOpen((prevState) => !prevState);
  };
  const handleClose = () => {
    setIsOpen(false);
  };

  const signature = {
    signing: [
      {
        title: "Signing reason",
        subtitle: `${sig.signing_reason}`,
      },
      {
        title: "Signing time",
        subtitle: `${formatTime(sig.signing_time)}`,
      },
      {
        title: "Qualified timestamp",
        subtitle: `${sig.qualified_timestamp}`,
      },
      {
        title: "Signature format",
        subtitle: `${sig.signature_format}`,
      },
      {
        title: "Signature scope",
        subtitle: `${sig.signature_scope}`,
      },
    ],
    certificated: [
      {
        title: "Certificated owner",
        subtitle: `${sig.certificate_owner}`,
      },
      // {
      //   title: "Unique ID number",
      //   subtitle: "38003160158",
      // },
      {
        title: "Certificate issuer",
        subtitle: `${sig.certificate_issuer}`,
      },
      {
        title: "Certificate validity period",
        subtitle: `${formatPeriodTime(sig.certificate_validity_period)}`,
      },
      {
        title: "Certificate type",
        subtitle: `${sig.certificate_type}`,
      },
    ],
  };

  const [expanded, setExpanded] = useState("panel");

  const handleChangeShow = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : true);
  };

  useEffect(() => {
    const resizeHandler = () => {
      const viewerContainer = document.getElementById("cookieSetting");
      if (viewerContainer) {
        const windowHeight = window.innerHeight;
        const offsetTop = viewerContainer.offsetTop;
        const viewerHeight = windowHeight - offsetTop;
        viewerContainer.style.height = viewerHeight + "px";
      }
    };
    resizeHandler();
    window.addEventListener("resize", resizeHandler);
    return () => {
      window.removeEventListener("resize", resizeHandler);
    };
  }, []);

  // event.stopPropagation() ngăn chặn sự kiện click này được truyền lên AccordionSummary và Accordion, giúp Switch được xử lý độc lập với AccordionSummary và Accordion.
  return (
    <div>
      <div onClick={toggleDrawer}>
        <Box
          sx={{
            display: "block",
            overflow: "hidden",
            padding: "5px",
            borderRadius: "12px",
            alignItems: "center",
          }}
        >
          <Box
            sx={{
              display: "flex",
              gap: "10px",
              justifyContent: "space-between",
              alignItems: "center",
              cursor: "pointer",
              p: 1,
              "&:hover": {
                background: "rgb(232, 235, 240)",
                borderRadius: "10px",
              },
            }}
          >
            <Box sx={{ display: "flex", gap: "10px", alignItems: "center" }}>
              {/* <img src="/logo_signing/icon_Chip_White.svg" alt="" /> */}
              {sign.icon}
              <Box width="100%">
                <Typography>{sig.certificate_owner}</Typography>
                <Box>
                  <Typography>{sign.title}</Typography>
                  {sign.name === "valid" && (
                    <Typography>{formatTime(sig.signing_time)}</Typography>
                  )}
                </Box>
              </Box>
            </Box>
            {/* <img src="/logo_signing/detail_document.svg" alt=""></img> */}
            <DocumentDetail />
          </Box>
        </Box>
      </div>

      <div>
        <Drawer
          open={isOpen}
          onClose={toggleDrawer}
          anchor="right"
          style={{ width: "350px", wordWrap: "break-word" }}
        >
          <div style={{ width: "350px", wordWrap: "break-word" }}>
            {/* ----------------------------Privacy preference center----------------------------------*/}
            <div className="header-cookie d-flex align-items-center">
              <div className="col-10 p-4">
                {/* <Title sx={{ textTransform: "uppercase" }}>
                  xuân khánh pham
                </Title> */}
                <div
                  style={{
                    fontSize: "14px",
                    fontWeight: "550",
                    textTransform: "uppercase",
                  }}
                >
                  {sig.certificate_owner}
                </div>

                {/* <Typography>38003160158</Typography> */}
              </div>
              <div className="col-2 d-flex">
                <button
                  className=" border-0 bg-transparent close"
                  aria-label="Close"
                  onClick={handleClose}
                >
                  <CloseIcon />
                </button>
              </div>
            </div>

            <div>
              <Box sx={{ borderBottom: "1px solid #e9e9e9", p: 3 }}>
                <Box
                  sx={{
                    display: "block",
                    overflow: "hidden",
                    borderRadius: "12px",
                    alignItems: "center",
                  }}
                >
                  <Box
                    sx={{
                      display: "flex",
                      gap: "10px",
                      justifyContent: "space-between",
                      p: 1,
                      background: "rgb(232, 235, 240)",
                      borderRadius: "10px",
                    }}
                  >
                    <Box sx={{ display: "flex", gap: "10px" }}>
                      <Avatar
                        sx={{ bgcolor: "#00CCFF", width: 32, height: 32 }}
                      >
                        {sign.icon}
                      </Avatar>
                      {sign.name === "valid" ? (
                        <Box>
                          {/* <Title>Signature is valid</Title> */}
                          <div
                            style={{
                              fontSize: "14px",
                              fontWeight: "550",
                            }}
                          >
                            {signType} is valid
                          </div>
                          <Box>
                            <Typography>{sign.title}</Typography>
                          </Box>
                        </Box>
                      ) : (
                        <Box>
                          {/* <Title>Signature is valid</Title> */}
                          <div
                            style={{
                              fontSize: "14px",
                              fontWeight: "550",
                            }}
                          >
                            {sign.title}
                          </div>
                          <Box>
                            <Typography>Electronic Signature</Typography>
                          </Box>
                        </Box>
                      )}
                    </Box>
                  </Box>
                </Box>
              </Box>

              {sig.errors.length > 0 && (
                <Accordion
                  expanded={expanded === "panel"}
                  onChange={handleChangeShow("panel")}
                  sx={{ boxShadow: "none", borderBottom: "1px solid #ccc" }}
                  style={{ margin: 0 }}
                  // className="content-signature"
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
                      paddingLeft: "24px",
                    }}
                  >
                    <Typography sx={{ width: "90%", flexShrink: 0 }}>
                      Signatures errors
                    </Typography>
                  </AccordionSummary>
                  {sig.errors.map((val, i) => {
                    return (
                      <Box key={i}>
                        <AccordionDetails
                          sx={{
                            padding: "15px 24px",
                            width: "100%",
                            // borderBottom: "1px solid #ccc",
                          }}
                        >
                          {val}
                        </AccordionDetails>
                        <Divider
                          sx={{
                            width: "calc(100% - 24px)",
                            marginLeft: "auto",
                            height: "2px",
                            // bgcolor: "blueviolet",
                          }}
                        />
                      </Box>
                    );
                  })}
                </Accordion>
              )}

              <div className="content-signature" id="cookieSetting">
                {/* Khi nhấn vào tắt Switch hiển thị  button Allow all*/}
                {/* --------------------------------------------------------------*/}
                {signature.signing.map((step, index) => (
                  <div key={step.title}>
                    <Typography className="font-title">{step.title}</Typography>
                    <Typography>{step.subtitle}</Typography>
                  </div>
                ))}
                {signature.certificated.map((step, index) => (
                  <div key={step.title}>
                    <Typography className="font-title">{step.title}</Typography>
                    <Typography>{step.subtitle}</Typography>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </Drawer>
      </div>
    </div>
  );
};

export default ShowSignature;
