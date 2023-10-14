import { Box } from "@mui/material";
import React from "react";
import PhoneInput from "react-phone-input-2";
import "react-phone-input-2/lib/material.css";

const PhoneInputField = ({
  label,
  phoneNumber,
  setPhoneNumber,
  handlePhoneNumber,
  enterToSubmit,
  disabled = false,
  copyNumbersOnly = true,
}) => {
  return (
    <Box>
      <PhoneInput
        country={"vn"}
        placeholder={label}
        enableSearch={true}
        specialLabel={label}
        value={phoneNumber}
        onChange={(phone, country) => handlePhoneNumber(phone, country)}
        onKeyDown={enterToSubmit}
        onBlur={(phone, country) => {
          if (phoneNumber === "") setPhoneNumber(`+ ${country.dialCode}`);
        }}
        onFocus={(phone, country) => {
          if (phoneNumber === "") setPhoneNumber(`+ ${country.dialCode}`);
        }}
        inputStyle={{ height: "56px", opacity: disabled ? "0.5" : "1" }}
        disabled={disabled}
        copyNumbersOnly={false}
      />
    </Box>
  );
};

export default PhoneInputField;
