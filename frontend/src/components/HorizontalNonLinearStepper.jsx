import { Alert, Box, Button, Stack, Typography } from "@mui/material";
import React, { Fragment, useEffect, useRef, useState } from "react";
import loading from "../assets/images/ajax-loader.gif";
import ISPluginClient from "../assets/js/checkid";

import jwtDecode from "jwt-decode";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { electronicService } from "../services/electronicService";
import {
  apiControllerManagerActions,
  useApiControllerManager,
} from "../store/apiControllerManager";
import {
  Step1,
  Step2,
  Step3,
  Step4,
  Step5,
  Step6,
  Step7,
  Step8,
} from "./ElectronicId";
import Step10 from "./ElectronicId/Step10";
import Step11 from "./ElectronicId/Step11";
import Step11a from "./ElectronicId/Step11a";
import Step12 from "./ElectronicId/Step12";
import Step13 from "./ElectronicId/Step13";
import Step9 from "./ElectronicId/Step9";
import { getSignature, getSignerId } from "../ultis/commonFunction";

// const steps = [
//   "Check Citizen Identity Card",
//   "Accept provider’s terms of service",
//   "Insert card reader",
//   "Personal information",
//   "Scan face",
//   "to be không tình yêu",
//   "dzì zọ",
// ];

const HorizontalNonLinearStepper = ({
  type,
  code,
  connectorName,
  workFlow,
  handleCloseModal1,
}) => {
  console.log("workFlow: ", workFlow);
  const { t } = useTranslation();
  const dispatch = useDispatch();

  let lang = localStorage.getItem("language");
  switch (lang) {
    case "Vietnamese":
      lang = "VN";
      break;
    default:
      lang = "EN";
      break;
  }

  const signerId = getSignerId(workFlow);

  const { signaturePrepare } = useApiControllerManager();
  const signature = getSignature(
    signaturePrepare,
    signerId,
    workFlow.workFlowId
  );
  console.log("signature: ", signature);

  const [subject, setSubject] = useState("");
  console.log("subject: ", subject);
  // const { subject, isIdentifyRegistered, personalInfomation, image } =
  //   useElectronicControllerManager();

  const [isIdentifyRegistered, setIsIdentifyRegistered] = useState(false);

  let typeAlias = "CITIZEN_CARD";

  const checkIdentity = async () => {
    const data = {
      lang: lang,
      code: code,
      type: typeAlias,
      connectorName: connectorName,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
    };
    try {
      const response = await electronicService.checkIdentity(data);
      if (response.data) {
        setSubject(response.data);
      }
      if (response.data.status === 0) {
        setIsIdentifyRegistered(true);
        setPersonalInformation(response.data.personal_informations);
        setImage(response.data.personal_informations.dg2);
      }
    } catch (error) {
      console.log("error: ", error);
    }
  };

  // const checkIdentity = () => {
  //   const data = {
  //     lang: lang,
  //     code: code,
  //     type: typeAlias,
  //     connectorName: connectorName,
  //     enterpriseId: workFlow.enterpriseId,
  //     workFlowId: workFlow.workFlowId,
  //   };

  //   dispatch(checkUserIdentity(data));
  // };

  const [errorPG, setErrorPG] = useState(null);
  const [faceSuccess, setFaceSuccess] = useState(null);
  const [isFetching, setIsFetching] = useState(false);
  // const [phoneNumber, setPhoneNumber] = useState(null);
  // const [email, setEmail] = useState(null);
  const phoneNumberRef = useRef(null);
  const emailRef = useRef(null);
  const [personalInfomation, setPersonalInformation] = useState(null);
  const [image, setImage] = useState(null);
  const [otp, setOtp] = useState(null);
  const [jwt, setJwt] = useState(null);
  const [certificate, setCertificate] = useState(null);
  const [certificateList, setCertificateList] = useState(null);
  const [processId, setProcessId] = useState(null);
  const [requestID, setRequestID] = useState(null);
  const [imageFace, setImageFace] = useState(null);
  const [shouldDetectFaces, setShouldDetectFaces] = useState(true);
  const [direction, setDirection] = useState(null);

  const providerSelected = useRef(null);

  const [activeStep, setActiveStep] = useState(0);
  console.log("activeStep: ", activeStep);
  const [skipped, setSkipped] = useState(new Set());

  const isStepOptional = (step) => {
    return step === 15;
  };

  const isStepSkipped = (step) => {
    return skipped.has(step);
  };

  const handleNext = (step = 1) => {
    let newSkipped = skipped;
    if (isStepSkipped(activeStep)) {
      newSkipped = new Set(newSkipped.values());
      newSkipped.delete(activeStep);
    }

    setActiveStep((prevActiveStep) => prevActiveStep + step);
    setSkipped(newSkipped);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleSkip = () => {
    if (!isStepOptional(activeStep)) {
      // You probably want to guard against something like this,
      // it should never occur unless someone's actively trying to break something.
      throw new Error("You can't skip a step that isn't optional.");
    }

    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    setSkipped((prevSkipped) => {
      const newSkipped = new Set(prevSkipped.values());
      newSkipped.add(activeStep);
      return newSkipped;
    });
  };

  const handleReset = () => {
    setActiveStep(0);
  };

  // step 2
  const [isSubmitDisabled, setIsSubmitDisabled] = useState(true);

  const handleDisableSubmit = (disabled) => {
    setIsSubmitDisabled(disabled);
  };

  let flagFailedConnectHTML = 0;
  let connectSuccessfully = false;
  const sdk = useRef(null);

  function disconnectWSHTML() {
    flagFailedConnectHTML = 0;
    console.log("đóng sdk");
    sdk.current.shutdown();
  }

  const connectToWS = () => {
    flagFailedConnectHTML = 1;

    const ipWS = "127.0.0.1";
    const portWS = "9505";
    const typeOfWS = "wss";

    var url = typeOfWS + "://" + ipWS + ":" + portWS + "/ISPlugin";
    sdk.current = new ISPluginClient(
      ipWS,
      portWS,
      typeOfWS,
      function () {
        console.log("connected");
        //            socket onopen => connected
        setIsFetching(true);
        if (activeStep === 2) {
          readCard();
        } else {
          // faceScan();
        }
        flagFailedConnectHTML = 1;
      },
      function () {
        //            socket disconnected
        console.log("Connect error");
      },
      function () {
        //            socket stopped
        flagFailedConnectHTML = 0;

        connectSuccessfully = false;
      },
      function (statusCallBack) {
        console.log("connected denied");
      },
      function (cmd, id, error, data) {
        //RECEIVE
        console.log("cmd: " + cmd);
      }
    );
  };

  const readCard = () => {
    setErrorPG(null);
    sdk.current.getInformationDetails(
      true,
      true,
      true,
      true,
      true,
      true,
      true,
      lang,
      code.slice(-6),
      60,
      function (response) {
        console.log("subject Get: ", response);
        setIsFetching(false);
        setPersonalInformation(response.optionalDetails);
        setImage(response.image);
        handleNext();
      },
      function (error, mess) {
        console.log("error: ", error);
        console.log("mess: ", mess);
        if (error === 1001) {
          setErrorPG(t("electronicid.eid not found"));
        }

        switch (error) {
          case 1001:
            setErrorPG(t("electronicid.eid not found"));
            break;
          case 1102:
            setErrorPG(t("electronicid.can wrong"));
            break;
          default:
            setErrorPG(mess);
            break;
        }
        setIsFetching(false);
        disconnectWSHTML();
      },
      function () {
        console.log("timeout");
        setIsFetching(false);
        sdk.current = null;
      }
    );
  };

  const faceAndCreate = async () => {
    setIsFetching(true);
    const data = {
      lang: lang,
      code: code,
      type: typeAlias,
      facialImage: image,
      imageFace: imageFace,
    };

    try {
      // const response = await api.post("/elec/faceAndCreate", data);
      const response = await electronicService.faceAndCreate(data);
      // console.log("faceAndCreate: ", response);
      if (response.data.status === 0) {
        setJwt(response.data.jwt);
        // setProcessId(response.data);
        // setIsFetching(false);
        try {
          var decoded = jwtDecode(response.data.jwt);

          // console.log("decoded", decoded);

          if (decoded.mobile === "" || decoded.phone_number === "") {
            console.log("kiểm tra: ");

            checkIdentity();
          }

          let stepToNavigate = -1; // Giá trị mặc định để không thực hiện việc nhảy step

          if (decoded.phone_number === "") {
            console.log("Đăng ký số điện thoại: ");
            // stepToNavigate = 1;
            stepToNavigate = 6;
          } else if (decoded.email === "") {
            console.log("Đăng ký email: ");
            // setPhoneNumber(decoded.phone_number);
            phoneNumberRef.current = decoded.phone_number;
            // stepToNavigate = 3;
            stepToNavigate = 8;
          } else {
            // setPhoneNumber(decoded.phone_number);
            // setEmail(decoded.email);
            phoneNumberRef.current = decoded.phone_number;
            emailRef.current = decoded.email;
            // stepToNavigate = 5;
            stepToNavigate = 10;
          }

          if (stepToNavigate !== -1) {
            setDirection(t("electronicid.faceSuccess"));
            setFaceSuccess(t("electronicid.camSuccess"));
            setTimeout(() => {
              setIsFetching(false);
              setFaceSuccess(null);
              // handleNext(stepToNavigate);
              setActiveStep(stepToNavigate);
            }, 1000);
          }
        } catch (error) {
          console.error("Lỗi khi giải mã JWT:", error);
        }
      }
    } catch (error) {
      console.error("Lỗi khi gọi API faceAndCreate:", error);
      setIsFetching(false);
      setErrorPG(error.response.data.message);
      // Xử lý lỗi tại đây nếu cần
    }
  };

  const updateSubject = async () => {
    const data = {
      lang: lang,
      jwt: jwt,
      phoneNumber: phoneNumberRef.current,
      email: emailRef.current,
      subject_id: personalInfomation.subject_id,
    };

    try {
      // const response = await api.post("/elec/updateSubject", data);
      const response = await electronicService.updateSubject(data);
      setProcessId(response.data);
      handleNext();
    } catch (error) {
      console.error("Lỗi khi gọi API updateSubject:", error);
      switch (activeStep) {
        case 6:
          setErrorPG(t("electronicid.phone error"));
          break;
        case 8:
          setErrorPG(t("electronicid.email error"));
          break;
        default:
          setErrorPG(error.response.data.message);
          break;
      }

      // Xử lý lỗi tại đây nếu cần
    }
  };

  const perFormProcess = async (otp) => {
    setErrorPG(null);
    const data = {
      lang: lang,
      otp: otp,
      subject_id: personalInfomation.subject_id,
      process_id: processId,
    };
    try {
      // const response = await api.post("/elec/processPerForm", data);
      const response = await electronicService.perFormProcess(data);
      if (response.data.status === 0) {
        setJwt(response.data.jwt);
        try {
          var decoded = jwtDecode(response.data.jwt);

          if (activeStep === 7) {
            if (decoded.email === "") {
              console.log("Đăng ký email: ");
              // handleNext(1); // Đăng ký email
              setActiveStep(8);
            } else {
              console.log("Email đã có, cập nhật: ");
              emailRef.current = decoded.email;
              // handleNext(3); // Chuyển tới step tiếp theo sau khi cập nhật email
              setActiveStep(10);
            }
          } else {
            console.log("Nhảy một bước tiến: ");
            // handleNext(1); // Nhảy một bước tiến nếu không phải step 7
            setActiveStep(10);
          }
        } catch (error) {
          console.error("Lỗi khi giải mã JWT:", error);
        }
      } else {
        setErrorPG(response.data.message);
        setIsFetching(false);
      }
    } catch (error) {
      console.log("error: ", error);
      // setErrorPG(t("electronicid.step136"));
      setErrorPG(error.response.data.message);
    }
  };

  const processOTPResend = async () => {
    setErrorPG(null);
    const data = {
      lang: lang,
      jwt: jwt,
      subject_id: personalInfomation.subject_id,
      process_id: processId,
    };
    try {
      // const response = await api.post("/elec/processOTPResend", data);
      const response = await electronicService.processOTPResend(data);
      if (response.data.status === 0) {
        handleNext();
      } else {
        setErrorPG(response.data.message);
      }
    } catch (error) {
      console.log("error", error);
    }
  };

  const checkCertificate = async () => {
    setIsFetching(true);
    const data = {
      lang: lang,
      jwt: jwt,
      connectorName: connectorName,
      connectorNameRSSP: providerSelected.current,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
    };
    try {
      // const response = await api.post("/elec/checkCertificate", data);
      const response = await electronicService.checkCertificate(data);
      setIsFetching(false);
      if (response.data.length === 0) {
        createCertificate();
        // handleNext(2);
      } else {
        setCertificateList(response.data);
        handleNext();
      }
      // setCertificate(response.data);
    } catch (error) {
      setIsFetching(false);
      console.log("error", error);
      setErrorPG(error.response.data.message);
    }
  };

  const createCertificate = async () => {
    setIsFetching(true);
    const data = {
      lang: lang,
      jwt: jwt,
      connectorName: connectorName,
      connectorNameRSSP: providerSelected.current,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
    };
    try {
      // const response = await api.post("/elec/createCertificate", data);
      const response = await electronicService.createCertificate(data);
      setIsFetching(false);
      setCertificate(response.data);
      // handleNext();
      setActiveStep(12);
    } catch (error) {
      setIsFetching(false);
      console.log("error", error);
      setErrorPG(error.response.data.message);
    }
  };

  const credentialOTP = async () => {
    setIsFetching(true);
    const data = {
      lang: lang,
      credentialID: certificate.credentialID,
      connectorName: connectorName,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
    };
    try {
      // const response = await api.post("/elec/credentialOTP", data);
      const response = await electronicService.credentialOTP(data);
      setRequestID(response.data);
      setIsFetching(false);
      if (activeStep === 12) {
        handleNext();
      }
    } catch (error) {
      setIsFetching(false);
      console.log("error", error);
    }
  };

  const authorizeOTP = async (otp) => {
    dispatch(apiControllerManagerActions.clearsetMessageSuccess());
    console.log("authorizeOTP");
    setIsFetching(true);
    setErrorPG(null);
    const data = {
      lang: lang,
      credentialID: certificate.credentialID,
      requestID: requestID,
      otp: otp,
      signerId: signerId,
      signingToken: workFlow.signingToken,
      fileName: workFlow.fileName,
      signerToken: workFlow.signerToken,
      connectorName: connectorName,
      signingOption: "electronic_id",
      codeNumber: code,
      type: type,
      certChain: certificate.cert,
      enterpriseId: workFlow.enterpriseId,
      workFlowId: workFlow.workFlowId,
      fieldName: signature ? signature.field_name : "",
      lastFileId: workFlow.lastFileId,
      documentId: workFlow.documentId,
    };
    try {
      // const response = await api.post("/elec/authorizeOTP", data);
      const response = await electronicService.authorizeOTP(data);
      // setRequestID(response.data);
      // handleNext();
      window.parent.postMessage(
        { data: response.data, status: "Success" },
        "*"
      );
      dispatch(apiControllerManagerActions.setMessageSuccess());
      // handleCloseModal1();
    } catch (error) {
      console.log("error", error);
      setIsFetching(false);
      setErrorPG(error.response.data.message);
    }
  };

  useEffect(() => {
    // switch (type) {
    //   case "CITIZEN-IDENTITY-CARD":
    //     typeAlias = "CITIZEN_CARD";
    //     break;
    //   default:
    //     typeAlias = "";
    //     break;
    // }
    checkIdentity();
    return () => {
      setErrorPG(null);
      if (sdk.current) {
        disconnectWSHTML();
      }
    };
  }, []);

  useEffect(() => {
    if (imageFace != null) {
      faceAndCreate();
    }
  }, [imageFace]);

  useEffect(() => {
    setErrorPG(null);
    // Tự động chuyển sang bước tiếp theo khi bỏ qua các bước

    // if (activeStep === 1 && isIdentifyRegistered) {
    //   setActiveStep((prevActiveStep) => prevActiveStep + 2);
    // }
    // if (activeStep === 6 && personalInfomation.subject_id === undefined) {
    //   console.log("chua có subject id");
    //   checkIdentity();
    // }
    // if (activeStep === 6 && personalInfomation.mobile) {
    //   // handleNext();
    //   setActiveStep((prevActiveStep) => prevActiveStep + 2);
    // }
    // if (activeStep === 4) {
    //   setActiveStep((prevActiveStep) => prevActiveStep + 2);
    // }
  }, [activeStep, personalInfomation, isIdentifyRegistered]);

  const handleSubmitClick = () => {
    switch (activeStep) {
      case 0:
        if (isIdentifyRegistered) {
          handleNext(3);
        } else {
          handleNext(1);
        }
        break;
      case 2:
        connectToWS();
        break;
      case 5:
        // faceAndCreate();
        setShouldDetectFaces(true);
        setErrorPG(null);
        break;
      case 6:
        updateSubject();
        break;
      case 7:
        perFormProcess(otp);
        break;
      case 8:
        updateSubject();
        break;
      case 9:
        perFormProcess(otp);
        break;
      case 10:
        checkCertificate();
        // createCertificate();
        break;
      case 11:
        if (certificate) {
          handleNext();
        } else {
          createCertificate();
        }
        // checkCertificate();
        break;
      case 12:
        credentialOTP();
        break;
      case 13:
        authorizeOTP(otp);
        break;
      default:
        // perFormProcess(); // chỉ để test
        handleNext();
    }
  };

  const steps = [
    // <Step11a />,

    <Step1 isIdentifyRegistered={isIdentifyRegistered} />,
    <Step2 onDisableSubmit={handleDisableSubmit} />,
    <Step3 />,
    <Step4 image={image} personalInfomation={personalInfomation} />,
    <Step5 />,
    <Step6
      handleCloseModal1={handleCloseModal1}
      setImageFace={setImageFace}
      shouldDetectFaces={shouldDetectFaces}
      setShouldDetectFaces={setShouldDetectFaces}
      setIsFetching={setIsFetching}
      setErrorPG={setErrorPG}
      direction={direction}
      setDirection={setDirection}
    />,
    <Step7
      onDisableSubmit={handleDisableSubmit}
      phoneNumberRef={phoneNumberRef}
    />,
    <Step8
      setOtp={setOtp}
      onDisableSubmit={handleDisableSubmit}
      processOTPResend={processOTPResend}
      handleCloseModal1={handleCloseModal1}
      setErrorPG={setErrorPG}
      perFormProcess={perFormProcess}
    />,
    <Step9
      onDisableSubmit={handleDisableSubmit}
      emailRef={emailRef}
      setErrorPG={setErrorPG}
    />,
    <Step10
      isFetching={isFetching}
      setOtp={setOtp}
      onDisableSubmit={handleDisableSubmit}
      processOTPResend={processOTPResend}
      handleCloseModal1={handleCloseModal1}
      setErrorPG={setErrorPG}
      perFormProcess={perFormProcess}
    />,
    <Step11
      onDisableSubmit={handleDisableSubmit}
      providerSelected={providerSelected}
      isFetching={isFetching}
    />,
    <Step11a
      certificateList={certificateList}
      setCertificate={setCertificate}
    />,
    <Step12 phoneNumberRef={phoneNumberRef} />,
    <Step13
      setOtp={setOtp}
      onDisableSubmit={handleDisableSubmit}
      phoneNumberRef={phoneNumberRef}
      resendCredentialOTP={credentialOTP}
      handleCloseModal1={handleCloseModal1}
      setErrorPG={setErrorPG}
      authorizeOTP={authorizeOTP}
      isFetching={isFetching}
    />,
  ];

  return (
    <Box sx={{ width: "100%" }}>
      {/* <Stepper activeStep={activeStep}>
        {steps.map((label, index) => {
          const stepProps = {};
          const labelProps = {};
          if (isStepOptional(index)) {
            labelProps.optional = (
              <Typography variant="caption">Optional</Typography>
            );
          }
          if (isStepSkipped(index)) {
            stepProps.completed = false;
          }
          return (
            <Step key={label} {...stepProps}>
              <StepLabel {...labelProps}>{label}</StepLabel>
            </Step>
          );
        })}
      </Stepper> */}
      {activeStep === steps.length ? (
        <Fragment>
          <Typography sx={{ mt: 2, mb: 1 }}>
            All steps completed - you&apos;re finished
          </Typography>
          <Box sx={{ display: "flex", flexDirection: "row", pt: 2 }}>
            <Box sx={{ flex: "1 1 auto" }} />
            <Button onClick={handleReset}>Reset</Button>
          </Box>
        </Fragment>
      ) : (
        subject &&
        Object.keys(subject).length !== 0 && (
          <Stack
            justifyContent="space-between"
            sx={{
              height: "625px",
            }}
          >
            {/* Nội dung ở đây */}
            <Typography component="div" sx={{ mt: 2, mb: 1, height: "80%" }}>
              {steps[activeStep]}
              {/* <div
                style={{ textAlign: "center", color: "red", marginTop: "5px" }}
              >
                {errorPG}
              </div> */}
            </Typography>
            {/* Hết nội dung */}
            <Typography component="div">
              {!faceSuccess && errorPG && (
                <Alert severity="error">{errorPG}</Alert>
              )}
              {faceSuccess && <Alert severity="success">{faceSuccess}</Alert>}
              <Box sx={{ display: "flex", flexDirection: "row", pt: 2 }}>
                <Button
                  color="inherit"
                  // disabled={activeStep === 0}
                  // onClick={handleBack}
                  // onClick={handleCloseModal1}
                  onClick={
                    activeStep === 1 ||
                    activeStep === 2 ||
                    activeStep === 11 ||
                    activeStep === 12
                      ? handleBack
                      : handleCloseModal1
                  }
                  sx={{
                    mr: 1,
                    fontFamily: "Montserrat, Nucleo, Helvetica, sans-serif",
                    "&:hover": {
                      backgroundColor: "transparent", // Tắt màu nền khi hover
                    },
                  }}
                >
                  {activeStep === 1 ||
                  activeStep === 2 ||
                  activeStep === 11 ||
                  activeStep === 12
                    ? t("electronicid.other9")
                    : t("electronicid.other5")}
                  {/* {t("electronicid.other5")} */}
                </Button>
                <Box sx={{ flex: "1 1 auto" }} />
                {isStepOptional(activeStep) && (
                  <Button color="inherit" onClick={handleSkip} sx={{ mr: 1 }}>
                    {t("electronicid.other6")}
                  </Button>
                )}

                <Button
                  onClick={handleSubmitClick}
                  disabled={
                    isFetching ||
                    // activeStep === 5 ||
                    ((activeStep === 1 ||
                      activeStep === 6 ||
                      activeStep === 7 ||
                      activeStep === 8 ||
                      activeStep === 9 ||
                      activeStep === 10 ||
                      activeStep === 13) &&
                      isSubmitDisabled)
                  }
                  sx={{
                    backgroundColor: "#1976D2",
                    color: "#fff",
                    padding: "10px 24px",
                    borderRadius: "25px",
                    "&:hover": {
                      backgroundColor: "#0056b3",
                      // Các thuộc tính khác khi hover (nếu muốn)
                    },
                    "&:disabled": {
                      color: "#fff",
                    },
                    fontFamily: "Montserrat, Nucleo, Helvetica, sans-serif",
                  }}
                >
                  {isFetching ? (
                    <div style={{ width: "76px" }}>
                      <img src={loading} width={20} alt="loading" />
                    </div>
                  ) : errorPG ? (
                    t("electronicid.other10")
                  ) : activeStep === steps.length - 1 ? (
                    t("electronicid.other7")
                  ) : (
                    t("electronicid.other8")
                  )}
                </Button>
              </Box>
            </Typography>
          </Stack>
        )
      )}
    </Box>
  );
};

export default HorizontalNonLinearStepper;
