import {
  Alert,
  Card,
  CardContent,
  Radio,
  Stack,
  Typography,
} from "@mui/material";
import React, { Fragment, useEffect, useState } from "react";

import { useTranslation } from "react-i18next";
import { api } from "../constants/api";
import { checkClassName, createCard } from "../ultis/commonFunction";
import FieldElecId from "./FieldElecId";
import FieldSmartId from "./FieldSmartId";
import FieldUsbToken from "./FieldUsbToken";

const Field = ({ workFlow, signingOption, cards, classList }) => {
  const { t } = useTranslation();

  const [cardList, setCardList] = useState(cards);
  useEffect(() => {
    setCardList(cards);
  }, [cards]);

  // const [classList, setCLassList] = useState([]);

  // const getConnectorName = async (param) => {
  //   try {
  //     const response = await api.post("/base64Logo", { param });
  //     return response.data;
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };

  let lang = localStorage.getItem("language");

  // const getCardList = async () => {
  //   let connectorNames = null;
  //   let cards = [];
  //   let nameOfClass = [];
  //   if (signingOption === "mobile") {
  //     connectorNames = await getConnectorName("MOBILE_ID_SIGNING");
  //     cards = createCard(connectorNames, signingOption);
  //     nameOfClass = checkClassName(signingOption);
  //   }
  //   if (signingOption === "smartid") {
  //     connectorNames = await getConnectorName("SMART_ID_SIGNING");
  //     cards = createCard(connectorNames, signingOption);
  //     nameOfClass = checkClassName(signingOption);
  //   }
  //   if (signingOption === "eidsigncloud") {
  //     connectorNames = await getConnectorName("ID_CARD_ESIGNCLOUD_SIGNING");
  //     cards = createCard(connectorNames, signingOption);
  //     nameOfClass = checkClassName(signingOption);
  //   }
  //   if (signingOption === "usbtoken") {
  //     connectorNames = await getConnectorName("USB_TOKEN_SIGNING");
  //     cards = createCard(connectorNames, signingOption);
  //     nameOfClass = checkClassName(signingOption);
  //   }
  //   if (signingOption === "eidwitnessing") {
  //     connectorNames = await getConnectorName("ID_CARD_WITNESSING_SIGNING");
  //     cards = createCard(connectorNames, signingOption);
  //     nameOfClass = checkClassName(signingOption);
  //   }
  //   if (signingOption === "electronic_id") {
  //     connectorNames = await getConnectorName("ELECTRONIC_ID");
  //     cards = createCard(connectorNames, signingOption);
  //     nameOfClass = checkClassName(signingOption);
  //   }
  //   setCardList(cards);
  //   setCLassList(nameOfClass);
  // };

  // useEffect(() => {
  //   getCardList();
  // }, [signingOption]);

  const [swError, setSwError] = useState(null);

  useEffect(() => {
    setIsCardChecked(null);
    setSwError(null);
    setConnectorName("");

    if (signingOption === "usbtoken" || signingOption === "electronic_id") {
      const ipWS = "127.0.0.1";
      const portWS = "9505";
      const typeOfWS = "wss";

      var url = typeOfWS + "://" + ipWS + ":" + portWS + "/ISPlugin";

      const socket = new WebSocket(url);

      // Xử lý sự kiện khi kết nối mở thành công
      socket.addEventListener("open", () => {
        console.log("Kết nối WebSocket đã thành công");
        socket.close();
      });

      // Xử lý sự kiện khi xảy ra lỗi trong quá trình kết nối
      socket.addEventListener("error", (error) => {
        console.error("Lỗi kết nối WebSocket:", error);
      });

      // Xử lý sự kiện khi kết nối bị đóng
      socket.addEventListener("close", (event) => {
        console.log("Kết nối WebSocket đã bị đóng");
        console.log("Mã đóng:", event.code);
        if (event.code === 1006) {
          setSwError("usb.checkidwaring");
        }
        console.log("Lí do:", event.reason);
      });

      // Kiểm tra trạng thái kết nối hiện tại
      console.log("Trạng thái kết nối:", socket.readyState);
    }
  }, [signingOption]);

  const handleCardClick = (id, code) => {
    setConnectorName(code);
    const updatedCardList = cardList.map((card) => {
      if (card.id === id) {
        return { ...card, isChecked: true };
      } else {
        return { ...card, isChecked: false };
      }
    });
    setCardList(updatedCardList);
    setIsCardChecked(id);
    // if (swError === "") {
    //   setIsCardChecked(id); // update isCardChecked based on whether any card is checked
    //   // handleValidate(); // Gọi hàm handleValidate() ở đây
    // } else if (signingOption !== "usbtoken") {
    //   setIsCardChecked(id);
    // }
  };
  const provider = (signingOption) => {
    switch (signingOption) {
      case "mobile":
        return t("mobileID.choose");
        break;
      case "smartid":
        return t("smartID.provider");
        break;
      case "usbtoken":
        return t("usb.provider");
        break;
      case "eidsigncloud":
        return t("esign.provider");
        break;
      case "eidwitnessing":
        return t("witness.provider");
        break;
      case "electronic_id":
        return t("electronic.provider");
        break;
      default:
        break;
    }
  };
  const [isCardChecked, setIsCardChecked] = useState(null); // added

  const [connectorName, setConnectorName] = useState("");
  console.log("connectorName: ", connectorName);

  return (
    <Fragment>
      <div style={{ color: "#9a9a9a" }}>{provider(signingOption)}</div>
      {swError &&
        (signingOption === "usbtoken" || signingOption === "electronic_id") &&
        isCardChecked !== null && (
          <Alert
            style={{
              color: "black",
              background: "#F4C7C7",
            }}
            severity="error"
            onClose={() => setSwError("")}
          >
            {t(swError)}
            <a href="/signing/download/checkid" download>
              {t("usb.download")}
            </a>
          </Alert>
        )}

      {/* <div
        style={{
          display: "flex",
          flexWrap: "wrap",

          marginTop: "10px",
        }}
      >
        {cardList?.map((card, index) => (
          <Card
            key={card.id}
            onClick={() => {
              handleCardClick(card.id, card.code);
            }}
            className={`${classList} cardTab`}
            sx={{
              m: 2,
              border: card.isChecked ? "2px solid #f45c5a" : "none",
              paddingX: card.isChecked ? "0" : "5px",
              backgroundColor: card.isChecked ? "#fdeeee" : "white",
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              alignItems: "center",
              cursor: "pointer",
            }}
          >
            <div
              style={{
                height: "100%",
                textAlign: "center",
                maxWidth: "100%",
              }}
            >
              <Radio
                checked={card.isChecked}
                color="primary"
                style={{ width: "16px" }}
              />
              <CardContent style={{ textAlign: "center" }}>
                <Typography variant="body2" color="text.secondary">
                  <img
                    src={card.icon}
                    alt="card-icon"
                    style={{
                      height: card.height || "auto",
                      maxWidth: "100%",
                    }}
                  />
                </Typography>
                <span>{card.description}</span>
              </CardContent>
            </div>
          </Card>
        ))}
      </div> */}

      <Stack direction="row" spacing={2} useFlexGap flexWrap="wrap" my={2}>
        {cardList?.map((card, index) => (
          <Card
            key={card.id}
            onClick={() => {
              handleCardClick(card.id, card.code);
            }}
            className={`${classList} cardTab`}
            sx={{
              // m: 2,
              border: card.isChecked ? "2px solid #f45c5a" : "none",
              paddingX: card.isChecked ? "0" : "5px",
              backgroundColor: card.isChecked ? "#fdeeee" : "white",
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              alignItems: "center",
              cursor: "pointer",
            }}
          >
            <div
              style={{
                height: "100%",
                textAlign: "center",
                maxWidth: "100%",
              }}
            >
              <Radio
                checked={card.isChecked}
                color="primary"
                style={{ width: "16px" }}
              />
              <CardContent style={{ textAlign: "center" }}>
                <Typography variant="body2" color="text.secondary">
                  <img
                    src={card.icon}
                    alt="card-icon"
                    style={{
                      height: card.height || "auto",
                      maxWidth: "100%",
                    }}
                  />
                </Typography>
                <span>{card.description}</span>
              </CardContent>
            </div>
          </Card>
        ))}
      </Stack>

      {signingOption === "smartid" && (
        <FieldSmartId
          isCardChecked={isCardChecked}
          connectorName={connectorName}
          workFlow={workFlow}
        />
      )}

      {signingOption === "usbtoken" && (
        <FieldUsbToken
          isCardChecked={isCardChecked}
          connectorName={connectorName}
          workFlow={workFlow}
          swError={swError}
        />
      )}

      {signingOption === "electronic_id" && (
        <FieldElecId
          isCardChecked={isCardChecked}
          connectorName={connectorName}
          workFlow={workFlow}
          swError={swError}
        />
        // <CustomFaceCapture />
        // <FaceAuth />
      )}
    </Fragment>
  );
};

export default Field;
