import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";
import FileDownloadOutlinedIcon from "@mui/icons-material/FileDownloadOutlined";
import {
  AppBar,
  Box,
  Button,
  IconButton,
  Paper,
  Toolbar,
  Tooltip,
  Typography,
  styled,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import { ReactComponent as Lock } from "../assets/images/lock_validate.svg";
import "../assets/styles/validation.css";
import PDFViewer from "../components/validate/PDFViewer";
import TabDocument from "../components/validate/TabDocument";
import { api } from "../constants/api";
import { useTranslation } from "react-i18next";

const CustomButton = styled(Button)`
  text-transform: none; /* Đặt textTransform thành none để bỏ chữ in hoa */
  border-radius: 50px;
  white-space: nowrap;
  background: rgb(90, 51, 139);
`;

const Validation = () => {
  const { t } = useTranslation();
  const Item = styled(Paper)(({ theme }) => ({
    backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
    ...theme.typography.body2,
    padding: theme.spacing(1),
    color: "black",
  }));

  const { upload_token } = useParams();

  const [isFetching, setIsFetching] = useState(false);

  // const [infoFile, setInfoFile] = useState({});
  const [validFile, setValidFile] = useState({});

  // const getFirstFileFromUploadToken = async (upload_token) => {
  //   setIsFetching(true);
  //   try {
  //     const response = await api.post("/getFirstFileFromUploadToken", {
  //       upload_token,
  //     });
  //     setIsFetching(false);
  //     setInfoFile(response.data);
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };

  const getValidView = async () => {
    setIsFetching(true);
    try {
      const response = await api.post("/val/getView", {
        uploadToken: upload_token,
      });

      setValidFile(response.data);
      setIsFetching(false);
      // setInfoFile(response.data);
    } catch (error) {
      setIsFetching(false);
      console.error(error);
    }
  };

  useEffect(() => {
    if (upload_token) {
      // getFirstFileFromUploadToken(upload_token);
      getValidView();
    }
  }, []);

  // const notSign =
  //   validFile.signatures.length === 0 && validFile.seals.length === 0;

  const [selectedOptionMobile, setSelectedOptionMobile] = useState(
    localStorage.getItem("previousOptionMobile") || ""
  );

  const [anchorEl, setAnchorEl] = React.useState(null);
  const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);

  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMobileMenuOpen = (event) => {
    setMobileMoreAnchorEl(event.currentTarget);
  };

  const location = useLocation();
  const selectedItemData = location.state; // Lấy toàn bộ object stateData truyền từ trang Validation/listing

  // Kiểm tra nếu không có selectedItemData (undefined) hoặc không có thuộc tính name
  // if (!selectedItemData || !selectedItemData.name) {
  //   return <div>Data not available</div>;
  // }
  // Nếu có selectedItemData và thuộc tính name, có thể truy cập và sử dụng các thuộc tính khác
  // const { name, quantity, status } = selectedItemData;
  return (
    <main>
      {isFetching && (
        <div
          className="modal backdrop fade show d-flex justify-content-center align-items-center"
          style={{ background: "#00000080" }}
        >
          <div className="loader" />
        </div>
      )}
      <div className="container preview-document-container isign-signing-show isign-signature-pdf ">
        <Box sx={{ flexGrow: 1 }}>
          <AppBar
            position="static"
            sx={{
              background: "#fff",
              color: "inherit",
              minHeight: "100%",
              height: "65px",
            }}
          >
            <Toolbar>
              {/* <IconButton
                  size="large"
                  edge="start"
                  color="inherit"
                  aria-label="open drawer"
                  sx={{ mr: 2 }}
                  // onClick={handleBack}
                >
                  <ArrowBackIcon />
                </IconButton> */}
              <Typography
                variant="h6"
                noWrap
                component="div"
                sx={{ display: { xs: "none", sm: "block" }, px: 1 }}
              >
                {validFile?.file?.fileName}
              </Typography>
              <Box
                sx={{
                  background: "rgba(0, 0, 0, 0.3)",
                  paddingTop: "2px",
                  padding: "2px 10px 2px 10px",
                  borderRadius: "15px",
                  color: "#fff",
                  fontWeight: "30px",
                }}
              >
                PDF
              </Box>
              <Box sx={{ flexGrow: 1 }} />
              <Box
                sx={{
                  display: { xs: "none", md: "flex" },
                  gap: "10px",
                  alignItems: "center",
                }}
              >
                {/* <IconButton
                    // aria-controls={menuId}
                    aria-haspopup="true"
                    onClick={handleProfileMenuOpen}
                    color="inherit"
                  >
                    <Avatar sx={{ background: "#E8B001", fontSize: "1.0rem" }}>
                      XP
                    </Avatar>
                  </IconButton> */}
                {/* <IconButton
                  size="large"
                  aria-label="show 4 new mails"
                  color="#ccc"
                >
                  <Tooltip title="Delete report">
                    <DeleteOutlineIcon />
                  </Tooltip>
                </IconButton> */}
                {/* <IconButton
                    size="large"
                    aria-label="show 17 new notifications"
                    color="#ccc"
                  >
                    <Tooltip title="Upload for signing">
                      <IosShareOutlinedIcon />
                    </Tooltip>
                  </IconButton> */}
                <Button
                  disabled={
                    validFile?.signatures?.length === 0 &&
                    validFile?.seals?.length === 0
                  }
                >
                  <a
                    style={{ color: "white", textDecoration: "none" }}
                    href={`${window.location.origin}/internalusage/api/validation/${upload_token}/download/report-pdf`}
                  >
                    <CustomButton
                      startIcon={<FileDownloadOutlinedIcon />}
                      variant="contained"
                    >
                      {t("validation.downloadReport")}
                    </CustomButton>
                  </a>
                </Button>

                {/* <CustomButtonFill
                  // startIcon={<FileDownloadOutlinedIcon />}
                  size="medium"
                  sx={{ px: 2 }}
                >
                  Download report
                </CustomButtonFill> */}
              </Box>

              <Box sx={{ display: { xs: "flex", md: "none" } }}>
                <IconButton
                  size="large"
                  aria-label="show more"
                  // aria-controls={mobileMenuId}
                  aria-haspopup="true"
                  onClick={handleMobileMenuOpen}
                  color="inherit"
                >
                  {/* <MoreIcon /> */}
                </IconButton>
              </Box>
            </Toolbar>
          </AppBar>
        </Box>
        <div className="content-sign">
          <div className="css-ufz7ne">
            <div className="css-91kxzg">
              <div className="css-r11a23">
                <div className="css-thqqgj css-10ktgt9">
                  <div className="css-h6dcj7">
                    {Object.keys(validFile).length !== 0 && (
                      <PDFViewer base64={validFile?.file?.content} />
                    )}
                  </div>
                </div>
                <div className="css-pmda5y css-1e61wdr css-wnj41j css-1nqdrf4 css-1r2zlre">
                  <div style={{ opacity: 1, transform: "none" }}>
                    <TabDocument validFile={validFile} />
                    {/* Form */}
                    <div className="css-10vgal8 css-19wcyhm">
                      <div className="css-1oy1ewu">
                        <Box
                          sx={{
                            overflow: "hidden",
                            background: "rgb(232, 235, 240)",
                          }}
                        >
                          <Box
                            sx={{
                              display: "flex",
                              alignItems: "center",
                              gap: "20px",
                              p: 2,
                            }}
                          >
                            {/* <img src={Lock} alt="lock"></img> */}
                            <Lock style={{ width: "100px" }} />
                            <Typography variant="h6">
                              {t("validation.val1")}
                              <u>{t("validation.val2")}</u>{" "}
                              {t("validation.val3")}
                            </Typography>
                          </Box>
                        </Box>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          {/* <CookieSetting /> */}
        </div>
      </div>
    </main>
  );
};

export default Validation;
