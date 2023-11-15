import { Close } from "@mui/icons-material";
import { useEffect, useRef, useState } from "react";
import { useDrag } from "react-dnd";
import { ResizableBox } from "react-resizable";
import { fpsService } from "../../services/fpsService";
import "../../assets/styles/react-resizable.css";
import { checkIsPosition, formatSignerId } from "../../ultis/commonFunction";

export default function Signature({
  useSignatureDataState = () => [null, () => {}],
  useSignaturesState = () => [null, () => {}],
  index,
  pdfPage,
  pdfInfo,
  handleValidateSignature,
  workFlow,
}) {
  const signer = workFlow?.participants?.find(
    (item) => item.signerToken === workFlow.signerToken
  );
  const signerId = signer.signerId;
  const [isSetPos, setIsSetPos] = useState(false);

  const dragRef = useRef();
  // const [signatures, setSignatures] = useSignaturesState();
  const [signatureData, setSignature] = useSignatureDataState();

  const [isShowModalSetting, setShowModalSetting] = useState(false);
  // const [isShowModalVefication, setShowModalVefication] = useState(false);
  const maxPosibleResizeWidth =
    (pdfPage.width * (100 - signatureData.dimension?.x)) / 100;
  const maxPosibleResizeHeight =
    (pdfPage.height * (100 - signatureData.dimension?.y)) / 100;
  // const [loadingSign, setLoadingSign] = useState(false);

  useEffect(() => {
    const metaInf1 = JSON.parse(signer.metaInformation);
    setIsSetPos(checkIsPosition(metaInf1));
  }, [signer]);

  const [{ isDragged }, drag] = useDrag({
    type: "Signature",
    item: {
      ...signatureData,
      index,
      dimension: {
        width: signatureData.dimension?.width,
        height: signatureData.dimension?.height,
      },
    },
    canDrag: signerId === signatureData.field_name && !isSetPos,
    end: (item, monitor) => {},
    collect: (monitor) => ({
      isDragging: monitor.isDragging(),
      isDragged: monitor.getItem(),
    }),
  });

  const handleRemoveSignature = async () => {
    // alert("Lam dep trai");
    if (isSetPos || signerId !== signatureData.field_name) return;
    fpsService.removeSignature(pdfInfo, signatureData.field_name);
    setSignature(null);
  };

  const handleUpdateSignatureData = async ({
    field_name,
    signatureLeft,
    signatureTop,
    signatureWidthPercent,
    signatureHeightPercent,
    signaturePage,
  }) => {
    if (signatureWidthPercent + signatureLeft > 100) {
    }

    if (signatureHeightPercent + signatureTop > 100) {
    }

    if (
      !handleValidateSignature({
        updatedSignature: {
          field_name: field_name,
          dimension: {
            x: signatureLeft,
            y: signatureTop,
            width: signatureWidthPercent,
            height: signatureHeightPercent,
          },
          page: signaturePage,
        },
      })
    ) {
      return;
    }

    fpsService.putSignature(
      pdfInfo,
      {
        field_name: field_name,
        dimension: {
          x: signatureLeft,
          y: signatureTop,
          width: signatureWidthPercent,
          height: signatureHeightPercent,
        },
        page: signaturePage,
        visible_enabled: true,
      },
      {
        field: "Signature",
      }
    );

    setSignature((prev) => ({
      ...prev,
      field_name: field_name,
      dimension: {
        x: signatureLeft,
        y: signatureTop,
        width: signatureWidthPercent,
        height: signatureHeightPercent,
      },
      page: signaturePage,
    }));
    setShowModalSetting(false);
  };

  const [showTopBar, setShowTopBar] = useState(false);

  const TopBar = ({ signatureData }) => {
    if (!showTopBar) return null;
    return (
      <div className={`z-10 flex`}>
        {signatureData.signed && (
          //   <CheckCircleFilled
          //     className="p-0.5 text-[16px] z-10 text-green-500 hover:cursor-pointer hover:opacity-80 rounded-full bg-white"
          //     onMouseDown={() => setShowModalVefication(true)}
          //   />
          <Close />
        )}
        {/* <CloseCircleFilled
          onMouseDown={handleRemoveSignature}
          className="p-0.5 text-[16px] z-10 text-red-500 hover:cursor-pointer hover:opacity-80 rounded-full bg-white"
        /> */}
        <div
          onClick={handleRemoveSignature}
          style={{
            background: "red",
            color: "white",
            borderRadius: "50%",
            width: "20px",
            height: "20px",
            textAlign: "center",
          }}
        >
          {/* <Close
            style={{
              background: "red",
              color: "white",
              borderRadius: "50%",
            }}
          /> */}
          X
        </div>
      </div>
    );
  };

  if (signatureData.page !== null && signatureData.page !== pdfPage.currentPage)
    return null;

  return (
    <>
      {["SIGNATURE", "INITIAL"].includes(signatureData.type) && (
        <ResizableBox
          width={
            signatureData.dimension?.width
              ? signatureData.dimension?.width * (pdfPage.width / 100)
              : Infinity
          }
          height={
            signatureData.dimension?.height
              ? signatureData.dimension?.height * (pdfPage.height / 100)
              : 150
          }
          style={{
            position: "absolute",
            top: signatureData.dimension?.y + "%",
            left: signatureData.dimension?.x + "%",
          }}
          // minConstraints={[
          //   signatureData.dimension?.width * (pdfPage.width / 100),
          //   signatureData.dimension?.height * (pdfPage.height / 100),
          // ]}
          // maxConstraints={[
          //   signatureData.dimension?.width * (pdfPage.width / 100),
          //   signatureData.dimension?.height * (pdfPage.height / 100),
          // ]}
          minConstraints={[
            isSetPos || signerId !== signatureData.field_name
              ? signatureData.dimension?.width * (pdfPage.width / 100)
              : pdfPage
              ? (pdfPage.width * 5) / 100
              : 50,
            isSetPos || signerId !== signatureData.field_name
              ? signatureData.dimension?.height * (pdfPage.height / 100)
              : pdfPage
              ? (pdfPage.height * 5) / 100
              : 50,
          ]}
          maxConstraints={[
            isSetPos || signerId !== signatureData.field_name
              ? signatureData.dimension?.width * (pdfPage.width / 100)
              : pdfPage
              ? maxPosibleResizeWidth
              : 200,
            isSetPos || signerId !== signatureData.field_name
              ? signatureData.dimension?.height * (pdfPage.height / 100)
              : pdfPage
              ? maxPosibleResizeHeight
              : 200,
          ]}
          onResize={(e, { size }) => {
            setSignature({
              ...signatureData,
              dimension: {
                ...signatureData.dimension,
                width: (size.width / pdfPage.width) * 100,
                height: (size.height / pdfPage.height) * 100,
              },
            });
          }}
          onResizeStop={(e, { size }) => {
            fpsService.putSignature(
              pdfInfo,
              {
                field_name: signatureData.field_name,
                page: pdfPage.currentPage,
                dimension: {
                  x: signatureData.dimension.x,
                  y: signatureData.dimension.y,
                  width: (size.width / pdfPage.width) * 100,
                  height: (size.height / pdfPage.height) * 100,
                },
                visible_enabled: true,
              },
              { field: signatureData.type.toLowerCase() }
            );
          }}
          className="mx-auto"
        >
          <div
            onDoubleClick={() => setShowModalSetting(true)}
            ref={drag(dragRef)}
            id="drag"
            onMouseEnter={() => setShowTopBar(true)}
            onMouseLeave={() => setShowTopBar(false)}
            className={`flex shadow-2xl border text-white hover:cursor-move mx-auto z-10 relative bg-opacity-80 hover:bg-opacity-50`}
            style={{
              background:
                signatureData.signed || signerId !== signatureData.field_name
                  ? "#4574da"
                  : "#51d35a",
              height: "100%",
              zIndex: 10,
            }}
          >
            <div className="flex items-center justify-center mx-auto max-w-full">
              <div
                style={{
                  position: "absolute",
                  top: signatureData.dimension?.y >= 5 && "0",
                  bottom: signatureData.dimension?.y < 5 && "0",
                  transform:
                    signatureData.dimension?.y < 5
                      ? "translateY(50%)"
                      : "translateY(-50%)",
                  left: signatureData.dimension?.x > 95 && "0",
                  right: signatureData.dimension?.x <= 95 && "0",
                }}
              >
                <TopBar signatureData={signatureData} />
              </div>
              <p
                className="text-center"
                style={{ overflowWrap: "break-word", fontSize: "10px" }}
              >
                {signatureData.field_name}
              </p>
            </div>
          </div>
        </ResizableBox>
      )}
    </>
  );
}
