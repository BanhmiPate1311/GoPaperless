import { Box, TextField, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

const Step9 = ({ onDisableSubmit, setErrorPG, emailRef }) => {
  const { t } = useTranslation();

  function isValidEmail(email) {
    return /\S+@\S+\.\S+/.test(email);
  }

  useEffect(() => {
    onDisableSubmit(true);
  }, []);

  const handleEmail = (event) => {
    if (!isValidEmail(event.target.value) || event.target.value.length === 0) {
      setErrorPG(t("electronicid.email invalid"));
      onDisableSubmit(true);
    } else {
      setErrorPG(null);
      onDisableSubmit(false);
    }

    // setEmail(event.target.value);
    emailRef.current = event.target.value;
  };
  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Enter your phone number to receive a verification code. */}
        {t("electronicid.step91")}
      </Typography>
      <Typography marginTop="26px" textAlign="center" fontSize="24px">
        {/* Phone Verification */}
        {t("electronicid.step92")}
      </Typography>

      <Box
        sx={{
          mt: 3,
          display: "flex",
          flexWrap: "wrap",
          fontFamily: "Montserrat, Nucleo, Helvetica, sans-serif",
          justifyContent: "center",
        }}
        autoComplete="off"
      >
        <TextField
          id="outlined-read-only-input"
          label={t("electronicid.step94")}
          // defaultValue={personalInfomation?.fullName}
          sx={{
            m: 1,
            width: "30ch",
            color: "#1976D2",
            // "& .MuiInputBase-input": {
            //   left: "40px",
            // },
          }}
          //   type="tel"
          type="email"
          //   inputRef={phoneNumberInputRef}
          autoComplete="new-password"
          InputLabelProps={{ shrink: true }} // shrink here
          inputProps={{
            style: {
              color: "#1976D2",
            },
            // maxLength: "16",
          }}
          onChange={handleEmail}
        />
      </Box>
    </Box>
  );
};

export default Step9;
