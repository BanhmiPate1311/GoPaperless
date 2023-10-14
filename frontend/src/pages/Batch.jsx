import React, { Fragment, useEffect, useState } from "react";
import { Button, Collapse, IconButton, Tab, Tabs } from "@mui/material";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import ArrowDropUpIcon from "@mui/icons-material/ArrowDropUp";
import Alert from "@mui/material/Alert";
import OpenInFullIcon from "@mui/icons-material/OpenInFull";
import CloseFullscreenIcon from "@mui/icons-material/CloseFullscreen";
import CheckCircleOutlineRoundedIcon from "@mui/icons-material/CheckCircleOutlineRounded";
import VisibilityIcon from "@mui/icons-material/Visibility";
import Box from "@mui/material/Box";
import { Search } from "@mui/icons-material";
import { useTranslation } from "react-i18next";
import { useParams } from "react-router-dom";
import { api } from "../constants/api";
import { Viewer, Worker } from "@react-pdf-viewer/core";
import "@react-pdf-viewer/core/lib/styles/index.css";
// default layout plugin
import { defaultLayoutPlugin } from "@react-pdf-viewer/default-layout";
// Import styles of default layout plugin
import "@react-pdf-viewer/default-layout/lib/styles/index.css";
import NavTab from "../components/NavTab";
import {
  apiControllerManagerActions,
  useApiControllerManager,
} from "../store/apiControllerManager";
import { useDispatch } from "react-redux";
import { checkStatusBatch } from "../ultis/commonFunction";
export const Batch = () => {
  const { t } = useTranslation();
  const defaultLayoutPluginInstance = defaultLayoutPlugin();

  // message when sign
  const { isSignSuccess } = useApiControllerManager("");

  const [showAlert, setShowAlert] = useState(true);
  const [isActive, setIsActive] = useState([false]);

  const [isFetching, setIsFetching] = useState(false);

  const handleAlertClose = () => {
    setShowAlert(false);
  };

  const fieldStyle = {
    background: isActive ? "#f5fbfe" : "white",
  };

  const commonStyles = {
    color: "#e5e5e5",
    width: "",
    height: "6rem",
  };

  const { batch_token } = useParams();
  const [signers, setSigners] = useState([]);

  const getSigner = async () => {
    try {
      setIsFetching(true);
      const response = await api.post("/getSigning", {
        batch_token: batch_token,
      });
      setSigners(response.data);
      setIsFetching(false);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    getSigner();
    // getUSBConnector();
  }, [isSignSuccess]);

  const [signedInfo, setSignedInfo] = useState([]);

  const getSignedInfo = async (fileId, i) => {
    try {
      const response = await api.post("/getSignedInfo", {
        fileId: fileId,
      });
      setSignedInfo((prevSignedInfo) => {
        const updatedSignedInfo = [...prevSignedInfo];
        updatedSignedInfo[i] = response.data;
        return updatedSignedInfo;
      });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (signers.length > 0) {
      for (let i = 0; i < signers.length; i++) {
        getSignedInfo(signers[i].fileId, i);
      }
    }
  }, [signers]);

  const [showAnotherField, setShowAnotherField] = useState([false]);

  const [isViewed, setIsViewed] = useState([false]); // thêm biến để kiểm tra trường đã được xem hay chưa

  const [isShowPDF, setIsShowPDF] = useState([false]); // thêm biến để show pdf ra giao diện

  const check =
    JSON.stringify(showAnotherField) ===
    JSON.stringify(Array(signers.length).fill(true));

  function handleInputClickTitle(index) {
    const newButtonStates = [...showAnotherField];
    newButtonStates[index] = !newButtonStates[index];
    setShowAnotherField(newButtonStates); // đảo ngược giá trị của biến showAnotherField

    setIsShowPDF(newButtonStates); // đảo ngược giá trị của biến isShowPDF

    if (!isViewed[index]) {
      // nếu trường chưa được xem
      const newViewStates = [...isViewed];
      newViewStates[index] = !newViewStates[index];
      setIsViewed(newViewStates); // đặt trạng thái là đã xem
    }

    const newActiveStates = [...isActive];
    newActiveStates[index] = !newActiveStates[index];
    setIsActive(newButtonStates);
  }

  function handleShowPDF(index) {
    const newShowPDFStates = [...isShowPDF];
    newShowPDFStates[index] = !newShowPDFStates[index];
    setIsShowPDF(newShowPDFStates); // đảo ngược giá trị của biến isShowPDF
  }

  function showAll() {
    const initialState = Array(signers.length).fill(true);
    setShowAnotherField(initialState);
    setIsShowPDF(initialState); // đảo ngược giá trị của biến isShowPDF
    setIsViewed(initialState);
    setIsActive(initialState);
  }

  function closeAll() {
    const initialState = Array(signers.length).fill(false);
    setShowAnotherField(initialState);
    setIsShowPDF(initialState); // đảo ngược giá trị của biến isShowPDF
    setIsActive(initialState);
  }

  const dispatch = useDispatch();

  // truyền signingOptions sang navtab
  const signingOptions = ["usbtoken"];

  const currentURL = window.location.href;
  const url = new URL(currentURL);
  const path = url.pathname;
  // if (path.includes("result")) {
  //   console.log("hahaha");
  // }

  return (
    <main>
      <div className="container preview-document-container isign-signing-show isign-signature-pdf ">
        <div className="row">
          <div>
            {path.includes("result") ? (
              <div style={{ padding: "0px 5px" }}>
                <Alert
                  className="d-flex justify-content-center"
                  variant="filled"
                  severity="success"
                  // onClose={() => setMessageError("")}
                >
                  {t("sequence.successMessage")}
                </Alert>
              </div>
            ) : (
              <Fragment>
                <span className="counter-count">{signers.length}</span>
                <div className="panel-batch">
                  <div className="expand-all">
                    {check ? (
                      <div onClick={() => closeAll()}>
                        <CloseFullscreenIcon />
                        <span>{t("batch.collapse")}</span>
                      </div>
                    ) : (
                      <div onClick={() => showAll()}>
                        <OpenInFullIcon />
                        <span>{t("batch.expand")}</span>
                      </div>
                    )}
                  </div>
                </div>
              </Fragment>
            )}
            {isFetching && (
              <div
                className="modal backdrop fade show d-flex justify-content-center align-items-center"
                style={{ background: "#00000080" }}
              >
                <div className="loader" />
              </div>
            )}
            <div className="col-xs-12 document-list-block isign-documentlist-block">
              <h2>{t("batch.documents")}</h2>
              {/* -------------------------------------------------field 1------------------------------------------------------------------ */}
              {!path.includes("result") && isSignSuccess && (
                <div style={{ padding: "0px" }}>
                  <Alert
                    style={{ margin: "12px 0px", padding: "0 10px !important" }}
                    variant="filled"
                    severity={"success"}
                    onClose={() => {
                      dispatch(
                        apiControllerManagerActions.clearsetMessageSuccess()
                      );
                    }}
                  >
                    {t("single.noti")}
                  </Alert>
                </div>
              )}
              {signers.map((signer, index) => (
                <div key={index}>
                  <div
                    className="row pointer bd-bottom"
                    onClick={() => handleInputClickTitle(index)}
                    style={{
                      background: isActive[index] ? "#f5fbfe" : "white",
                    }}
                  >
                    <div style={{ borderBottom: "1px solid #e5e5e5" }}></div>
                    <div className="d-flex align-items-center">
                      <IconButton
                        className="col-xs-1"
                        onClick={() => handleInputClickTitle(index)}
                        style={{ color: "#009ede" }}
                      >
                        {showAnotherField[index] ? (
                          <ArrowDropUpIcon />
                        ) : (
                          <ArrowDropDownIcon />
                        )}
                      </IconButton>
                      <div
                        className="col-md-5 "
                        style={{ marginLeft: "1.5rem" }}
                      >
                        {signer.documentName}
                      </div>

                      <div className="document col-md-6 text-color">
                        {/* {signer.participants.every(
                          (item) => item.signerStatus === 2
                        ) ? (
                          <Fragment>
                            <CheckCircleOutlineRoundedIcon
                              style={{ color: "#0B95E5", marginRight: "5px" }}
                            />
                            <span style={{ color: "#0B95E5" }}>
                              {t("single.signatureValid")}
                            </span>
                          </Fragment>
                        ) : isViewed[index] ? (
                          <div>
                            <VisibilityIcon fontSize="small"></VisibilityIcon>
                            <span>{t("batch.viewed")}</span>
                          </div>
                        ) : (
                          <div>
                            <span>{t("batch.waiting")}</span>
                          </div>
                        )} */}
                        {signer.participants.every(
                          (item) => item.signerStatus === 2
                        ) ? (
                          <Fragment>
                            <CheckCircleOutlineRoundedIcon
                              style={{ color: "#0B95E5", marginRight: "5px" }}
                            />
                            <span style={{ color: "#0B95E5" }}>
                              {t("single.signatureValid")}
                            </span>
                          </Fragment>
                        ) : signer.participants.find(
                            (item) =>
                              item.signerStatus === 2 &&
                              item.signerToken === signer.signerToken
                          ) ? (
                          <div>
                            <span>{t("batch.waiting")}</span>
                          </div>
                        ) : isViewed[index] ? (
                          <div className="d-flex justify-content-end">
                            <VisibilityIcon
                              fontSize="small"
                              style={{ marginRight: "4px" }}
                            ></VisibilityIcon>
                            <span>{t("batch.viewed")}</span>
                          </div>
                        ) : (
                          <div>
                            <span>{t("batch.waitingmysignature")}</span>
                          </div>
                        )}
                      </div>
                    </div>
                  </div>

                  {showAnotherField[index] && (
                    <div className="row bd-bottom">
                      <div
                        className="col-sm-5"
                        style={{ borderRight: "1px solid rgba(0, 0, 0, 0.1)" }}
                      >
                        <div
                          className="col-xs-1 pointer"
                          // onClick={handleInputClickName6}
                          onClick={() => handleShowPDF(index)}
                        >
                          <IconButton style={{ color: "#009ede" }}>
                            <Search />
                          </IconButton>
                          <div
                            style={{ borderBottom: "1px solid #e5e5e5" }}
                          ></div>
                          <div
                            className="col-xs-4"
                            style={{ marginLeft: "2rem", marginTop: "-30px" }}
                          >
                            {signer.fileName} (
                            {(signer.fileSize * 0.000001).toFixed(1)} MB)
                          </div>
                        </div>
                      </div>

                      {/* <div className="hidden-batch col-sm-1">
                        <Box sx={{ ...commonStyles, borderRight: 1 }} />
                      </div> */}

                      <div className="col-sm-1">
                        <div
                          style={{
                            margin: "15px 10px 24px 18px",
                            width: "100%",
                          }}
                        >
                          {t("batch.signers")}
                        </div>
                      </div>
                      <div className="col-sm-6">
                        <table
                          style={{
                            margin: "15px 10px 24px 18px",
                            width: "100%",
                          }}
                        >
                          <tbody>
                            {signedInfo[index]?.length > 0 &&
                              signedInfo[index].map((e, i) => {
                                let signedType =
                                  e.is_seal === true ? "SEAL" : "NORMAL";
                                const info = JSON.parse(e.value);
                                return (
                                  <tr key={index}>
                                    <td>
                                      {info.certificate.subject.common_name}
                                    </td>
                                    <td
                                      className="text-color"
                                      style={{ width: "40%" }}
                                    >
                                      {checkStatusBatch(2, signedType)}
                                    </td>
                                  </tr>
                                );
                              })}
                            {signer.participants.map((value, index) => (
                              <tr key={index}>
                                {/* <td className="text-color">
                                  {t("batch.signers")}
                                </td> */}
                                <td>
                                  {value.firstName + " " + value.lastName}
                                </td>
                                <td
                                  className="text-color"
                                  style={{ width: "40%" }}
                                >
                                  {checkStatusBatch(
                                    value.signerStatus,
                                    value.signedType
                                  )}
                                </td>
                              </tr>
                            ))}
                          </tbody>
                        </table>
                        {signer.participants.length !== 0 &&
                          signer.participants.every(
                            (item) => item.signerStatus === 2
                          ) && (
                            <div
                              className="d-flex justify-content-end"
                              style={{
                                paddingRight: "20px",
                                marginBottom: "10px",
                              }}
                            >
                              <a
                                style={{
                                  background: "#0194D0",
                                  borderColor: "#0194D0",
                                  marginTop: "20px",
                                  whiteSpace: "nowrap",
                                  color: "white",
                                }}
                                className="btn"
                                href={`${window.location.origin}${process.env.PUBLIC_URL}/${signer.signingToken}/download?access_token=${signer.signerToken}`}
                                download
                              >
                                {t("single.btnDownload")}
                              </a>
                            </div>
                          )}
                      </div>
                    </div>
                  )}
                  {signer.participants.length !== 0 &&
                  signer.participants.every(
                    (item) => item.signerStatus === 2
                  ) ? null : (
                    <Collapse
                      className="re-sign"
                      in={isShowPDF[index]}
                      direction="up"
                    >
                      <div style={{ height: "400px" }}>
                        {signer.base64 && (
                          <Worker workerUrl="https://unpkg.com/pdfjs-dist@2.16.105/build/pdf.worker.min.js">
                            <Viewer
                              fileUrl={`data:application/pdf;base64,${signer.base64}`}
                              plugins={[defaultLayoutPluginInstance]}
                            ></Viewer>
                          </Worker>
                        )}
                      </div>
                    </Collapse>
                  )}
                </div>
              ))}
            </div>
          </div>
          {signers.length > 0 &&
          signers.every((item) =>
            item.participants.some(
              (participant) =>
                item.signerToken === participant.signerToken &&
                participant.signerStatus === 2
            )
          ) ? null : (
            <div className="col-xs-12 isign-sign-pane">
              <h2>{t("single.content")}</h2>
              {isViewed.includes(false) ||
              isViewed.length !== signers.length ? (
                <React.Fragment>
                  <Alert
                    severity="info"
                    // onClose={handleAlertClose}
                    style={{
                      color: "#31708f",
                      backgroundColor: "#d9edf7",
                      borderColor: "#bce8f1",
                      marginBottom: "10px",
                    }}
                  >
                    {t("batch.notification")}
                  </Alert>
                  <Box sx={{ width: "100%" }}>
                    <Box sx={{ borderBottom: 1, borderColor: "divider" }}>
                      <Tabs value={0} aria-label="basic tabs example">
                        <Tab
                          sx={{ textTransform: "none" }}
                          label={t("navTab.usb")}
                        />
                      </Tabs>
                    </Box>
                    <Button
                      disabled={true}
                      size="large"
                      variant="contained"
                      className="col-sm-2"
                      style={{
                        opacity: "0.5",
                        ":hover": {
                          cursor: "not-allowed",
                        },
                        marginTop: "20px",
                        // marginLeft: "-25px",
                        width: "300px",
                      }}
                    >
                      {t("usb.sign")}
                    </Button>
                  </Box>
                </React.Fragment>
              ) : (
                <NavTab
                  workFlow={signers}
                  signingOptions={signingOptions}
                ></NavTab>
              )}
            </div>
          )}
        </div>
      </div>
    </main>
  );
};
export default Batch;
