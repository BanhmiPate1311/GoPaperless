import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import React from "react";
import { useTranslation } from "react-i18next";

const ComboBoxField = ({
  data,
  value,
  handleMenuItemChange,
  disabled = false,
  valueExtractorValue,
  valueExtractorText,
}) => {
  const { t } = useTranslation();
  return (
    <FormControl fullWidth>
      <InputLabel id="demo-simple-select-label">
        {t("electronic.document type")}
      </InputLabel>
      <Select
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        value={value}
        onChange={handleMenuItemChange}
        label={t("electronic.document type")}
        sx={{ width: "300px" }}
        inputProps={{
          name: "search-criteria",
          id: "search-criteria",
        }}
        disabled={disabled}
      >
        {data?.map((item, index) => (
          <MenuItem key={index} value={valueExtractorValue(item)}>
            {valueExtractorText(item)}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default ComboBoxField;
