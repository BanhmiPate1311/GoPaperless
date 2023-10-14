import React, { useState } from "react";
import { Outlet, useParams } from "react-router-dom";
import { Footer, Header } from "../components";
import { useEffect } from "react";
import { api } from "../constants/api";

const MainLayout = () => {
  const { signing_token } = useParams();
  const { batch_token } = useParams();
  const { upload_token } = useParams();
  // const { headerFooter } = useApiControllerManager();
  const [headerFooter, setHeaderFooter] = useState(null);
  const [logo, setLogo] = useState(null);

  // let colorHeader = "-webkit-linear-gradient(right, #0A98E7 0%, #3E3A94 100%)";
  // if (headerFooter[0]?.value) {
  //   colorHeader = JSON.parse(headerFooter[0].value);
  // }
  // const [isHeader, setIsHeader] = useState(1);

  const [isHeader, setIsHeader] = useState(1);

  const checkHeader = async (signing_token) => {
    try {
      const response = await api.post("/checkHeader", {
        signingToken: signing_token,
      });
      setIsHeader(response.data.visibleHeaderFooter);
    } catch (e) {
      console.log(e);
    }
  };

  const checkHeaderBS = async (batch_token) => {
    try {
      const response = await api.post("/checkHeaderBS", {
        batchToken: batch_token,
      });

      setIsHeader(response.data);
    } catch (e) {
      console.log(e);
    }
  };
  const CheckheaderFooterOpen = async (upload_token) => {
    try {
      const response = await api.post("/CheckheaderFooterOpen", {
        upload_token: upload_token,
      });

      setIsHeader(response.data);
    } catch (e) {
      console.log(e);
    }
  };
  const getHeaderFooterOpen = async (upload_token) => {
    try {
      const response = await api.post("/headerFooterOpen", {
        upload_token: upload_token,
      });
      if (response.data[0].logo) {
        setLogo(response.data[0].logo);
      }
      if (response.data[0].value) {
        setHeaderFooter(JSON.parse(response.data[0].value));
      }
    } catch (e) {
      console.log(e);
    }
  };

  const getHeaderFooter = async (signing_token) => {
    try {
      const response = await api.post("/headerFooter", {
        signingToken: signing_token,
      });
      if (response.data[0].logo) {
        setLogo(response.data[0].logo);
      }
      if (response.data[0].value) {
        setHeaderFooter(JSON.parse(response.data[0].value));
      }
    } catch (error) {
      // console.log("error: ", error.response.data);
    }
  };

  const headerfooterBatch = async (batchToken) => {
    try {
      const response = await api.post("/headerfooterBatch", {
        batchToken: batchToken,
      });
      if (response.data[0].logo) {
        setLogo(response.data[0].logo);
      }
      if (response.data[0].value) {
        setHeaderFooter(JSON.parse(response.data[0].value));
      }
      // setHeaderFooter(response.data);
    } catch (error) {
      console.log("error: ", error.response.data);
    }
  };

  useEffect(() => {
    if (signing_token) {
      checkHeader(signing_token);
      getHeaderFooter(signing_token);
    } else if (batch_token) {
      checkHeaderBS(batch_token);
      headerfooterBatch(batch_token);
    }
    // } else if (upload_token) {
    //   CheckheaderFooterOpen(upload_token);
    //   getHeaderFooterOpen(upload_token);
    // }
  }, []);

  const currentURL = window.location.href;
  const url = new URL(currentURL);
  const path = url.pathname;
  // if (path.includes("notFound")) {
  //   console.log("hahaha");
  // }

  // console.log("main layout");
  return (
    <div>
      {!path.includes("notFound") && isHeader === 1 && (
        <Header logo={logo} headerFooter={headerFooter} />
      )}
      <Outlet />
      {!path.includes("notFound") && isHeader === 1 && (
        <Footer headerFooter={headerFooter} />
      )}
    </div>
  );
};

export default MainLayout;
