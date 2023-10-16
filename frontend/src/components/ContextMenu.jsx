import { Dns, People, PermMedia, Public } from "@mui/icons-material";
import {
  Box,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import { styled, ThemeProvider, createTheme } from "@mui/material/styles";
import React from "react";
import { ReactComponent as Signature } from "../assets/images/contextmenu/signature.svg";
import { ReactComponent as Initial } from "../assets/images/contextmenu/initial.svg";
import { ReactComponent as Name } from "../assets/images/contextmenu/name.svg";
import { ReactComponent as Email } from "../assets/images/contextmenu/email.svg";
import { ReactComponent as JobTitle } from "../assets/images/contextmenu/jobtitle.svg";
import { ReactComponent as Company } from "../assets/images/contextmenu/company.svg";
import { ReactComponent as Date } from "../assets/images/contextmenu/date.svg";
import { ReactComponent as TextField } from "../assets/images/contextmenu/textfield.svg";
import { ReactComponent as TextArea } from "../assets/images/contextmenu/textarea.svg";
import { ReactComponent as RadioButton } from "../assets/images/contextmenu/radiobutton.svg";
import { ReactComponent as CheckBox } from "../assets/images/contextmenu/checkbox.svg";
import { ReactComponent as QRCode } from "../assets/images/contextmenu/qrcode.svg";
import { ReactComponent as AddText } from "../assets/images/contextmenu/addtext.svg";
import { useTranslation } from "react-i18next";

const ContextMenu = ({ x, y, onClose, onMenuItemClick }) => {
  const { t } = useTranslation();

  const data = [
    {
      icon: <Signature />,
      label: t("context-menu.signature"),
      value: "Signature",
    },
    { icon: <Initial />, label: t("context-menu.initials"), value: "Initial" },
    { icon: <Name />, label: t("context-menu.name"), value: "Name" },
    { icon: <Email />, label: t("context-menu.email"), value: "Email" },
    {
      icon: <JobTitle />,
      label: t("context-menu.jobtitle"),
      value: "JobTitle",
    },
    { icon: <Company />, label: t("context-menu.company"), value: "Company" },
    // { icon: <Date />, label: "Date" },
    // { icon: <TextField />, label: "Text Field" },
    // { icon: <TextArea />, label: "Text Area" },
    // { icon: <RadioButton />, label: "Radio Button" },
    // { icon: <CheckBox />, label: "Check Box" },
    { icon: <QRCode />, label: t("context-menu.qrcode"), value: "QRCode" },
    { icon: <AddText />, label: t("context-menu.addtext"), value: "AddText" },
  ];

  return (
    // <div className="context-menu" style={{ left: x, top: y }}>
    //   <ul>
    //     <li onClick={() => onMenuItemClick("Mục 1")}>Mục 1</li>
    //     <li onClick={() => onMenuItemClick("Mục 2")}>Mục 2</li>
    //     <li onClick={() => onMenuItemClick("Mục 3")}>Mục 3</li>
    //   </ul>
    //   <button onClick={onClose}>Đóng</button>
    // </div>
    <Box
      sx={{
        pb: 1,
        left: x,
        top: y,
        color: "#1F2937",
      }}
      className="context-menu"
    >
      {data.map((item) => (
        <ListItemButton
          key={item.label}
          sx={{
            py: 0,
            minHeight: 32,
            color: "rgba(255,255,255,.8)",
            "& .MuiListItemIcon-root": {
              minWidth: "29px",
            },
          }}
          onClick={(e) => onMenuItemClick(item.value, e)}
        >
          <ListItemIcon sx={{ color: "inherit" }}>{item.icon}</ListItemIcon>
          <ListItemText
            primary={item.label}
            primaryTypographyProps={{
              fontSize: 14,
              fontWeight: "medium",
              color: "#1F2937",
            }}
          />
        </ListItemButton>
      ))}
    </Box>
  );
};

export default ContextMenu;
