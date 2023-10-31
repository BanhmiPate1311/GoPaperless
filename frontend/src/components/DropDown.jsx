import React, { useEffect, useState } from "react";
import { Button, Menu, MenuItem } from "@mui/material";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import i18n from "i18next";
import { useLocation } from "react-router-dom";

function Dropdown({ color }) {
  const location = useLocation();
  const isValidationPath = location.pathname.includes("/validation");

  const [anchorEl, setAnchorEl] = useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  let lang = localStorage.getItem("language");
  if (!lang) {
    lang = "English";
    localStorage.setItem("language", "English");
  }
  // const [language, setLanguage] = useState(localStorage.getItem("language"));

  useEffect(() => {
    if (lang) {
      // setLanguage(lang);
      switch (lang) {
        case "English":
          i18n.changeLanguage("0");
          break;
        case "Vietnamese":
          i18n.changeLanguage("1");
          break;
        case "Germany":
          i18n.changeLanguage("2");
          break;
        case "China":
          i18n.changeLanguage("3");
          break;
        case "Estonian":
          i18n.changeLanguage("4");
          break;
        case "Russian":
          i18n.changeLanguage("5");
          break;
        default:
          break;
      }
    }
  }, []);

  const handleLanguage = (lang) => {
    // setLanguage(lang);
    handleClose();
    switch (lang) {
      case "English":
        i18n.changeLanguage("0");
        break;
      case "Vietnamese":
        i18n.changeLanguage("1");
        break;
      case "Germany":
        i18n.changeLanguage("2");
        break;
      case "China":
        i18n.changeLanguage("3");
        break;
      case "Estonian":
        i18n.changeLanguage("4");
        break;
      case "Russian":
        i18n.changeLanguage("5");
        break;
      default:
        break;
    }
    localStorage.setItem("language", lang);
  };
  return (
    <div style={{ marginTop: "0px !important" }}>
      <Button
        aria-controls="simple-menu"
        aria-haspopup="true"
        onClick={handleClick}
        endIcon={<ArrowDropDownIcon />}
        style={{ color: color, zIndex: "2" }}
        disabled={isValidationPath}
      >
        {lang}
      </Button>
      <Menu
        id="simple-menu"
        anchorEl={anchorEl}
        keepMounted
        open={Boolean(anchorEl)}
        onClose={handleClose}
      >
        <MenuItem onClick={() => handleLanguage("Vietnamese")}>
          Vietnamese
        </MenuItem>
        <MenuItem onClick={() => handleLanguage("English")}>English</MenuItem>
        <MenuItem onClick={() => handleLanguage("Germany")}>Germany</MenuItem>
        <MenuItem onClick={() => handleLanguage("China")}>China</MenuItem>
        <MenuItem onClick={() => handleLanguage("Estonian")}>Estonian</MenuItem>
        <MenuItem onClick={() => handleLanguage("Russian")}>Russian</MenuItem>
      </Menu>
    </div>
  );
}

export default Dropdown;
