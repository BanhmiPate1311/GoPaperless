import React, { Fragment, useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import { useApiControllerManager } from "../store/apiControllerManager";

import { api } from "../constants/api";
import SigningComponent from "../components/SigningComponent";
import NotFound from "./NotFound";

export const Signing = () => {
  const [search, setSearchParams] = useSearchParams();

  const [isFetching, setIsFetching] = useState(false);

  const { signing_token } = useParams();

  const [signerToken] = useState(search.get("access_token"));

  const [workFlow, setWorkFlow] = useState({});

  const getSigningWorkFlow = async (signing_token) => {
    try {
      setIsFetching(true);
      const response = await api.post("/getSigningWorkFlow", {
        signingToken: signing_token,
      });
      setWorkFlow({
        ...response.data,
        signerToken: signerToken,
      });
      setIsFetching(false);
    } catch (error) {
      setIsFetching(false);
      console.error(error);
    }
  };

  useEffect(() => {
    checkValid();
  }, []);

  // message when sign
  const { isSignSuccess } = useApiControllerManager("");

  useEffect(() => {
    getSigningWorkFlow(signing_token);
  }, [isSignSuccess]);

  const [valid, setValid] = useState(null);

  const checkValid = async () => {
    try {
      const data = {};
      data.signingToken = signing_token;
      data.signerToken = signerToken;
      const response = await api.post("/getCheckValid", data);

      setValid(response.data);
    } catch (e) {
      console.log(e);
    }
  };

  // const navigate = useNavigate();

  if (valid === null) {
    return (
      <div
        className="modal backdrop fade show d-flex justify-content-center align-items-center"
        style={{ background: "#00000080" }}
      >
        <div className="loader" />
      </div>
    );
  } else if (valid === 0) {
    // navigate("/signing/notFound");
    return <NotFound />;
  } else {
    return (
      <Fragment>
        {isFetching ? (
          <div
            className="modal backdrop fade show d-flex justify-content-center align-items-center"
            style={{ background: "#00000080" }}
          >
            <div className="loader" />
          </div>
        ) : (
          <SigningComponent workFlow={workFlow} />
        )}
        ;
      </Fragment>
    );
  }
};

export default Signing;
