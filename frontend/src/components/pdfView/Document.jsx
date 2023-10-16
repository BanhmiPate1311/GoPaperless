import { Box } from "@mui/material";
import React from "react";
import { useRef } from "react";
import { useDrop } from "react-dnd";
import { v4 as uuidv4 } from "uuid";
import Signature from "./Signature";
import { fpsService } from "../../services/fpsService";

const Document = ({
  useSignaturesState,
  pdfPage,
  pdfInfo,
  handleValidateSignature,
  index,
  handlePdfPage,
}) => {
  const [signatures, setSignatures] = useSignaturesState();
  const dropSigRef = useRef();
  const [, dropSig] = useDrop({
    accept: "Signature",
    drop: async (item, monitor) => {
      try {
        const offset = monitor.getSourceClientOffset();
        if (offset && dropSigRef.current) {
          const dropTargetXy = dropSigRef.current.getBoundingClientRect();
          const widthDoc = pdfPage.width;
          const heightDoc = pdfPage.height;
          const widthItem = item.dimension.width * (pdfPage.width / 100);
          const heightItem = item.dimension.height * (pdfPage.height / 100);

          let left = offset.x - dropTargetXy.left;
          let top = offset.y - dropTargetXy.top;

          if (left + widthItem > widthDoc) {
            const diff = left + widthItem - widthDoc;
            // sometimes the widthDoc rendered by html is not equal to the widthDoc in state
            left = left - diff < 0 ? 0 : left - diff;
          } else if (left < 0) {
            const diff = left;
            left = 0;
          }

          if (top + heightItem > heightDoc) {
            const diff = top + heightItem - heightDoc;
            // sometimes the heightDoc rendered by html is not equal to the heightDoc in state
            top = top - diff < 0 ? 0 : top - diff;
          } else if (top < 0) {
            top = 0;
          }

          if (
            !handleValidateSignature({
              updatedSignature: {
                field_name: item.field_name,
                dimension: {
                  x: (left / widthDoc) * 100,
                  y: (top / heightDoc) * 100,
                  width: item.dimension.width,
                  height: item.dimension.height,
                },
                page: pdfPage.currentPage,
              },
              signatures,
            })
          )
            return;

          let body = {};
          switch (item.type) {
            case "SIGNATURE": {
              body = {
                field_name: item.field_name,
                page: pdfPage.currentPage,
                dimension: {
                  x: (left / widthDoc) * 100,
                  y: (top / heightDoc) * 100,
                  width: item.dimension.width,
                  height: item.dimension.height,
                },
                visible_enabled: true,
              };
              break;
            }
            case "TEXT": {
              body = {
                field_name: item.field_name,
                page: pdfPage.currentPage,
                type: "TEXT_FIELD",
                format_type: item.format_type,
                value: item.value,
                read_only: item.read_only,
                multiline: item.multiline,
                color: item.color,
                align: item.align,
                dimension: {
                  x: (left / widthDoc) * 100,
                  y: (top / heightDoc) * 100,
                  width: item.dimension.width,
                  height: item.dimension.height,
                },
                visible_enabled: true,
              };
              break;
            }
            default:
              throw new Error("Invalid item.type");
          }

          await fpsService.putSignature(pdfInfo, body, {
            field: item.type.toLowerCase(),
          });

          handleDragSignature({
            ...item,
            field_name: item.field_name,
            dimension: {
              x: (left / widthDoc) * 100,
              y: (top / heightDoc) * 100,
              width: item.dimension.width,
              height: item.dimension.height,
            },
            page: pdfPage.currentPage,
          });
        }
      } catch (error) {
        // message.error("Failed to add signature");
        console.log(error);
      }
    },
    collect: (monitor) => ({}),
  });
  const handleDragSignature = (data) => {
    const { field_name } = data;
    setSignatures((prev) => {
      const index = prev.findIndex((item) => item.field_name === field_name);
      if (index !== -1) {
        return prev.map((item, i) => (i === index ? data : item));
      } else {
        return [...prev, data];
      }
    });
  };

  return (
    <Box id={`page-${index}`} ref={dropSig(dropSigRef)} position="relative">
      <Box
        sx={{
          backgroundImage: `url(${pdfPage.imgURI})`,
          backgroundSize: "100% 100%",
          minWidth: pdfPage.width + "px",
          minHeight: pdfPage.height + "px",
          width: pdfPage.width + "px",
          height: pdfPage.height + "px",
          marginBottom: `${index === 0 ? "30px" : 0}`,
        }}
        className="pdf-page"
        onContextMenu={(e) => handlePdfPage(pdfPage, e)}
      >
        {signatures.map((signatureData, index) => {
          return (
            <Signature
              key={index}
              index={index}
              useSignatureDataState={() => [
                signatureData,
                (newData) => {
                  if (typeof newData === "function") {
                    setSignatures((prev) =>
                      prev.map((item, i) =>
                        i === index ? newData(item) : item
                      )
                    );
                  } else {
                    setSignatures((prev) =>
                      newData
                        ? prev.map((item, i) => (i === index ? newData : item))
                        : prev.filter((item, i) => i !== index)
                    );
                  }
                },
              ]}
              useSignaturesState={useSignaturesState}
              pdfInfo={pdfInfo}
              pdfPage={pdfPage}
              handleValidateSignature={handleValidateSignature}
            />
          );
        })}
      </Box>
    </Box>
  );
};

export default Document;
