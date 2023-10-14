import { Alert, Box, Divider, Tooltip, Typography } from "@mui/material";
import React from "react";
import CheckCircleIcon from "@mui/icons-material/CheckCircle";
import PeopleOutlinedIcon from "@mui/icons-material/PeopleOutlined";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import WorkspacePremiumIcon from "@mui/icons-material/WorkspacePremium";
import { formatTime } from "../../ultis/commonFunction";
import { Error } from "@mui/icons-material";

const Overview = ({ validFile }) => {
  const statusToIcon = {
    Valid: <CheckCircleIcon sx={{ fontSize: "1.5rem", color: "#228B22" }} />,
    "There are warnings": (
      <Error sx={{ color: "rgb(235, 106, 0)", fontSize: "18px" }} />
    ),
    "There are errors": (
      <Error sx={{ color: "rgb(216, 81, 63)", fontSize: "18px" }} />
    ),
    // Thêm các ánh xạ khác nếu cần
  };
  return (
    <div>
      <Box sx={{ p: 3 }}>
        {/* <Title>Overview</Title> */}
        <div style={{ fontSize: "14px", fontWeight: "550" }}>Overview</div>
      </Box>
      <Divider />
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
          <Box sx={{ display: "flex", gap: "10px" }}>
            {statusToIcon[validFile.status] || (
              <CheckCircleIcon sx={{ fontSize: "1.5rem", color: "#228B22" }} />
            )}
            {/* {validFile.valid && ( */}
            <Box sx={{ display: "block" }}>
              <Typography>{validFile.status}</Typography>
              <Box sx={{ display: "flex" }}>
                <PeopleOutlinedIcon
                  fontSize="small"
                  sx={{ fill: "#9E9C9C", marginRight: "2px" }}
                />
                <span>
                  {validFile.total_valid_signatures} /{" "}
                  {validFile.total_signatures} valid signatures
                </span>
              </Box>
              <Box sx={{ display: "flex" }}>
                <WorkspacePremiumIcon
                  fontSize="small"
                  sx={{ fill: "#9E9C9C", marginRight: "2px" }}
                />
                <span>
                  {validFile.total_valid_seal} / {validFile.total_seals} valid
                  seals
                </span>
              </Box>
            </Box>
            {/* )} */}
          </Box>
        </Box>
      </Box>
      <Divider />
      <Box sx={{ p: 3 }}>
        <Typography>Validation time</Typography>
        <Typography sx={{ pb: 2 }}>
          {formatTime(validFile.validation_time)}
        </Typography>
        <Alert
          severity="error"
          icon={<InfoOutlinedIcon sx={{ fill: "#9E9C9C" }} />}
          sx={{
            background: "rgb(232, 235, 240)",
          }}
        >
          <Typography>
            This validation report shows the validity of the signatures and
            seals at the specified validation time.
          </Typography>
        </Alert>
      </Box>
      <Divider />
      <Box sx={{ p: 3 }}>
        <Typography>Selected validation policy</Typography>
        <Box
          sx={{
            display: "flex",
          }}
        >
          <Typography>
            Advanced and Qualified Electronic Signatures and Seals
          </Typography>
          <Tooltip title="Qualified validation for electronic signatures and seals with a standard liability assurance of 100 EUR.">
            <InfoOutlinedIcon sx={{ fill: "#9E9C9C", cursor: "pointer" }} />
          </Tooltip>
        </Box>
      </Box>
      <Divider />
    </div>
  );
};

export default Overview;
