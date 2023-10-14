import { Box, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import PhoneInputField from "../form/phone_input_field";

export const Step7 = ({ onDisableSubmit, phoneNumberRef }) => {
  const { t } = useTranslation();

  const [phoneNumber, setPhoneNumber] = useState("");

  const checkParam = (value, condition) => {
    if (value.length >= condition) {
      onDisableSubmit(false);
    } else {
      onDisableSubmit(true);
    }
  };

  const handlePhoneNumber = (phone, country) => {
    setPhoneNumber(phone);
    phoneNumberRef.current = phone;
    checkParam(phone, 11);
  };

  useEffect(() => {
    onDisableSubmit(true);
  }, []);
  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Enter your phone number to receive a verification code. */}
        {t("electronicid.step71")}
      </Typography>
      <Typography marginTop="26px" textAlign="center" fontSize="24px">
        {/* Phone Verification */}
        {t("electronicid.step72")}
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
        <PhoneInputField
          phoneNumber={phoneNumber}
          setPhoneNumber={setPhoneNumber}
          handlePhoneNumber={handlePhoneNumber}
        />
      </Box>
    </Box>
  );
};

export default Step7;
