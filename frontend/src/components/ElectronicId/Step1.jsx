import { Box, Stack, Typography } from "@mui/material";
import React from "react";
import { useTranslation } from "react-i18next";
import { ReactComponent as CardReader } from "../../assets/images/cardreader.svg";
import { ReactComponent as CCard } from "../../assets/images/ccard.svg";
import { ReactComponent as Connectdevice } from "../../assets/images/connectdevice.svg";
import { ReactComponent as Light } from "../../assets/images/light.svg";
import { ReactComponent as Wifi } from "../../assets/images/wifi.svg";

export const Step1 = ({ isIdentifyRegistered }) => {
  console.log("isIdentifyRegistered: ", isIdentifyRegistered);
  const { t } = useTranslation();
  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Get a Qualified Electronic Signature in minutes. */}
        {t("electronicid.step11")}
      </Typography>

      <Typography fontSize={14} my="16px">
        {/* Make sure you have the following before starting: */}
        {t("electronicid.step12")}
      </Typography>

      <Stack direction="row" alignItems="center" mb="12px">
        <Stack
          direction="row"
          justifyContent="center"
          alignItems="center"
          minWidth={40}
          width={40}
          height={40}
          borderRadius="50%"
          mr="12px"
          bgcolor="rgb(243, 245, 248)"
        >
          <Light />
        </Stack>
        <Typography fontSize="14px"> {t("electronicid.step13")}</Typography>
      </Stack>

      {!isIdentifyRegistered && (
        <Stack direction="row" alignItems="center" mb="12px">
          <Stack
            direction="row"
            justifyContent="center"
            alignItems="center"
            width={40}
            height={40}
            borderRadius="50%"
            mr="12px"
            bgcolor="rgb(243, 245, 248)"
            minWidth={40}
          >
            <CCard />
          </Stack>
          <Typography>
            {/* A valid document in its original form to verify your identity */}
            {t("electronicid.step14")}
          </Typography>
        </Stack>
      )}

      <Stack direction="row" alignItems="center" mb="12px">
        <Stack
          direction="row"
          justifyContent="center"
          alignItems="center"
          minWidth={40}
          width={40}
          height={40}
          borderRadius="50%"
          mr="12px"
          bgcolor="rgb(243, 245, 248)"
        >
          <Wifi />
        </Stack>
        <Typography fontSize="14px">{t("electronicid.step15")}</Typography>
      </Stack>

      <Stack direction="row" alignItems="center" mb="12px">
        <Stack
          direction="row"
          justifyContent="center"
          alignItems="center"
          minWidth={40}
          width={40}
          height={40}
          borderRadius="50%"
          mr="12px"
          bgcolor="rgb(243, 245, 248)"
        >
          <Connectdevice />
        </Stack>
        <Typography fontSize="14px">{t("electronicid.step16")}</Typography>
      </Stack>

      {!isIdentifyRegistered && (
        <Stack direction="row" alignItems="center" mb="12px">
          <Stack
            direction="row"
            justifyContent="center"
            alignItems="center"
            width={40}
            height={40}
            borderRadius="50%"
            mr="12px"
            bgcolor="rgb(243, 245, 248)"
            minWidth={40}
          >
            <CardReader />
          </Stack>
          <Typography>
            {/* A valid document in its original form to verify your identity */}
            {t("electronicid.step17")}
          </Typography>
        </Stack>
      )}
    </Box>
  );
};

export default Step1;
