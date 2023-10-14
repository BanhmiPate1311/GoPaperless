import React, { useEffect, useRef } from "react";
import intlTelInput from "intl-tel-input";
import "intl-tel-input/build/css/intlTelInput.css";
import { useTranslation } from "react-i18next";
import { Box, TextField, Typography } from "@mui/material";

const Step12 = ({ phoneNumberRef }) => {
  const { t } = useTranslation();
  const phoneNumberInputRef = useRef(null);
  // const phoneNumber = "84901790767";
  const maskedPhoneNumber = () => {
    const hiddenPart = phoneNumberRef.current
      .substring(
        phoneNumberRef.current.length - 7,
        phoneNumberRef.current.length - 2
      )
      .replace(/\d/g, "*");
    return (
      "+" +
      phoneNumberRef.current.slice(0, phoneNumberRef.current.length - 7) +
      hiddenPart +
      phoneNumberRef.current.slice(-2)
    );
  };

  useEffect(() => {
    // When showPersonalCode changes, reset personalCode to empty string

    const phoneNumberInput = phoneNumberInputRef.current;
    if (!phoneNumberInput) {
      return;
    }
    const countryCode = intlTelInput(phoneNumberInput, {
      initialCountry: "vn",
    });
  }, []);

  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* You are ready to sign the document. */}
        {t("electronicid.step121")}
      </Typography>

      <Typography fontSize="14px" mb="50px" mt="16px">
        {/* Confirm your phone number in order to sign the document with a Qualified
        Electronic Signature. */}
        {t("electronicid.step122")}
      </Typography>

      <Box
        sx={{
          display: "flex",
          flexWrap: "wrap",
          fontFamily: "Montserrat, Nucleo, Helvetica, sans-serif",
          justifyContent: "center",
        }}
        autoComplete="off"
      >
        <TextField
          id="outlined-read-only-input"
          label={t("smartID.phoneNumber")}
          // defaultValue={personalInfomation?.fullName}
          sx={{
            m: 1,
            width: "25ch",
            color: "#1976D2",
            // "& .MuiInputBase-input": {
            //   left: "40px",
            // },
          }}
          type="tel"
          inputRef={phoneNumberInputRef}
          value={maskedPhoneNumber()}
          // maxLength="16"
          autoComplete="new-password"
          InputLabelProps={{ shrink: true }}
          InputProps={{
            readOnly: true,
          }}
          // onChange={handlePhoneNumber}
          inputProps={{
            style: {
              color: "#1976D2",
            },
          }}
        />
      </Box>
    </Box>
  );
};

export default Step12;
