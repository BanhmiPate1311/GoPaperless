package vn.mobileid.paperless.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import vn.mobileid.paperless.API.*;
import vn.mobileid.paperless.aws.datatypes.PadesConstants;
import vn.mobileid.paperless.aws.response.PerformResponse;
import vn.mobileid.paperless.aws.response.SubjectResponse;
import vn.mobileid.paperless.aws.response.TokenResponse;
import vn.mobileid.paperless.aws.request.CheckIdentityRequest;
import vn.mobileid.paperless.aws.request.FaceAndCreateRequest;
import vn.mobileid.paperless.object.ConnectorName;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Repository
public class ElectronicRepository {

    final private int timeOut = 5000;
    private String url;
    final private String contentType = "application/json";
    private Gson gson = new Gson();
    private String accessToken;

    @Autowired
    private AWSCall aWSCall;

    private synchronized String getAccessToken(boolean renewAccessToken) {
        String accessToken = null;
        if (renewAccessToken) {
//            if (LogManager.isShowDebugLog()) {
//                LOG.debug("Get new accessToken");
//            }

            String act = null;
            try {
                act = getToken();
            } catch (Exception e) {
                e.printStackTrace();
//                if (LogManager.isShowErrorLog()) {
//                    LOG.error("Error while getting accessToken. Details: " + Utils.printStackTrace(e));
//                }
            }
            if (!Utils.isNullOrEmpty(act)) {
                accessToken = act;
            } else {
                accessToken = null;
            }
            return accessToken;
        } else {
            if (accessToken == null) {
                return getAccessToken(true);
            } else {
                return accessToken;
            }
        }
    }

    public String getToken() throws IOException {
        String sPropertiesFMS = "";
        ArrayList<ConnectorName> connector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        if (connector.size() > 0) {
            for (int m = 0; m < connector.size(); m++) {
                if (connector.get(m).CONNECTOR_NAME.equals("MOBILE_ID_IDENTITY")) {
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
                if (annotationObject.get("name").getAsString().equals("BASICTOKEN")) {
                    PadesConstants.BASIC_TOKEN = annotationObject.get("value").getAsString();
                }
            }
        }
        System.out.println("PadesConstants.BASE_URL" + PadesConstants.BASE_URL);
        System.out.println("PadesConstants.ACCESSKEY" + PadesConstants.ACCESSKEY);
        System.out.println("PadesConstants.SECRETKEY" + PadesConstants.SECRETKEY);
        System.out.println("PadesConstants.BASIC_TOKEN" + PadesConstants.BASIC_TOKEN);

        String tokenUrl = PadesConstants.BASE_URL + "/v2/e-identity/general/token/get";
        System.out.println("tokenUrl" + tokenUrl);
        aWSCall = new AWSCall(tokenUrl,
                "GET",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                null);
        // /v1/e-verification/oidc/token {get acess_token}
//        String token = aWSCall.v1VeriOidcToken("/v2/e-identity/general/token/get", basicToken);

        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(tokenUrl),
                "GET",
                this.timeOut,
                aWSCall.getAWSV4Auth(null, "/v2/e-identity/general/token/get", PadesConstants.BASIC_TOKEN),
                null);

        //Response
        System.out.println("response" + response);
        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);
        //Past Bearer for Step 2 (E-verification/pades)
//        String bearerToken = "Bearer " + tokenResponse.access_token;
//        System.out.println("Bearer Token: " + bearerToken);
        this.accessToken = tokenResponse.access_token;
        return tokenResponse.access_token;
    }

    public SubjectResponse getSubject(CheckIdentityRequest checkIdentityRequest) throws IOException {
        String access_token = getToken();
        String bearerToken = "Bearer " + access_token;
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> identity_document = new HashMap<>();

        request.put("email", "");
        request.put("mobile", "");
        identity_document.put("type", checkIdentityRequest.getType());
        identity_document.put("value", checkIdentityRequest.getCode());
        request.put("identity_document", identity_document);
        request.put("dg1_enabled", false);
        request.put("dg2_enabled", true);
        request.put("dg3_enabled", false);
        request.put("dg13_enabled", false);
        request.put("lang", checkIdentityRequest.getLang());

        String bodyRequest = gson.toJson(request);

        String getSubjectUrl = PadesConstants.BASE_URL + "/v2/e-identity/subject/get";
        System.out.println("ownerUrl: " + getSubjectUrl);
        TreeMap<String, String> queryParametes = new TreeMap<>();
        String base64 = Base64.getEncoder().encodeToString(bodyRequest.getBytes(StandardCharsets.UTF_8));
        queryParametes.put("request_data_base64", base64);

        aWSCall = new AWSCall(getSubjectUrl,
                "GET",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                queryParametes);

        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(getSubjectUrl),
                "GET",
                this.timeOut,
                aWSCall.getAWSV4AuthForFormData(null, bearerToken, null),
                bodyRequest);

        System.out.println("getSubject: " + response);
        SubjectResponse subjectResponse = gson.fromJson(response, SubjectResponse.class);
        return subjectResponse;
    }

    public String createSubject(FaceAndCreateRequest faceAndCreateRequest) throws Exception {
        String bearerToken = "Bearer " + accessToken;
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> identity_document = new HashMap<>();

//        request.put("email", "");
//        request.put("mobile", "");
        request.put("facial_image", faceAndCreateRequest.getFacialImage());
        identity_document.put("type", faceAndCreateRequest.getType());
        identity_document.put("value", faceAndCreateRequest.getCode());
        request.put("identity_document", identity_document);
        request.put("lang", faceAndCreateRequest.getLang());

        String bodyRequest = gson.toJson(request);

        String createUrl = PadesConstants.BASE_URL + "/v2/e-identity/subject/create";
        System.out.println("createUrl: " + createUrl);

        aWSCall = new AWSCall(
                createUrl,
                "POST",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                null);

        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(createUrl),
                "POST",
                this.timeOut,
                aWSCall.getAWSV4AuthForFormData(bodyRequest, bearerToken, null),
                bodyRequest);
        System.out.println("createSubject: " + response);
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        if (jsonObject.get("status").getAsInt() != 0) {
            throw new Exception(jsonObject.get("message").getAsString());
        }
        return jsonObject.get("subject_id").getAsString();
    }

    public PerformResponse faceAndCreate(FaceAndCreateRequest faceAndCreateRequest) throws Exception {
        String subject_id = createSubject(faceAndCreateRequest);
        String process_type = "FACIAL_MATCHING";
        String purpose = "CREATE";
        String process_id = createProcess(faceAndCreateRequest.getLang(), null, null, subject_id, null, process_type, purpose);

        String sImageFace = faceAndCreateRequest.getImageFace().replace("data:image/png;base64,", "");
        // using split

        PerformResponse performResponse = processPerForm(faceAndCreateRequest.getLang(), faceAndCreateRequest.getCode(), faceAndCreateRequest.getType(), null, subject_id, process_id, sImageFace);
        performResponse.setSubject_id(subject_id);
        return performResponse;
    }

    public String updateSubject(String lang, String mobile, String subject_id) throws MalformedURLException {
        String bearerToken = "Bearer " + accessToken;
        Map<String, Object> request = new HashMap<>();

        request.put("subject_id", subject_id);
        request.put("mobile", mobile);
        request.put("lang", lang);

        String bodyRequest = gson.toJson(request);

        String updateUrl = PadesConstants.BASE_URL + "/v2/e-identity/subject/update";
        System.out.println("createUrl: " + updateUrl);

        aWSCall = new AWSCall(
                updateUrl,
                "PUT",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                null);

        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(updateUrl),
                "PUT",
                this.timeOut,
                aWSCall.getAWSV4AuthForFormData(bodyRequest, bearerToken, null),
                bodyRequest);
        System.out.println("SubjectResponse: " + gson.toJson(response));

        return "OK";
    }

    public String createProcess(String lang, String mobile, String email, String subject_id, String jwt, String process_type, String purpose) throws Exception {
//        updateSubject(lang, mobile, subject_id);

        String bearerToken = "Bearer " + accessToken;
//        String process_type = "MOBILE_AUTHENTICATION";
        String provider = "mobile-id";
//        String purpose = "AMEND";

        Map<String, Object> request = new HashMap<>();
        Map<String, Object> claim_sources = new HashMap<>();

        request.put("subject_id", subject_id);
        request.put("provider", provider);
        request.put("purpose", purpose);
        if (mobile != null && !mobile.equals("null")) {
            request.put("mobile", mobile);
        }

        if (email != null && !email.equals("null")) {
            request.put("email", email);
        }

        request.put("process_type", process_type);
        request.put("lang", lang);
        if (jwt != null) {
            claim_sources.put("JWT", jwt);
            request.put("claim_sources", claim_sources);
        }

        String bodyRequest = gson.toJson(request);
        System.out.println("bodyRequest: " + bodyRequest);

        String createProcessUrl = PadesConstants.BASE_URL + "/v2/e-identity/process/create";
        System.out.println("createProcessUrl: " + createProcessUrl);

        aWSCall = new AWSCall(
                createProcessUrl,
                "POST",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                null);

        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(createProcessUrl),
                "POST",
                this.timeOut,
                aWSCall.getAWSV4AuthForFormData(bodyRequest, bearerToken, null),
                bodyRequest);
        System.out.println("SubjectResponse: " + response);

        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        if (jsonObject.get("status").getAsInt() != 0) {
            throw new Exception(jsonObject.get("message").getAsString());
        }
        String processId = jsonObject.get("process_id").getAsString();
        System.out.println("processId: " + processId);

        return processId;
    }

    public PerformResponse processPerForm(String lang, String code, String type, String otp, String subject_id, String process_id, String imageFace) throws Exception {
        String bearerToken = "Bearer " + accessToken;

        String boundary = "===" + System.currentTimeMillis() + "===";

        Map<String, Object> request = new HashMap<>();
        if (imageFace != null) {
            List<String> list = new ArrayList<>();
            list.add(imageFace);
            request.put("frames", list);
            Map<String, Object> identity_document = new HashMap<>();

            identity_document.put("type", type);
            identity_document.put("value", code);
            request.put("identity_document", identity_document);
        }

        request.put("subject_id", subject_id);
        request.put("process_id", process_id);
        if (otp != null) {
            request.put("otp", otp);
        }

        request.put("lang", lang);

        String bodyRequest = gson.toJson(request);
        System.out.println("bodyRequest: " + bodyRequest);

        String processPerFormUrl = PadesConstants.BASE_URL + "/v2/e-identity/process/perform";
        System.out.println("processPerFormUrl: " + processPerFormUrl);

        aWSCall = new AWSCall(
                processPerFormUrl,
                "PUT",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                null);

        String response = HttpUtilsAWS.invokeHttpMutltiPartRequest(
                new URL(processPerFormUrl),
                "PUT",
                this.timeOut,
                aWSCall.getAWSV4AuthForFormDataReal(bodyRequest, bearerToken, null, boundary),
                bodyRequest,
                null,
                boundary);
        PerformResponse performResponse = gson.fromJson(response, PerformResponse.class);
        if (performResponse.getStatus() != 0) {
            throw new Exception(performResponse.getMessage());
        }

        System.out.println("processPerForm: " + response);

        return performResponse;
    }

    public String processOTPResend(String lang, String jwt, String subject_id, String process_id) throws MalformedURLException {
        String bearerToken = "Bearer " + accessToken;
        Map<String, Object> request = new HashMap<>();
        Map<String, Object> claim_sources = new HashMap<>();

        request.put("subject_id", subject_id);
        request.put("process_id", process_id);
        request.put("lang", lang);
        if (jwt != null) {
            claim_sources.put("JWT", jwt);
            request.put("claim_sources", claim_sources);
        }

        String bodyRequest = gson.toJson(request);

        String otpResendUrl = PadesConstants.BASE_URL + "/v2/e-identity/process/otp/resend ";
        System.out.println("otpResendUrl: " + otpResendUrl);

        aWSCall = new AWSCall(
                otpResendUrl,
                "PUT",
                PadesConstants.ACCESSKEY,
                PadesConstants.SECRETKEY,
                PadesConstants.REGIONNAME,
                PadesConstants.SERVICENAME,
                5000,
                PadesConstants.XAPIKEY,
                contentType,
                null);
        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(otpResendUrl),
                "PUT",
                this.timeOut,
                aWSCall.getAWSV4AuthForFormData(bodyRequest, bearerToken, null),
                bodyRequest);
        System.out.println("SubjectResponse: " + response);

        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        String message = jsonObject.get("message").getAsString();
        System.out.println("processId: " + message);

        return message;
    }
}
