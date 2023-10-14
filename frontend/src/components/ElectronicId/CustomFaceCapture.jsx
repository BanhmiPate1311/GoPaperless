import { useState, useEffect, useRef } from "react";
import Webcam from "react-webcam";
import * as tf from "@tensorflow/tfjs";
import * as blazeface from "@tensorflow-models/blazeface";
// import { Button, Progress, Spin, message } from "antd";
// import {
//   ArrowLeftOutlined,
//   RollbackOutlined,
//   WarningFilled,
// } from "@ant-design/icons";
import { useDispatch } from "react-redux";
import { LinearProgress } from "@mui/material";
// import { IconAlert } from "../Modal/ModalAlert";
// import axios from "../../api/axios";
// import { prevStep } from "../../features/step/stepTransInfoSlice";

const getMirroredX = (x, videoWidth) => {
  return x + (videoWidth / 2 - x) * 2;
};

export default function CustomFaceCapture({
  onBackClick = () => {},
  onPhotoTaken = () => new Promise(),
  dataTransaction = {},
}) {
  const dispatch = useDispatch();
  const [returnTensors] = useState(false);
  const [isCorrectNosePosition, setIsCorrectNosePosition] = useState(false);
  const [model, setModel] = useState(null);
  const [intervalDetectFace, setIntervalDetectFace] = useState(null);
  const [sequenceCorrectNosePosition, setSequenceCorrectNosePosition] =
    useState(0);
  const [capturePicture, setCapturePicture] = useState(null);
  const webcamRef = useRef(null);
  // console.log("webcamRef: ", webcamRef.current);
  const canvasRef = useRef(null);
  const nosePoint = useRef(null);
  const wrapperRef = useRef(null);
  const [waitTime] = useState(3);
  const [loadingInitCamera, setLoadingInitCamera] = useState(false);
  const [webcamAllowed, setWebcamAllowed] = useState(true);
  const [detectMoreThanOneFace, setDetectMoreThanOneFace] = useState(false);
  const [facePointCapture, setFacePointCapture] = useState({
    x: 0,
    y: 0,
    width: 0,
    height: 0,
  });
  const [camReady, setCamReady] = useState(false);

  useEffect(() => {
    setLoadingInitCamera(true);
    blazeface
      .load()
      .then((model) => {
        console.log(model);
        setModel(model);
      })
      .catch((err) => console.log(err))
      .finally(() => {
        console.log("done");
        setLoadingInitCamera(false);
      });
  }, []);

  useEffect(() => {
    if (!model) return;
    const interval = setInterval(() => {
      detect(model);
    }, 100);
    setIntervalDetectFace(interval);
    return () => clearInterval(interval);
  }, [model]);

  useEffect(() => {
    if (sequenceCorrectNosePosition === waitTime) {
      const image = captureFace();
      setCapturePicture(image);
      onPhotoTaken(image).then((isSuccess) => {
        if (isSuccess) {
          clearInterval(intervalDetectFace);
          const ctx = canvasRef.current.getContext("2d");
          ctx.clearRect(
            0,
            0,
            canvasRef.current.width,
            canvasRef.current.height
          );
        } else {
          setSequenceCorrectNosePosition(0);
        }
      });
    }
  }, [sequenceCorrectNosePosition]);

  const captureFace = () => {
    // const webcam = webcamRef.current;
    // const video = webcam.video;
    // const canvas = document.createElement("canvas");
    // const context = canvas.getContext("2d");

    // Custom width, height, and position
    // const width = Math.abs(facePointCapture.width) + 30;
    // const height = facePointCapture.height;
    // const x = getMirroredX(facePointCapture.x, video.width);
    // const y = facePointCapture.y;

    // console.log(facePointCapture);
    // canvas.width = width;
    // canvas.height = height;
    // context.drawImage(video, x, y, width, height, 0, 0, width, height);

    return webcamRef.current.getScreenshot();
  };

  const detect = async (model) => {
    // console.log(webcamRef.current);
    if (
      typeof webcamRef.current !== "undefined" &&
      webcamRef.current !== null &&
      webcamRef.current.video.readyState === 4
    ) {
      if (!camReady) {
        setCamReady(true);
      }
      // Get video properties
      const video = webcamRef.current.video;
      const videoWidth = webcamRef.current.video.videoWidth;
      const videoHeight = webcamRef.current.video.videoHeight;

      //Set video height and width
      webcamRef.current.video.width = videoWidth;
      webcamRef.current.video.height = videoHeight;

      //Set canvas height and width
      canvasRef.current.width = videoWidth;
      canvasRef.current.height = videoHeight;

      wrapperRef.current.style.width = videoWidth + "px";
      wrapperRef.current.style.height = videoHeight + "px";

      //get nose point x, y
      const noseX = nosePoint.current.offsetLeft;
      const noseY = nosePoint.current.offsetTop;

      // Make detections

      const prediction = await model.estimateFaces(video, returnTensors);

      prediction.nosePoint = {
        x: noseX,
        y: noseY,
      };

      // console.log(prediction);

      if (prediction && prediction.length > 0) {
        const nose = prediction[0].landmarks[2];
        const realNoseX = nose[0];
        const realNoseY = nose[1];
        const distance = Math.sqrt(
          Math.pow(realNoseX - noseX, 2) + Math.pow(realNoseY - noseY, 2)
        );
        if (distance < 18) {
          console.log("toi day roi", distance);
          setIsCorrectNosePosition(true);
          setSequenceCorrectNosePosition((prev) => prev + 1);
        } else {
          setIsCorrectNosePosition(false);
          setSequenceCorrectNosePosition(0);
        }
      }
      if (prediction.length > 1) {
        setDetectMoreThanOneFace(true);
      } else {
        setDetectMoreThanOneFace(false);
      }
      const ctx = canvasRef.current.getContext("2d");
      draw(prediction, ctx, videoWidth, videoHeight);
    }
  };

  const draw = (predictions, ctx, videoWidth, videoHeight) => {
    if (predictions.length > 1) {
      for (let i = 0; i < predictions.length; i++) {
        const start = predictions[i].topLeft;
        const end = predictions[i].bottomRight;
        const size = [
          getMirroredX(end[0], videoWidth) - getMirroredX(start[0], videoWidth),
          end[1] - start[1],
        ];
        // Render a rectangle over each detected face.
        ctx.beginPath();
        ctx.lineWidth = "2";
        ctx.strokeStyle = "white";
        ctx.globalAlpha = 0.5;
        ctx.rect(
          getMirroredX(start[0], videoWidth),
          start[1],
          size[0],
          size[1]
        );
        ctx.stroke();
      }
      return;
    }
    if (predictions.length > 0) {
      for (let i = 0; i < predictions.length; i++) {
        const start = predictions[i].topLeft;
        const end = predictions[i].bottomRight;
        const size = [
          // Warning: Vẽ ngược từ phải sang trái về nên khúc này ra âm
          getMirroredX(end[0], videoWidth) - getMirroredX(start[0], videoWidth),
          end[1] - start[1],
        ];
        setFacePointCapture({
          x: getMirroredX(start[0], videoWidth),
          y: start[1] - 100,
          width: size[0],
          height: size[1] + 100,
        });
        // ctx.beginPath();
        // ctx.lineWidth = "2";
        // ctx.strokeStyle = "white";
        // ctx.globalAlpha = 0.5;
        // ctx.rect(
        //   getMirroredX(start[0], videoWidth),
        //   start[1] - 100,
        //   size[0],
        //   size[1] + 100
        // );
        // ctx.stroke();
      }
    }
    if (predictions.length === 0) return;

    // draw 6 points over each detected face
    const colors = ["red", "yellow", "#0EA5E9", "pink", "purple", "cyan"];
    // for (let i = 0; i < predictions.length; i++) {
    const keypoints = predictions[0].landmarks;

    // just take nose point
    for (let j = 2; j < 3; j++) {
      const x = getMirroredX(keypoints[j][0], videoWidth);
      const y = keypoints[j][1];
      ctx.beginPath();
      ctx.arc(x, y, 3, 0, 2 * Math.PI);
      ctx.fillStyle = colors[j];
      ctx.fill();
    }
    // }
  };

  const handleCaptureWebcam = () => {};

  const handleStartOver = () => {
    window.location.reload();
  };

  return (
    <div className="h-screen overflow-hidden bg-[#072146] relative">
      <div className="w-full h-[50px] lg:h-[70px] bg-[#072146] text-[20px] lg:text-[32px] font-bold flex items-center justify-center text-white relative">
        {/* <ArrowLeftOutlined
          onClick={() => dispatch(prevStep())}
          className="absolute left-[5px] md:left-[5%]"
        /> */}
        <div>Xác nhận giao dịch</div>
      </div>
      <div className="w-full flex items-center justify-center bg-[#072146]">
        {
          <div className="">
            <div
              className={`${
                loadingInitCamera || !webcamAllowed || !camReady ? "hidden" : ""
              } relative`}
            >
              <div className={`relative`} ref={wrapperRef}>
                <Webcam
                  ref={webcamRef}
                  // mirrored={true}
                  className={`absolute z-10 ${
                    isCorrectNosePosition
                      ? "border-2 border-blue-600"
                      : "border-2 border-gray-300"
                  }`}
                />
                <canvas ref={canvasRef} className="absolute z-20" />
                <div className="h-full w-full absolute z-20 flex items-center justify-center">
                  <div>
                    <span
                      ref={nosePoint}
                      className="relative flex h-3 w-3 mx-auto"
                    >
                      <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-sky-400 opacity-75"></span>
                      <span className="relative inline-flex rounded-full h-3 w-3 bg-sky-500"></span>
                    </span>
                  </div>
                </div>
                {detectMoreThanOneFace && (
                  <div className="absolute top-[20px] left-[50%] -translate-x-1/2 flex items-center z-10 bg-black bg-opacity-30 px-10 text-center text-white">
                    Vui lòng chỉ đưa vào một khuôn mặt{" "}
                    {/* <WarningFilled className="text-yellow-400" /> */}
                  </div>
                )}
              </div>
            </div>

            {!loadingInitCamera && !webcamAllowed && (
              <div>
                {/* <IconAlert type={"error"} /> */}
                <div className="text-center mt-3">
                  Không thể truy cập camera, vui lòng kiểm tra lại
                </div>
              </div>
            )}

            {(loadingInitCamera || camReady === false) && (
              <div className="flex flex-cols items-center justify-center py-8 text-white">
                {/* <Spin className="text-[32px] mr-4" /> */}
                <div>Đang khởi tạo camera, vui lòng đợi...</div>
              </div>
            )}

            {!loadingInitCamera && webcamAllowed && camReady && (
              <div className="z-[100] absolute bottom-0 left-0 w-[100vw] mx-auto flex items-center justify-center bg-[#0F4764] h-[70px] text-[18px] rounded-t-[30px]">
                <div className="text-white mb-2 opacity-70 font-bold">
                  Đưa mũi bạn vào điểm tròn
                </div>
              </div>
            )}
          </div>
        }
        {/* {capturePicture && (
      <Button
        className="flex items-center mx-auto mt-3"
        icon={<RollbackOutlined />}
        onClick={handleStartOver}
      >
        Retry
      </Button>
    )} */}
        {/* {false && (
          <img src={capturePicture} className="mt-3" alt="capture-result" />
        )} */}
      </div>
    </div>
  );
}
