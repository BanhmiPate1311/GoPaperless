import { Box, Divider, IconButton, Tooltip, Typography } from "@mui/material";
import React from "react";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import { useParams } from "react-router-dom";

const Details = ({ validFile }) => {
  const { upload_token } = useParams();
  return (
    <div>
      <Box sx={{ p: 3 }}>
        {/* <Title>Details</Title> */}
        <div style={{ fontSize: "14px", fontWeight: "550" }}>Details</div>
      </Box>
      <Divider />
      <Box sx={{ p: 3 }}>
        <Box sx={{ pb: 2 }}>
          <Typography>Validation report ID</Typography>
          <Typography>{validFile.validation_report_id}</Typography>
        </Box>
        <Box sx={{ pb: 2, overflowWrap: "break-word", maxWidth: "345px" }}>
          <Typography>Validation document hash</Typography>
          <Typography>{validFile.validated_document_hash}</Typography>
        </Box>
        <Box>
          <Typography>Diagnostic data</Typography>
          <a
            href={`${window.location.origin}/internalusage/api/validation/${upload_token}/download/diagnostic-data-xml`}
            style={{ color: "#211529" }}
          >
            Download diagnostic data
          </a>
        </Box>
      </Box>
      <Divider />
      <Box sx={{ p: 3 }}>
        <Typography>Detailed validation report</Typography>
        <a
          href={`${window.location.origin}/internalusage/api/validation/${upload_token}/download/detailed-report-pdf`}
          style={{ color: "#211529" }}
        >
          Download detailed report
        </a>
      </Box>
      <Divider />
      <Box
        sx={{
          p: 3,
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
        }}
      >
        <Box>
          <Typography>Liability level</Typography>
          <Typography>Basic liability</Typography>
        </Box>
        <Tooltip title="Qualified validation for electronic signatures and seals with a standard liability assurance of 100 EUR.">
          <IconButton>
            <InfoOutlinedIcon sx={{ fill: "#9E9C9C" }} />
          </IconButton>
        </Tooltip>
      </Box>
      <Divider />
    </div>
  );
};

export default Details;
