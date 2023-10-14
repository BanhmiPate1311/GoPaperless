import React, { useEffect, useRef } from "react";
import Webcam from "react-webcam";
import * as blazeface from "@tensorflow-models/blazeface";
import "@tensorflow/tfjs";
import { useState } from "react";

function FaceCapture({ handleNext }) {
  const webcamRef = useRef(null);
  const [model, setModel] = useState(null);
  const [cameraSetup, setCameraSetup] = useState(false);

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
        })
        .catch((error) => {
          console.error("Error accessing camera:", error);
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

      const ctx = canvasRef.current.getContext("2d");
      ctx.drawImage(webcamRef.current.video, 0, 0, 440, 300);

      prediction.current.forEach((pred) => {
        ctx.beginPath();
        ctx.lineWidth = "4";
        ctx.strokeStyle = "blue";
        ctx.globalAlpha = 0.5;
        ctx.rect(
          pred.topLeft[0],
          pred.topLeft[1],
          pred.bottomRight[0] - pred.topLeft[0],
          pred.bottomRight[1] - pred.topLeft[1]
        );
        ctx.stroke();
        // console.log("landmark81: ", pred.landmarks);
        ctx.fillStyle = "red";
        pred.landmarks.forEach((landmark) => {
          ctx.fillRect(landmark[0], landmark[1], 5, 5);
        });
        checkFaceDirection(pred.landmarks);
      });
    }
  };

  const isWaitingForCaptureRef = useRef(false);

  const checkFaceDirection = (landmarks) => {
    const eyeLeft = landmarks[0];
    const eyeRight = landmarks[2];

    const dx = eyeRight[0] - eyeLeft[0];
    const dy = eyeRight[1] - eyeLeft[1];

    const angle = Math.atan2(dy, dx);
    const degree = angle * (180 / Math.PI);

    // Kiểm tra hướng khuôn mặt
    if (degree > -40 && degree < 40) {
      console.log("degree: ", degree);
      isWaitingForCaptureRef.current = true;
      setTimeout(() => {
        if (isWaitingForCaptureRef.current) {
          clearInterval(intervalId); // Dừng interval sau khi chụp hình
          isWaitingForCaptureRef.current = false;
          captureFace(prediction.current);
        }
      }, 3000); // Chờ 3 giây trước khi chụp hình
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
      if (temp) {
        const tracks = temp.srcObject.getTracks();
        tracks.forEach((track) => track.stop());
      }
    };
  }, []);

  const [intervalId, setIntervalId] = useState(null);

  useEffect(() => {
    if (model && cameraSetup) {
      const id = setInterval(detectFaces, 100);
      setIntervalId(id);
      //   detectFaces(); // Gọi detectFaces() sau khi model và camera được tải
      return () => clearInterval(id);
    }
  }, [model, cameraSetup]);

  //   return (
  //     <div className="App">
  //       <Webcam ref={webcamRef} autoPlay />
  //     </div>
  //   );

  const captureFace = (prediction) => {
    console.log("dzo dzo");
    const canvas = document.createElement("canvas");
    canvas.width = canvasRef.current.width;
    canvas.height = canvasRef.current.height;
    const ctx = canvas.getContext("2d");

    prediction.forEach((pred) => {
      const width = pred.bottomRight[0] - pred.topLeft[0];
      const height = pred.bottomRight[1] - pred.topLeft[1];

      ctx.drawImage(
        canvasRef.current,
        pred.topLeft[0],
        pred.topLeft[1],
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
    const image = canvas.toDataURL("image/png");
    console.log("Captured image:", image);
    handleNext();
    return image;
  };
  // const captureFace = () => {
  //   console.log("shao không dzô");
  //   const canvas = document.createElement("canvas");
  //   canvas.width = canvasRef.current.width;
  //   canvas.height = canvasRef.current.height;
  //   const context = canvas.getContext("2d");
  //   context.drawImage(
  //     canvasRef.current,
  //     0,
  //     0,
  //     canvasRef.current.width,
  //     canvasRef.current.height
  //   );
  //   const image = canvas.toDataURL("image/png");
  //   console.log("Captured image:", image);
  //   handleNext();
  //   return image;
  // };

  return (
    <div>
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
      <button
        onClick={() => {
          const image = captureFace(prediction.current);
          handleNext();
          console.log("Captured image:", image);
        }}
      >
        Chụp ảnh
      </button>
    </div>
  );
}

export default FaceCapture;
