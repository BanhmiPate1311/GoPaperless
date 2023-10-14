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
const UsbModalField = ({
  title,
  subtitle,
  open,
  scroll,
  errorPG,
  handleClose,
  urlWithoutProtocol,
  data,
  handleModalClick,
  handleModalChange,
  disabled = false,
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

  const handleChange = (event, nextView) => {
    if (nextView !== null) {
      handleModalChange(nextView);
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
      scroll={scroll}
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
          {errorPG && (
            <Typography className="text-danger mt-3 mb-0 text-center fw-bold">
              {errorPG}
            </Typography>
          )}
          <ToggleButtonGroup
            fullWidth
            orientation="vertical"
            value={value}
            exclusive
            onChange={handleChange}
          >
            {data.map((value, index) => (
              <ToggleButtonStyle
                sx={{ textTransform: "capitalize" }}
                value={index}
                aria-label="list"
                key={index}
                onDoubleClick={handleModalClick}
              >
                <Stack
                  direction="row"
                  alignItems="center"
                  sx={{ width: "100%" }}
                >
                  <Box width={50} height={50} mr={1}>
                    <Card />
                  </Box>
                  <Box flexGrow={1} textAlign="left">
                    <Typography fontWeight="bold" fontSize="14px">
                      {value.subject.commonName}
                    </Typography>
                    <Typography fontSize="14px">
                      {t("usb.usb9")}
                      {value.issuer.commonName}
                    </Typography>
                    <Typography fontSize="14px">
                      {t("usb.usb11")}{" "}
                      {moment(value.validFrom).format("DD/MM/YYYY")}{" "}
                      {t("usb.usb12")}{" "}
                      {moment(value.validTo).format("DD/MM/YYYY")}
                    </Typography>
                  </Box>
                </Stack>
              </ToggleButtonStyle>
            ))}
          </ToggleButtonGroup>
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
          onClick={handleModalClick}
          style={{
            backgroundColor: "#013f94",
            color: "#fff",
            textTransform: "capitalize",
            fontFamily: "Montserrat,Nucleo,Helvetica,sans-serif",
          }}
          disabled={disabled}
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

export default UsbModalField;
