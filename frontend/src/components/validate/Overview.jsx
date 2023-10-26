import { Alert, Box, Divider, Stack, Tooltip, Typography } from "@mui/material";
import React from "react";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import PeopleOutlinedIcon from "@mui/icons-material/PeopleOutlined";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import WorkspacePremiumIcon from "@mui/icons-material/WorkspacePremium";
import { createValidStatus, formatTime } from "../../ultis/commonFunction";
import { CheckCircleOutline, Error } from "@mui/icons-material";
import { useTranslation } from "react-i18next";

const Overview = ({ validFile }) => {
  const { t } = useTranslation();
  const statusToIcon = {
    // Valid: <CheckCircleIcon sx={{ fontSize: "1.5rem", color: "#228B22" }} />,
    "There are warnings": (
      <Error sx={{ color: "rgb(235, 106, 0)", fontSize: "18px", mt: "1px" }} />
    ),
    "There are errors": (
      <Error sx={{ color: "rgb(216, 81, 63)", fontSize: "18px", mt: "1px" }} />
    ),
    // Thêm các ánh xạ khác nếu cần
  };
  return (
    <Box>
      <Box sx={{ p: 3 }}>
        {/* <Title>Overview</Title> */}
        <Box style={{ fontSize: "14px", fontWeight: "550" }}>
          {t("validation.tab1")}
        </Box>
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
      <Box sx={{ p: 3 }}>
        <Box
          sx={{
            display: "block",
            overflow: "hidden",
            padding: "12px 12px 12px calc(30px)",
            borderRadius: "12px",
            alignItems: "center",
            background: "rgb(232, 235, 240)",
          }}
        >
          {validFile.total_signatures === 0 && validFile.total_seals === 0 ? (
            <Stack direction="row" alignItems="center" gap="10px">
              <CheckCircleOutline
                sx={{ fontSize: "1.5rem", color: "#9E9C9C" }}
              />
              <Typography variant="h5">
                {t("validation.overviewNotFound")}
              </Typography>
            </Stack>
          ) : (
            <Box sx={{ display: "flex", gap: "10px" }}>
              {statusToIcon[validFile.status] || (
                <CheckCircleIcon
                  sx={{ fontSize: "1.5rem", color: "#228B22" }}
                />
              )}
              {/* {validFile.valid && ( */}
              <Box sx={{ display: "block" }}>
                <Typography variant="h6">
                  {createValidStatus(validFile.status)}
                </Typography>
                <Box sx={{ display: "flex" }}>
                  <PeopleOutlinedIcon
                    fontSize="small"
                    sx={{ fill: "#9E9C9C", marginRight: "2px" }}
                  />
                  <Typography variant="h5">
                    {validFile.total_valid_signatures} /{" "}
                    {validFile.total_signatures} {t("validation.overview1")}
                  </Typography>
                </Box>
                <Box sx={{ display: "flex" }}>
                  <WorkspacePremiumIcon
                    fontSize="small"
                    sx={{ fill: "#9E9C9C", marginRight: "2px" }}
                  />
                  <Typography variant="h5">
                    {validFile.total_valid_seal} / {validFile.total_seals}{" "}
                    {t("validation.overview2")}
                  </Typography>
                </Box>
              </Box>
              {/* )} */}
            </Box>
          )}
        </Box>
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
      <Box sx={{ p: 3 }}>
        <Typography variant="h6" fontWeight="bold">
          {t("validation.overview3")}
        </Typography>
        <Typography variant="h5" sx={{ pb: 2 }}>
          {formatTime(validFile.validation_time)}
        </Typography>
        <Alert
          severity="error"
          icon={<InfoOutlinedIcon sx={{ fill: "#9E9C9C" }} />}
          sx={{
            background: "rgb(232, 235, 240)",
            borderRadius: "12px",
          }}
        >
          <Typography variant="h5">{t("validation.overview4")}</Typography>
        </Alert>
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
      <Box sx={{ p: 3 }}>
        <Typography variant="h5" fontWeight="bold">
          {t("validation.overview5")}
        </Typography>
        <Box
          sx={{
            display: "flex",
          }}
        >
          <Typography variant="h5">{t("validation.overview6")}</Typography>
          <Tooltip title={t("validation.tooltip")}>
            <InfoOutlinedIcon sx={{ fill: "#9E9C9C", cursor: "pointer" }} />
          </Tooltip>
        </Box>
      </Box>
      <Divider sx={{ borderColor: "black", borderBottomWidth: 1 }} />
    </Box>
  );
};

export default Overview;
