import React from "react";
import facescan from "../../assets/images/facescan.jpg";
import { useTranslation } from "react-i18next";
import { Box, Typography } from "@mui/material";
export const Step5 = () => {
  const { t } = useTranslation();
  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Please smile and follow the prompts below. */}
        {t("electronicid.step51")}
      </Typography>
      <Typography marginTop="16px" fontSize="24px" textAlign="center">
        {/* Scan Face */}
        {t("electronicid.step52")}
      </Typography>
      <Typography fontSize="14px" marginBottom="10px" textAlign="center">
        {/* Please, look to the camera to scan your face */}
        {t("electronicid.step53")}
      </Typography>
      <Box width={376} marginX="auto">
        <img
          src={facescan}
          width="100%"
          //   height={250}
          style={{ borderRadius: "50%" }}
          alt="chip"
        />
      </Box>
    </Box>
  );
};

export default Step5;
