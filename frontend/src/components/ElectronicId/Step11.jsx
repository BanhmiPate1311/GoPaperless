import { AccountCircle, Email, Phone } from "@mui/icons-material";
import {
  Box,
  FormControl,
  FormGroup,
  InputAdornment,
  InputLabel,
  ListItemIcon,
  ListItemSecondaryAction,
  MenuItem,
  Select,
  Stack,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { api } from "../../constants/api";
import { useTranslation } from "react-i18next";

const Step11 = ({ onDisableSubmit, providerSelected, isFetching }) => {
  const { t } = useTranslation();
  const [isChecked1, setIsChecked1] = useState(false);
  const [isChecked2, setIsChecked2] = useState(false);

  const [selectedOption, setSelectedOption] = useState("");

  const handleCheckbox1Change = (event) => {
    setIsChecked1(event.target.checked);
    // Truyền giá trị isSubmitDisabled lên component cha
    onDisableSubmit(
      !(event.target.checked && isChecked2 && selectedOption !== "")
    );
  };

  const handleCheckbox2Change = (event) => {
    setIsChecked2(event.target.checked);
    // Truyền giá trị isSubmitDisabled lên component cha
    onDisableSubmit(
      !(event.target.checked && isChecked1 && selectedOption !== "")
    );
  };

  useEffect(() => {
    onDisableSubmit(!(isChecked1 && isChecked2 && selectedOption !== ""));
  }, [isChecked1, isChecked2, selectedOption]);

  const handleChange = (event) => {
    setSelectedOption(event.target.value);
    providerSelected.current = event.target.value;
  };

  const [options, SetOptions] = useState([]);

  const getConnectorName = async (param) => {
    try {
      const response = await api.post("/base64Logo", { param });
      SetOptions(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getConnectorName("SMART_ID_SIGNING");
  }, []);
  useEffect(() => {
    onDisableSubmit(true);
  }, []);

  return (
    <Stack fontSize="24px" justifyContent="space-between" height="100%">
      <Box>
        <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
          {/* Your identity has been verified. */}
          {t("electronicid.step111")}
        </Typography>

        <Typography fontSize="14px" my="16px">
          {/* Please accept our certification terms to sign the document. */}
          {t("electronicid.step112")}
        </Typography>

        <FormGroup>
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
                /* Đổ màu cho ô input */
                accentColor: "#1976D2",
              }}
            />
            <div className="css-192euok ">
              <div className="css-19zaiad">
                {/* I have read the{" "} */}
                {t("electronicid.step113")}{" "}
                <Link to="https://rssp.mobile-id.vn/vi/privacy" target="_blank">
                  {/* Certification Practices Statement. */}
                  {t("electronicid.step114")}
                </Link>
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
                /* Đổ màu cho ô input */
                accentColor: "#1976D2",
              }}
            />
            <div className="css-192euok ">
              <div className="css-19zaiad">
                {/* I agree to the{" "} */}
                {t("electronicid.step113")}{" "}
                <Link to="https://rssp.mobile-id.vn/vi/terms" target="_blank">
                  {/* Terms and Conditions */}
                  {t("electronicid.step115")}
                </Link>{" "}
                {/* on the Video Identification Process. */}
                {t("electronicid.step116")}
              </div>
            </div>
          </label>
        </FormGroup>
      </Box>

      <Box sx={{ fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif" }}>
        <FormControl
          fullWidth
          sx={{
            marginTop: "20px",
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif !important",
          }}
        >
          <InputLabel
            id="demo-simple-select-label"
            sx={{
              fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
            }}
          >
            {/* Select Remote Signing Service Provider */}
            {t("electronicid.step117")}
          </InputLabel>
          <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={selectedOption}
            label={t("electronicid.step117")}
            onChange={handleChange}
            sx={{
              "& .MuiListItemSecondaryAction-root": {
                right: "30px",
              },
              fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
            }}
            disabled={!isChecked1 || !isChecked2 || isFetching}
          >
            {options.length > 0 &&
              options.map((val, index) => (
                <MenuItem key={index} value={val.connector_name}>
                  {val.remark}
                  <ListItemSecondaryAction>
                    <img src={val.logo} height="25" alt="logo" />
                  </ListItemSecondaryAction>
                </MenuItem>
              ))}
          </Select>
        </FormControl>
      </Box>
    </Stack>
  );
};

export default Step11;
