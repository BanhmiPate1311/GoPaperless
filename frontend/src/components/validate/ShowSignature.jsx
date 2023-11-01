import {
  Accordion,
  AccordionDetails,
  AccordionSummary,
  Avatar,
  Box,
  Divider,
  Drawer,
  Stack,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import CloseIcon from "@mui/icons-material/Close";
import { ReactComponent as IconChipWhite } from "../../assets/images/icon_Chip_White.svg";
import { ReactComponent as DocumentDetail } from "../../assets/images/detail_document.svg";
import {
  createValidSubTitle,
  createValidTitle,
  formatPeriodTime,
  formatTime,
} from "../../ultis/commonFunction";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { useTranslation } from "react-i18next";

const ShowSignature = ({ sig, sign, signType }) => {
  const { t } = useTranslation();

  const signTitle = signType + " is valid";
  const subTitle = "Electronic " + signType;

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
        title: t("validation.sigReason"),
        subtitle: sig.signing_reason ? sig.signing_reason : null,
      },
      {
        title: t("validation.sigTime"),
        subtitle: sig.signing_time ? sig.signing_time : null,
      },
      {
        title: t("validation.sigTimestamp"),
        subtitle: sig.qualified_timestamp ? sig.qualified_timestamp : null,
      },
      {
        title: t("validation.sigFormat"),
        subtitle: sig.signature_format ? sig.signature_format : null,
      },
      {
        title: t("validation.sigScope"),
        subtitle: sig.signature_scope ? sig.signature_scope : null,
      },
    ].filter((item) => item.subtitle !== null),
    certificated: [
      {
        title: t("validation.sigOwner"),
        subtitle: sig.certificate_owner ? sig.certificate_owner : null,
      },
      {
        title: t("validation.sigIssuer"),
        subtitle: sig.certificate_issuer ? sig.certificate_issuer : null,
      },
      {
        title: t("validation.sigPeriod"),
        subtitle: sig.certificate_validity_period
          ? sig.certificate_validity_period
          : null,
      },
      {
        title: t("validation.sigType"),
        subtitle: sig.certificate_type ? sig.certificate_type : null,
      },
    ].filter((item) => item.subtitle !== null),
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
              <Stack
                direction="row"
                justifyContent="center"
                bgcolor="rgb(255, 240, 226)"
                borderRadius="50px"
              >
                {sign.icon}
              </Stack>
              <Box width="100%">
                <Typography variant="h5" fontWeight="bold">
                  {sig.certificate_owner}
                </Typography>
                <Box>
                  <Typography variant="h5">{sign.title}</Typography>
                  {sign.name === "valid" && (
                    <Typography variant="h5">{sig.signing_time}</Typography>
                  )}
                </Box>
              </Box>
            </Box>
            {/* <img src="/logo_signing/detail_document.svg" alt=""></img> */}
            <DocumentDetail />
          </Box>
        </Box>
      </div>

      <Drawer
        open={isOpen}
        onClose={toggleDrawer}
        anchor="right"
        sx={{
          "& .MuiDrawer-paper": {
            borderRadius: "10px",
          },
        }}
      >
        <Box style={{ width: "350px", wordWrap: "break-word" }}>
          {/* ----------------------------Privacy preference center----------------------------------*/}
          <Box className="header-cookie d-flex align-items-center">
            <Box className="col-10 p-4">
              {/* <Title sx={{ textTransform: "uppercase" }}>
                  xuân khánh pham
                </Title> */}
              <Typography
                variant="h5"
                fontSize="18px"
                fontWeight="550"
                textTransform="uppercase"
              >
                {sig.certificate_owner}
              </Typography>

              {/* <Typography>38003160158</Typography> */}
            </Box>
            <Box className="col-2 d-flex">
              <button
                className=" border-0 bg-transparent close"
                aria-label="Close"
                onClick={handleClose}
              >
                <CloseIcon />
              </button>
            </Box>
          </Box>

          <Box mb={3}>
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
                    <Avatar sx={{ bgcolor: "#00CCFF", width: 32, height: 32 }}>
                      {sign.icon}
                    </Avatar>
                    {sign.name === "valid" ? (
                      <Box>
                        {/* <Title>Signature is valid</Title> */}
                        <Typography variant="h5" fontWeight={550}>
                          {createValidTitle(signTitle)}
                        </Typography>
                        <Box>
                          <Typography variant="h5">{sign.title}</Typography>
                        </Box>
                      </Box>
                    ) : (
                      <Box>
                        {/* <Title>Signature is valid</Title> */}
                        <Typography variant="h5" fontWeight={550}>
                          {sign.title}
                        </Typography>
                        <Box>
                          <Typography variant="h5">
                            {createValidSubTitle(subTitle)}
                          </Typography>
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
                  <Typography variant="h6" sx={{ width: "90%", flexShrink: 0 }}>
                    {t("validation.sigErrors")}
                  </Typography>
                </AccordionSummary>
                {sig.errors.map((val, i) => {
                  return (
                    <Box key={i}>
                      <AccordionDetails
                        sx={{
                          fontSize: "13px",
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

            <Box className="content-signature" id="cookieSetting">
              {/* Khi nhấn vào tắt Switch hiển thị  button Allow all*/}
              {/* --------------------------------------------------------------*/}
              {signature.signing.map((step, index) => (
                <Box key={index}>
                  <Typography variant="h6" className="font-title">
                    {step.title}
                  </Typography>
                  <Typography variant="h5">{step.subtitle}</Typography>
                </Box>
              ))}
              {signature.certificated.map((step, index) => (
                <Box key={index}>
                  <Typography variant="h6" className="font-title">
                    {step.title}
                  </Typography>
                  <Typography variant="h5">{step.subtitle}</Typography>
                </Box>
              ))}
            </Box>
          </Box>
        </Box>
      </Drawer>
    </div>
  );
};

export default ShowSignature;
