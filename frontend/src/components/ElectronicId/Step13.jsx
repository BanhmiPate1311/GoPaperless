import { Box, Typography } from "@mui/material";
import CircularProgress, {
  circularProgressClasses,
} from "@mui/material/CircularProgress";
import React, { useEffect, useState, useRef } from "react";
import AuthCode from "react-auth-code-input";
import PropTypes from "prop-types";
import { useTranslation } from "react-i18next";

function CircularProgressWithLabel(props) {
  const formatTime = (seconds) => {
    const totalTime = 300;
    const minutes = Math.floor(((seconds / 100) * totalTime) / 60); // Số phút là phần nguyên khi chia cho 60
    const remainingSeconds = Math.floor(((seconds / 100) * totalTime) % 60); // Số giây còn lại là phần dư khi chia cho 60

    // Chuyển định dạng sang mm:ss
    const formattedTime = `${minutes}:${remainingSeconds
      .toString()
      .padStart(2, "0")}`;

    return formattedTime;
  };
  return (
    <Box sx={{ position: "relative", display: "inline-flex" }}>
      <CircularProgress
        variant="determinate"
        sx={{
          color: (theme) =>
            theme.palette.grey[theme.palette.mode === "light" ? 200 : 800],
        }}
        thickness={4}
        {...props}
        value={100}
      />
      <CircularProgress
        variant="determinate"
        sx={{
          color: (theme) =>
            theme.palette.mode === "light" ? "#1a90ff" : "#308fe8",
          animationDuration: "550ms",
          position: "absolute",
          left: 0,
          [`& .${circularProgressClasses.circle}`]: {
            strokeLinecap: "round",
          },
        }}
        {...props}
      />
      <Box
        sx={{
          top: 0,
          left: 0,
          bottom: 0,
          right: 0,
          position: "absolute",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Typography
          variant="caption"
          sx={{ fontSize: "1.75rem" }}
          component="div"
          color="text.secondary"
        >
          {/* {`${Math.round(props.value)}%`} */}
          {formatTime(props.value)}
        </Typography>
      </Box>
    </Box>
  );
}

CircularProgressWithLabel.propTypes = {
  /**
   * The value of the progress indicator for the determinate variant.
   * Value between 0 and 100.
   * @default 0
   */
  value: PropTypes.number.isRequired,
};

const Step13 = ({
  setOtp,
  phoneNumberRef,
  onDisableSubmit,
  resendCredentialOTP,
  handleCloseModal1,
  setErrorPG,
  authorizeOTP,
  isFetching,
}) => {
  const { t } = useTranslation();

  const maskedPhoneNumber = () => {
    const hiddenPart = phoneNumberRef.current
      .substring(
        phoneNumberRef.current.length - 7,
        phoneNumberRef.current.length - 2
      )
      .replace(/\d/g, "*");
    return (
      "+" +
      phoneNumberRef.current.slice(0, phoneNumberRef.current.length - 7) +
      hiddenPart +
      phoneNumberRef.current.slice(-2)
    );
  };
  const [progress, setProgress] = useState(100);
  const [isFirst, setIsFirst] = useState(true);
  const AuthInputRef = useRef(null);

  useEffect(() => {
    if (progress > 0.5) {
      const timer = setInterval(() => {
        setProgress((prevProgress) => prevProgress - 1 / 3);
      }, 1000);

      return () => {
        clearInterval(timer);
      };
    }
  }, [progress]);

  // close
  useEffect(() => {
    if (progress <= 0.5) {
      setProgress(0);
      setErrorPG(t("electronicid.timeout"));
      // handleCloseModal1();
    }
  }, [progress]);

  // const [result, setResult] = useState();
  // console.log("result: ", result);
  const handleOnChange = (res) => {
    console.log("res: ", res.length);
    if (res.length === 6) {
      onDisableSubmit(false);
      if (isFirst) {
        setIsFirst(false);
        authorizeOTP(res);
      }
    } else {
      onDisableSubmit(true);
    }
    setOtp(res);
  };
  useEffect(() => {
    onDisableSubmit(true);
  }, []);

  const [enResend, setEnResend] = useState(true);

  const handleResend = () => {
    if (enResend) {
      setProgress(100);
      setErrorPG(null);
      setIsFirst(true);
      AuthInputRef.current?.clear();
      resendCredentialOTP();
      setEnResend(false);
      setTimeout(() => {
        setEnResend(true);
      }, 180000);
    }
  };

  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Enter the code that was sent to your phone */}
        {t("electronicid.step131")}
      </Typography>

      <Typography fontSize="14px" mb="10px" mt="16px">
        {/* A verification code has been sent to your phone: {maskedPhoneNumber()} */}
        {t("electronicid.step132")}{" "}
        <span style={{ fontWeight: "bold" }}>{maskedPhoneNumber()}</span>
      </Typography>

      <Typography
        fontSize="14px"
        color="#1976D2"
        mt="26px"
        mb="10px"
        textAlign="center"
      >
        {/* Enter The Code That Was Sent To Your Phone */}
        {t("electronicid.step133")}
      </Typography>
      <Box className="container">
        <AuthCode
          ref={AuthInputRef}
          containerClassName="phoneContainer"
          inputClassName="phoneinputverified"
          onChange={handleOnChange}
          disabled={isFetching}
        />
      </Box>
      <Box textAlign="center" marginTop="50px">
        <CircularProgressWithLabel size={100} value={progress} />
      </Box>
      <Typography fontSize="14px" textAlign="center" marginTop="30px">
        {/* Verification process might take up to 5 minitues. */}
        {t("electronicid.step134")}
      </Typography>
      <Box
        textAlign="center"
        marginTop="15px"
        sx={{
          cursor: enResend ? "pointer" : "not-allowed",
          color: enResend ? "#1976D2" : "#26293f",
        }}
        onClick={handleResend}
        // className="buttontet"
        disabled={!enResend}
      >
        {/* Resend OTP */}
        {t("electronicid.step135")}
      </Box>
    </Box>
  );
};

export default Step13;
