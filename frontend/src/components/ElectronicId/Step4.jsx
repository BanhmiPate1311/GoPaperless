import { Box, Grid, Typography } from "@mui/material";

import TextField from "@mui/material/TextField";
import React from "react";
import { useTranslation } from "react-i18next";
export const Step4 = ({ image, personalInfomation }) => {
  const { t } = useTranslation();
  // const detail = personalInfomation?.optionalDetails;
  return (
    <Box color="#26293F">
      <Typography
        fontSize="24px"
        fontWeight={600}
        lineHeight="36px"
        textAlign="center"
      >
        {/* Personal Information */}
        {t("electronicid.step41")}
      </Typography>

      <Box
        width="120px"
        height="120px"
        marginX="auto"
        borderRadius="50%"
        overflow="hidden"
      >
        <img
          src={`data:image/png;base64,${image}`}
          style={{
            width: "100%",
            height: "100%",
            objectFit: "cover", // Để hình ảnh không bị méo
          }}
          alt="image123"
        />
      </Box>

      <Box sx={{ flexGrow: 1 }} mt={1}>
        <Grid container spacing={1.5}>
          <Grid item xs={12}>
            <TextField
              id="outlined-read-only-input"
              label={t("electronicid.step42")}
              defaultValue={personalInfomation?.fullName}
              sx={{ width: "100%" }}
              InputProps={{
                readOnly: true,
              }}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              id="outlined-read-only-input"
              label={t("electronicid.step43")}
              defaultValue={personalInfomation?.gender}
              sx={{ width: "100%" }}
              InputProps={{
                readOnly: true,
              }}
            />
          </Grid>
          <Grid item xs={6}>
            <TextField
              id="outlined-read-only-input"
              label={t("electronicid.step44")}
              defaultValue={personalInfomation?.birthDate}
              sx={{ width: "100%" }}
              InputProps={{
                readOnly: true,
              }}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="outlined-read-only-input"
              label={t("electronicid.step45")}
              defaultValue={personalInfomation?.personalNumber}
              sx={{ width: "100%" }}
              InputProps={{
                readOnly: true,
              }}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="outlined-read-only-input"
              label={t("electronicid.step46")}
              defaultValue={personalInfomation?.nationality}
              sx={{ width: "100%" }}
              InputProps={{
                readOnly: true,
              }}
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              id="outlined-read-only-input"
              label={t("electronicid.step47")}
              defaultValue={personalInfomation?.placeOfOrigin}
              sx={{ width: "100%" }}
              InputProps={{
                readOnly: true,
              }}
            />
          </Grid>
        </Grid>
      </Box>
    </Box>
  );
};

export default Step4;
