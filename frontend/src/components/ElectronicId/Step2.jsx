import {
  Box,
  Checkbox,
  FormControlLabel,
  FormGroup,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";
export const Step2 = ({ onDisableSubmit }) => {
  const [isChecked1, setIsChecked1] = useState(false);
  const [isChecked2, setIsChecked2] = useState(false);
  const { t } = useTranslation();
  const handleCheckbox1Change = (event) => {
    setIsChecked1(event.target.checked);
    // Truyền giá trị isSubmitDisabled lên component cha
    onDisableSubmit(!(event.target.checked && isChecked2));
  };

  const handleCheckbox2Change = (event) => {
    setIsChecked2(event.target.checked);
    // Truyền giá trị isSubmitDisabled lên component cha
    onDisableSubmit(!(event.target.checked && isChecked1));
  };

  useEffect(() => {
    onDisableSubmit(!(isChecked1 && isChecked2));
  }, [isChecked1, isChecked2]);

  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Please accept provider’s terms of service. */}
        {t("electronicid.step21")}
      </Typography>

      <FormGroup>
        <FormControlLabel
          control={
            <Checkbox
              size="small"
              sx={{
                paddingTop: "0",
                color: "#26293F",
                "&.Mui-checked": {
                  color: "#0d6efd",
                },
              }}
              checked={isChecked1}
              onChange={handleCheckbox1Change}
            />
          }
          label={
            <Typography component="span" fontSize="14px">
              {t("electronicid.step22")}{" "}
              <Link
                to="https://www.dokobit.com/terms/ElectronicId-privacy-policy"
                target="_blank"
              >
                {/* Privacy Policy */}
                {t("electronicid.step23")}
              </Link>{" "}
              {t("electronicid.step24")}
            </Typography>
          }
          sx={{ alignItems: "flex-start", marginTop: "16px" }}
        />
        <FormControlLabel
          control={
            <Checkbox
              size="small"
              sx={{
                paddingTop: "0",
                color: "#26293F",
                "&.Mui-checked": {
                  color: "#0d6efd",
                },
              }}
              checked={isChecked2}
              onChange={handleCheckbox2Change}
            />
          }
          label={
            <Typography component="span" fontSize="14px">
              {t("electronicid.step25")}{" "}
              <Link
                to="https://www.dokobit.com/terms/ElectronicId-terms-and-conditions-of-the-video-identification-process"
                target="_blank"
              >
                {/* Terms and Conditions */}
                {t("electronicid.step26")}
              </Link>{" "}
              {/* on the Video Identification Process. */}
              {t("electronicid.step27")}
            </Typography>
          }
          sx={{ alignItems: "flex-start", marginTop: "16px" }}
        />
      </FormGroup>

      {/* <FormGroup>
        <label
          className="d-flex align-items-start"
          style={{ marginTop: "16px", cursor: "pointer" }}
          onChange={handleCheckbox1Change}
        >
          <input
            type="checkbox"
            style={{
              marginTop: "4px",
              marginRight: "15px",
              size: "16px",

              accentColor: "#1976D2",
            }}
          />
          <div className="css-192euok ">
            <div className="css-19zaiad">
              {t("electronicid.step22")}{" "}
              <Link
                to="https://www.dokobit.com/terms/ElectronicId-privacy-policy"
                target="_blank"
              >
                {t("electronicid.step23")}
              </Link>{" "}
              {t("electronicid.step24")}
            </div>
          </div>
        </label>

        <label
          className="d-flex align-items-start"
          style={{ marginTop: "16px", cursor: "pointer" }}
          onChange={handleCheckbox2Change}
        >
          <input
            type="checkbox"
            style={{
              marginTop: "4px",
              marginRight: "15px",
              size: "16px",

              accentColor: "#1976D2",
            }}
          />
          <div className="css-192euok ">
            <div className="css-19zaiad">
              {t("electronicid.step25")}{" "}
              <Link
                to="https://www.dokobit.com/terms/ElectronicId-terms-and-conditions-of-the-video-identification-process"
                target="_blank"
              >
                {t("electronicid.step26")}
              </Link>{" "}
              {t("electronicid.step27")}
            </div>
          </div>
        </label>
      </FormGroup> */}
    </Box>
  );
};

export default Step2;
