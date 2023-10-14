import { TextField } from "@mui/material";
import React from "react";

const InputField = ({
  label,
  number = undefined,
  handleInputChange,
  enterToSubmit = undefined,
  disabled = false,
  maxLength = undefined,
}) => {
  return (
    <TextField
      id="outlined-basic"
      fullWidth
      label={label}
      variant="outlined"
      autoComplete="off"
      onKeyDown={enterToSubmit}
      value={number}
      onChange={(e) => handleInputChange(e.target.value)}
      disabled={disabled}
      InputLabelProps={{ shrink: true }}
      inputProps={{
        maxLength: maxLength,
      }}
    />
  );
};

export default InputField;
