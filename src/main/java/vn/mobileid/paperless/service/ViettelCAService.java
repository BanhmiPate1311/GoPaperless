package vn.mobileid.paperless.service;

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
            String credentialID,
            String signingToken,
            String signerToken,
            String signerId,
            String certChain,
            String connectorName,
            String accessToken,
            String fileName,
            String serialNumber,
            String signingOption,
            HttpServletRequest request,
            @RequestParam String enterpriseId,
            @RequestParam String workFlowId) throws Exception {
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
            if (rsFile[0].length > 0) {
                sFileID_Last = rsFile[0][0].getLAST_PPL_FILE_SIGNED_ID();
                sUUID_Last = rsFile[0][0].getLAST_PPL_FILE_UUID();
            }

            // download first file
            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();

            String document_name = rsWFList[0][0].WORKFLOW_DOCUMENT_NAME;

            int document_id = rsFile[0][0].getLAST_PPL_FILE_SIGNED_ID();

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
            docDetail.put("document_id", document_id);
            docDetail.put("document_name", docbasse64);

            documents.add(docDetail);

            List<String> hashRequest = new ArrayList<>();
            hashRequest.add(hashList);

            int async = 0;

            ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
            connectorLogRequest.setpCONNECTOR_NAME(connectorName);
            connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
            connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));

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
}
