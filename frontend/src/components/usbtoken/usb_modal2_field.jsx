import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Stack,
  ToggleButton,
  ToggleButtonGroup,
  Typography,
} from "@mui/material";
import React, { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import Dropdown from "../DropDown";
import { styled } from "@mui/material/styles";
import { ReactComponent as PostCard } from "../../assets/images/modal/post_card.svg";
import { ReactComponent as Card } from "../../assets/images/modal/card.svg";
import { ReactComponent as NextArrow } from "../../assets/images/modal/next_arrow.svg";
import moment from "moment";

const ToggleButtonStyle = styled(ToggleButton)({
  "&.Mui-selected, &.Mui-selected:hover": {
    border: "2px solid #0f6dca !important",
  },
  "&:not(.Mui-selected)": {
    // Đặt kiểu cho các phần tử không được chọn
    color: "#111", // tắt chức năng làm mờ của Mui
  },
  marginBottom: "4px",
  border: "1px solid gray !important",
});
const UsbModalField2 = ({
  title,
  subtitle,
  open,
  errorPG,
  handleClose,
  urlWithoutProtocol,
  data,
  handleModalClick,
  disabled = false,
  minLength,
  maxLength,
  value,
}) => {
  const { t } = useTranslation();
  const descriptionElementRef = useRef(null);
  useEffect(() => {
    if (open) {
      const { current: descriptionElement } = descriptionElementRef;
      if (descriptionElement !== null) {
        descriptionElement.focus();
      }
    }
  }, [open]);
  const [checkLength, setCheckLength] = useState(false);

  const handlePIN = (event) => {
    // setPinValue(event.target.value);
    value.current = event.target.value;
    if (
      event.target.value.length >= minLength &&
      event.target.value.length <= maxLength
    ) {
      setCheckLength(true);
    } else {
      setCheckLength(false);
    }
  };

  return (
    <Dialog
      sx={{
        "& .MuiDialog-container": {
          "& .MuiPaper-root": {
            width: "100%",
            maxWidth: "480px", // Set your width here
          },
          "& .MuiTypography-root": {
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          },
        },
      }}
      open={open}
      onClose={handleClose}
      aria-labelledby="scroll-dialog-title"
      aria-describedby="scroll-dialog-description"
    >
      <DialogTitle
        component="div"
        id="scroll-dialog-title"
        sx={{ padding: "16px" }}
      >
        <Stack direction="row" justifyContent="space-between">
          <Typography color="#2978eb" fontSize={30} fontWeight="bold">
            {title}
          </Typography>
          <Dropdown color={"#013f94"} />
        </Stack>
        <Typography color="#000" fontSize={14}>
          {subtitle}
        </Typography>
        <Stack direction="row" mt={1}>
          <Box width={23} height={23}>
            <PostCard />
          </Box>
          <Typography ml={1} color="#013F94" fontWeight="bold">
            {urlWithoutProtocol}
          </Typography>
        </Stack>
      </DialogTitle>
      <DialogContent sx={{ padding: "16px" }}>
        <DialogContentText
          id="scroll-dialog-description"
          ref={descriptionElementRef}
          tabIndex={-1}
          component="div"
        >
          <Stack
            direction="row"
            alignItems="center"
            sx={{
              width: "100%",
              border: "2px solid #0f6dca !important",
              padding: "16px",
              borderRadius: "5px",
              color: "#111",
            }}
          >
            <Box width={50} height={50} mr={1}>
              <Card />
            </Box>
            <Box flexGrow={1} textAlign="left">
              <Typography fontWeight="bold" fontSize="14px">
                {data.subject.commonName}
              </Typography>
              <Typography>
                {t("usb.usb9")}
                {data.issuer.commonName}
              </Typography>
              <Typography>
                {t("usb.usb11")} {moment(data.validFrom).format("DD/MM/YYYY")}{" "}
                {t("usb.usb12")} {moment(data.validTo).format("DD/MM/YYYY")}
              </Typography>
            </Box>
          </Stack>
          <Box className="text-center mx-auto mt-3">
            <Typography fontWeight="bold">{t("usb.usb10")}</Typography>

            <Box className="input" width="50%" marginX="auto">
              <input
                id="pinNumber"
                type="search"
                className="form-control mx-auto"
                style={{ width: "100%", textAlign: "center" }}
                // placeholder="PIN"
                aria-label="Username"
                aria-describedby="basic-addon1"
                autoComplete="off"
                disabled={disabled}
                onChange={handlePIN}
                onKeyDown={(e) => {
                  if (checkLength && e.key === "Enter") {
                    handleModalClick();
                  }
                }}
              />
            </Box>
            {errorPG && (
              <Typography className="text-danger mt-3 mb-0 text-center fw-bold">
                {errorPG}
              </Typography>
            )}
          </Box>
        </DialogContentText>
      </DialogContent>
      <DialogActions sx={{ justifyContent: "space-between", px: "16px" }}>
        <Button
          variant="secondary"
          onClick={handleClose}
          style={{
            backgroundColor: "rgb(240, 237, 234)",
            textTransform: "capitalize",
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          }}
        >
          {t("usb.usb5")}
        </Button>

        <Button
          variant="primary"
          onClick={() => handleModalClick(data)}
          style={{
            backgroundColor: "#013f94",
            color: "#fff",
            textTransform: "capitalize",
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          }}
          disabled={!checkLength || disabled}
        >
          <Stack alignItems="center" width={18} height={18}>
            <NextArrow />
          </Stack>
          <Typography>{t("usb.usb6")}</Typography>
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UsbModalField2;
