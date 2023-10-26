import { Box, Tab, Tabs, Typography } from "@mui/material";
import React, { useEffect } from "react";
import PropTypes from "prop-types";
import { useState } from "react";
import { createValidIcon, createValidLabel } from "../../ultis/commonFunction";
import Overview from "./Overview";
import Signatures from "./Signatures";
import Details from "./Details";
import { useTranslation } from "react-i18next";

function TabPanel(props) {
  const { children, value, index, quantity, status, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`vertical-tabpanel-${index}`}
      aria-labelledby={`vertical-tab-${index}`}
      {...other}
      style={{ width: "392px" }}
    >
      {value === index && (
        <Box>
          <Typography component="div">{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `vertical-tab-${index}`,
    "aria-controls": `vertical-tabpanel-${index}`,
  };
}

const TabDocument = ({ validFile }) => {
  const filteredObject = Object.keys(validFile).reduce((result, key) => {
    // Kiểm tra nếu giá trị của key không phải là mảng rỗng thì thêm vào object kết quả
    if (
      !Array.isArray(validFile[key]) ||
      validFile[key].length > 0 ||
      key === "signatures"
    ) {
      result[key] = validFile[key];
    }
    return result;
  }, {});

  const tabName = Object.keys(filteredObject).filter(
    (tab) => tab !== "file" && tab !== "lang"
  );

  const [value, setValue] = useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const [expanded, setExpanded] = useState("panel");

  const handleChangeShow = (panel) => (event, isExpanded) => {
    setExpanded(isExpanded ? panel : true);
  };

  useEffect(() => {
    const resizeHandler = () => {
      const viewerContainer = document.getElementById("cookieSetting");
      if (viewerContainer) {
        const windowHeight = window.innerHeight;
        const offsetTop = viewerContainer.offsetTop;
        const viewerHeight = windowHeight - offsetTop;
        viewerContainer.style.height = viewerHeight + "px";
      }
    };
    resizeHandler();
    window.addEventListener("resize", resizeHandler);
    return () => {
      window.removeEventListener("resize", resizeHandler);
    };
  }, []);
  return (
    <Box
      sx={{
        flexGrow: 1,
        bgcolor: "background.paper",
        display: "flex",
        flexDirection: "row-reverse",
        borderRadius: "20px",
      }}
    >
      <Tabs
        id="cookieSetting"
        orientation="vertical"
        variant="scrollable"
        value={value}
        onChange={handleChange}
        aria-label="Vertical tabs example"
        sx={{
          borderLeft: 1,
          borderColor: "divider",
          "& .css-9x166v-MuiButtonBase-root-MuiTab-root.Mui-selected ": {
            color: "#000000",
            // marginRight: '20px',
            // marginLeft: '5px',
            // marginTop: '5px',
            // borderRadius: '10px',
            background: " rgb(232, 235, 240)",
          },

          "& .css-10d9dml-MuiTabs-indicator": {
            background: "transparent",
          },
        }}
      >
        {tabName.map((val, index) => {
          return (
            <Tab
              key={index}
              sx={{ textTransform: "capitalize", fontFamily: "Montserrat" }}
              icon={createValidIcon(val)}
              // label="with Mobile-ID"
              // label={createValidLabel(val)}
              label={createValidLabel(val)}
              {...a11yProps(index)}
            />
          );
        })}
        {/* <Tab
          icon={<InsertDriveFileOutlinedIcon />}
          label="Overview"
          sx={{ textTransform: "capitalize", fontFamily: "Montserrat" }}
          {...a11yProps(0)}
        />
        <Tab
          icon={<GroupOutlinedIcon />}
          label="Signatures"
          sx={{ textTransform: "capitalize", fontFamily: "Montserrat" }}
          {...a11yProps(1)}
        />
        <Tab
          icon={<DescriptionOutlinedIcon />}
          label="Details"
          sx={{ textTransform: "capitalize", fontFamily: "Montserrat" }}
          {...a11yProps(2)}
        /> */}
      </Tabs>

      <Box
        component="div"
        sx={{ display: "flex", flexDirection: "column", width: "100%" }}
      >
        {tabName[value] === "overview" && (
          <TabPanel value={value} index={value}>
            <Overview validFile={validFile.overview} />
          </TabPanel>
        )}
        {tabName[value] === "signatures" && (
          <TabPanel value={value} index={value}>
            <Signatures validFile={validFile.signatures} signType="Signature" />
          </TabPanel>
        )}

        {tabName[value] === "seals" && (
          <TabPanel value={value} index={value}>
            <Signatures validFile={validFile.seals} signType="Seal" />
          </TabPanel>
        )}
        {tabName[value] === "details" && (
          <TabPanel value={value} index={value}>
            <Details
              validFile={validFile.details}
              notSign={
                validFile.signatures.length === 0 &&
                validFile.seals.length === 0
              }
            />
          </TabPanel>
        )}
      </Box>
    </Box>
  );
};

export default TabDocument;
