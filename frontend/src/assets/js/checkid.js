import { api } from "../../constants/api";

var socket;
var flagFistTime = 1;
var flagTimeOut = 0;
var wsTimeout;
var mapRequestID = new Map();
var pWorkFlowIdList = [];
var pEnterpriseIdList = [];
var pURL = null;
var connectorLogRequest = {
  pUSER_EMAIL: null,
  pCONNECTOR_NAME: null,
  pENTERPRISE_ID: null,
  pWORKFLOW_ID: null,
  pAPP_NAME: null,
  API_KEY: null,
  pVERSION: null,
  pSERVICE_NAME: null,
  pURL: null,
  pHTTP_VERB: null,
  pSTATUS_CODE: null,
  pREQUEST: null,
  pRESPONSE: null,
  pHMAC: null,
  pCREATED_BY: null,
  pAccessToken: null,
};

export default class ISPluginClient {
  constructor(
    ip,
    port,
    isSecure,
    cbConnected,
    cbDisconnected,
    cbStopped,
    cbConnectionDenied,
    cbReceive
  ) {
    flagTimeOut = 0;

    var url = isSecure + "://" + ip + ":" + port + "/ISPlugin";
    socket = new WebSocket(url);

    function shutdown() {
      flagTimeOut = 1;
      socket.close(1000, "work complete");
    }

    socket.onopen = function (event) {
      console.log("socket open");
      clearTimeout(wsTimeout);
      flagFistTime = 1;
      cbConnected();
    };

    socket.onclose = function (event) {
      console.log(`Closed ${event.code}`);

      if (flagTimeOut === 0) {
        console.log("reconnect!!!");
        console.log("flagFistTime: ", flagFistTime);

        if (flagFistTime === 1) {
          flagFistTime = 0;
          cbDisconnected();
          wsTimeout = setTimeout(timeoutWS, 30000);

          function timeoutWS() {
            if (socket.readyState !== 1) {
              flagTimeOut = 1;
              console.log("connect failure!");
              clearTimeout(wsTimeout);
              flagFistTime = 1;
              cbStopped();
              socket.close(1000, "Connect failure");
            }
          }
        }

        new ISPluginClient(
          ip,
          port,
          isSecure,
          cbConnected,
          cbDisconnected,
          cbStopped,
          cbConnectionDenied,
          cbReceive
        );
      }
    };

    socket.onmessage = async function (event) {
      console.log("onmessage");
      var response = {};
      response = JSON.parse(event.data);
      // console.log(event);
      // console.log("response", response);

      for (var i = 0; i < pWorkFlowIdList.length; i++) {
        // connectorLogRequest.pCONNECTOR_NAME = pCONNECTOR_NAME;
        connectorLogRequest.pENTERPRISE_ID = pEnterpriseIdList[i];
        connectorLogRequest.pWORKFLOW_ID = pWorkFlowIdList[i];
        // connectorLogRequest.pURL = pURL;
        // connectorLogRequest.pHTTP_VERB = pHTTP_VERB;
        connectorLogRequest.pSTATUS_CODE = response.errorCode;
        connectorLogRequest.pRESPONSE = event.data;
        // connectorLogRequest.pREQUEST = pREQUEST;
        try {
          // console.log("co dzo day khong");
          await api.post("/connectorLogAdd", connectorLogRequest);
        } catch (e) {
          console.log("error", e);
        }
      }

      // console.log("connectorLogRequest", connectorLogRequest);

      if (!response) {
        console.log("Skip Response because response is null");
      } else {
        var cmd = response.cmdType;
        var id = response.requestID;
        var error = response.errorCode;
        var errorMsg = response.errorMessage;
        var data = response.data;

        connectorLogRequest.pRESPONSE = response;
        if (!cmd) {
          console.log("Skip Response because cmdType is null");
          if (error && error === 1008) {
            cbConnectionDenied(errorMsg);
          }
          // } else if (error && error === 1101) {
          //   cbConnectionDenied(errorMsg);
        } else {
          if (cbReceive) {
            cbReceive(cmd, id, error, data);
          }
          if (mapRequestID.has(id)) {
            var req = mapRequestID.get(id);
            mapRequestID.delete(id);

            if (!req) {
            } else if (req.cmdType !== cmd) {
              //error if != cmdType
              req.cb_error(
                -1,
                "cmdType does not match, got [" +
                  cmd +
                  "] but expect [" +
                  req.cmdType +
                  "]"
              );
            } else if (error === 0) {
              //success
              if (req.cmdType === "BiometricAuthentication") {
                console.log("BiometricAuthentication");
                req.cb_success(response);
              } else {
                req.cb_success(data);
              }
            } else {
              //error
              req.cb_error(error, errorMsg);
            }
          } else {
            console.log(
              "Skip Response because not found requestID [" + id + "]"
            );
          }
        }
      }
    };

    function create_uuidv4() {
      return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, (c) =>
        (
          c ^
          (crypto.getRandomValues(new Uint8Array(1))[0] & (15 >> (c / 4)))
        ).toString(16)
      );
    }

    function getDeviceDetails(
      deviceDetailsEnabled,
      presenceEnabled,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "GetDeviceDetails",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "GetDeviceDetails",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          data: {
            deviceDetailsEnabled: deviceDetailsEnabled,
            presenceEnabled: presenceEnabled,
          },
        })
      );

      let getDeviceDetailsTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout(); //callback when time out
        }
      }, timeOutInterval * 1000);
    }

    // = getInformationDetails old
    function getInformationDetails(
      mrzEnabled,
      imageEnabled,
      dataGroupEnabled,
      optionalDetailsEnabled,
      // canValue,
      // challenge,
      caEnabled,
      taEnabled,
      paEnabled,
      lang,
      canValue,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      console.log("getInformationDetails 123");
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "GetInfoDetails",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "GetInfoDetails",
          requestID,
          timeOutInterval,
          lang: lang,
          data: {
            mrzEnabled,
            imageEnabled,
            dataGroupEnabled,
            optionalDetailsEnabled,
            canValue,
            caEnabled,
            taEnabled,
            paEnabled,
          },
        })
      );

      let GetInfoDetailsTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function biometricAuthentication(
      biometricType,
      // challengeBiometric,
      // challengeType,
      livenessEnabled,
      cardNo,
      lang,
      biometricEvidenceEnabled,
      challengeType,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      console.log("bat dau biometricAuthentication");
      mapRequestID.set(requestID, {
        cmdType: "BiometricAuthentication",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "BiometricAuthentication",
          requestID: requestID,
          lang,
          timeOutInterval: timeOutInterval,
          data: {
            biometricType: biometricType,
            cardNo: cardNo,
            livenessEnabled,
            biometricEvidenceEnabled,
            challengeType,
          },
        })
      );

      let biometricAuthenticationTimeOut = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
      console.log("biometricAuthentication ket thuc");
    }

    function connectToDevice(
      confirmEnabled,
      confirmCode,
      clientName,
      automaticEnabled,
      mrzEnabled,
      imageEnabled,
      dataGroupEnabled,
      optionalDetailsEnabled,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "ConnectToDevice",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "ConnectToDevice",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          data: {
            clientName: clientName,
            confirmEnabled: confirmEnabled,
            confirmCode: confirmCode,
            configuration: {
              automaticEnabled: automaticEnabled,
              mrzEnabled: mrzEnabled,
              imageEnabled: imageEnabled,
              dataGroupEnabled: dataGroupEnabled,
              optionalDetailsEnabled: optionalDetailsEnabled,
            },
          },
        })
      );

      let connectToDeviceTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function displayInformation(
      title,
      type,
      value,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "DisplayInformation",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "DisplayInformation",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          data: {
            title: title,
            type: type,
            value: value,
          },
        })
      );
      let displayInformationTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function refreshReader(
      deviceDetailsEnabled,
      presenceEnabled,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "Refresh",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "Refresh",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          data: {
            deviceDetailsEnabled: deviceDetailsEnabled,
            presenceEnabled: presenceEnabled,
          },
        })
      );

      let refreshReaderTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function scanDocument(
      scanType,
      saveEnabled,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "ScanDocument",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "ScanDocument",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          data: {
            scanType: scanType,
            saveEnabled: saveEnabled,
          },
        })
      );

      let scanDocumentTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function getBiometricEvidence(
      biometricType,
      timeOutInterval,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "BiometricEvidence",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      socket.send(
        JSON.stringify({
          cmdType: "BiometricEvidence",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          data: {
            biometricType: biometricType,
          },
        })
      );

      let biometricEvidenceTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function getTokenCertificate(
      timeOutInterval,
      dllNameList,
      currentDomain,
      connectorName,
      workFlowIdList,
      enterpriseIdList,
      lang,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "GetTokenCertificates",
        cb_success: cbSuccess,
        cb_error: cbError,
      });

      pWorkFlowIdList = workFlowIdList;
      pEnterpriseIdList = enterpriseIdList;
      connectorLogRequest.pCONNECTOR_NAME = connectorName;
      connectorLogRequest.pURL = "GetTokenCertificates";
      connectorLogRequest.pHTTP_VERB = "wss";
      connectorLogRequest.pREQUEST = JSON.stringify({
        cmdType: "GetTokenCertificates",
        requestID,
        timeOutInterval,
        lang,
        data: {
          // dllNames: ["cmcca_csp11_v1", "eps2003csp11"],
          dllNames: dllNameList,
          currentDomain,
        },
      });
      socket.send(
        JSON.stringify({
          cmdType: "GetTokenCertificates",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          lang,
          data: {
            // dllNames: ["cmcca_csp11_v1", "eps2003csp11"],
            dllNames: dllNameList,
            currentDomain,
          },
        })
      );

      let getTokenCertificateTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    function signTokenCertificate(
      certId,
      certPin,
      signObjects,
      timeOutInterval,
      lang,
      cbSuccess,
      cbError,
      cbTimeout
    ) {
      var requestID = create_uuidv4();
      mapRequestID.set(requestID, {
        cmdType: "SignTokenCertificate",
        cb_success: cbSuccess,
        cb_error: cbError,
      });
      connectorLogRequest.pURL = "SignTokenCertificate";
      connectorLogRequest.pREQUEST = JSON.stringify({
        cmdType: "SignTokenCertificate",
        requestID: requestID,
        timeOutInterval: timeOutInterval,
        lang,
        data: {
          certId,
          certPin,
          currentDomain: "id.mobile-id.vn",
          signObjects,
        },
      });
      socket.send(
        JSON.stringify({
          cmdType: "SignTokenCertificate",
          requestID: requestID,
          timeOutInterval: timeOutInterval,
          lang,
          data: {
            certId,
            certPin,
            currentDomain: "id.mobile-id.vn",
            signObjects,
          },
        })
      );
      let signTokenCertificateTimeout = setTimeout(function () {
        if (mapRequestID.has(requestID)) {
          mapRequestID.delete(requestID);
          cbTimeout();
        }
      }, timeOutInterval * 1000);
    }

    return {
      getDeviceDetails: getDeviceDetails,
      getInformationDetails: getInformationDetails,
      biometricAuthentication: biometricAuthentication,
      connectToDevice: connectToDevice,
      displayInformation: displayInformation,
      refreshReader: refreshReader,
      scanDocument: scanDocument,
      getBiometricEvidence: getBiometricEvidence,
      shutdown: shutdown,
      getTokenCertificate: getTokenCertificate,
      signTokenCertificate,
    };
  }
}
