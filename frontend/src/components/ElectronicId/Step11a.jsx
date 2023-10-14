import {
  Box,
  Button,
  ButtonGroup,
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
  Stack,
  Typography,
} from "@mui/material";
import React, { Fragment, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";

const Step11a = ({ certificateList, setCertificate }) => {
  const { t } = useTranslation();
  const [value, setValue] = useState("certs");

  const handleChange = (event) => {
    setValue(event.target.value);
  };
  const [cerSelected, setCerSelected] = useState(0);
  const handleTokenSelected = (index) => {
    setCerSelected(index);
  };
  useEffect(() => {
    if (value === "none") {
      setCertificate(null);
    } else {
      setCertificate(certificateList[cerSelected]);
    }
  }, [value]);
  return (
    <Box>
      <Stack>
        <Typography
          className="modal-title fw-bold"
          style={{ color: "#2978eb", fontSize: "30px" }}
          id="exampleModalToggleLabel2"
        >
          {t("usb.usb3")}
        </Typography>
      </Stack>
      <Typography
        style={{
          color: "#000",
          fontSize: "14px",
          fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
        }}
      >
        {t("usb.usb4")}
      </Typography>

      <FormControl fullWidth>
        {/* <FormLabel id="demo-controlled-radio-buttons-group">Gender</FormLabel> */}
        <RadioGroup
          aria-labelledby="demo-controlled-radio-buttons-group"
          name="controlled-radio-buttons-group"
          value={value}
          onChange={handleChange}
        >
          <FormControlLabel
            value="certs"
            control={<Radio />}
            label={t("electronicid.step11a1")}
            sx={{
              "& .MuiFormControlLabel-label": {
                fontWeight: "bold !important",
              },
              color: "#2978EB",
            }}
          />
          {/* button group ở đây */}
          {/* <ButtonGroup disabled={value !== "certs"}>
              <Button>Option 1</Button>
              <Button>Option 2</Button>
              <Button>Option 3</Button>
            </ButtonGroup> */}
          <Typography
            component="div"
            id="transition1-modal-description"
            sx={{
              overflowY: "auto",
              maxHeight: 300,
              opacity: value !== "certs" ? 0.5 : 1, // Thay đổi opacity tùy thuộc vào giá trị của value
              pointerEvents: value !== "certs" ? "none" : "auto", // Vô hiệu hóa sự kiện chạm và click tùy thuộc vào giá trị của value
            }}
            disabled={value !== "certs"}
          >
            <div
              className="btn-group-vertical w-100"
              role="group"
              aria-label="Vertical radio toggle button group"
            >
              {certificateList?.map(function (cer, index) {
                return (
                  <Fragment key={index}>
                    <input
                      type="radio"
                      className="btn-check"
                      defaultValue={index}
                      name="vbtn-radio"
                      id={`vbtn-radio${index}`}
                      // autoComplete="off"
                      defaultChecked={index === 0}
                    />
                    <label
                      className="btn btn-outline-secondary my-1"
                      htmlFor={`vbtn-radio${index}`}
                      onClick={() => handleTokenSelected(index)}
                      // onDoubleClick={sign}
                    >
                      <div
                        className="d-flex align-items-center"
                        style={{ color: "#000" }}
                      >
                        <div className="mx-2">
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            width={50}
                            height={50}
                            fill="currentColor"
                            color="#2a61a7"
                            className="bi bi-person-vcard"
                            viewBox="0 0 16 16"
                          >
                            <path d="M5 8a2 2 0 1 0 0-4 2 2 0 0 0 0 4Zm4-2.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5ZM9 8a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4A.5.5 0 0 1 9 8Zm1 2.5a.5.5 0 0 1 .5-.5h3a.5.5 0 0 1 0 1h-3a.5.5 0 0 1-.5-.5Z" />
                            <path d="M2 2a2 2 0 0 0-2 2v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2H2ZM1 4a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1v8a1 1 0 0 1-1 1H8.96c.026-.163.04-.33.04-.5C9 10.567 7.21 9 5 9c-2.086 0-3.8 1.398-3.984 3.181A1.006 1.006 0 0 1 1 12V4Z" />
                          </svg>
                        </div>
                        <div className="text-start my-auto ms-2">
                          <p className="mb-0 fw-bold">{cer.subject} </p>
                          <p className="mb-0">
                            {t("usb.usb9")}
                            {cer.issuer}
                          </p>
                          <p className="mb-0">
                            {t("usb.usb11")} {cer.validFrom.split(" ")[0]}{" "}
                            {t("usb.usb12")} {cer.validTo.split(" ")[0]}
                          </p>
                        </div>
                      </div>
                    </label>
                  </Fragment>
                );
              })}
            </div>
          </Typography>
          <FormControlLabel
            value="none"
            control={<Radio />}
            label={t("electronicid.step11a2")}
            sx={{
              "& .MuiFormControlLabel-label": {
                fontWeight: "bold !important",
              },
              color: "#2978EB",
            }}
          />
        </RadioGroup>
      </FormControl>
    </Box>
  );
};

export default Step11a;
