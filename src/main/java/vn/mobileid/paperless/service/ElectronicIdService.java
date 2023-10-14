package vn.mobileid.paperless.service;

import com.google.gson.*;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.API.ICertificate;
import vn.mobileid.paperless.API.IServerSession;
import vn.mobileid.paperless.API.Utils;
import vn.mobileid.paperless.Model.Certificate;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.Model.Request.DocumentDigests;
import vn.mobileid.paperless.Model.Response.BaseCertificateInfo;
import vn.mobileid.paperless.aws.datatypes.JwtModel;
import vn.mobileid.paperless.aws.datatypes.PadesConstants;
import vn.mobileid.paperless.aws.dto.CertResponse;
import vn.mobileid.paperless.aws.request.*;
import vn.mobileid.paperless.aws.response.PerformResponse;
import vn.mobileid.paperless.aws.response.SubjectResponse;
import vn.mobileid.paperless.controller.ViettelCAController;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.repository.CommonRepository;
import vn.mobileid.paperless.repository.ElectronicRepository;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

import static vn.mobileid.paperless.utils.CommonFunction.VoidCertificateComponents;

@Service
public class ElectronicIdService {

    @Autowired
    private ElectronicRepository electronicRepository;

    @Autowired
    private RSSPService rsspService;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private process connect;

    @Autowired
    private GatewayAPI gatewayAPI;

    private Logger logger = LoggerFactory.getLogger(ViettelCAController.class);

    public static IServerSession session;
    public static ICertificate crt;

    public String checkPersonalCode(String lang, String code, String type, String connectorName) throws IOException {

        String sPropertiesFMS = "";
        ArrayList<ConnectorName> connector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONECTOR_NAME);
        if (connector.size() > 0) {
            for (int m = 0; m < connector.size(); m++) {
                if (connector.get(m).CONNECTOR_NAME.equals(connectorName)) {
                    sPropertiesFMS = connector.get(m).IDENTIFIER;
                }
            }
            JsonObject jsonObject = new JsonParser().parse(sPropertiesFMS).getAsJsonObject();
            JsonArray attributes = jsonObject.getAsJsonArray("attributes");

            for (JsonElement att : attributes) {
                JsonObject annotationObject = att.getAsJsonObject();
                if (annotationObject.get("name").getAsString().equals("URI")) {
                    PadesConstants.BASE_URL = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("ACCESSKEY")) {
                    PadesConstants.ACCESSKEY = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("SECRETKEY")) {
                    PadesConstants.SECRETKEY = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("REGIONNAME")) {
                    PadesConstants.REGIONNAME = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("SERVICENAME")) {
                    PadesConstants.SERVICENAME = annotationObject.get("value").getAsString();
                }
                if (annotationObject.get("name").getAsString().equals("XAPIKEY")) {
                    PadesConstants.XAPIKEY = annotationObject.get("value").getAsString();
                }
            }
        }

        String token = electronicRepository.getToken();
        return token;
    }

    public SubjectResponse checkExisted(CheckIdentityRequest checkIdentityRequest) throws IOException {
        SubjectResponse response = electronicRepository.getSubject(checkIdentityRequest);
        return response;
    }

    public PerformResponse faceAndCreate(FaceAndCreateRequest faceAndCreateRequest) throws Exception {
        PerformResponse response = electronicRepository.faceAndCreate(faceAndCreateRequest);
        return response;
    }

    public String updateSubject(UpdateSubjectRequest updateSubjectRequest) throws Exception {
        String process_type = "MOBILE_AUTHENTICATION";
        String purpose = "AMEND";
        if (updateSubjectRequest.getPhoneNumber() != null && !updateSubjectRequest.getPhoneNumber().equals("null")) {
            process_type = "MOBILE_AUTHENTICATION";
            purpose = "AMEND";
        }

        if (updateSubjectRequest.getEmail() != null && !updateSubjectRequest.getEmail().equals("null")) {
            process_type = "EMAIL_AUTHENTICATION";
            purpose = "AMEND";
        }
        String response = electronicRepository.createProcess(updateSubjectRequest.getLang(), updateSubjectRequest.getPhoneNumber(), updateSubjectRequest.getEmail(), updateSubjectRequest.getSubject_id(), updateSubjectRequest.getJwt(), process_type, purpose);
        return response;
    }

    public PerformResponse processPerForm(ProcessPerFormRequest processPerFormRequest) throws Exception {
        PerformResponse response = electronicRepository.processPerForm(processPerFormRequest.getLang(), null, null, processPerFormRequest.getOtp(), processPerFormRequest.getSubject_id(), processPerFormRequest.getProcess_id(), null);
        return response;
    }

    public String processOTPResend(ProcessPerFormRequest processPerFormRequest) throws Exception {
        String response = electronicRepository.processOTPResend(processPerFormRequest.getLang(), processPerFormRequest.getJwt(), processPerFormRequest.getSubject_id(), processPerFormRequest.getProcess_id());
        return response;
    }

    public List<CertResponse> checkCertificate(CheckCertificateRequest checkCertificateRequest) throws Exception {
        SignedJWT jwt1 = (SignedJWT) JWTParser.parse(checkCertificateRequest.getJwt());
        Gson gson = new Gson();
        JwtModel jwtModel = gson.fromJson(jwt1.getPayload().toString(), JwtModel.class);


        logger.info("getNationality: " + jwtModel.getNationality());
        logger.info("getCity_province: " + jwtModel.getCity_province());

        if (jwtModel.getDocument_type().equals("CITIZENCARD")) {
            jwtModel.setDocument_type("CITIZEN-IDENTITY-CARD");
        }
//        String mobile = jwtModel.getPhone_number().replace("84", "0");
//        jwtModel.setPhone_number(mobile);

        logger.info("connectorName: " + checkCertificateRequest.getConnectorNameRSSP());
//        session = rsspService.handShake(lang, connectorNameRSSP, enterpriseId, workFlowId);
//        if (session == null) {
//            session = rsspService.handShake(checkCertificateRequest.getLang(), checkCertificateRequest.getConnectorNameRSSP(), checkCertificateRequest.getEnterpriseId(), checkCertificateRequest.getWorkFlowId());
////            commonRepository.connectorLog(connectorLogRequest);
//        }
        session = rsspService.handShake(checkCertificateRequest.getLang(), checkCertificateRequest.getConnectorNameRSSP(), checkCertificateRequest.getEnterpriseId(), checkCertificateRequest.getWorkFlowId());

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(checkCertificateRequest.getConnectorName());
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(checkCertificateRequest.getEnterpriseId()));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(checkCertificateRequest.getWorkFlowId()));

        session.ownerCreate(connectorLogRequest, jwtModel, checkCertificateRequest.getLang());
        List<CertResponse> listCertificate = new ArrayList<>();
        try {
            List<ICertificate> list = session.listCertificates(jwtModel.getDocument_type() + ":" + jwtModel.getDocument_number(), connectorLogRequest, checkCertificateRequest.getLang());
            List<Certificate> listCert = new ArrayList<>();

            for (ICertificate iCertificate : list) {
                if (iCertificate instanceof Certificate) {
                    Certificate certificate = (Certificate) iCertificate;
                    listCert.add(certificate);
                } else {
                    // Xử lý tương ứng nếu đối tượng không phải là lớp Certificate
                }
            }
            if (listCert.size() > 0) {
                for (Certificate cert : listCert) {
                    String credentialID = cert.baseCredentialInfo().getCredentialID();
                    crt = session.certificateInfo(credentialID, connectorLogRequest, checkCertificateRequest.getLang());
                    if(crt !=null){
                        String authMode = crt.credentialInfo().getAuthMode().toString();
                        String status = crt.baseCredentialInfo().getStatus();

                        if ("EXPLICIT_OTP_SMS".equals(authMode) && "OPERATED".equals(status)) {
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

            }
            commonRepository.connectorLog(connectorLogRequest);
        } catch (Exception e) {
//            log.error(e.getMessage());
            commonRepository.connectorLog(connectorLogRequest);
            throw new Exception(e.getMessage());
        }


        return listCertificate;
    }

    public CertResponse createCertificate(CheckCertificateRequest checkCertificateRequest) throws Throwable {

        SignedJWT jwt1 = (SignedJWT) JWTParser.parse(checkCertificateRequest.getJwt());
        Gson gson = new Gson();
        JwtModel jwtModel = gson.fromJson(jwt1.getPayload().toString(), JwtModel.class);

        logger.info("getNationality: " + jwtModel.getNationality());
        logger.info("getCity_province: " + jwtModel.getCity_province());

        if (jwtModel.getDocument_type().equals("CITIZENCARD")) {
            jwtModel.setDocument_type("CITIZEN-IDENTITY-CARD");
        }
//        String mobile = jwtModel.getPhone_number().replace("84", "0");
//        jwtModel.setPhone_number(mobile);

        logger.info("connectorName: " + checkCertificateRequest.getConnectorNameRSSP());
//        session = rsspService.handShake(lang, connectorNameRSSP, enterpriseId, workFlowId);
        if (session == null) {
            session = rsspService.handShake(checkCertificateRequest.getLang(), checkCertificateRequest.getConnectorNameRSSP(), checkCertificateRequest.getEnterpriseId(), checkCertificateRequest.getWorkFlowId());
//            commonRepository.connectorLog(connectorLogRequest);
        }

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(checkCertificateRequest.getConnectorName());
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(checkCertificateRequest.getEnterpriseId()));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(checkCertificateRequest.getWorkFlowId()));

//        session.ownerCreate(connectorLogRequest, jwtModel, lang);
//        List<ICertificate> list = session.listCertificates(jwtModel.getDocument_type() + ":" + jwtModel.getDocument_number(), connectorLogRequest, lang);
//        List<Certificate> listCert = new ArrayList<>();
//        for (ICertificate iCertificate : list) {
//            if (iCertificate instanceof Certificate) {
//                Certificate certificate = (Certificate) iCertificate;
//                listCert.add(certificate);
//            } else {
//                // Xử lý tương ứng nếu đối tượng không phải là lớp Certificate
//            }
//        }
//        String credentialID = "";
//        boolean foundCredential = false;
//        if (listCert.size() > 0) {
//            for (Certificate cert : listCert) {
//                String authMode = cert.credentialInfo().getAuthMode().toString();
//                String status = cert.baseCredentialInfo().getStatus();
//                System.out.println("authMode: " + authMode);
//                System.out.println("status: " + status);
////                if (authMode.equals("EXPLICIT_OTP_SMS") && status != null && status.equals("OPERATED")) {
////                    credentialID = cert.baseCredentialInfo().getCredentialID();
////                    foundCredential = true;
////                    break;
////                }
//                if ("EXPLICIT_OTP_SMS".equals(authMode) && "OPERATED".equals(status)) {
//                    credentialID = cert.baseCredentialInfo().getCredentialID();
//                    foundCredential = true;
//                    break;
//                }
//            }
//        }
//        if (!foundCredential) {
//            credentialID = session.credentialsIssue(connectorLogRequest, jwtModel, lang);
//        }
        String credentialID = session.credentialsIssue(connectorLogRequest, jwtModel, checkCertificateRequest.getLang());
        logger.info("credentialID: " + credentialID);
        crt = session.certificateInfo(credentialID, connectorLogRequest, checkCertificateRequest.getLang());
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
            return certResponse;
        }

        return null;
    }

    public String credentialOTP(CheckCertificateRequest checkCertificateRequest) throws Throwable {

        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(checkCertificateRequest.getConnectorName());
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(checkCertificateRequest.getEnterpriseId()));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(checkCertificateRequest.getWorkFlowId()));

        String otpRequestID = crt.sendOTP(connectorLogRequest, checkCertificateRequest.getLang(), checkCertificateRequest.getCredentialID(), null, null); // truyền thêm số CCCD agreementUUID
        return otpRequestID;
    }

    public String authorizeOTP(AuthorizeOTPRequest authorizeOTPRequest, HttpServletRequest request) throws Throwable {
        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
        connectorLogRequest.setpCONNECTOR_NAME(authorizeOTPRequest.getConnectorName());
        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(authorizeOTPRequest.getEnterpriseId()));
        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(authorizeOTPRequest.getWorkFlowId()));

        try {
            boolean error = false;
            WorkFlowList[][] rsWFList = new WorkFlowList[1][];
            connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, authorizeOTPRequest.getSigningToken());
            String sResult = "0";

            // check workflow status
            if (rsWFList[0] == null || rsWFList[0].length == 0 || rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {
                error = true;
                sResult = "Signer Status invalid";// trạng thái không hợp lệ
                throw new Exception(sResult);
            }

            // check workflow participant
            Participants[][] rsParticipant = new Participants[1][];
            connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, authorizeOTPRequest.getSignerToken());
            if (rsParticipant[0] == null || rsParticipant[0].length == 0 || rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {
                sResult = "The document has already been signed";
            }
            String sad = crt.authorize(connectorLogRequest, authorizeOTPRequest.getLang(), authorizeOTPRequest.getCredentialID(), 1, null, null, authorizeOTPRequest.getRequestID(), authorizeOTPRequest.getOtp());
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
//            String[] sResultConnector = new String[2];
//            System.out.println("connectorName: " + connectorName);
//            String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
//            String prefixCode = sResultConnector[1];
//
//            long millis = System.currentTimeMillis();
//            String sSignatureHash = signerToken + millis;
////            String sSignature_id = prefixCode + "-" + CommonHash.hashPass(sSignatureHash.getBytes());
//            String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();
//
//            System.out.println("credentialID: " + credentialID);
//            System.out.println("signingOption: " + signingOption);

//            List<String> hashList = commonRepository.createHashList(signerToken, signingToken, certChain, credentialID, "", sSignature_id);
            PrepareSigningRequest prepareSigningRequest = new PrepareSigningRequest(
                    authorizeOTPRequest.getSigningToken(),
                    authorizeOTPRequest.getSignerToken(),
                    authorizeOTPRequest.getSigningOption(),
                    authorizeOTPRequest.getSignerId(),
                    authorizeOTPRequest.getCertChain(),
                    authorizeOTPRequest.getConnectorName(),
                    null
            );
            String hashList = gatewayAPI.PrepareSign(prepareSigningRequest);
            System.out.println("PrepareSign finish");
            HashAlgorithmOID hashAlgo = HashAlgorithmOID.SHA_256;
            DocumentDigests doc = new DocumentDigests();
            doc.hashAlgorithmOID = hashAlgo;
            doc.hashes = new ArrayList<>();
            doc.hashes.add(Utils.base64Decode(hashList));


//            commonRepository.connectorLog(connectorLogRequest);
            SignAlgo signAlgo = SignAlgo.RSA;
            List<byte[]> signatures = crt.signHash(connectorLogRequest, authorizeOTPRequest.getLang(), authorizeOTPRequest.getCredentialID(), doc, signAlgo, sad);
//            commonRepository.connectorLog(connectorLogRequest);

            String signature = Base64.getEncoder().encodeToString(signatures.get(0));
            System.out.println("kiem tra lần 2: ");
            String result = gatewayAPI.sign(authorizeOTPRequest.getSigningToken(), authorizeOTPRequest.getSignerToken(), authorizeOTPRequest.getSignerId(), signature);

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
//            commonRepository.postBack(callBackLogRequest, rsParticipant, pdfSigned, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, tsTimeSigned, rsWFList, sFileID_Last, certChain, codeNumber, signingOption, type, request);

            return result;

        } catch (Exception e) {
//            commonRepository.connectorLog(connectorLogRequest);
            throw new Exception(e.getMessage());
        }

//        return sad;
    }

//    public String authorizeOTP(
//            String lang,
//            String credentialID,
//            String requestID,
//            String otp,
//            String signerId,
//            String signingToken,
//            String fileName,
//            String signerToken,
//            String connectorName,
//            String signingOption,
//            String codeNumber,
//            String type,
//            String certChain,
//            String enterpriseId,
//            String workFlowId,
//            HttpServletRequest request) throws Throwable {
//
//        ConnectorLogRequest connectorLogRequest = new ConnectorLogRequest();
//        connectorLogRequest.setpCONNECTOR_NAME(connectorName);
//        connectorLogRequest.setpENTERPRISE_ID(Integer.parseInt(enterpriseId));
//        connectorLogRequest.setpWORKFLOW_ID(Integer.parseInt(workFlowId));
//
//        try {
//            boolean error = false;
//            WorkFlowList[][] rsWFList = new WorkFlowList[1][];
//            connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, signingToken);
//            String sResult = "0";
//
//            // check workflow status
//            if (rsWFList[0] == null || rsWFList[0].length == 0 || rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {
//                error = true;
//                sResult = "Signer Status invalid";// trạng thái không hợp lệ
//                throw new Exception(sResult);
//            }
//
//            // check workflow participant
//            Participants[][] rsParticipant = new Participants[1][];
//            connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, signerToken);
//            if (rsParticipant[0] == null || rsParticipant[0].length == 0 || rsParticipant[0][0].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {
//                sResult = "The document has already been signed";
//            }
//
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
//            String[] sResultConnector = new String[2];
//            System.out.println("connectorName: " + connectorName);
//            String pIdentierConnector = connect.getIdentierConnector(connectorName, sResultConnector);
//            String prefixCode = sResultConnector[1];
//
//            long millis = System.currentTimeMillis();
//            String sSignatureHash = signerToken + millis;
////            String sSignature_id = prefixCode + "-" + CommonHash.hashPass(sSignatureHash.getBytes());
//            String sSignature_id = prefixCode + "-" + CommonHash.toHexString(CommonHash.hashPass(sSignatureHash)).toUpperCase();
//
//            System.out.println("credentialID: " + credentialID);
//
//            List<String> hashList = commonRepository.createHashList(signerToken, signingToken, certChain, credentialID, "", sSignature_id);
//
//            HashAlgorithmOID hashAlgo = HashAlgorithmOID.SHA_256;
//            DocumentDigests doc = new DocumentDigests();
//            doc.hashAlgorithmOID = hashAlgo;
//            doc.hashes = new ArrayList<>();
//            doc.hashes.add(Utils.base64Decode(hashList.get(0)));
//
//            String sad = crt.authorize(connectorLogRequest, lang, credentialID, 1, null, null, requestID, otp);
//
////            commonRepository.connectorLog(connectorLogRequest);
//            SignAlgo signAlgo = SignAlgo.RSA;
//            List<byte[]> signatures = crt.signHash(connectorLogRequest, lang, credentialID, doc, signAlgo, sad);
////            commonRepository.connectorLog(connectorLogRequest);
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
//            commonRepository.postBack(callBackLogRequest, rsParticipant, pdfSigned, fileName, signingToken, pDMS_PROPERTY, sSignature_id, signerToken, tsTimeSigned, rsWFList, sFileID_Last, certChain, codeNumber, signingOption, type, request);
//
//            return "OK";
//
//        } catch (Exception e) {
////            commonRepository.connectorLog(connectorLogRequest);
//            throw new Exception(e.getMessage());
//        }
//
////        return sad;
//    }
}
