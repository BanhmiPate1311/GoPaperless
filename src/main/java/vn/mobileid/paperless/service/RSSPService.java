/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;
import vn.mobileid.paperless.API.*;
import vn.mobileid.paperless.Model.Certificate;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.MobileDisplayTemplate;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.Model.Request.DocumentDigests;
import vn.mobileid.paperless.Model.Response.BaseCertificateInfo;
import vn.mobileid.paperless.Model.smartId.request.SignRequest;
import vn.mobileid.paperless.aws.dto.CertResponse;
import vn.mobileid.paperless.config.RSSPConfig;
import vn.mobileid.paperless.fps.request.FpsSignRequest;
import vn.mobileid.paperless.fps.request.HashFileRequest;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.repository.CommonRepository;
import vn.mobileid.paperless.repository.RsspRepository;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.CommonHash;
import vn.mobileid.paperless.utils.Difinitions;

import javax.servlet.http.HttpServletRequest;

import static vn.mobileid.paperless.utils.CommonFunction.VoidCertificateComponents;

/**
 * @author Mr Spider
 */
@Service
public class RSSPService {

    public static Properties prop = new Properties();

    @Autowired
    private RsspRepository rsspRepository;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private process connect;

    @Autowired
    private GatewayAPI gatewayAPI;

    @Autowired
    private VCStoringService vcStoringService;

    public static IServerSession session;
    public static ICertificate crt;

    private Logger logger = LoggerFactory.getLogger(RSSPService.class);

    @Autowired
    private RSSPConfig rsspConfig;

    @Autowired
    private FpsService fpsService;

    @Value("${dev.mode}")
    private boolean devMode;

    public IServerSession handShake(String lang,
                                    String connectorName,
                                    String enterpriseId,
                                    String workFlowId) throws Exception {
        boolean codeEnable = true;
        String baseUrl = "";
        String relyingParty = "";
        String relyingPartyUser = "";
        String relyingPartyPassword = "";
        String relyingPartySignature = "";
        String relyingPartyKeyStoreValue = "";
        String relyingPartyKeyStorePassword = "";
        String[] sResultConnector = new String[2];
        String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
        String prefixCode = sResultConnector[1];
        ObjectMapper objectMapper = new ObjectMapper();
        CONNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sResultConnector[0], CONNECTOR_ATTRIBUTE.class);
        for (CONNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_URI)) {
                baseUrl = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_NAME)) {
                relyingParty = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_USERNAME)) {
                relyingPartyUser = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_PASSWORD)) {
                relyingPartyPassword = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_SIGNATURE)) {
                relyingPartySignature = attribute.getValue();
            }
            if (devMode) {
                relyingPartyKeyStoreValue = "D:/project/file/PAPERLESS.p12";
            } else if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_KEYSTORE_FILE_URL)) {
                relyingPartyKeyStoreValue = attribute.getValue();//
            }
            if (attribute.getName()
                    .equals(Difinitions.CONFIG_CONNECTOR_DMS_MOBILE_ID_KEYSTORE_PASSWORD)) {
                relyingPartyKeyStorePassword = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_WORKFLOW_VERIFICATION_CODE_ENABLED)) {
                codeEnable = Boolean.parseBoolean(attribute.getValue());
            }

        }
//        logger.info("session: " + session.toString());
        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));

        Property property = rsspConfig.loadRSSPConfig(baseUrl, relyingParty, relyingPartyUser, relyingPartyPassword,
                relyingPartySignature, relyingPartyKeyStoreValue, relyingPartyKeyStorePassword);
        if (session == null) {
            session = rsspRepository.Handshake_func(property, lang, connectorLogRequest);
            commonRepository.connectorLog(connectorLogRequest);
        }
        return session;
    }

    public Map<String, Object> getCertificate(
            String lang,
            String connectorName,
            String codeNumber,
            String enterpriseId,
            String workFlowId
    ) throws Exception {
        boolean codeEnable = true;
        String baseUrl = "";
        String relyingParty = "";
        String relyingPartyUser = "";
        String relyingPartyPassword = "";
        String relyingPartySignature = "";
        String relyingPartyKeyStoreValue = "";
        String relyingPartyKeyStorePassword = "";
        String[] sResultConnector = new String[2];
        System.out.println("connectorName: " + connectorName);
        String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
        String prefixCode = sResultConnector[1];
        ObjectMapper objectMapper = new ObjectMapper();
        CONNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sResultConnector[0], CONNECTOR_ATTRIBUTE.class);
        for (CONNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_URI)) {
                baseUrl = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_NAME)) {
                relyingParty = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_USERNAME)) {
                relyingPartyUser = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_PASSWORD)) {
                relyingPartyPassword = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_SIGNATURE)) {
                relyingPartySignature = attribute.getValue();
            }
            if (devMode) {
                if (connectorName.equals("SMART_ID_LCA")) {
                    relyingPartyKeyStoreValue = "D:/project/file/LCA_GOPAPERLESS.p12";
                } else {
                    relyingPartyKeyStoreValue = "D:/project/file/PAPERLESS.p12";
                }

            } else if (attribute.getName().equals(Difinitions.CONFIG_CONNECTOR_RSSP_MOBILE_ID_KEYSTORE_FILE_URL)) {
                relyingPartyKeyStoreValue = attribute.getValue();//
            }
            if (attribute.getName()
                    .equals(Difinitions.CONFIG_CONNECTOR_DMS_MOBILE_ID_KEYSTORE_PASSWORD)) {
                relyingPartyKeyStorePassword = attribute.getValue();
            }
            if (attribute.getName().equals(Difinitions.CONFIG_WORKFLOW_VERIFICATION_CODE_ENABLED)) {
                codeEnable = Boolean.parseBoolean(attribute.getValue());
            }

        }
//        logger.info("session: " + session.toString());
//        System.out.println("baseUrl: " + baseUrl);
//        System.out.println("relyingParty: " + relyingParty);
//        System.out.println("relyingPartyUser: " + relyingPartyUser);
//        System.out.println("relyingPartyPassword: " + relyingPartyPassword);
//        System.out.println("relyingPartyKeyStoreValue: " + relyingPartyKeyStoreValue);

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));

        Property property = rsspConfig.loadRSSPConfig(baseUrl, relyingParty, relyingPartyUser, relyingPartyPassword,
                relyingPartySignature, relyingPartyKeyStoreValue, relyingPartyKeyStorePassword);
//        if (session == null) {
//            session = rsspRepository.Handshake_func(property, lang, connectorLogRequest);
//            commonRepository.connectorLog(connectorLogRequest);
//        }
        session = rsspRepository.Handshake_func(property, lang, connectorLogRequest);
//        log.info("Login xong");
//        log.info("get certChain");
        Map<String, Object> response = new HashMap<>();
        logger.info("codeNumber: " + codeNumber);
        response.put("relyingParty", relyingParty);
        response.put("prefixCode", prefixCode);
        response.put("codeEnable", codeEnable);
        try {
            List<ICertificate> list = session.listCertificates(codeNumber, connectorLogRequest, lang);
            List<Certificate> listCert = new ArrayList<>();
            List<CertResponse> listCertificate = new ArrayList<>();
            for (ICertificate iCertificate : list) {
                if (iCertificate instanceof Certificate) {
                    Certificate certificate = (Certificate) iCertificate;
                    listCert.add(certificate);
                } else {
                    // Xử lý tương ứng nếu đối tượng không phải là lớp Certificate
                }
            }

            commonRepository.connectorLog(connectorLogRequest);
            if (listCert.size() > 0) {
                for (Certificate cert : listCert) {
                    String credentialID = cert.baseCredentialInfo().getCredentialID();
                    crt = session.certificateInfo(credentialID, connectorLogRequest, lang);
                    if (crt != null) {
                        String authMode = crt.credentialInfo().getAuthMode().toString();
                        String status = crt.baseCredentialInfo().getStatus();
                        logger.info("authMode: " + authMode);
                        logger.info("status: " + status);

                        if ("IMPLICIT_TSE".equals(authMode) && "OPERATED".equals(status)) {
//                        String credentialID = cert.baseCredentialInfo().getCredentialID();
//                        crt = session.certificateInfo(credentialID, connectorLogRequest, lang);
                            BaseCertificateInfo info = crt.baseCredentialInfo();
                            String certChain = info.getCertificates()[info.getCertificates().length - 1];

                            Object[] info1 = new Object[3];
                            String[] time = new String[2];
                            int[] intRes = new int[1];

                            VoidCertificateComponents(certChain, info1, time, intRes);
                            if (intRes[0] == 0) {
                                CertResponse certResponse = new CertResponse();
                                certResponse.setSubject(CommonFunction.getCommonnameInDN(info1[0].toString()));
                                certResponse.setIssuer(CommonFunction.getCommonnameInDN(info1[1].toString()));
                                certResponse.setValidFrom(time[0]);
                                certResponse.setValidTo(time[1]);
                                certResponse.setCert(certChain);
                                certResponse.setCredentialID(credentialID);
//                        certResponse.setCodeNumber(codeNumber);
                                certResponse.setRelyingParty(relyingParty);
                                certResponse.setPrefixCode(prefixCode);
                                certResponse.setCodeEnable(codeEnable);
                                listCertificate.add(certResponse);
                            }
                        }
                    }
                }
//                if(listCertificate.size() > 0){
//                    response.put("listCertificate", listCertificate);
//                } else {
//                    throw new Exception("not available");
//                }
                response.put("listCertificate", listCertificate);
            }
            commonRepository.connectorLog(connectorLogRequest);
        } catch (Exception e) {
//            log.error(e.getMessage());
            commonRepository.connectorLog(connectorLogRequest);
            throw new Exception(e.getMessage());
        }
        return response;
    }

    public String signFile(
            SignRequest signRequest,
            HttpServletRequest request) throws Throwable {
        System.out.println("field_name: " + signRequest.getFieldName());
        String field_name = signRequest.getFieldName();
        String connectorName = signRequest.getConnectorName();
        int enterpriseId = signRequest.getEnterpriseId();
        int workFlowId = signRequest.getWorkFlowId();
        String signingToken = signRequest.getSigningToken();
        String signerToken = signRequest.getSignerToken();
        String lang = signRequest.getLang();
        String codeNumber = signRequest.getCodeNumber();
        String relyingParty = signRequest.getRelyingParty();
        String prefixCode = signRequest.getPrefixCode();
        boolean codeEnable = signRequest.isCodeEnable();
        String credentialID = signRequest.getCredentialID();
        String signingOption = signRequest.getSigningOption();
        String signerId = signRequest.getSignerId();
        String certChain = signRequest.getCertChain();
        String fileName = signRequest.getFileName();
        String requestID = signRequest.getRequestID();
        int lastFileId = signRequest.getLastFileId();

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
        connectorLogRequest.setpENTERPRISE_ID(enterpriseId);
        connectorLogRequest.setpWORKFLOW_ID(workFlowId);
        try {
            System.out.println("connectorName: " + connectorName);
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

//            String sUUID_Last = "";
//            int sFileID_Last = 0;
//            LastFile[][] rsFile = new LastFile[1][];
//            connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingToken);
//            if (rsFile[0].length > 0) {
//                sFileID_Last = rsFile[0][0].getLAST_PPL_FILE_SIGNED_ID();
//                sUUID_Last = rsFile[0][0].getLAST_PPL_FILE_UUID();
//            }

            // download first file
            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            long millis = System.currentTimeMillis();
            String sSignatureHash = signerToken + millis;
//            String sSignature_id = prefixCode + "-" + CommonHash.hashPass(sSignatureHash.getBytes());
            String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();

            System.out.println("credentialID: " + credentialID);

//            List<String> hashList = commonRepository.createHashList(signerToken, signingToken, certChain, credentialID, "", sSignature_id);

//            Gson gson = new GsonBuilder().create();
//            String json = gson.toJson(hashList);
//            System.out.println("json ne: " + json);

            // get user-agent
            String userAgent = request.getHeader("User-Agent");
            Parser parser = new Parser();
            Client c = parser.parse(userAgent);
            // set app interface
            String rpName = "{\"OPERATING SYSTEM\":\"" + c.os.family + " " + c.os.major + "\",\"BROWSER\":\"" + c.userAgent.family + " " + c.userAgent.major + "\",\"RP NAME\":\"" + relyingParty + "\"}";

            String fileType2 = fileName.substring(fileName.lastIndexOf(".") + 1);
            String message = " {\"FILE NAME\":\"" + fileName + "\", \"FILE TYPE\":\"" + fileType2 + "\"}";

            MobileDisplayTemplate template = new MobileDisplayTemplate();
            template.setScaIdentity("PAPERLESS GATEWAY");
            template.setMessageCaption("DOCUMENT SIGNING");
            template.setNotificationMessage("PAPERLESS GATEWAY ACTIVITES");
            template.setMessage(message);
            template.setRpName(rpName);
            template.setVcEnabled(codeEnable);
            template.setAcEnabled(true);

            String hashList = "";

            if (field_name == null || field_name.isEmpty()) {
                System.out.println("kiem tra:");
//                PrepareSigningRequest prepareSigningRequest = new PrepareSigningRequest(
//                        signingToken,
//                        signerToken,
//                        signingOption,
//                        signerId,
//                        certChain,
//                        connectorName,
//                        null
//                );
//                hashList = gatewayAPI.PrepareSign(prepareSigningRequest);
                hashList = commonRepository.createHashList(signerToken, signingToken, certChain, credentialID, "", sSignature_id);
            } else {
                HashFileRequest hashFileRequest = commonRepository.getMetaData(signRequest.getSignerToken(), "meta");
                List<String> listCertChain = new ArrayList<>();
                listCertChain.add(certChain);
                hashFileRequest.setCertificateChain(listCertChain);
                hashFileRequest.setFieldName(signRequest.getFieldName());

                hashList = fpsService.hashSignatureField(signRequest.getDocumentId(), hashFileRequest);

            }

            System.out.println("hashList: " + hashList);
            System.out.println("signingOption :" + signingOption);


            HashAlgorithmOID hashAlgo = HashAlgorithmOID.SHA_256;
            DocumentDigests doc = new DocumentDigests();
            doc.hashAlgorithmOID = hashAlgo;
            doc.hashes = new ArrayList<>();
            doc.hashes.add(Utils.base64Decode(hashList));

            if (codeEnable) {
                List<byte[]> list = new ArrayList<>();
                list.add(Base64.getMimeDecoder().decode(hashList));
                String codeVC = CommonFunction.computeVC(list);
                vcStoringService.store(requestID, codeVC);
            }


            String sad = crt.authorize(connectorLogRequest, lang, credentialID, 1, doc, null, template);

//            commonRepository.connectorLog(connectorLogRequest);

            SignAlgo signAlgo = SignAlgo.RSA;
            List<byte[]> signatures = crt.signHash(connectorLogRequest, lang, credentialID, doc, signAlgo, sad);
            String signature = Base64.getEncoder().encodeToString(signatures.get(0));
            System.out.println("kiem tra signature: " + signature);

            if (field_name == null || field_name.isEmpty()) {
                return gatewayAPI.sign(signingToken, signerToken, signerId, signature);
            } else {
                int isSetPosition = 1;
                FpsSignRequest fpsSignRequest = new FpsSignRequest();
                fpsSignRequest.setFieldName(signRequest.getFieldName());
                fpsSignRequest.setHashValue(hashList);
                fpsSignRequest.setSignatureValue(signature);

                List<String> listCertChain = new ArrayList<>();
                listCertChain.add(certChain);
                fpsSignRequest.setCertificateChain(listCertChain);

                System.out.println("kiem tra progress: ");

                String responseSign = fpsService.signDocument(signRequest.getDocumentId(), fpsSignRequest);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseSign);
                String uuid = jsonNode.get("uuid").asText();
                int fileSize = jsonNode.get("file_size").asInt();
                String digest = jsonNode.get("digest").asText();
                String signedHash = jsonNode.get("signed_hash").asText();
                String signedTime = jsonNode.get("signed_time").asText();

                CallBackLogRequest callBackLogRequest = new CallBackLogRequest();
                callBackLogRequest.setpENTERPRISE_ID(enterpriseId);
                callBackLogRequest.setpWORKFLOW_ID(workFlowId);

//                String fileName = "abc"; // tạm thời
                commonRepository.postBack2(callBackLogRequest, isSetPosition, signerId, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, signedTime, rsWFList, lastFileId, certChain, codeNumber, signingOption, uuid, fileSize, enterpriseId, digest, signedHash, signature, lastFileId, request);
                return responseSign;
            }


//            commonRepository.connectorLog(connectorLogRequest);
//
//            byte[] pdfSigned = null;
//
//            for (byte[] s : signatures) {
//                List<String> signature = new ArrayList<>();
//                signature.add(Utils.base64Encode(s));
//
//                pdfSigned = commonRepository.packFile(certChain, signature, credentialID);
//            }
//
//            Date signTime = CommonFunction.getSigningTime(pdfSigned);
//            Timestamp tsTimeSigned = null;
//
//            if (signTime != null) {
//                tsTimeSigned = new Timestamp(signTime.getTime());
////                System.out.println("tsTimeSigned: " + tsTimeSigned);
//            }
//
//            CallBackLogRequest callBackLogRequest = new CallBackLogRequest();
//            callBackLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
//            callBackLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));
//
//            commonRepository.postBack(callBackLogRequest, rsParticipant, pdfSigned, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, tsTimeSigned, rsWFList, sFileID_Last, certChain, codeNumber, signingOption, sType, request);

//            return "OK";
        } catch (Exception e) {
            commonRepository.connectorLog(connectorLogRequest);
            throw new Exception(e.getMessage());
        } finally {
            vcStoringService.remove(requestID);
        }


    }

    public String signFileFps(
            SignRequest signRequest,
            HttpServletRequest request) throws Throwable {
        String field_name = signRequest.getFieldName();
        String connectorName = signRequest.getConnectorName();
        int enterpriseId = signRequest.getEnterpriseId();
        int workFlowId = signRequest.getWorkFlowId();
        String signingToken = signRequest.getSigningToken();
        String signerToken = signRequest.getSignerToken();
        String lang = signRequest.getLang();
        String codeNumber = signRequest.getCodeNumber();
        String relyingParty = signRequest.getRelyingParty();
        String prefixCode = signRequest.getPrefixCode();
        boolean codeEnable = signRequest.isCodeEnable();
        String credentialID = signRequest.getCredentialID();
        String signingOption = signRequest.getSigningOption();
        String signerId = signRequest.getSignerId();
        String certChain = signRequest.getCertChain();
        String fileName = signRequest.getFileName();
        String requestID = signRequest.getRequestID();
        int lastFileId = signRequest.getLastFileId();
        int documentId = signRequest.getDocumentId();

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
        connectorLogRequest.setpENTERPRISE_ID(enterpriseId);
        connectorLogRequest.setpWORKFLOW_ID(workFlowId);
        try {
            System.out.println("connectorName: " + connectorName);
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
                return sResult = "The document has already been signed";
            }

            String meta = rsParticipant[0][0].META_INFORMATION;
            JsonObject jsonObject = new Gson().fromJson(meta, JsonObject.class);

            int isSetPosition = 0;
            if (jsonObject != null && jsonObject.has("pdf")) {
                JsonObject pdfObject = jsonObject.getAsJsonObject("pdf");

                JsonElement annotationElement = pdfObject.get("annotation");
                if (annotationElement != null) {
                    JsonObject annotationObject = annotationElement.getAsJsonObject();
                    if (annotationObject.has("top") && annotationObject.has("left")) {
                        isSetPosition = 1;
                    }
                }
            }

            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            long millis = System.currentTimeMillis();
            String sSignatureHash = signerToken + millis;
            String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();

            String documentDetails = fpsService.getDocumentDetails(documentId);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(documentDetails);
            JsonNode documentNode = jsonNode.get(0).get("document_height");

//            System.out.println("documentDetails: " + jsonNode.get(0).get("document_height").asInt());

            int pageHeight = 0;
            int pageWidth = 0;
            if (documentNode != null) {
                pageHeight = documentNode.get("document_height").asInt();
                pageWidth = documentNode.get("document_width").asInt();
            }

            // get user-agent
            String userAgent = request.getHeader("User-Agent");
            Parser parser = new Parser();
            Client c = parser.parse(userAgent);
            // set app interface
            String rpName = "{\"OPERATING SYSTEM\":\"" + c.os.family + " " + c.os.major + "\",\"BROWSER\":\"" + c.userAgent.family + " " + c.userAgent.major + "\",\"RP NAME\":\"" + relyingParty + "\"}";

            String fileType2 = fileName.substring(fileName.lastIndexOf(".") + 1);
            String message = " {\"FILE NAME\":\"" + fileName + "\", \"FILE TYPE\":\"" + fileType2 + "\"}";

            MobileDisplayTemplate template = new MobileDisplayTemplate();
            template.setScaIdentity("PAPERLESS GATEWAY");
            template.setMessageCaption("DOCUMENT SIGNING");
            template.setNotificationMessage("PAPERLESS GATEWAY ACTIVITES");
            template.setMessage(message);
            template.setRpName(rpName);
            template.setVcEnabled(codeEnable);
            template.setAcEnabled(true);

            String hashList = "";

            if (field_name == null || field_name.isEmpty()) {
                System.out.println("kiem tra:");
                hashList = commonRepository.addSign(pageHeight, pageWidth, signingToken, certChain, credentialID, "", sSignature_id, meta, documentId, signerToken);
            } else {
                HashFileRequest hashFileRequest = commonRepository.getMetaData(signRequest.getSignerToken(), meta);
                List<String> listCertChain = new ArrayList<>();
                listCertChain.add(certChain);
                hashFileRequest.setCertificateChain(listCertChain);
                hashFileRequest.setFieldName(signRequest.getFieldName());

                hashList = fpsService.hashSignatureField(signRequest.getDocumentId(), hashFileRequest);
            }

            HashAlgorithmOID hashAlgo = HashAlgorithmOID.SHA_256;
            DocumentDigests doc = new DocumentDigests();
            doc.hashAlgorithmOID = hashAlgo;
            doc.hashes = new ArrayList<>();
            doc.hashes.add(Utils.base64Decode(hashList));

            if (codeEnable) {
                List<byte[]> list = new ArrayList<>();
                list.add(Base64.getMimeDecoder().decode(hashList));
                String codeVC = CommonFunction.computeVC(list);
                vcStoringService.store(requestID, codeVC);
            }


            String sad = crt.authorize(connectorLogRequest, lang, credentialID, 1, doc, null, template);

//            commonRepository.connectorLog(connectorLogRequest);

            SignAlgo signAlgo = SignAlgo.RSA;
            List<byte[]> signatures = crt.signHash(connectorLogRequest, lang, credentialID, doc, signAlgo, sad);
            String signature = Base64.getEncoder().encodeToString(signatures.get(0));
            System.out.println("kiem tra signature: " + signature);

            if (field_name == null || field_name.isEmpty()) {
                return gatewayAPI.sign(signingToken, signerToken, signerId, signature);
            } else {
                isSetPosition = 1;
                FpsSignRequest fpsSignRequest = new FpsSignRequest();
                fpsSignRequest.setFieldName(signRequest.getFieldName());
                fpsSignRequest.setHashValue(hashList);
                fpsSignRequest.setSignatureValue(signature);

                List<String> listCertChain = new ArrayList<>();
                listCertChain.add(certChain);
                fpsSignRequest.setCertificateChain(listCertChain);

                System.out.println("kiem tra progress: ");

                String responseSign = fpsService.signDocument(signRequest.getDocumentId(), fpsSignRequest);

//                ObjectMapper objectMapper = new ObjectMapper();
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
                commonRepository.postBack2(callBackLogRequest, isSetPosition, signerId, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, signedTime, rsWFList, lastFileId, certChain, codeNumber, signingOption, uuid, fileSize, enterpriseId, digest, signedHash, signature, lastFileId, request);
                return responseSign;
            }

        } catch (Exception e) {
            commonRepository.connectorLog(connectorLogRequest);
            throw new Exception(e.getMessage());
        } finally {
            vcStoringService.remove(requestID);
        }


    }
}
