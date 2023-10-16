import React, { Fragment, useState } from "react";
import {
  checkStatus,
  checkType,
  formatTime,
  handleInputClickName,
  handleInputClickNameInfo,
} from "../ultis/commonFunction";
import { Collapse, IconButton } from "@mui/material";
import { AccessTimeRounded, ExpandLess, ExpandMore } from "@mui/icons-material";
import moment from "moment";
import { useTranslation } from "react-i18next";

const FileSigned = ({ listSignedInfo }) => {
  const [buttonStates, setButtonStates] = useState([]);
  const { t } = useTranslation();
  let signedType = "NORMAL";
  // if (signedInfo) {
  //   signedType = signedInfo.is_seal === true ? "SEAL" : "NORMAL";
  // }
  return (
    <>
      {listSignedInfo.map((e, index) => {
        const signedInfo = JSON.parse(e.value);

        signedType = e.is_seal === true ? "SEAL" : "NORMAL";
        return (
          <Fragment key={index}>
            <div
              className="row pointer bd-bottom"
              style={{ alignItems: "center" }}
              onClick={() =>
                handleInputClickName(index, buttonStates, setButtonStates)
              }
            >
              <div className="col-6">
                <span>
                  <IconButton
                    onClick={() =>
                      handleInputClickName(index, buttonStates, setButtonStates)
                    }
                    style={{ color: "#009ede" }}
                  >
                    {buttonStates[index] ? <ExpandLess /> : <ExpandMore />}
                  </IconButton>
                </span>

                {signedInfo.signature.certificate.subject.common_name}
              </div>

              <div className="col-6">
                <div style={{ float: "right" }}>
                  {checkStatus(2, signedType)}
                </div>
              </div>
            </div>
            <Collapse
              className="re-sign"
              in={buttonStates[index]}
              direction="up"
              style={{
                borderBottom: "1px solid #e5e5e5",
              }}
            >
              <div>
                <div style={{ padding: "7px" }}>
                  <div className="re-color">
                    <div>
                      <div
                        style={{
                          borderBottom: "1px solid rgba(0, 0, 0, 0.1)",
                          paddingTop: "5px",
                          paddingBottom: "9px",
                        }}
                      >
                        {checkType(signedType)}
                      </div>
                    </div>
                    <div className="row row-cols-2 gx-2">
                      <div
                        className="col-6"
                        style={{
                          fontSize: "14px",
                          marginTop: "5px",
                        }}
                      >
                        <span style={{ color: "#AAA" }}>
                          {t("single.signingTime")}:
                        </span>{" "}
                        {formatTime(signedInfo.signature.signing_time)}
                        <span
                          data-tooltip={t("single.signed")}
                          data-side="right"
                          tabIndex="0"
                        >
                          <AccessTimeRounded
                            style={{
                              fontSize: "14px",
                              marginLeft: "4px",
                              color: "#0B95E5",
                            }}
                          />
                        </span>
                      </div>
                      <div
                        className="col-6"
                        style={{
                          fontSize: "14px",
                          marginTop: "5px",
                          marginBottom: "5px",
                        }}
                      >
                        <span style={{ color: "#AAA" }}>
                          {t("single.certificateOwner")}:
                        </span>{" "}
                        {signedInfo.signature.certificate.subject.common_name}
                      </div>
                      <div
                        className="col-6"
                        style={{
                          fontSize: "14px",
                          marginTop: "5px",
                          marginBottom: "5px",
                        }}
                      >
                        <span style={{ color: "#AAA" }}>
                          {t("single.certificateIssuer")}:
                        </span>{" "}
                        {signedInfo.signature.certificate.issuer.organization}
                      </div>
                      <div
                        className="col-6"
                        style={{
                          fontSize: "14px",
                          marginTop: "5px",
                          marginBottom: "5px",
                        }}
                      >
                        <span style={{ color: "#AAA" }}>
                          {t("single.certificatevValidite")}:
                        </span>{" "}
                        {moment(
                          signedInfo.signature.certificate.valid_from,
                          "YYYY-MM-DD HH:mm:ss"
                        ).format("DD/MM/YYYY HH:mm:ss")}
                        {" - "}
                        {moment(
                          signedInfo.signature.certificate.valid_to,
                          "YYYY-MM-DD HH:mm:ss"
                        ).format("DD/MM/YYYY HH:mm:ss")}
                      </div>
                      <div
                        className="col-6"
                        style={{
                          fontSize: "14px",
                          marginTop: "5px",
                          marginBottom: "5px",
                        }}
                      >
                        <span style={{ color: "#AAA" }}>
                          {t("single.resolution")}:
                        </span>{" "}
                        Signature
                      </div>

                      {signedInfo.signature.metadata.location !== "" && (
                        <div
                          className="col-6"
                          style={{
                            fontSize: "14px",
                            marginTop: "5px",
                            marginBottom: "5px",
                          }}
                        >
                          <span style={{ color: "#AAA" }}>
                            {t("single.location")}:
                          </span>{" "}
                          {signedInfo.signature.metadata.location}
                        </div>
                      )}

                      <div
                        id="reason"
                        className="col-6"
                        style={{
                          fontSize: "14px",
                          marginTop: "5px",
                          marginBottom: "5px",
                        }}
                      >
                        <span style={{ color: "#AAA" }}>
                          {t("single.reason")}
                        </span>{" "}
                        {signedInfo.signature.metadata.reason}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </Collapse>
          </Fragment>
        );
      })}
    </>
  );
};

export default FileSigned;
