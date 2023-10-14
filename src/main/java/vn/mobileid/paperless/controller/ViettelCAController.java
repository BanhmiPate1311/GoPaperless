package vn.mobileid.paperless.controller;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
            @RequestParam String userId,
            @RequestParam String connectorName,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId) throws Exception {
        logger.info("login: userId = " + userId);

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));

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
            @RequestParam String credentialID,
            @RequestParam String signingToken,
            @RequestParam String signerToken,
            @RequestParam String signerId,
            @RequestParam String certChain,
            @RequestParam String connectorName,
            @RequestParam String accessToken,
            @RequestParam String fileName,
            @RequestParam String serialNumber,
            String signingOption,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId,
            HttpServletRequest request
    ) throws Exception {

        String result = viettelCAService.signHash(
                credentialID,
                signingToken,
                signerToken,
                signerId,
                certChain,
                connectorName,
                accessToken,
                fileName,
                serialNumber,
                signingOption,
                request,
                enterpriseId, workFlowId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
