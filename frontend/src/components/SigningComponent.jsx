import {
  AccessTimeRounded,
  ExpandLess,
  ExpandMore,
  InsertDriveFile,
  Search,
} from "@mui/icons-material";
import { Alert, Button, Collapse, IconButton } from "@mui/material";
import React, { Fragment, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import {
  apiControllerManagerActions,
  useApiControllerManager,
} from "../store/apiControllerManager";
// Import the styles
// import "@react-pdf-viewer/core/lib/styles/index.css";
// default layout plugin
// Import styles of default layout plugin
// import "@react-pdf-viewer/default-layout/lib/styles/index.css";
import { api } from "../constants/api";
import {
  checkStatus,
  checkType,
  handleDocument,
  handleInputClickName,
} from "../ultis/commonFunction";
import FileSigned from "./FileSigned";
import NavTab from "./NavTab";
import PdfView from "./pdfView/PdfView";
// import { ReactComponent as ReactLogo } from "../assets/images/finger.svg";
// <ReactLogo />

export const SigningComponent = ({ workFlow, ischange }) => {
  // console.log("workFlow: ", workFlow);
  const dispatch = useDispatch();
  const { t } = useTranslation();

  const { isSignSuccess } = useApiControllerManager(false);

  const [signingOptions, setSigningOptions] = useState([]);
  const [isFetching, setIsFetching] = useState(false);

  const [signedInfo, setSignedInfo] = useState({});

  useEffect(() => {
    if (workFlow && Object.keys(workFlow).length !== 0) {
      const participant = workFlow?.participants?.find(
        (item) => item.signerToken === workFlow?.signerToken
      );
      if (participant) {
        setSigningOptions(
          participant?.signingOptions !== null
            ? JSON.parse(participant?.signingOptions).signing_options.filter(
                (item) => item !== "eidsigncloud" || item !== "eidwitnessing"
              )
            : ["mobile", "smartid", "usbtoken", "electronic_id"]
        );
      }
      // if (participant?.signingOptions !== null) {
      //   setSigningOptions(
      //     JSON.parse(participant?.signingOptions).signing_options
      //   );
      // }

      getSignedInfo(workFlow.fileId);
    }
  }, [workFlow]);

  const getSignedInfo = async (fileId) => {
    try {
      setIsFetching(true);
      const response = await api.post("/getSignedInfo", {
        fileId: fileId,
      });
      setSignedInfo(response.data);
      setIsFetching(false);
    } catch (error) {
      console.error(error);
    }
  };

  // Signers section
  const [buttonStates, setButtonStates] = useState([]);

  //Content section
  const [showDocument, setShowDocument] = useState(false);

  const currentURL = window.location.href;
  const url = new URL(currentURL);
  const path = url.pathname;
  // if (path.includes("sequence")) {
  // }

  return (
    <main>
      {isFetching && (
        <div
          className="modal backdrop fade show d-flex justify-content-center align-items-center"
          style={{ background: "#00000080" }}
        >
          <div className="loader" />
        </div>
      )}

      <div className="container preview-document-container isign-signing-show isign-signature-pdf ">
        {!path.includes("sequence") && isSignSuccess && (
          <div style={{ padding: "0px" }}>
            <Alert
              style={{
                margin: "12px 0px",
                padding: "0 10px !important",
                background: "#3fc380",
              }}
              variant="filled"
              severity={"success"}
              onClose={() => {
                dispatch(apiControllerManagerActions.clearsetMessageSuccess());
              }}
            >
              {t("single.noti")}
            </Alert>
          </div>
        )}
        <div className="col-xs-12 isign-details-block">
          <h2>{t("single.title")}</h2>
          <div className="row" style={{ paddingBottom: "10px" }}>
            <div className="col-sm-2 col-4" style={{ color: "#aaaaaa" }}>
              {t("single.documentName")}
            </div>
            <div className="col-sm-2 col-8" style={{ marginLeft: "4px" }}>
              {workFlow?.documentName}
            </div>
            {/* <div className="col-sm-2 col-8">{documentName}</div> */}
          </div>

          <div className="row">
            <div className="col-sm-2 col-4" style={{ color: "#aaaaaa" }}>
              {t("single.documentFormat")}
            </div>
            <div className="col-sm-6 col-8">
              {/* <img src="/signing/icon_pdf.png"></img> {documentFormat} */}
              <InsertDriveFile style={{ color: "#1976d2" }} />{" "}
              {"PDF - " + t("single.standard")}
            </div>
          </div>
        </div>
        {/* {!path.includes("sequence") && isSignSuccess && (
          <div style={{ padding: "0px 5px" }}>
            <Alert
              style={{ margin: "12px 0px", padding: "0 10px !important" }}
              variant="filled"
              severity={"success"}
              onClose={() => {
                dispatch(apiControllerManagerActions.clearsetMessageSuccess());
              }}
            >
              {isSignSuccess}
            </Alert>
          </div>
        )} */}
        <div className="col-xs-12 isign-signers-block">
          <h2>{t("single.signer")}</h2>
          {signedInfo && Object.keys(signedInfo).length !== 0 && (
            <FileSigned listSignedInfo={signedInfo} />
          )}

          {workFlow?.participants?.map((e, index) => {
            const metaInf = JSON.parse(e.metaInformation);
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
                          handleInputClickName(
                            index,
                            buttonStates,
                            setButtonStates
                          )
                        }
                        style={{ color: "#009ede" }}
                      >
                        {buttonStates[index] ? <ExpandLess /> : <ExpandMore />}
                      </IconButton>
                    </span>

                    {e.firstName + " " + e.lastName}
                  </div>

                  <div className="col-6">
                    <div style={{ float: "right" }}>
                      {checkStatus(e.signerStatus, e.signedType)}
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
                          {e.signerStatus === 2 && (
                            <div
                              style={{
                                borderBottom: "1px solid rgba(0, 0, 0, 0.1)",
                                paddingTop: "5px",
                                paddingBottom: "9px",
                              }}
                            >
                              {checkType(e.signedType)}
                            </div>
                          )}
                        </div>
                        <div className="row row-cols-2 gx-2">
                          {e.signerStatus === 2 && (
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
                              {e.signedTime}{" "}
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
                          )}
                          {e.signerStatus === 2 && (
                            <Fragment>
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
                                {e.owner}
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
                                {e.issuer}
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
                                {e.validFrom + " - " + e.validTo}
                              </div>
                            </Fragment>
                          )}
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

                          {metaInf.company && (
                            <div
                              className="col-6"
                              style={{
                                fontSize: "14px",
                                marginTop: "5px",
                                marginBottom: "5px",
                              }}
                            >
                              <span style={{ color: "#AAA" }}>
                                {t("single.company")}:
                              </span>{" "}
                              {metaInf.company}
                            </div>
                          )}

                          {metaInf.position && (
                            <div
                              className="col-6"
                              style={{
                                fontSize: "14px",
                                marginTop: "5px",
                                marginBottom: "5px",
                              }}
                            >
                              <span style={{ color: "#AAA" }}>
                                {t("single.position")}:
                              </span>{" "}
                              {metaInf.position}
                            </div>
                          )}
                          {metaInf.structural_subdivision && (
                            <div
                              className="col-6"
                              style={{
                                fontSize: "14px",
                                marginTop: "5px",
                                marginBottom: "5px",
                              }}
                            >
                              <span style={{ color: "#AAA" }}>
                                {t("single.struturalSubdivision")}:
                              </span>{" "}
                              {metaInf.structural_subdivision}
                            </div>
                          )}

                          {metaInf.country && (
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
                              {metaInf.country} {metaInf.city}{" "}
                              {metaInf.postal_code}
                            </div>
                          )}

                          {metaInf.pdf && metaInf.pdf.reason && (
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
                              {metaInf.pdf.reason}
                            </div>
                          )}
                        </div>
                      </div>
                    </div>
                  </div>
                </Collapse>
              </Fragment>
            );
          })}
        </div>

        <div className="col-xs-12 document-list-block isign-documentlist-block">
          <h2>{t("single.content")}</h2>

          <div
            className="row pointer bd-bottom"
            onClick={() => handleDocument(showDocument, setShowDocument)}
          >
            <div style={{ borderBottom: "1px solid #e5e5e5" }}></div>
            <div className="col-1" style={{ marginTop: "2px" }}>
              <IconButton
                onClick={() => handleDocument(showDocument, setShowDocument)}
                style={{ color: "#009ede" }}
              >
                <Search />
              </IconButton>
            </div>
            {workFlow && Object.keys(workFlow).length !== 0 && (
              <div className="col-xs-8 mobile-content laptop-content">
                {workFlow?.fileName} (
                {(workFlow?.fileSize * 0.000001).toFixed(1)} MB)
              </div>
            )}
          </div>
        </div>

        <Collapse
          className="re-sign"
          in={showDocument}
          direction="up"
          style={{ paddingTop: "2px" }}
        >
          <PdfView workFlow={workFlow} />
        </Collapse>

        {workFlow?.participants?.length !== 0 &&
        Array.isArray(workFlow?.participants) &&
        workFlow?.participants.filter((item) => item.signerStatus !== 1)
          .length === workFlow?.participants.length ? (
          <div className="col-xs-12 isign-sign-pane">
            <h2>{t("single.signedDocument")}</h2>
            <div>{t("single.notification")}</div>

            <a
              // href={`${window.location.origin}${process.env.PUBLIC_URL}/${workFlow.signingToken}/download?access_token=${workFlow.signerToken}`}
              href={`${window.location.origin}${process.env.PUBLIC_URL}/fps/download/${workFlow.documentId}`}
              download
              // href={`http://localhost:8080/signing/${workFlow.signingToken}/download?access_token=${workFlow.signerToken}`}
            >
              <Button
                size="large"
                variant="contained"
                className="col-sm-2"
                style={{
                  background: "#3FC380",
                  borderColor: "#3CB06C",
                  marginTop: "20px",
                  width: "300px",
                }}
                sx={{
                  fontFamily:
                    '"Montserrat", "Nucleo", "Helvetica", "sans-serif"',
                }}
              >
                {t("single.btnDownload")}
              </Button>
            </a>
          </div>
        ) : workFlow?.participants &&
          workFlow?.participants.find(
            (item) =>
              item.signerToken === workFlow.signerToken &&
              item.signerStatus !== 1
          ) ? (
          <div className="col-xs-12 isign-sign-pane">
            <h2>{t("single.documentStatus")}</h2>
            <div>{t("single.instruct")}</div>
          </div>
        ) : (
          <div className="col-xs-12 isign-sign-pane">
            <h2>{t("single.signDocument")}</h2>
            <NavTab
              workFlow={workFlow}
              signingOptions={signingOptions}
              ischange={ischange}
            ></NavTab>
          </div>
        )}
      </div>
    </main>
  );
};

export default SigningComponent;
