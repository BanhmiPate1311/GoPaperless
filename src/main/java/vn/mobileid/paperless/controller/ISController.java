package vn.mobileid.paperless.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.exsig.PdfProfile;
import vn.mobileid.exsig.PdfProfileCMS;
import vn.mobileid.exsig.VerifyResult;
import vn.mobileid.fms.client.JCRFile;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.Model.smartId.request.IsRequest;
import vn.mobileid.paperless.Model.smartId.request.SignRequest;
import vn.mobileid.paperless.fps.request.FpsSignRequest;
import vn.mobileid.paperless.fps.request.HashFileRequest;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.API.SigningMethodAsyncImp;
import vn.mobileid.paperless.repository.CommonRepository;
import vn.mobileid.paperless.service.FileJRBService;
import vn.mobileid.paperless.service.FpsService;
import vn.mobileid.paperless.service.RSSPService;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.CommonHash;
import vn.mobileid.paperless.utils.Difinitions;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/is")
public class ISController {

    @Autowired
    private RSSPService rsspService;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private process connect;

    @Autowired
    private GatewayAPI gatewayAPI;

    @Autowired
    private FpsService fpsService;

    @PostMapping("/getHashFile")
    public String getHashFile(@RequestParam String signingToken) throws Exception {
        // get UUID of last file

        String sUUID_Last = "";
        int sFileID_Last = 0;
        LastFile[][] rsFile = new LastFile[1][];
        connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingToken);
        if (rsFile[0].length > 0) {
            sFileID_Last = rsFile[0][0].getLAST_PPL_FILE_SIGNED_ID();
            sUUID_Last = rsFile[0][0].getLAST_PPL_FILE_UUID();
        }

        // download file
        String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();
        String jrbFile = FileJRBService.downloadFMS(sUUID_Last);
        InputStream inputStreamNotSigned = null;
        if (jrbFile != null) {
//            inputStreamNotSigned = jrbFile.getStream();
            //hash file
            byte[] pdfFile = Base64.decodeBase64(jrbFile);

//            byte[] pdfFile = IOUtils.toByteArray(inputStreamNotSigned);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256Hash = digest.digest(pdfFile);

            return CommonFunction.bytesToHex(sha256Hash);
        } else {
            return "File not found";
        }
    }

    @PostMapping("/getHashFile2")
    public Map<String, String> getHashFile2(
            @RequestParam String signingToken,
            @RequestParam String cerId,
            @RequestParam String signerId,
            @RequestParam String signerToken,
            @RequestParam String signingOption,
            @RequestParam String certEncode,
            @RequestParam String signName,
            @RequestParam String connectorName) throws Exception {


        String[] sResultConnector = new String[2];
        String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
        String prefixCode = sResultConnector[1];

        long millis = System.currentTimeMillis();
        String sSignatureHash = signerToken + millis;
//        String sSignature_id = prefixCode + "-" + CommonHash.hashPass(sSignatureHash.getBytes());
        String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();
//        System.out.println("sSignature_id: " + sSignature_id);
//        List<String> hashList = commonRepository.createHashList(signerToken, signingToken, certEncode, cerId, "", sSignature_id);

        PrepareSigningRequest prepareSigningRequest = new PrepareSigningRequest(
                signingToken,
                signerToken,
                signingOption,
                signerId,
                certEncode,
                connectorName,
                null
        );
        String hashList = gatewayAPI.PrepareSign(prepareSigningRequest);

        if(hashList == null) {
            return null;
        }

        byte[] decoded = Base64.decodeBase64(hashList);
        String hash = Hex.encodeHexString(decoded);
        Map<String, String> response = new HashMap<>();
        response.put("hash", hash);
        response.put("signatureId", sSignature_id);

        return response;
    }

    @PostMapping("/getHashFileFps")
    public ResponseEntity<?> getHashFileFps(
            @RequestBody SignRequest signRequest) throws Exception {
        String field_name = signRequest.getFieldName();
        String signerToken = signRequest.getSignerToken();
        String connectorName = signRequest.getConnectorName();
        int documentId = signRequest.getDocumentId();
        String certChain = signRequest.getCertChain();
        String signingToken = signRequest.getSigningToken();
        String credentialID = signRequest.getCredentialID();



        // check workflow participant

        System.out.println("signerToken: " + signerToken);
        Participants[][] rsParticipant = new Participants[1][];
        connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, signerToken);
        if (rsParticipant[0] == null || rsParticipant[0].length == 0 || rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {

            return new ResponseEntity<>("The document has already been signed", HttpStatus.OK);
        }

        String meta = rsParticipant[0][0].META_INFORMATION;

        String[] sResultConnector = new String[2];
        connect.getIdentierConnector(connectorName, sResultConnector);
        String prefixCode = sResultConnector[1];

        long millis = System.currentTimeMillis();
        String sSignatureHash = signerToken + millis;
        String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();

        String documentDetails = fpsService.getDocumentDetails(documentId);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(documentDetails);
        JsonNode documentNode = jsonNode.get(0);

        int pageHeight = 0;
        int pageWidth = 0;
        if (documentNode != null) {
            pageHeight = documentNode.get("document_height").asInt();
            pageWidth = documentNode.get("document_width").asInt();
        }

        List<String> listCertChain = new ArrayList<>();
        listCertChain.add(certChain);

        HashFileRequest hashFileRequest = commonRepository.getMetaData(signRequest.getSignerToken(), meta);
        hashFileRequest.setCertificateChain(listCertChain);

        if (field_name == null || field_name.isEmpty()) {
            System.out.println("kiem tra:");
            commonRepository.addSign(pageHeight, pageWidth, signingToken, certChain, credentialID, "", sSignature_id, meta, documentId, signerToken);
//                hashFileRequest.setFieldName(signerToken);
        }
        hashFileRequest.setFieldName(!signRequest.getFieldName().isEmpty() ? signRequest.getFieldName() : signerToken);
        String hashList = fpsService.hashSignatureField(signRequest.getDocumentId(), hashFileRequest);

        byte[] decoded = Base64.decodeBase64(hashList);
        String hash = Hex.encodeHexString(decoded);
        Map<String, String> response = new HashMap<>();
        response.put("hashPG", hash);
        response.put("hash", hashList);
        response.put("signatureId", sSignature_id);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signUsbToken")
    public ResponseEntity<?> signUsbToken(
            @RequestParam List<String> signatures,
            @RequestParam String certEncode,
            @RequestParam String cerId,
            @RequestParam String signerId,
            @RequestParam String fileName,
            @RequestParam String signingToken,
            @RequestParam String signerToken,
            @RequestParam String connectorName,
            @RequestParam String serialNumber,
            @RequestParam String signingOption,
            @RequestParam String signatureId,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId,
            HttpServletRequest request) throws Exception {

        try {

            boolean error = false;


            WorkFlowList[][] rsWFList = new WorkFlowList[1][];
            connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, signingToken);
            String sResult = "0";

            // check workflow status
            if (rsWFList[0] == null || rsWFList[0].length == 0 || rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {
                error = true;
                sResult = "Signer Status invalid";// trạng thái không hợp lệ
                throw new Exception(sResult);
            }

            // check workflow participant
            Participants[][] rsParticipant = new Participants[1][];
            connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, signerToken);
            if (rsParticipant[0] == null || rsParticipant[0].length == 0 || rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {
                sResult = "The document has already been signed";
            }

            String signature = signatures.get(0);
            String result = gatewayAPI.sign(signingToken, signerToken, signerId, signature);

//            String sUUID_Last = "";
//            int sFileID_Last = 0;
//            PPLFile[][] rsFile = new PPLFile[1][];
//            connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingToken);
//            if (rsFile != null && rsFile[0].length > 0) {
//                sFileID_Last = rsFile[0][0].ID;
//                sUUID_Last = rsFile[0][0].FILE_UUID;
//            }
//
//            // download first file
//            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();
//
//            byte[] pdfSigned = commonRepository.packFile(certEncode,signatures,cerId);
//
//            Date signTime = CommonFunction.getSigningTime(pdfSigned);
//
//            Timestamp tsTimeSigned = null;
//
//            if (signTime != null) {
//                tsTimeSigned = new Timestamp(signTime.getTime());
//            }
//            String sType = Difinitions.CONFIG_PREFIX_UID_TOKEN;
//
//            CallBackLogRequest callBackLogRequest = new CallBackLogRequest();
//            callBackLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
//            callBackLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));
//
//            commonRepository.postBack(callBackLogRequest,rsParticipant,pdfSigned,fileName, signingToken,pDMS_PROPERTY,signatureId, signerToken,tsTimeSigned,rsWFList,sFileID_Last,certEncode,serialNumber,signingOption,sType,request);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/signUsbTokenFps")
    public ResponseEntity<?> signUsbTokenFps(
            @RequestBody IsRequest signRequest,
            HttpServletRequest request) throws Exception {

        try {
            String field_name = signRequest.getFieldName();
            String signingToken = signRequest.getSigningToken();
            String signerToken = signRequest.getSignerToken();
            String certChain = signRequest.getCertChain();
            String hashList = signRequest.getHashList();
            String signature = signRequest.getSignature();
            int documentId = signRequest.getDocumentId();
            int enterpriseId = signRequest.getEnterpriseId();
            int workFlowId = signRequest.getWorkFlowId();
            String signerId = signRequest.getSignerId();
            String fileName = signRequest.getFileName();
            String signatureId = signRequest.getSignatureId();
            int lastFileId = signRequest.getLastFileId();
            String codeNumber = signRequest.getSerialNumber();
            String signingOption = signRequest.getSigningOption();



            WorkFlowList[][] rsWFList = new WorkFlowList[1][];
            connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, signingToken);
            String sResult = "0";

            // check workflow status
            if (rsWFList[0] == null || rsWFList[0].length == 0 || rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {

                sResult = "Signer Status invalid";// trạng thái không hợp lệ
                throw new Exception(sResult);
            }

            // check workflow participant
            Participants[][] rsParticipant = new Participants[1][];
            connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, signerToken);
            if (rsParticipant[0] == null || rsParticipant[0].length == 0 || rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {
                sResult = "The document has already been signed";
                throw new Exception(sResult);
            }

            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            String meta = rsParticipant[0][0].META_INFORMATION;
            JsonObject jsonObject = new Gson().fromJson(meta, JsonObject.class);

            int isSetPosition = 0;
            if (field_name != null && !field_name.isEmpty()) {
                isSetPosition = 1;
            } else if (jsonObject != null && jsonObject.has("pdf")) {
                JsonObject pdfObject = jsonObject.getAsJsonObject("pdf");

                JsonElement annotationElement = pdfObject.get("annotation");
                if (annotationElement != null) {
                    JsonObject annotationObject = annotationElement.getAsJsonObject();
                    if (annotationObject.has("top") && annotationObject.has("left")) {
                        isSetPosition = 1;
                    }
                }
            }

            List<String> listCertChain = new ArrayList<>();
            listCertChain.add(certChain);
            FpsSignRequest fpsSignRequest = new FpsSignRequest();
            fpsSignRequest.setFieldName(!signRequest.getFieldName().isEmpty() ? signRequest.getFieldName() : signerToken);
            fpsSignRequest.setHashValue(hashList);
            fpsSignRequest.setSignatureValue(signature);

            fpsSignRequest.setCertificateChain(listCertChain);

            Gson gson = new Gson();
            System.out.println("fpsSignRequest: " + gson.toJson(fpsSignRequest));

            String responseSign = fpsService.signDocument(documentId, fpsSignRequest);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode signNode = objectMapper.readTree(responseSign);
            String uuid = signNode.get("uuid").asText();
            int fileSize = signNode.get("file_size").asInt();
            String digest = signNode.get("digest").asText();
            String signedHash = signNode.get("signed_hash").asText();
            String signedTime = signNode.get("signed_time").asText();

            CallBackLogRequest callBackLogRequest = new CallBackLogRequest();
            callBackLogRequest.setpENTERPRISE_ID(enterpriseId);
            callBackLogRequest.setpWORKFLOW_ID(workFlowId);

//                String fileName = "abc"; // tạm thời
            commonRepository.postBack2(callBackLogRequest, isSetPosition, signerId, fileName, signingToken, pDMS_PROPERTY, signatureId, signerToken, signedTime, rsWFList, lastFileId, certChain, codeNumber, signingOption, uuid, fileSize, enterpriseId, digest, signedHash, signature, request);

            return new ResponseEntity<>(responseSign, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/signUsbToken2")
    public ResponseEntity<String> signUsbToken2(
            @RequestParam List<String> signatures,
            @RequestParam String certEncode,
            @RequestParam String cerId,
            @RequestParam String fileName,
            @RequestParam String signingToken,
            @RequestParam String signerToken,
            @RequestParam String connectorName,
            @RequestParam String serialNumber,
            @RequestParam String signingOption,
            @RequestParam String signatureId,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId,
            HttpServletRequest request) throws Exception {

        try {

            boolean error = false;


            WorkFlowList[][] rsWFList = new WorkFlowList[1][];
            connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, signingToken);
            String sResult = "0";

            // check workflow status
            if (rsWFList[0] == null || rsWFList[0].length == 0 || rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {
                error = true;
                sResult = "Signer Status invalid";// trạng thái không hợp lệ
                throw new Exception(sResult);
            }

            // check workflow participant
            Participants[][] rsParticipant = new Participants[1][];
            connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, signerToken);
            if (rsParticipant[0] == null || rsParticipant[0].length == 0 || rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {
                sResult = "The document has already been signed";
            }

            String sUUID_Last = "";
            int sFileID_Last = 0;
            LastFile[][] rsFile = new LastFile[1][];
            connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingToken);
            if (rsFile != null && rsFile[0].length > 0) {
                sFileID_Last = rsFile[0][0].getLAST_PPL_FILE_SIGNED_ID();
                sUUID_Last = rsFile[0][0].getLAST_PPL_FILE_UUID();
            }

            // download first file
            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            byte[] pdfSigned = commonRepository.packFile(certEncode,signatures,cerId);

            Date signTime = CommonFunction.getSigningTime(pdfSigned);

            Timestamp tsTimeSigned = null;

            if (signTime != null) {
                tsTimeSigned = new Timestamp(signTime.getTime());
            }
            String sType = Difinitions.CONFIG_PREFIX_UID_TOKEN;

            CallBackLogRequest callBackLogRequest = new CallBackLogRequest();
            callBackLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
            callBackLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));

            commonRepository.postBack(callBackLogRequest,rsParticipant,pdfSigned,fileName, signingToken,pDMS_PROPERTY,signatureId, signerToken,tsTimeSigned,rsWFList,sFileID_Last,certEncode,serialNumber,signingOption,sType,request);

            return new ResponseEntity<>("OK", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
