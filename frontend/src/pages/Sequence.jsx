import { ExpandLess, ExpandMore } from "@mui/icons-material";
import CheckCircleOutlineRoundedIcon from "@mui/icons-material/CheckCircleOutlineRounded";
import RadioButtonUncheckedIcon from "@mui/icons-material/RadioButtonUnchecked";
import {
  Alert,
  Box,
  Button,
  Collapse,
  IconButton,
  LinearProgress,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Link, useNavigate, useParams } from "react-router-dom";
import SigningComponent from "../components/SigningComponent";
import SequenceButtonField from "../components/form/sequence_button_field";
import { api } from "../constants/api";
import { useApiControllerManager } from "../store/apiControllerManager";

export const Sequence = () => {
  const { t } = useTranslation();
  const { batch_token } = useParams();

  const [isFetching, setIsFetching] = useState(false);

  // message when sign
  const { isSignSuccess } = useApiControllerManager("");

  //   const [headerVisible, setHeaderVisible] = useState(0);
  //   console.log("headerVisible: ", headerVisible);

  //   const checkHeaderVisible = async (batch_token) => {
  //     try {
  //       const response = await api.post("/checkHeaderBS", {
  //         batchToken: batch_token,
  //       });
  //       setHeaderVisible(response.data);
  //     } catch (error) {}
  //   };

  const [workFlows, setWorkFlows] = useState([]);

  const getSigner = async (batch_token) => {
    try {
      setIsFetching(true);
      const response = await api.post("/getSigning", {
        batch_token: batch_token,
      });
      setWorkFlows(response.data);
      setIsFetching(false);
    } catch (e) {
      console.log(e);
    }
  };

  // const [messageData, setMessageData] = useState(
  //   JSON.parse(sessionStorage.getItem("messageHome"))
  // );

  useEffect(() => {
    getSigner(batch_token);
    // getUSBConnector();
  }, [isSignSuccess]);

  const [showDocumentSign, setShowDocumentSign] = useState(false);

  function handleDocumentSign() {
    setShowDocumentSign(!showDocumentSign); // đảo ngược giá trị của biến showAnotherField
  }

  const navigate = useNavigate();
  if (
    workFlows.length > 0 &&
    workFlows.every((item) =>
      item.participants.some(
        (participant) =>
          item.signerToken === participant.signerToken &&
          participant.signerStatus === 2
      )
    )
  ) {
    navigate(`/signing/sequence/${batch_token}/result`);
  }

  useEffect(() => {
    if (isSignSuccess) {
      setIndex(
        workFlows.findIndex((signer) =>
          signer.participants.find(
            (participant) =>
              signer.signerToken === participant.signerToken &&
              participant.signerStatus === 1
          )
        )
      );
    }
  }, [workFlows]);

  const checkIndex = (index) => {
    if (index === 0) {
      return (
        <Box width={155} sx={{ "& button": { m: 1 } }}>
          <SequenceButtonField
            handleButtonClick={() =>
              setIndex(index < workFlows.length - 1 ? index + 1 : 0)
            }
            text={t("sequence.next")}
          />
        </Box>
      );
    } else if (index === workFlows.length - 1) {
      return (
        <Box width={155} sx={{ "& button": { m: 1 } }}>
          <SequenceButtonField
            handleButtonClick={() =>
              setIndex(index > 0 ? index - 1 : workFlows.length - 1)
            }
            text={t("sequence.previous")}
          />
        </Box>
      );
    } else {
      return (
        <Box>
          <Box width={155} sx={{ "& button": { m: 1 } }}>
            <SequenceButtonField
              handleButtonClick={() =>
                setIndex(index > 0 ? index - 1 : workFlows.length - 1)
              }
              text={t("sequence.previous")}
            />
          </Box>

          <Box width={155} sx={{ "& button": { m: 1 } }}>
            <SequenceButtonField
              handleButtonClick={() =>
                setIndex(index < workFlows.length - 1 ? index + 1 : 0)
              }
              text={t("sequence.next")}
            />
          </Box>
        </Box>
      );
    }
  };

  const [index, setIndex] = useState(0);

  const [progress, setProgress] = useState(0);

  useEffect(() => {
    if (Array.isArray(workFlows) && workFlows.length > 0) {
      calProgress();
    }
  }, [workFlows, isSignSuccess]);

  const calProgress = () => {
    const temp = workFlows.reduce((count, signer) => {
      const completedParticipants = signer.participants.filter(
        (participant) =>
          signer.signerToken === participant.signerToken &&
          participant.signerStatus === 2
      );
      return count + completedParticipants.length;
    }, 0);
    setProgress((temp / workFlows.length) * 100);
  };

  return (
    <main>
      <div className="container preview-document-container isign-signing-show isign-signature-pdf ">
        <div className="row">
          {/* Document to sign sequentially */}
          {isFetching && (
            <div
              className="modal backdrop fade show d-flex justify-content-center align-items-center"
              style={{ background: "#00000080" }}
            >
              <div className="loader" />
            </div>
          )}
          <div>
            <div className="row">
              <div
                className="col-sm-7 col-md-4 d-flex align-items-center pointer"
                onClick={handleDocumentSign}
              >
                <IconButton style={{ color: "#009ede" }}>
                  {showDocumentSign ? <ExpandLess /> : <ExpandMore />}
                </IconButton>
                <div
                  style={{ margin: 0, fontSize: "18px" }}
                  className="sequentially"
                >
                  {workFlows.length} {t("sequence.document")}
                </div>
              </div>
              <div className="sequentially col-sm-5 col-md-3 d-flex align-items-center">
                <div className="progress">
                  <Box sx={{ width: "100%" }}>
                    <LinearProgress variant="determinate" value={progress} />
                  </Box>
                </div>
                <span className="progress-text">
                  {Math.round((progress / 100) * workFlows.length)}/
                  {workFlows.length} {t("sequence.signed")}
                </span>
              </div>
              <div className="sequentially col-sm-12 col-md-5 d-flex justify-content-center align-items-center sequence-btn">
                {checkIndex(index)}
              </div>
            </div>
            {isSignSuccess?.message && (
              <div style={{ padding: "0px 5px" }}>
                <Alert
                  style={{ margin: "12px 0px" }}
                  variant="filled"
                  severity="error"
                  onClose={() => {
                    isSignSuccess(null);
                  }}
                >
                  {isSignSuccess}
                </Alert>
              </div>
            )}
            <Collapse
              in={showDocumentSign}
              direction="up"
              style={{ padding: "7px 11px" }}
            >
              {workFlows.map((signer, i) => {
                return (
                  <div
                    key={i}
                    className="col-sm-7 col-md-4 d-flex align-items-center"
                  >
                    {signer.participants.some(
                      (e) =>
                        e.signerToken === signer.signerToken &&
                        e.signerStatus === 2
                    ) ? (
                      <CheckCircleOutlineRoundedIcon
                        style={{
                          color: "#0B95E5",
                          marginRight: "5px",
                          fontSize: "20px",
                        }}
                      />
                    ) : (
                      <RadioButtonUncheckedIcon
                        style={{
                          color: "#0B95E5",
                          marginRight: "5px",
                          fontSize: "20px",
                        }}
                      />
                    )}

                    <div
                      className="pointer"
                      style={{ margin: 0, paddingLeft: "5px" }}
                    >
                      <Link
                        className="link-visited"
                        // style={
                        //   {fontWeight: "bold"}
                        // }
                        onClick={() => {
                          setIndex(i);
                        }}
                        style={{ textDecoration: "none" }}
                      >
                        <span className={i === index ? "fw-bold" : ""}>
                          {signer.documentName}
                        </span>
                      </Link>
                    </div>
                  </div>
                );
              })}
            </Collapse>
          </div>
          {/* Document Information */}
          {workFlows.length > 0 && (
            <SigningComponent workFlow={workFlows[index]} ischange={index} />
          )}
        </div>
      </div>
    </main>
  );
};

export default Sequence;
