import {
  AdsClick,
  KeyboardArrowDown,
  KeyboardArrowUp,
} from "@mui/icons-material";
import {
  Box,
  Button,
  FormControl,
  MenuItem,
  Select,
  Stack,
  TextField,
  Typography,
} from "@mui/material";
import React from "react";
import { ReactComponent as Search } from "../../assets/images/pdf/search.svg";
import { ReactComponent as ZoomIn } from "../../assets/images/pdf/zoom_in.svg";
import { ReactComponent as ZoomOut } from "../../assets/images/pdf/zoom_out.svg";
import { ReactComponent as FullScreen } from "../../assets/images/pdf/full_screen.svg";
import { ReactComponent as Print } from "../../assets/images/pdf/print.svg";
import { ReactComponent as DownLoad } from "../../assets/images/pdf/download.svg";
import { ReactComponent as ThumbNail } from "../../assets/images/pdf/thumbnail.svg";
import { ReactComponent as Bookmark } from "../../assets/images/pdf/bookmark.svg";
import { ReactComponent as Attachment } from "../../assets/images/pdf/attachment.svg";
import { useState } from "react";
import { useEffect } from "react";
import { fpsService } from "../../services/fpsService";
import Document from "./Document";
import { DndProvider } from "react-dnd";
import { HTML5Backend } from "react-dnd-html5-backend";
import { useRef } from "react";
import ContextMenu from "../ContextMenu";
import { v4 as uuidv4 } from "uuid";
import { useSearchParams } from "react-router-dom";

const PdfView = ({ workFlow }) => {
  const menuRef = useRef(null);
  const [mousePosition, setMousePosition] = useState({ x: 0, y: 0 });
  const [contextMenuVisible, setContextMenuVisible] = useState(false);
  const [contextMenuPosition, setContextMenuPosition] = useState({
    x: 0,
    y: 0,
  });
  const [selectedMenuItem, setSelectedMenuItem] = useState("");
  // console.log("selectedMenuItem: ", selectedMenuItem);
  const [signatures, setSignatures] = useState([]);
  const [pdfPages, setPdfPages] = useState([]); // list of all pdf pages
  const [pdfInfo, setPdfInfo] = useState(null); // pdf info
  const [zoom, setZoom] = useState(1);
  const [previousViewPageIndex, setPreviousViewPageIndex] = useState(0);
  const [search] = useSearchParams();
  const [signerToken] = useState(search.get("access_token"));

  useEffect(() => {
    const getDocumentDetails = async () => {
      try {
        const response = await fpsService.getDocumentDetails({
          documentId: workFlow.documentId,
        });
        setPdfInfo(response.data[0]);
        handleInitDocument(response.data[0]);
      } catch (error) {
        console.log(error);
      }
    };

    getDocumentDetails();
  }, []);

  useEffect(() => {
    // nghe sự kiện bên trong file pdf
    const pdfRange = document.getElementById("pdf-view");
    const cursor = document.querySelector(".cursor");

    const mouseMove = (e) => {
      if (
        e.target instanceof SVGElement ||
        e.target.className.includes("MuiListItemText-primary") ||
        e.target.className.includes("MuiListItemButton-root")
      ) {
        cursor.style.display = "none";
      } else {
        pdfRange.style.cursor = "none";
        setMousePosition({
          x: e.clientX,
          y: e.clientY,
        });
        cursor.style.display = "block";
      }
    };

    const mouseOut = () => {
      cursor.style.display = "none";
    };

    pdfRange.addEventListener("mousemove", mouseMove);

    pdfRange.addEventListener("mouseleave", mouseOut);

    // Trình nghe sự kiện click và mousedown toàn bộ trang
    // const handleGlobalClickAndMouseDown = (e) => {
    //   console.log("e: ", e);
    //   if (
    //     (menuRef.current.contains(e.target) &&
    //       e.target.className?.includes("pdf-page")) ||
    //     !menuRef.current.contains(e.target)
    //   ) {
    //     console.log("object");
    //     handleCloseContextMenu();
    //   }
    // };

    // Đăng ký trình nghe sự kiện click và mousedown
    // window.addEventListener("click", handleGlobalClickAndMouseDown);
    // window.addEventListener("mousedown", handleGlobalClickAndMouseDown);

    return () => {
      // Hủy đăng ký trình nghe sự kiện khi component unmount
      // window.removeEventListener("click", handleGlobalClickAndMouseDown);
      // window.removeEventListener("mousedown", handleGlobalClickAndMouseDown);
      pdfRange.removeEventListener("mousemove", mouseMove);
      pdfRange.removeEventListener("mouseleave", mouseOut);
    };
  }, []);

  const handleInitDocument = async (pdfInfo) => {
    try {
      const documentID = pdfInfo.document_id;
      const pagesNumber = pdfInfo.document_pages;

      const documentCustomPage = pdfInfo.document_custom_page;
      console.log({ documentCustomPage });
      const pdfPages = [];
      for (let i = 0; i < pagesNumber; i++) {
        const page = documentCustomPage.find(
          (item) => item.page_start <= i + 1 && item.page_end >= i + 1
        );
        // console.log(page);
        // console.log("i", i);
        pdfPages.push({
          currentPage: i + 1,
          actualHeight: page.page_height,
          actualWidth: page.page_width,
          height: page.page_height * zoom,
          width: page.page_width * zoom,
          imgURI: "",
        });
      }
      setPdfPages(pdfPages);
      const resFields = await fpsService.getFields({
        documentId: documentID,
      });
      console.log({ resFields });
      let signatures = Object.values(resFields.data)
        .flat()
        .map((item) => {
          const { verification, ...repairedSignature } = item;
          return repairedSignature;
        });

      signatures = await handleGetSignatureAfterVerify(documentID, [
        ...signatures,
      ]);
      setSignatures(signatures);
      console.log("signatures: ", signatures);

      for (let i = 1; i <= pagesNumber; i++) {
        fpsService
          .getImage({
            documentId: documentID,
            page: i,
          })
          .then((res) => {
            const imgURI = URL.createObjectURL(res.data);
            setPdfPages((prev) =>
              prev.map((item, index) =>
                index === i - 1 ? { ...item, imgURI } : item
              )
            );
          });
      }
    } catch (e) {
      console.log(e);
    }
  };

  const handleGetSignatureAfterVerify = async (documentID, signatures) => {
    try {
      const resVerification = await fpsService.getVerification({
        documentId: documentID,
      });
      const verifiedSignatures = resVerification.data;
      verifiedSignatures.forEach((signatureVeriInfo) => {
        let signatureIndex = signatures.findIndex(
          (signature) => signature.field_name === signatureVeriInfo.field_name
        );
        if (signatureIndex === -1) return;
        signatures[signatureIndex] = {
          ...signatures[signatureIndex],
          ...signatureVeriInfo,
          signed: true,
        };
      });
      return signatures;
    } catch (err) {
      console.log(err);
      return signatures;
    }
  };

  const handleMoveToIndexPage = (index) => {
    const pdfInfosContainer = document.getElementById("pdfPages-container");
    const targetPage = document.getElementById(`page-${index}`);
    pdfInfosContainer.scrollTo({
      top: targetPage.offsetTop - pdfInfosContainer.offsetTop,
      behavior: "smooth",
    });
    console.log(pdfInfosContainer);
    console.log(targetPage);
    setPreviousViewPageIndex(index);
  };

  const handleZoom = (value) => {
    setZoom(value);
    setPdfPages((prev) =>
      prev.map((item) => ({
        ...item,
        width: item.actualWidth * value,
        height: item.actualHeight * value,
      }))
    );
  };

  const handleMenuItemClick = (item) => {
    console.log("item: ", item);
    if (
      signatures.findIndex((item) => item.signerToken === signerToken) !== -1
    ) {
      return alert("Signature Duplicated");
    }
    const newSignature = {
      type: String(item).toUpperCase(),
      field_name: String(item).toUpperCase() + uuidv4(),
      page: 1,
      dimension: {
        x: 0,
        y: 0,
        width: 22,
        height: 5,
      },
      visible_enabled: true,
      signerToken,
    };

    setSignatures((prev) => [...prev, newSignature]);
    fpsService.addSignature(
      pdfInfo,
      {
        field_name: newSignature.field_name,
        page: newSignature.page,
        dimension: newSignature.dimension,
        visible_enabled: true,
      },
      {
        field: item.toLowerCase(),
      }
    );
    setSelectedMenuItem(item);
    setContextMenuVisible(false);
  };

  const handleCloseContextMenu = () => {
    setContextMenuVisible(false);
  };

  const handleValidateSignature = (
    {
      updatedSignature = {
        field_name: null,
        dimension: {
          x: null,
          y: null,
          width: null,
          height: null,
        },
        page: null,
      },
    },
    updatedArraySignatures = signatures
  ) => {
    const pdfPage = pdfPages.find(
      (item) => item.currentPage === updatedSignature.page
    );
    if (!pdfPage) {
      return false;
    }
    const updatedSignatureWidth = updatedSignature.dimension.width;
    const updatedSignatureHeight = updatedSignature.dimension.height;
    const updatedSignatureLeft = updatedSignature.dimension.x;
    const updatedSignatureTop = updatedSignature.dimension.y;

    for (let i = 0; i < updatedArraySignatures.length; i++) {
      if (updatedSignature.page !== updatedArraySignatures[i].page) continue;
      if (updatedSignature.field_name === updatedArraySignatures[i].field_name)
        continue;

      const signature = updatedArraySignatures[i];
      const signatureWidth = signature.dimension.width;
      const signatureHeight = signature.dimension.height;
      const signatureLeft = signature.dimension.x;
      const signatureTop = signature.dimension.y;

      if (
        updatedSignatureLeft < signatureLeft + signatureWidth &&
        updatedSignatureTop < signatureTop + signatureHeight &&
        updatedSignatureLeft + updatedSignatureWidth > signatureLeft &&
        updatedSignatureTop + updatedSignatureHeight > signatureTop
      ) {
        return false;
      }
    }
    return true;
  };

  const handleContextMenu = (e) => {
    e.preventDefault();
    // const menuWidth = menuRef.current.offsetWidth;
    // console.log("menuWidth: ", menuWidth);
    // const menuHeight = menuRef.current.offsetHeight;
    // console.log("menuHeight: ", menuHeight);
    // context menu height:265
    // 0: 523, 621
    // 1: 880, 790
    // context menu width: 158
    const pageX = e.pageX;
    // console.log("pageX: ", pageX);
    const pageY = e.pageY;
    // console.log("pageY: ", pageY);
    // const maxX = window.innerWidth;
    // console.log("maxX: ", maxX);
    // const maxY = window.innerHeight;
    // console.log("maxY: ", maxY);

    // if pageY - 265 > 523 : pageY - 266
    const contextMenuHeight = 265;
    const contextMenuWidth = 158;
    const minY = 523;
    const maxY = 880;

    setContextMenuPosition({
      x: pageX + contextMenuWidth < 750 ? pageX : pageX - contextMenuWidth,
      y:
        pageY + contextMenuHeight < 920
          ? pageY - (pageY - minY) / 2
          : pageY - contextMenuHeight + (maxY - pageY) / 2,
    });
    setContextMenuVisible(true);
  };

  return (
    <Box height="100%">
      <Stack
        direction="row"
        alignItems="center"
        justifyContent="space-between"
        // className="px-2 py-2 flex items-center justify-between relative bg-gray-100 border-b"
        // style={{
        //   visibility: pdfInfo !== null ? "visible" : "hidden",
        // }} giữ lại để xét điều kiện
        borderBottom="1px solid rgba(0, 0, 0, 0.1)"
      >
        <Stack direction="row" alignItems="center">
          <Box ml="10px">
            <Search />
          </Box>
          <Button
            sx={{
              width: "25px",
              minWidth: "0",
              mx: "4px",
              opacity: previousViewPageIndex === 0 ? 0.3 : 1,
            }}
            disabled={previousViewPageIndex === 0}
            onClick={() => {
              if (previousViewPageIndex > 0) {
                handleMoveToIndexPage(previousViewPageIndex - 1);
              }
            }}
          >
            <KeyboardArrowUp />
          </Button>
          <TextField
            sx={{
              width: "40px",
              "& input": {
                padding: "3px 8px",
              },
            }}
            id="outlined-basic"
            variant="outlined"
            value={previousViewPageIndex + 1}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                const index = parseInt(e.target.value);
                if (index > 0 && index <= pdfPages.length) {
                  handleMoveToIndexPage(index - 1);
                }
              }
            }}
          />
          <Box mx="4px">/</Box>
          {pdfInfo && <Typography>{pdfInfo.document_pages}</Typography>}
          <Button
            sx={{
              width: "25px",
              minWidth: "0",
              mx: "4px",
              opacity: previousViewPageIndex === pdfPages.length - 1 ? 0.3 : 1,
            }}
            onClick={() => {
              if (previousViewPageIndex < pdfPages.length - 1) {
                handleMoveToIndexPage(previousViewPageIndex + 1);
              }
            }}
          >
            <KeyboardArrowDown />
          </Button>
        </Stack>

        <Stack direction="row" alignItems="center">
          <Button
            sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}
            onClick={() => zoom > 0.5 && handleZoom(zoom - 0.25)}
            disabled={zoom === 0.5}
          >
            <ZoomOut />
          </Button>

          <FormControl variant="standard" sx={{ minWidth: 60 }}>
            <Select
              disableUnderline={true}
              labelId="demo-simple-select-standard-label"
              id="demo-simple-select-standard"
              value={zoom}
              onChange={(e) => handleZoom(e.target.value)}
              label="Age"
            >
              {Array.from({ length: 9 }, (_, i) => (
                <MenuItem key={i} value={0.25 + i * 0.25}>
                  {25 + i * 25}%
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <Button
            sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}
            onClick={() => zoom < 2.5 && handleZoom(zoom + 0.25)}
          >
            <ZoomIn />
          </Button>
        </Stack>
        <Stack direction="row" alignItems="center">
          <Button sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}>
            <FullScreen />
          </Button>
          <Button sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}>
            <DownLoad />
          </Button>
          <Button sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}>
            <Print />
          </Button>
        </Stack>
      </Stack>
      {/* {JSON.stringify(signatures)} */}
      <Stack direction="row" height="100%">
        <Box>
          <Stack width={41} height="100%">
            <Button
              sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}
            >
              <ThumbNail />
            </Button>
            <Button
              sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}
            >
              <Bookmark />
            </Button>
            <Button
              sx={{ width: "30px", minWidth: "0", mx: "4px", paddingX: 0 }}
            >
              <Attachment />
            </Button>
          </Stack>
        </Box>
        <DndProvider backend={HTML5Backend}>
          <div
            onContextMenu={handleContextMenu}
            ref={menuRef}
            style={{
              margin: "0 auto",
              height: "400px",
              overflowY: "auto",
            }}
            id="pdf-view"
          >
            <div
              className="cursor"
              style={{
                top: mousePosition.y,
                left: mousePosition.x,
                pointerEvents: "none",
                translate: "-10px -10px",
              }}
            >
              <AdsClick id="mouse-icon" />
              <div style={{ marginLeft: "20px" }}>Right Click</div>
            </div>

            {contextMenuVisible && (
              <ContextMenu
                x={contextMenuPosition.x}
                y={contextMenuPosition.y}
                onClose={handleCloseContextMenu}
                onMenuItemClick={handleMenuItemClick}
              />
            )}
            <Box flexGrow={1}>
              {/* {JSON.stringify(pdfPages)} */}
              {pdfPages.map((item, index) => (
                <Document
                  useSignaturesState={() => [signatures, setSignatures]}
                  handleValidateSignature={handleValidateSignature}
                  pdfPage={item}
                  pdfInfo={pdfInfo}
                  index={index}
                  key={index}
                />
              ))}
            </Box>
          </div>
        </DndProvider>
      </Stack>
    </Box>
  );
};

export default PdfView;