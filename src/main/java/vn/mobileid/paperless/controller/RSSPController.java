/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.controller;

//import com.sun.xml.internal.ws.wsdl.writer.document.Definitions;

import java.io.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;

import vn.mobileid.paperless.API.ICertificate;
import vn.mobileid.paperless.API.IServerSession;
import vn.mobileid.paperless.service.RSSPService;
import vn.mobileid.paperless.service.VCStoringService;

/**
 * @author Mr Spider
 */
@RestController
public class RSSPController {

    @Autowired
    private VCStoringService vcStoringService;

    @Autowired
    private RSSPService rsspService;

    @Autowired
    private GatewayAPI gatewayAPI;

    public static IServerSession session;
    public static ICertificate crt;

    private Logger logger = LoggerFactory.getLogger(ViettelCAController.class);

    @Autowired
    private process connect;

    // download get mapping with signingToken path variable
    @GetMapping("{signingToken}/download")
    public ResponseEntity<?> downloadFile(@PathVariable String signingToken,
            @RequestParam("access_token") String accessToken) {
        try {

//            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();
//            String sUUID_Last = "";
            InputStream inputStreamFile = null;
//            WorkFlowList[][] rsWFList = new WorkFlowList[1][];
//            connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, signingToken);
//            if (rsWFList != null && rsWFList[0].length > 0) {
//                if (rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {
//                    Participants[][] rsParticipant = new Participants[1][];
//                    connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, accessToken);
//                    if (rsParticipant[0] != null && rsParticipant[0].length > 0) {
//                        if (rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING
//                                && rsParticipant[0][0].PPL_WORKFLOW_ID == rsWFList[0][0].ID) {
//                            PPLFile[][] rsFile = new PPLFile[1][];
//                            connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingToken);
//                            if (rsFile != null && rsFile[0].length > 0) {
//                                sUUID_Last = rsFile[0][0].FILE_UUID;
//
//                            }
//
//                        }
//                    }
//                }
//            }
            FirstFile[][] file = new FirstFile[1][];
            String fileFile = "";
            connect.USP_GW_PPL_WORKFLOW_GET_FIRST_FILE(file, signingToken);
            if (file != null && file[0].length > 0) {
                fileFile = file[0][0].FILE_NAME.replace(".pdf", "");
            }
//
//            if (!"".equals(sUUID_Last)) {
//                byte[] jrbFile = FileJRBService.downloadFMS2(sUUID_Last);
////                byte[] jrbFile = gatewayAPI.getFileFromUploadToken(uploadToken);
//                if (jrbFile != null) {
//                    inputStreamFile = new ByteArrayInputStream(jrbFile);
//                }
//            }

//            byte[] jrbFile = FileJRBService.downloadFMS2(sUUID_Last);
            byte[] jrbFile = gatewayAPI.getLastFile(signingToken, accessToken);
            if (jrbFile != null) {
                inputStreamFile = new ByteArrayInputStream(jrbFile);
            }

            if (inputStreamFile != null) {
                // trả về stream input file để download kèm header content type và content
                // length để browser hiểu
                HttpHeaders headers = new HttpHeaders();
//                headers.add("Content-Disposition", "attachment; filename=" + "file.pdf");
                headers.add("Content-Disposition", "attachment; filename=" + fileFile + "_signed.pdf");
                // jrbFile.getFileName());
                InputStreamResource inputStreamResource = new InputStreamResource(inputStreamFile);
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .headers(headers)
                        .body(inputStreamResource);
            } else {
                // trả về lỗi không tìm thấy file để download
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
            }
        } catch (Exception e) {
//            log.error("Error when download file", e.getMessage());
            throw new RuntimeException("Error when download file");
//            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/getCertificate")
    public ResponseEntity<?> getCertificate(
            @RequestParam String lang,
            @RequestParam String connectorName,
            @RequestParam String codeNumber,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId) throws Exception {
        System.out.println("codeNumber123: " + codeNumber);
        Map<String, Object> certificate = rsspService.getCertificate(lang, connectorName, codeNumber, enterpriseId, workFlowId);

        return new ResponseEntity<>(certificate, HttpStatus.OK);

    }

    @PostMapping("/signFile")
    public ResponseEntity<?> signFile(
            @RequestParam("requestID") String requestID,
            @RequestParam("signingToken") String signingTokenRequest,
            @RequestParam("filename") String fileName,
            @RequestParam("signerToken") String signerTokenRequest,
            @RequestParam("connectorName") String connectorName,
            @RequestParam("signingOption") String singingOption,
            @RequestParam("codeNumber") String codeNumber,
            @RequestParam("credentialID") String credentialID,
            @RequestParam("signerId") String signerId,
            @RequestParam("certChain") String certChain,
            @RequestParam("type") String typeRequest,
            @RequestParam("prefixCode") String prefixCode,
            @RequestParam("relyingParty") String relyingParty,
            @RequestParam("codeEnable") String codeEnable,
            @RequestParam("lang") String lang,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId,
            HttpServletRequest request)
            throws Throwable {
        String result = rsspService.signFile(requestID, signingTokenRequest, fileName, signerTokenRequest, singingOption,
                codeNumber, credentialID, signerId, certChain, typeRequest, prefixCode, relyingParty, codeEnable, request, connectorName, enterpriseId, workFlowId, lang);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
