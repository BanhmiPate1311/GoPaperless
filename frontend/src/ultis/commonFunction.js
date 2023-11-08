import CheckCircleOutlineRoundedIcon from "@mui/icons-material/CheckCircleOutlineRounded";
import AssignmentTurnedInRoundedIcon from "@mui/icons-material/AssignmentTurnedInRounded";
import HttpsOutlinedIcon from "@mui/icons-material/HttpsOutlined";
import InsertDriveFileOutlinedIcon from "@mui/icons-material/InsertDriveFileOutlined";
import GroupOutlinedIcon from "@mui/icons-material/GroupOutlined";
import WorkspacePremiumIcon from "@mui/icons-material/WorkspacePremium";
import DescriptionOutlinedIcon from "@mui/icons-material/DescriptionOutlined";
import i18n from "./language/i18n";
import moment from "moment";

export const handleSubmitOnEnter = (event, callback) => {
  if (event.key === "Enter") {
    callback();
  }
};

export function handleInputClickName(index, buttonStates, setButtonStates) {
  const newButtonStates = [...buttonStates];
  newButtonStates[index] = !newButtonStates[index];
  setButtonStates(newButtonStates); // đảo ngược giá trị của biến showAnotherField
}

export function handleInputClickNameInfo(buttonStates, setButtonStates) {
  setButtonStates(!buttonStates); // đảo ngược giá trị của biến showAnotherField
}

export function handleDocument(showDocument, setShowDocument) {
  setShowDocument(!showDocument); // đảo ngược giá trị của biến showAnotherField
}

export function checkStatus(status, signedType) {
  if (status === 1) {
    return (
      <div>
        <span
          style={{
            color: "rgba(0, 0, 0, 0.1)",

            fontSize: "25px",
          }}
        >
          |
        </span>
        <span
          style={{
            width: "245px",
            display: "inline-block",
            textAlign: "left",
          }}
        >
          <span style={{ marginLeft: "8px" }}>
            {i18n.t("single.waitingSignature")}
          </span>
        </span>
      </div>
    );
  }
  if (status === 2 && signedType === "NORMAL") {
    return (
      <div className="d-flex align-items-center">
        <CheckCircleOutlineRoundedIcon
          style={{ color: "#3fc380", marginRight: "5px" }}
        />
        <span style={{ color: "#3fc380" }}>
          {i18n.t("single.signatureValid")}
        </span>
        <span
          style={{
            color: "rgba(0, 0, 0, 0.1)",
            marginLeft: "10px",
            fontSize: "25px",
          }}
        >
          |
        </span>
        <span
          style={{
            width: "245px",
            display: "inline-block",
            textAlign: "left",
          }}
        >
          <HttpsOutlinedIcon style={{ color: "#0B95E5", marginLeft: "5px" }} />
          <span style={{ marginRight: "5px" }}>
            {i18n.t("single.criterion")}
          </span>
        </span>
      </div>
    );
  }
  if (status === 2 && signedType === "ESEAL") {
    return (
      <div className="d-flex align-items-center">
        <CheckCircleOutlineRoundedIcon
          style={{
            color: "#3fc380",
            // marginRight: "5px"
          }}
        />
        <span style={{ color: "#3fc380", marginRight: "5px" }}>
          {" "}
          {i18n.t("single.seal1")}{" "}
        </span>
        <span
          style={{
            color: "rgba(0, 0, 0, 0.1)",

            fontSize: "25px",
          }}
        >
          |
        </span>
        <span
          style={{
            width: "245px",
            display: "inline-block",
            textAlign: "left",
            paddingLeft: "8px",
          }}
        >
          <AssignmentTurnedInRoundedIcon
            style={{ color: "#0B95E5", marginLeft: "-3px" }}
          />
          <span> {i18n.t("single.seal2")} </span>
        </span>
      </div>
    );
  }
}

export function checkStatusBatch(status, signedType) {
  if (status === 1) {
    return (
      <div>
        <span
          style={{
            width: "245px",
            display: "inline-block",
            textAlign: "left",
          }}
        >
          <span style={{ marginLeft: "8px" }}>
            {i18n.t("single.waitingSignature")}
          </span>
        </span>
      </div>
    );
  }
  if (status === 2 && signedType === "NORMAL") {
    return (
      <div>
        <span
          style={{
            width: "245px",
            display: "inline-block",
            textAlign: "left",
          }}
        >
          <span style={{ marginLeft: "8px", color: "#3fc380" }}>
            {i18n.t("single.signatureValid")}
          </span>
        </span>
      </div>
    );
  }
  if (status === 2 && signedType === "ESEAL") {
    return (
      <div>
        <CheckCircleOutlineRoundedIcon
          style={{
            color: "#3fc380",
            // marginRight: "5px"
          }}
        />
        <span style={{ color: "#3fc380" }}> {i18n.t("single.seal1")} </span>
        <span
          style={{
            color: "rgba(0, 0, 0, 0.1)",

            fontSize: "25px",
          }}
        >
          |
        </span>
        <span
          style={{
            width: "245px",
            display: "inline-block",
            textAlign: "left",
          }}
        >
          <AssignmentTurnedInRoundedIcon
            style={{ color: "#0B95E5", marginLeft: "5px" }}
          />
          <span style={{ marginRight: "43px" }}>
            {" "}
            {i18n.t("single.seal2")}{" "}
          </span>
        </span>
      </div>
    );
  }
}

export function checkType(type) {
  if (type === "NORMAL") {
    return (
      <>
        <HttpsOutlinedIcon style={{ color: "#0B95E5", marginLeft: "5px" }} />
        <span style={{ marginRight: "5px" }}>{i18n.t("single.condition")}</span>
      </>
    );
  }
  if (type === "ESEAL") {
    return (
      <>
        <AssignmentTurnedInRoundedIcon
          style={{ color: "#0B95E5", marginLeft: "5px" }}
        />
        <span style={{ marginRight: "5px" }}>{i18n.t("single.seal3")}</span>
      </>
    );
  }
}

export function checkClassName(value) {
  switch (value) {
    case "mobile":
      return "with-mobile";
    case "smartid":
    case "usbtoken":
      return "with-smart";
    case "eidwitnessing":
    case "eidsigncloud":
    case "electronic_id":
      return "with-card";
    default:
      return "Unknown Tab";
  }
}

export function createCard(connectorNames, signingName) {
  const cards = [];
  for (var i = 0; i < connectorNames.length; i++) {
    const rename = connectorNames[i].connector_name;
    const reList = {};
    reList["id"] = i;
    reList["description"] = connectorNames[i].remark;
    reList["code"] = rename;
    reList["height"] = (() => {
      switch (signingName) {
        case "mobile":
          return 85;
        case "smartid":
        case "usbtoken":
          return 70;
        case "eidwitnessing":
        case "eidsigncloud":
        case "electronic_id":
          return 16;
        default:
          return "Unknown Tab";
      }
    })();
    reList["isChecked"] = false;
    reList["icon"] = connectorNames[i].logo;
    cards.push(reList);
  }
  return cards;
}

export function createLabel(value) {
  switch (value) {
    case "mobile":
      return i18n.t("navTab.mobile");
    case "smartid":
      return i18n.t("navTab.smart");
    case "usbtoken":
      return i18n.t("navTab.usb");
    case "eidwitnessing":
      return i18n.t("navTab.witnessing");
    case "eidsigncloud":
      return i18n.t("navTab.esign");
    case "electronic_id":
      return i18n.t("navTab.electronicid");
    default:
      return "Unknown Tab";
  }
}

export const createValidIcon = (value) => {
  switch (value) {
    case "overview":
      return <InsertDriveFileOutlinedIcon />;
    case "signatures":
      return <GroupOutlinedIcon />;
    case "seals":
      return <WorkspacePremiumIcon />;
    case "details":
      return <DescriptionOutlinedIcon />;
    default:
      return "Unknown Tab";
  }
};

export const createValidLabel = (value) => {
  switch (value) {
    case "overview":
      return i18n.t("validation.tab1");
    case "signatures":
      return i18n.t("validation.tab2");
    case "seals":
      return i18n.t("validation.tab3");
    case "details":
      return i18n.t("validation.tab4");
    default:
      return "Unknown Tab";
  }
};

export const createValidStatus = (value) => {
  switch (value) {
    case "Valid":
      return i18n.t("validation.overviewStatus1");
    case "There are warnings":
      return i18n.t("validation.overviewStatus2");
    case "There are errors":
      return i18n.t("validation.overviewStatus3");
    default:
      return null;
  }
};

export const createValidName = (value) => {
  switch (value) {
    case "valid Signature":
      return i18n.t("validation.validSig");
    case "indeterminate Signature":
      return i18n.t("validation.indeterminateSig");
    case "invalid Signature":
      return i18n.t("validation.invalidSig");
    case "valid Seal":
      return i18n.t("validation.validSeal");
    case "indeterminate Seal":
      return i18n.t("validation.indeterminateSeal");
    case "invalid Seal":
      return i18n.t("validation.invalidSeal");
    default:
      return null;
  }
};

export const createValidTitle = (value) => {
  switch (value) {
    case "Signature is valid":
      return i18n.t("validation.sigValidTitle2");
    case "Seal is valid":
      return i18n.t("validation.sealValidTitle2");
    default:
      return null;
  }
};

export const createValidSubTitle = (value) => {
  switch (value) {
    case "Electronic Signature":
      return i18n.t("validation.signSubTitle");
    case "Electronic Seal":
      return i18n.t("validation.sealSubTitle");
    default:
      return null;
  }
};

export const formatTime = (time) => {
  // Tìm và cắt chuỗi múi giờ nếu có
  let timeFormatted = time;
  // const matches = time.match(/\(GMT[+-]\d{1,2}\)/);
  // if (matches && matches.length > 0) {
  //   const gmtString = matches[0];
  //   timeFormatted = time.replace(gmtString, ""); // Cắt chuỗi múi giờ
  // }

  // Chuyển đổi định dạng ngày giờ sử dụng Moment.js
  timeFormatted = moment(timeFormatted, "YYYY-MM-DD HH:mm:ss").format(
    "DD/MM/YYYY HH:mm:ss"
  );

  // // Thêm lại chuỗi múi giờ nếu có
  // if (matches && matches.length > 0) {
  //   timeFormatted += ` ${matches[0]}`;
  // }

  return timeFormatted;
};

export const formatPeriodTime = (time) => {
  // Tìm và cắt chuỗi múi giờ nếu có
  let timeFormatted = time.split(" - ");
  timeFormatted =
    formatTime(timeFormatted[0]) + " - " + formatTime(timeFormatted[1]);
  return timeFormatted;
};

export const formatSignerId = (value) =>
  value.substring("YOUR_PROVIDED_".length);
