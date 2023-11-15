import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import "@react-pdf-viewer/core/lib/styles/index.css";
// default layout plugin
// import { defaultLayoutPlugin } from "@react-pdf-viewer/default-layout";
// Import styles of default layout plugin
// import "@react-pdf-viewer/default-layout/lib/styles/index.css";
import { useParams } from "react-router-dom";
import { Viewer, Worker } from "@react-pdf-viewer/core";
import { Alert, Collapse, IconButton } from "@mui/material";
import { api } from "../constants/api";
import { Search } from "@mui/icons-material";
import iconPdf from "../assets/images/icon_pdf.png";
import FileSigned from "../components/FileSigned";
import PDFViewer from "../components/validate/PDFViewer";

export const Open = () => {
  const { t } = useTranslation();
  // const defaultLayoutPluginInstance = defaultLayoutPlugin();

  const [showAlert, setShowAlert] = useState(true);
  const [showDocument, setShowDocument] = useState(true);

  const [documentFormat, setdocumentFormat] = useState();
  useEffect(() => setdocumentFormat("PDF - " + t("single.standard")), [t]);

  const handleAlertClose = () => {
    setShowAlert(false);
  };

  function handleDocument() {
    setShowDocument(!showDocument); // đảo ngược giá trị của biến showAnotherField
  }

  const { upload_token } = useParams();

  const [signedInfo, setSignedInfo] = useState([]);
  console.log("signedInfo: ", signedInfo);

  const getFileInfo = async (upload_token) => {
    try {
      const response = await api.post("/getFileInfo", {
        upload_token,
      });

      setSignedInfo(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const [originalFile, setOriginalFile] = useState({});
  console.log("originalFile: ", originalFile);

  const getFirstFileFromUploadToken = async (upload_token) => {
    try {
      const response = await api.post("/getFirstFileFromUploadToken", {
        upload_token,
      });

      setOriginalFile(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (upload_token) {
      getFileInfo(upload_token);
      getFirstFileFromUploadToken(upload_token);
    }
  }, []);

  return (
    <main>
      <div className="container preview-document-container isign-signing-show isign-signature-pdf ">
        <div className="col-xs-12 isign-sign-pane">
          {signedInfo.length === 0 ? (
            showAlert && (
              <Alert
                severity="info"
                onClose={handleAlertClose}
                style={{
                  color: "#ffa119",
                  backgroundColor: "#fcf8e3",
                  border: "1px solid #faebcc",
                  marginBottom: "10px",
                }}
              >
                {t("open.notSigned")}
              </Alert>
            )
          ) : (
            <Alert
              style={{ margin: "12px 0px", padding: "0 10px !important" }}
              severity="success"
              onClose={handleAlertClose}
            >
              {t("open.valid")}
            </Alert>
          )}
          {/* Metadata */}
          <div className="col-xs-12 isign-details-block">
            <h2>
              {" "}
              <span className="preview-document-step">1. </span>
              {t("open.metadata")}
            </h2>
            <div className="row" style={{ paddingBottom: "10px" }}>
              <div className="col-sm-2 col-4" style={{ color: "#aaaaaa" }}>
                {t("open.name")}{" "}
              </div>
              <div className="col-sm-2 col-8">
                {originalFile !== null &&
                  Object.keys(originalFile).length !== 0 &&
                  originalFile?.fileName.split(".").slice(0, -1).join(".")}
              </div>
            </div>

            <div className="row">
              <div className="col-sm-2 col-4" style={{ color: "#aaaaaa" }}>
                {t("open.documentFormat")}
              </div>
              <div className="col-sm-6 col-8">
                <img src={iconPdf}></img> {documentFormat}
              </div>
            </div>
          </div>

          {/* Content */}
          <div className="col-xs-12 document-list-block isign-documentlist-block">
            <h2>
              {" "}
              <span className="preview-document-step">2. </span>
              {t("open.content")}
            </h2>
            <div className="row pointer bd-bottom" onClick={handleDocument}>
              <div style={{ borderBottom: "1px solid #e5e5e5" }}></div>
              <div className="col-1" style={{ marginTop: "2px" }}>
                <IconButton
                  onClick={handleDocument}
                  style={{ color: "#009ede" }}
                >
                  <Search />
                </IconButton>
              </div>
              <div className="col-xs-8 mobile-content laptop-content">
                {originalFile?.fileName} (
                {(originalFile?.fileSize * 0.000001).toFixed(1)} MB)
              </div>
            </div>
          </div>

          {/* Show file PDF */}
          <Collapse
            className="re-sign"
            in={!showDocument}
            direction="up"
            // style={{ marginBottom: "50px" }}
          >
            <div style={{ height: "400px" }}>
              {originalFile?.base64 && (
                <PDFViewer base64={originalFile?.base64} />
              )}
            </div>
          </Collapse>

          {/* Signers */}
          {signedInfo && Object.keys(signedInfo).length !== 0 && (
            <div className="col-xs-12 document-list-block isign-documentlist-block">
              <h2>
                <span className="preview-document-step">3. </span>
                {t("single.signer")}
              </h2>
              <FileSigned listSignedInfo={signedInfo} />
            </div>
          )}
        </div>
      </div>
    </main>
  );
};

export default Open;
