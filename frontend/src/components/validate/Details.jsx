import { Box, Divider, IconButton, Tooltip, Typography } from "@mui/material";
import React from "react";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import { useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

const Details = ({ validFile, notSign }) => {
  const { t } = useTranslation();
  const { upload_token } = useParams();
  return (
    <Box>
      <Box sx={{ p: 3 }}>
        {/* <Title>Details</Title> */}
        <Typography
          variant="h6"
          style={{ fontSize: "14px", fontWeight: "550" }}
        >
          {t("validation.tab4")}
        </Typography>
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
      <Box sx={{ p: 3 }}>
        <Box sx={{ pb: 2 }}>
          <Typography variant="h5" fontWeight="bold">
            {t("validation.reportId")}
          </Typography>
          <Typography variant="h5">{validFile.validation_report_id}</Typography>
        </Box>
        <Box sx={{ pb: 2, overflowWrap: "break-word" }}>
          <Typography variant="h5" fontWeight="bold">
            {t("validation.documentHash")}
          </Typography>
          <Typography variant="h5">
            {validFile.validated_document_hash}
          </Typography>
        </Box>
        {!notSign && (
          <Box>
            <Typography variant="h5" fontWeight="bold">
              {t("validation.diadata")}
            </Typography>
            <a
              href={`${window.location.origin}/internalusage/api/validation/${upload_token}/download/diagnostic-data-xml`}
              style={{ color: "#211529" }}
            >
              {t("validation.downloadDia")}
            </a>
          </Box>
        )}
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
      {!notSign && (
        <>
          <Box sx={{ p: 3 }}>
            <Typography variant="h5" fontWeight="bold">
              {t("validation.detailReport")}
            </Typography>
            <a
              href={`${window.location.origin}/internalusage/api/validation/${upload_token}/download/detailed-report-pdf`}
              style={{ color: "#211529" }}
            >
              {t("validation.downloadDetail")}
            </a>
          </Box>
          <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
        </>
      )}

      <Box
        sx={{
          p: 3,
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Box>
          <Typography variant="h5" fontWeight="bold">
            {t("validation.liability")}
          </Typography>
          <Typography variant="h5">{t("validation.basicLiability")}</Typography>
        </Box>
        <Tooltip title={t("validation.tooltip")}>
          <IconButton>
            <InfoOutlinedIcon sx={{ fill: "#9E9C9C" }} />
          </IconButton>
        </Tooltip>
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
    </Box>
  );
};

export default Details;
