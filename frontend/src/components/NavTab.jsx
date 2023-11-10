import React, { memo, useEffect, useMemo, useState } from "react";
import PropTypes from "prop-types";
import Typography from "@mui/material/Typography";
import { Box, Tab, Tabs } from "@mui/material";
import Field from "./Field";
import {
  checkClassName,
  createCard,
  createLabel,
} from "../ultis/commonFunction";
import { useTranslation } from "react-i18next";
import { api } from "../constants/api";

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ py: 3 }}>
          <Typography component="div">{children}</Typography>
        </Box>
      )}
    </Typography>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    "aria-controls": `simple-tabpanel-${index}`,
  };
}
const NavTab = ({ workFlow, signingOptions, ischange }) => {
  const { t } = useTranslation();
  const [value, setValue] = useState(0);
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  let lang = localStorage.getItem("language");
  const getTabs = useMemo(() => {
    return signingOptions.map((value, index) => {
      return (
        <Tab
          key={index}
          sx={{ textTransform: "none" }}
          // label="with Mobile-ID"
          label={createLabel(value)}
          {...a11yProps(index)}
        />
      );
    });
  }, [signingOptions, lang]);
  useEffect(() => {
    setValue(0);
  }, [ischange]);

  // const { isLoading } = useApiControllerManager(false);

  const getConnectorName = async (param) => {
    try {
      const response = await api.post("/base64Logo", { param });
      return response.data;
    } catch (error) {
      console.error(error);
    }
  };

  const [cardList, setCardList] = useState([]);
  const [classList, setCLassList] = useState([]);

  useEffect(() => {
    const getCardList = async () => {
      if (signingOptions) {
        const newCardList = [];
        const newCLassList = [];
        for (const e of signingOptions) {
          let connectorNames = null;
          let cards = [];
          let nameOfClass = [];
          if (e === "mobile") {
            connectorNames = await getConnectorName("MOBILE_ID_SIGNING");
            cards = createCard(connectorNames, e);
            nameOfClass = checkClassName(e);
          } else if (e === "smartid") {
            connectorNames = await getConnectorName("SMART_ID_SIGNING");
            cards = createCard(connectorNames, e);

            nameOfClass = checkClassName(e);
          } else if (e === "eidsigncloud") {
            connectorNames = await getConnectorName(
              "ID_CARD_ESIGNCLOUD_SIGNING"
            );
            cards = createCard(connectorNames, e);
            nameOfClass = checkClassName(e);
          } else if (e === "usbtoken") {
            connectorNames = await getConnectorName("USB_TOKEN_SIGNING");
            cards = createCard(connectorNames, e);
            nameOfClass = checkClassName(e);
          } else if (e === "eidwitnessing") {
            connectorNames = await getConnectorName(
              "ID_CARD_WITNESSING_SIGNING"
            );
            cards = createCard(connectorNames, e);
            nameOfClass = checkClassName(e);
          } else if (e === "electronic_id") {
            connectorNames = await getConnectorName("ELECTRONIC_ID");
            cards = createCard(connectorNames, e);
            nameOfClass = checkClassName(e);
          }
          if (connectorNames) {
            newCardList.push(cards);
            newCLassList.push(nameOfClass);
          }
        }

        setCardList(newCardList);
        setCLassList(newCLassList);
      }
    };

    getCardList();
  }, [signingOptions]);

  return (
    <Box sx={{ width: "100%" }}>
      <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
        <Tabs
          id="navTab"
          value={value}
          onChange={handleChange}
          aria-label="basic tabs example"
          // disabled={isLoading}
        >
          {getTabs}
        </Tabs>
      </Box>
      <TabPanel value={value} index={value}>
        <Field
          workFlow={workFlow}
          cards={cardList[value]}
          classList={classList[value]}
          signingOption={signingOptions[value]}
        ></Field>
      </TabPanel>
    </Box>
  );
};

export default memo(NavTab);
