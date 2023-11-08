import React, { useEffect, useRef, useState } from "react";
import "../../assets/styles/pdfViewer.css";

import { Box } from "@mui/material";
import { Viewer, Worker } from "@react-pdf-viewer/core";
import "@react-pdf-viewer/core/lib/styles/index.css";
import { defaultLayoutPlugin } from "@react-pdf-viewer/default-layout";
import "@react-pdf-viewer/default-layout/lib/styles/index.css";
import ContextMenu from "../ContextMenu";

const PDFViewer = ({ base64 }) => {
  // get current page
  const [currentPage, setCurrentPage] = useState(0);

  const [totalPages, setTotalPages] = useState(0);

  const handleLoadSuccess = (pdfDocument) => {
    console.log("totalPages: ", pdfDocument);
    // You can get the total number of pages here if needed
    setTotalPages(pdfDocument.doc._pdfInfo.numPage);
  };

  const handlePageChange = (page) => {
    console.log("page: ", page.currentPage + 1);
  };

  // console.log("currentPage: ", CurrentPageLabel);

  const [contextMenuVisible, setContextMenuVisible] = useState(false);
  const [contextMenuPosition, setContextMenuPosition] = useState({
    x: 0,
    y: 0,
  });

  const menuRef = useRef(null);

  useEffect(() => {
    // Đăng ký trình nghe sự kiện click và mousedown
    window.addEventListener("click", handleCloseContextMenu);
    // window.addEventListener("mousedown", handleGlobalClickAndMouseDown);

    return () => {
      // Hủy đăng ký trình nghe sự kiện khi component unmount
      window.removeEventListener("click", handleCloseContextMenu);
      // window.removeEventListener("mousedown", handleGlobalClickAndMouseDown);
    };
  }, []);

  const handleContextMenu = (e, index) => {
    setCurrentPage(index);

    e.preventDefault();

    const rect = e.target.getBoundingClientRect(); // Lấy kích thước và vị trí của phần tử được click
    const x = e.clientX - rect.left; // Xác định vị trí x dựa trên vị trí của chuột

    const y = e.clientY - rect.top;

    // const menuWidth = menuRef.current.offsetWidth;
    // console.log("menuWidth: ", menuWidth);
    // const menuHeight = menuRef.current.offsetHeight;
    // console.log("menuHeight: ", menuHeight);
    // context menu height:265
    // 0: 523, 621
    // 1: 880, 790
    // context menu width: 158
    const pageX = e.clientX;
    // console.log("pageX: ", pageX);
    const pageY = e.clientY;
    // console.log("pageY: ", pageY);
    // const maxX = window.innerWidth;
    // console.log("maxX: ", maxX);
    // const maxY = window.innerHeight;
    // console.log("maxY: ", maxY);

    // if pageY - 265 > 523 : pageY - 266
    const contextMenuHeight = 265;
    const contextMenuWidth = 131;
    const minY = 620;
    const maxY = 1008;

    setContextMenuPosition({
      x: x,
      y: y,
    });
    setContextMenuVisible(true);
  };

  const handleCloseContextMenu = () => {
    setContextMenuVisible(false);
  };

  const renderPage = (props) => {
    // console.log("pageIndex: ", props.pageIndex);
    return (
      <div
        className={`cuong-page-${props.pageIndex + 1}`}
        onContextMenu={(e) => handleContextMenu(e, props.pageIndex + 1)}
        ref={menuRef}
        style={{
          margin: "0 auto",
          // height: "400px",
          // overflowY: "auto",
          width: "100%",
        }}
        id="pdf-view"
      >
        {/* {console.log("props: ", props)} */}
        {contextMenuVisible && currentPage === props.pageIndex + 1 && (
          <ContextMenu
            x={contextMenuPosition.x}
            y={contextMenuPosition.y}
            onClose={handleCloseContextMenu}
            // onMenuItemClick={handleMenuItemClick}
          />
        )}

        {props.canvasLayer.children}
        <div
          style={{
            alignItems: "center",
            display: "flex",
            height: "100%",
            justifyContent: "center",
            left: 0,
            position: "absolute",
            top: 0,
            width: "100%",
            zIndex: 2,
          }}
        >
          <div
            style={{
              color: "rgba(0, 0, 0, 0.2)",
              fontSize: `${8 * props.scale}rem`,
              fontWeight: "bold",
              textTransform: "uppercase",
              transform: "rotate(-45deg)",
              userSelect: "none",
            }}
          >
            Draft
          </div>
        </div>
        {props.annotationLayer.children}
        {props.textLayer.children}
      </div>
    );
  };

  const pageLayout = {
    transformSize: ({ size }) => ({
      height: size.height + 30,
      width: size.width + 30,
    }),
  };

  const defaultLayoutPluginInstance = defaultLayoutPlugin();
  return (
    <Box className="pdfviewer" id="pdfViewerContainer">
      <Worker workerUrl="https://unpkg.com/pdfjs-dist@3.4.120/build/pdf.worker.min.js">
        <Viewer
          fileUrl={`data:application/pdf;base64,${base64}`}
          plugins={[defaultLayoutPluginInstance]}
          renderPage={renderPage}
          // pageLayout={pageLayout}
          onDocumentLoad={handleLoadSuccess}
          onPageChange={handlePageChange}
        />
      </Worker>
    </Box>
  );
};

export default PDFViewer;
