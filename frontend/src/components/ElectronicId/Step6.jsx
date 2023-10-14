import React, { useEffect, useRef, useState } from "react";
import Webcam from "react-webcam";
import * as blazeface from "@tensorflow-models/blazeface";
import "@tensorflow/tfjs";
import { useTranslation } from "react-i18next";
import { Box, Typography } from "@mui/material";

export const Step6 = ({
  handleCloseModal1,
  setImageFace,
  shouldDetectFaces,
  setShouldDetectFaces,
  setIsFetching,
  setErrorPG,
  direction,
  setDirection,
}) => {
  const { t } = useTranslation();

  const webcamRef = useRef(null);
  const [model, setModel] = useState(null);
  // const [direction, setDirection] = useState(null);
  const [cameraSetup, setCameraSetup] = useState(false);
  const [hasCamera, setHasCamera] = useState(false);

  const setupCamera = () => {
    if (webcamRef.current) {
      navigator.mediaDevices
        .getUserMedia({
          video: { width: 440, height: 300 },
          audio: false,
        })
        .then((stream) => {
          webcamRef.current.srcObject = stream;
          setCameraSetup(true);
          setHasCamera(true);
        })
        .catch((error) => {
          console.error("Error accessing camera:", error);
          setErrorPG(t("electronicid.camera not found"));
          setHasCamera(false);
        });
    }
  };
  const canvasRef = useRef(null);

  const prediction = useRef(null);
  const detectFaces = async () => {
    if (model && webcamRef.current) {
      prediction.current = await model.estimateFaces(
        webcamRef.current.video,
        false
      );
      //   console.log("prediction:", prediction);
      const MAX_FACE_WIDTH = 150; // Ngưỡng kích thước khuôn mặt (tùy chọn)
      const MAX_FACE_HEIGHT = 110; // Ngưỡng kích thước khuôn mặt (tùy chọn)

      const ctx = canvasRef.current.getContext("2d");
      ctx.drawImage(webcamRef.current.video, 0, 0, 440, 300);

      if (prediction.current.length > 1) {
        setDirection(t("electronicid.cam oneface"));
      } else {
        setDirection(null);
        const pred = prediction.current[0];
        const faceWidth = pred.bottomRight[0] - pred.topLeft[0];
        // console.log("faceWidth: ", faceWidth);
        const faceHeight = pred.bottomRight[1] - pred.topLeft[1];
        // console.log("faceHeight: ", faceHeight);
        // So sánh kích thước khuôn mặt với ngưỡng
        if (faceWidth < MAX_FACE_WIDTH || faceHeight < MAX_FACE_HEIGHT) {
          setDirection(t("electronicid.cam closer"));
          // console.log("first");
        } else {
          setDirection(null);
          prediction.current.forEach((pred) => {
            ctx.beginPath();
            ctx.lineWidth = "4";
            ctx.strokeStyle = "transparent";
            ctx.globalAlpha = 0.5;
            ctx.rect(
              pred.topLeft[0],
              pred.topLeft[1] - 20,
              pred.bottomRight[0] - pred.topLeft[0],
              pred.bottomRight[1] - pred.topLeft[1] + 20
            );
            ctx.stroke();
            // console.log("landmark81: ", pred.landmarks);
            ctx.fillStyle = "transparent";
            pred.landmarks.forEach((landmark) => {
              ctx.fillRect(landmark[0], landmark[1], 5, 5);
            });
            checkFaceDirection(pred.landmarks);
          });
        }
      }
    }
  };

  const isWaitingForCaptureRef = useRef(false);

  // const checkFaceDirection = (landmarks) => {
  //   // Xác định các điểm landmark cần thiết để xác định hướng
  //   const eyeLeft = landmarks[0]; // Điểm mắt trái
  //   const eyeRight = landmarks[1]; // Điểm mắt phải
  //   const noseTip = landmarks[2]; // Điểm mũi

  //   // Tính vector giữa mắt trái và mắt phải
  //   const eyeVector = [eyeRight[0] - eyeLeft[0], eyeRight[1] - eyeLeft[1]];

  //   // Tính góc giữa vector mắt và trục ngang
  //   const angle = Math.atan2(eyeVector[1], eyeVector[0]) * (180 / Math.PI);

  //   // Xác định hướng dựa trên góc
  //   if (angle > -30 && angle < 30) {
  //     // Hướng thẳng
  //     console.log("Hướng thẳng");
  //   } else if (angle <= -30) {
  //     // Hướng qua trái
  //     console.log("Hướng qua trái");
  //   } else {
  //     // Hướng qua phải
  //     console.log("Hướng qua phải");
  //   }

  //   // Kiểm tra góc nghiêng
  //   const verticalAngle =
  //     Math.atan2(
  //       noseTip[1] - (eyeLeft[1] + eyeRight[1]) / 2,
  //       noseTip[0] - (eyeLeft[0] + eyeRight[0]) / 2
  //     ) *
  //     (180 / Math.PI);
  //   if (verticalAngle > 15) {
  //     // Ngước lên
  //     console.log("Ngước lên");
  //   } else if (verticalAngle < -15) {
  //     // Cúi xuống
  //     console.log("Cúi xuống");
  //   }
  // };

  const checkFaceDirection = (landmarks) => {
    const eyeLeft = landmarks[0];
    const eyeRight = landmarks[2];

    const dx = eyeRight[0] - eyeLeft[0];
    const dy = eyeRight[1] - eyeLeft[1];

    const angle = Math.atan2(dy, dx);
    const degree = angle * (180 / Math.PI);

    // console.log("degree: ", degree);
    //Kiểm tra hướng khuôn mặt
    if (degree < 30) {
      setDirection(t("electronicid.cam lookstraight"));
    } else if (degree > 40) {
      setDirection(t("electronicid.cam lookstraight"));
    } else if (degree >= 30 && degree <= 40) {
      setDirection(t("electronicid.cam holdsteady"));
      // console.log("degree: ", degree);
      const ctx = canvasRef.current.getContext("2d");
      ctx.drawImage(webcamRef.current.video, 0, 0, 440, 300);

      prediction.current.forEach((pred) => {
        ctx.beginPath();
        ctx.lineWidth = "4";
        ctx.strokeStyle = "transparent";
        ctx.globalAlpha = 0.5;
        ctx.rect(
          pred.topLeft[0],
          pred.topLeft[1] - 20,
          pred.bottomRight[0] - pred.topLeft[0],
          pred.bottomRight[1] - pred.topLeft[1] + 20
        );
        ctx.stroke();
      });
      isWaitingForCaptureRef.current = true;
      setTimeout(() => {
        if (isWaitingForCaptureRef.current) {
          clearInterval(intervalId); // Dừng interval sau khi chụp hình
          isWaitingForCaptureRef.current = false;
          captureFace(prediction.current);
          setShouldDetectFaces(false);
        }
      }, 1000); // Chờ 3 giây trước khi chụp hình
    } else {
      // console.log("degree: ", degree);
      isWaitingForCaptureRef.current = false;
    }
  };

  useEffect(() => {
    setupCamera();
    blazeface
      .load()
      .then((loadedModel) => {
        console.log(loadedModel);
        setModel(loadedModel);
      })
      .catch((err) => console.log(err))
      .finally(() => {
        console.log("done");
      });

    const temp = webcamRef.current;

    return () => {
      // Tắt camera khi component unmount
      if (temp && hasCamera) {
        const tracks = temp.srcObject.getTracks();
        tracks.forEach((track) => track.stop());
      }
    };
  }, []);

  const [intervalId, setIntervalId] = useState(null);

  useEffect(() => {
    if (shouldDetectFaces) {
      setIsFetching(true);
    }
  }, [shouldDetectFaces]);

  useEffect(() => {
    if (model && cameraSetup && shouldDetectFaces && hasCamera) {
      const id = setInterval(detectFaces, 100);
      setIntervalId(id);
      //   detectFaces(); // Gọi detectFaces() sau khi model và camera được tải
      return () => clearInterval(id);
    }
  }, [model, cameraSetup, shouldDetectFaces, hasCamera]);

  //   return (
  //     <div className="App">
  //       <Webcam ref={webcamRef} autoPlay />
  //     </div>
  //   );

  const captureFace = (prediction) => {
    if (shouldDetectFaces) {
      const canvas = document.createElement("canvas");
      canvas.width = canvasRef.current.width;
      canvas.height = canvasRef.current.height;
      const ctx = canvas.getContext("2d");

      prediction.forEach((pred) => {
        const width = pred.bottomRight[0] - pred.topLeft[0];
        const height = pred.bottomRight[1] - pred.topLeft[1] + 20;

        ctx.drawImage(
          canvasRef.current,
          pred.topLeft[0],
          pred.topLeft[1] - 20,
          width,
          height,
          0,
          0,
          width,
          height
        );
      });
      const temp = webcamRef.current;
      const tracks = temp.srcObject.getTracks();
      tracks.forEach((track) => track.stop());
      setImageFace(canvas.toDataURL("image/png"));

      console.log("Captured image:");
    }
  };

  return (
    <Box color="#26293F">
      <Typography fontSize="24px" fontWeight={600} lineHeight="36px">
        {/* Identity verification process in progress. */}
        {t("electronicid.step61")}
      </Typography>

      <Typography fontSize="14px" marginBottom="10px" marginTop="6px">
        {/* Please do not close this page. After your identity is verified, next
        step will open automatically. */}
        {t("electronicid.step62")}
      </Typography>
      {direction && (
        <Typography
          className="video-direction"
          fontSize="14px"
          bgcolor="#0b95e5"
          textAlign="center"
          color="white"
        >
          {direction}
        </Typography>
      )}

      <Box>
        <Webcam
          ref={webcamRef}
          // mirrored={true}
          autoPlay={true}
          audio={false} // Vô hiệu hóa âm thanh để tránh lỗi trong một số trình duyệt
          // style={{ display: "none" }}
          style={{ zIndex: -100 }}
          videoConstraints={{
            width: 440,
            height: 300,
          }}
        />
        <canvas width={440} height={300} ref={canvasRef} />
        {/* <button
        onClick={() => {
          const image = captureFace(prediction.current);
          handleNext();
          console.log("Captured image:", image);
        }}
      >
        Chụp ảnh
      </button> */}
      </Box>
    </Box>
  );
};

export default Step6;
