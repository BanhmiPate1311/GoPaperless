import { Box, Button, styled } from "@mui/material";
import React from "react";
import spinner from "../../assets/images/1488.gif";

const ColorButton = styled(Button)(({ theme }) => ({
  // color: theme.palette.getContrastText(purple[500]),
  backgroundColor: "rgb(63, 195, 128)",
  "&:hover, &:disabled": {
    backgroundColor: "rgb(63, 195, 128)",
    color: "white",
  },
  "&:disabled": {
    cursor: "not-allowed !important",
  },

  fontFamily: '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
}));
const ButtonField = ({
  handleButtonClick,
  disabled,
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

export default ButtonField;
