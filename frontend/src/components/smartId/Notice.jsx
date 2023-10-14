import { Box, Stack, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import imageLoading from "../../assets/images/ajax-loader.gif";

const Notice = ({
  timeout = 60,
  codeVC,
  handleCancelSign,
  VCEnabled = false,
}) => {
  const { t } = useTranslation();
  const [signTimeout, setSignTimeout] = useState(timeout);
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
    if (!codeVC) return;
    if (signTimeout <= 0 && !codeVC) return;
    if (signTimeout <= 0) {
      // alert("Signing timeout")
      window.location.reload();
    }
    const timeout = setTimeout(() => {
      setSignTimeout(signTimeout - 1);
    }, 1000);

    return () => clearTimeout(timeout);
  }, [signTimeout, codeVC]);
  return (
    <Box>
      <Stack
        spacing={1}
        direction="row"
        useFlexGap
        flexWrap="wrap"
        alignItems="center"
      >
        <Typography
          sx={{
            fontFamily: '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
            fontSize: "14px",
          }}
        >
          {VCEnabled
            ? t("smartID.verificationCode")
            : t("smartID.signingNotice")}
        </Typography>
        {VCEnabled && (
          <Typography
            sx={{
              fontFamily: '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
              fontSize: "14px",
            }}
            className="badge bg-info text-wrap fw-bold rounded-pill"
          >
            {codeVC !== " " ? codeVC : "...."}
          </Typography>
        )}

        <img style={{ width: "20px" }} src={imageLoading} alt="loading" />
        <Typography
          fontWeight="bold"
          sx={{
            fontFamily: '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
            fontSize: "14px",
          }}
        >
          {signTimeout}
          {lang === "VN" ? " giây" : " seconds"}
        </Typography>
        <Typography
          color="red"
          sx={{
            cursor: "pointer",
            fontFamily: '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
            fontSize: "14px",
          }}
          onClick={handleCancelSign}
        >
          {t("smartID.cancel")}
        </Typography>
      </Stack>
      <Box>{t("smartID.warning")}</Box>
    </Box>
  );
};

export default Notice;
