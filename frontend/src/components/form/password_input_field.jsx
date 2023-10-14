import { Visibility, VisibilityOff } from "@mui/icons-material";
import {
  Box,
  FormControl,
  IconButton,
  InputAdornment,
  InputLabel,
  OutlinedInput,
} from "@mui/material";
import React, { useState } from "react";

const PasswordField = ({
  handlePassWordChange,
  enterToSubmit = undefined,
  disabled = false,
}) => {
  const [showPassword, setShowPassword] = useState(false);

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };
  return (
    <Box
      sx={{
        "& .MuiTextField-root": { m: 1, width: "25ch" },
      }}
      autoComplete="off"
    >
      <FormControl sx={{ m: 1, width: "25ch" }} variant="outlined">
        {/* <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel> */}
        <OutlinedInput
          id="outlined-adornment-password"
          type={showPassword ? "text" : "password"}
          endAdornment={
            <InputAdornment position="end">
              <IconButton
                aria-label="toggle password visibility"
                onClick={handleClickShowPassword}
                onMouseDown={handleMouseDownPassword}
                edge="end"
              >
                {showPassword ? <VisibilityOff /> : <Visibility />}
              </IconButton>
            </InputAdornment>
          }
          //   label="Password"
          onChange={handlePassWordChange}
          onKeyDown={enterToSubmit}
          disabled={disabled}
          autoComplete="off"
        />
      </FormControl>
    </Box>
  );
};

export default PasswordField;
