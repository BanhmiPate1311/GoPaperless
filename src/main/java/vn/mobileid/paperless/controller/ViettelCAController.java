package vn.mobileid.paperless.controller;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.paperless.Model.smartId.request.VtCAGetCertificateRequest;
import vn.mobileid.paperless.Model.smartId.request.VtCASignHashRequest;
import vn.mobileid.paperless.object.ConnectorLogRequest;
import vn.mobileid.paperless.service.ViettelCAService;
import vn.mobileid.paperless.viettelca.response.CertDetail;
import vn.mobileid.paperless.viettelca.response.VTCertResponse;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/viettel-ca")
public class ViettelCAController {

    @Autowired
    private ViettelCAService viettelCAService;

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(ViettelCAController.class);

    @PostMapping("/getCertificate")
    public ResponseEntity<?> getCertificate(
            @RequestBody VtCAGetCertificateRequest request) throws Exception {
        logger.info("login: userId = " + request.getUserId());
        String userId = request.getUserId();
        String connectorName = request.getConnectorName();
        int enterpriseId = request.getEnterpriseId();
        int workFlowId = request.getWorkFlowId();

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
        connectorLogRequest.setpENTERPRISE_ID(enterpriseId);
        connectorLogRequest.setpWORKFLOW_ID(workFlowId);

        String accessToken = viettelCAService.login(userId, connectorName, connectorLogRequest);

        logger.info("getCertificate: accessToken = " + accessToken);

        CertDetail[] jsonObject = viettelCAService.getCertificate(accessToken, connectorLogRequest);
        VTCertResponse response = new VTCertResponse();
        response.setData(jsonObject);
        response.setAccess_token(accessToken);

        logger.info(gson.toJson(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signHash")
    public ResponseEntity<?> hashFile(
            @RequestBody VtCASignHashRequest vtCASignHashRequest,
            HttpServletRequest request
    ) throws Exception {

        String result = viettelCAService.signHashFps(vtCASignHashRequest, request);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
