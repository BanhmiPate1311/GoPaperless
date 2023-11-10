import styled from "@emotion/styled";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import React from "react";
import spinner from "../../assets/images/1488.gif";

const ColorButton = styled(Button)(({ theme }) => ({
  // color: theme.palette.getContrastText(purple[500]),
  backgroundColor: "#1976D2",
  "&:hover, &:disabled": {
    backgroundColor: "#1976D2",
    color: "white",
  },
  "&:disabled": {
    cursor: "not-allowed !important",
  },

  fontFamily: '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
}));

const SequenceButtonField = ({
  handleButtonClick,
  disabled = false,
  text,
  loading = false,
}) => {
  return (
    <ColorButton
      fullWidth
      variant="contained"
      size="large"
      disabled={disabled}
      onClick={handleButtonClick}
    >
      {loading && (
        <Box sx={{ background: "transparent" }}>
          <img style={{ width: "20px" }} src={spinner} alt="loading" />
        </Box>
      )}
      {text}
    </ColorButton>
  );
};

export default SequenceButtonField;
