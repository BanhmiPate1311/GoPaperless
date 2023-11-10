package vn.mobileid.paperless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import vn.mobileid.fms.client.JCRFile;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.API.HttpResponse;
import vn.mobileid.paperless.API.HttpUtils;
import vn.mobileid.paperless.API.Utils;
import vn.mobileid.paperless.Model.APIException;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.Model.smartId.request.VtCASignHashRequest;
import vn.mobileid.paperless.fps.request.FpsSignRequest;
import vn.mobileid.paperless.fps.request.HashFileRequest;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.repository.CommonRepository;
import vn.mobileid.paperless.repository.RsspRepository;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.CommonHash;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;
import vn.mobileid.paperless.viettelca.VariableLocal;
import vn.mobileid.paperless.viettelca.VtSession;
import vn.mobileid.paperless.viettelca.response.CertDetail;
import vn.mobileid.paperless.viettelca.response.VTSignResponse;
import vn.mobileid.paperless.viettelca.response.ViettelLoginResponse;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Service
public class ViettelCAService {

    @Autowired
    private RSSPService rsspService;

    @Autowired
    private VtSession vtSession;

    @Autowired
    private process connect;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private GatewayAPI gatewayAPI;

    @Autowired
    private FpsService fpsService;

    public String login(String userId, String connectorName, ConnectorLogRequest connectorLogRequest) throws Exception {

        String sPropertiesFMS = "";
        String clientId = "";
        String baseURL = "";
        String clientSecret = "";
        String profileId = "";
        ArrayList<ConnectorName> connector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        if (connector.size() > 0) {
            for (int m = 0; m < connector.size(); m++) {
                if (connector.get(m).CONNECTOR_NAME.equals(connectorName)) {
                    sPropertiesFMS = connector.get(m).IDENTIFIER;
                }
            }
            JsonObject jsonObject = new JsonParser().parse(sPropertiesFMS).getAsJsonObject();
            JsonArray attributes = jsonObject.getAsJsonArray("attributes");
//            for (int j = 0; j < arr.size(); j++) {
//                dllUSBToken = arr.get(j).getAsJsonObject().get("value").toString();
//            }
            for (JsonElement att : attributes) {
                JsonObject annotationObject = att.getAsJsonObject();
                if (annotationObject.get("name").getAsString().equals("URI")) {
                    baseURL = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("CLIENT_ID")) {
                    clientId = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("CLIENT_SECRET")) {
                    clientSecret = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("PROFILE_ID")) {
                    profileId = annotationObject.get("value").getAsString();
                }
            }
        }
        try {
            String accessToken = vtSession.login(baseURL, clientId, userId, clientSecret, profileId, connectorLogRequest);

            commonRepository.connectorLog(connectorLogRequest);
            return accessToken;
        } catch (Exception e) {
            System.out.println("Kiem tra lỗi đăng nhập");
            commonRepository.connectorLog(connectorLogRequest);
            throw new Exception(e.getMessage());
        }

    }

    public CertDetail[] getCertificate(String accessToken, ConnectorLogRequest connectorLogRequest) throws Exception {
        CertDetail[] response = vtSession.getCertificate(accessToken, connectorLogRequest);
        commonRepository.connectorLog(connectorLogRequest);
        return response;
    }

    public String signHash(
            VtCASignHashRequest vtCASignHashRequest,
            HttpServletRequest request) throws Exception {
        try {
            String field_name = vtCASignHashRequest.getFieldName();
            String connectorName = vtCASignHashRequest.getConnectorName();
            int enterpriseId = vtCASignHashRequest.getEnterpriseId();
            int workFlowId = vtCASignHashRequest.getWorkFlowId();
            String signingToken = vtCASignHashRequest.getSigningToken();
            String signerToken = vtCASignHashRequest.getSignerToken();
            String codeNumber = vtCASignHashRequest.getCodeNumber();
            String credentialID = vtCASignHashRequest.getCredentialID();
            String signingOption = vtCASignHashRequest.getSigningOption();
            String signerId = vtCASignHashRequest.getSignerId();
            String certChain = vtCASignHashRequest.getCertChain();
            String fileName = vtCASignHashRequest.getFileName();
            String accessToken = vtCASignHashRequest.getAccessToken();
            int documentId = vtCASignHashRequest.getDocumentId();

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


            // download first file
            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            String meta = rsParticipant[0][0].META_INFORMATION;
            int isSetPosition = CommonFunction.checkIsSetPosition(field_name, meta);

            String document_name = rsWFList[0][0].WORKFLOW_DOCUMENT_NAME;

            String[] sResultConnector = new String[2];
            String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
            String prefixCode = sResultConnector[1];

            long millis = System.currentTimeMillis();
            String sSignatureHash = signerToken + millis;
//            String sSignature_id = prefixCode + "-" + CommonHash.hashPass(sSignatureHash.getBytes());
            String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();
//            System.out.println("sSignature_id: " + sSignature_id);

//            List<String> hashList = commonRepository.createHashList(signerToken, signingToken, certChain, credentialID, "", sSignature_id);
            PrepareSigningRequest prepareSigningRequest = new PrepareSigningRequest(
                    signingToken,
                    signerToken,
                    signingOption,
                    signerId,
                    certChain,
                    connectorName,
                    null
            );
            String hashList = gatewayAPI.PrepareSign(prepareSigningRequest);

            String docbasse64 = CommonFunction.convertBase64(document_name);

            List<Map<String, Object>> documents = new ArrayList<>();
            Map<String, Object> docDetail = new HashMap<>();
            docDetail.put("document_id", documentId);
            docDetail.put("document_name", docbasse64);

            documents.add(docDetail);

            List<String> hashRequest = new ArrayList<>();
            hashRequest.add(hashList);

            int async = 0;

            ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
            connectorLogRequest.setpCONNECTOR_NAME(connectorName);
            connectorLogRequest.setpENTERPRISE_ID(enterpriseId);
            connectorLogRequest.setpWORKFLOW_ID(workFlowId);

            List<String> signatures = vtSession.signHash(credentialID, documents, hashRequest, accessToken, async, connectorLogRequest);

//            String signature = Base64.getEncoder().encodeToString(signatures.get(0));
            String signature = signatures.get(0);
            System.out.println("kiem tra lần 2: ");
            String result = gatewayAPI.sign(signingToken, signerToken, signerId, signature);

//            commonRepository.connectorLog(connectorLogRequest);
//
//            byte[] pdfSigned = commonRepository.packFile(certChain, signatures, credentialID);
//
//            Date signTime = CommonFunction.getSigningTime(pdfSigned);
////            System.out.println("signTime: " + signTime);
//
//            Timestamp tsTimeSigned = null;
//
//            if (signTime != null) {
//                tsTimeSigned = new Timestamp(signTime.getTime());
////                System.out.println("tsTimeSigned: " + tsTimeSigned);
//            }
//            String sType = Difinitions.CONFIG_PREFIX_UID_USER_ID;
//
//            CallBackLogRequest callBackLogRequest = new CallBackLogRequest();
//            callBackLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
//            callBackLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));
//            commonRepository.postBack(callBackLogRequest, rsParticipant, pdfSigned, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, tsTimeSigned, rsWFList, sFileID_Last, certChain, serialNumber, signingOption, sType, request);
            return result;
        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception(e.getMessage());
        }
    }

    public String signHashFps(
            VtCASignHashRequest vtCASignHashRequest,
            HttpServletRequest request) throws Exception {
        try {
            String field_name = vtCASignHashRequest.getFieldName();
            String connectorName = vtCASignHashRequest.getConnectorName();
            int enterpriseId = vtCASignHashRequest.getEnterpriseId();
            int workFlowId = vtCASignHashRequest.getWorkFlowId();
            String signingToken = vtCASignHashRequest.getSigningToken();
            String signerToken = vtCASignHashRequest.getSignerToken();
            String codeNumber = vtCASignHashRequest.getCodeNumber();
            String credentialID = vtCASignHashRequest.getCredentialID();
            String signingOption = vtCASignHashRequest.getSigningOption();
            String signerId = vtCASignHashRequest.getSignerId();
            String certChain = vtCASignHashRequest.getCertChain();
            String fileName = vtCASignHashRequest.getFileName();
            String accessToken = vtCASignHashRequest.getAccessToken();
            int documentId = vtCASignHashRequest.getDocumentId();
            int lastFileId = vtCASignHashRequest.getLastFileId();

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


            // download first file
            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            String meta = rsParticipant[0][0].META_INFORMATION;
            int isSetPosition = CommonFunction.checkIsSetPosition(field_name, meta);

            String document_name = rsWFList[0][0].WORKFLOW_DOCUMENT_NAME;

            String[] sResultConnector = new String[2];
            String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
            String prefixCode = sResultConnector[1];

            long millis = System.currentTimeMillis();
            String sSignatureHash = signerToken + millis;
            String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();

            String documentDetails = fpsService.getDocumentDetails(documentId);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(documentDetails);
            JsonNode documentNode = jsonNode.get(0);

//            System.out.println("documentDetails: " + jsonNode.get(0).get("document_height").asInt());
            int pageHeight = 0;
            int pageWidth = 0;
            if (documentNode != null) {
                pageHeight = documentNode.get("document_height").asInt();
                pageWidth = documentNode.get("document_width").asInt();
            }

            List<String> listCertChain = new ArrayList<>();
            listCertChain.add(certChain);

            HashFileRequest hashFileRequest = commonRepository.getMetaData(signerToken, meta);
            hashFileRequest.setCertificateChain(listCertChain);

            if (field_name == null || field_name.isEmpty()) {
                System.out.println("kiem tra:");
                commonRepository.addSign(pageHeight, pageWidth, signingToken, signerId, meta, documentId);
            }
            System.out.println("kiem tra1:");
            hashFileRequest.setFieldName(!field_name.isEmpty() ? field_name : signerId);
            String hashList = fpsService.hashSignatureField(documentId, hashFileRequest);

//            PrepareSigningRequest prepareSigningRequest = new PrepareSigningRequest(
//                    signingToken,
//                    signerToken,
//                    signingOption,
//                    signerId,
//                    certChain,
//                    connectorName,
//                    null
//            );
//            String hashList = gatewayAPI.PrepareSign(prepareSigningRequest);

            String docbasse64 = CommonFunction.convertBase64(document_name);

            List<Map<String, Object>> documents = new ArrayList<>();
            Map<String, Object> docDetail = new HashMap<>();
            docDetail.put("document_id", documentId);
            docDetail.put("document_name", docbasse64);

            documents.add(docDetail);

            List<String> hashRequest = new ArrayList<>();
            hashRequest.add(hashList);

            int async = 0;

            ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
            connectorLogRequest.setpCONNECTOR_NAME(connectorName);
            connectorLogRequest.setpENTERPRISE_ID(enterpriseId);
            connectorLogRequest.setpWORKFLOW_ID(workFlowId);

            List<String> signatures = vtSession.signHash(credentialID, documents, hashRequest, accessToken, async, connectorLogRequest);

//            String signature = Base64.getEncoder().encodeToString(signatures.get(0));
            String signature = signatures.get(0);
            System.out.println("kiem tra lần 2: ");

            FpsSignRequest fpsSignRequest = new FpsSignRequest();
            fpsSignRequest.setFieldName(!field_name.isEmpty() ? field_name : signerId);
            fpsSignRequest.setHashValue(hashList);
            fpsSignRequest.setSignatureValue(signature);

//                List<String> listCertChain = new ArrayList<>();
//                listCertChain.add(certChain);
            fpsSignRequest.setCertificateChain(listCertChain);

            System.out.println("kiem tra progress: ");

            String responseSign = fpsService.signDocument(documentId, fpsSignRequest);

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
            commonRepository.postBack2(callBackLogRequest, isSetPosition, signerId, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, signedTime, rsWFList, lastFileId, certChain, codeNumber, signingOption, uuid, fileSize, enterpriseId, digest, signedHash, signature, request);
            return responseSign;
        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception(e.getMessage());
        }
    }
}
