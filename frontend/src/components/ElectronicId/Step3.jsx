import React from "react";
import checkid from "../../assets/images/checkid.png";
import { useTranslation } from "react-i18next";
import { Box, Typography } from "@mui/material";
export const Step3 = () => {
  const { t } = useTranslation();
  return (
    <Box color="#26293F" textAlign="center">
      <Typography
        fontSize="24px"
        fontWeight={600}
        lineHeight="36px"
        textAlign="center"
      >
        {/* Read the document chip */}
        {t("electronicid.step31")}
      </Typography>
      <Typography marginTop="16px" fontSize="24px">
        {/* Insert/place the document on the card reader. */}
        {t("electronicid.step32")}
      </Typography>
      <Typography marginBottom="20px" fontSize="14px">
        {/* Do not move while reading in progress */}
        {t("electronicid.step33")}
      </Typography>
      <Box
        style={{ textAlign: "center" }}
        width="250px"
        height="250px"
        marginX="auto"
      >
        <img
          src={checkid}
          width="100%"
          height="100%"
          style={{ borderRadius: "50%" }}
          alt="chip"
        />
      </Box>
    </Box>
  );
};

export default Step3;
