package vn.mobileid.paperless.viettelca;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Repository;
import vn.mobileid.paperless.API.HttpResponse;
import vn.mobileid.paperless.API.HttpUtils;
import vn.mobileid.paperless.API.Utils;
import vn.mobileid.paperless.Model.APIException;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.object.ConnectorLogRequest;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.viettelca.response.CertDetail;
import vn.mobileid.paperless.viettelca.response.VTSignResponse;
import vn.mobileid.paperless.viettelca.response.ViettelLoginResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VtSession {
    private String baseURL;
    public String login(String pbaseURL,String clientId, String userId, String clientSecret, String profileId, ConnectorLogRequest connectorLogRequest) throws Exception {
        this.baseURL = pbaseURL;
        Map<String, Object> request = new HashMap<>();
        request.put("client_id", clientId);
        request.put("user_id", userId);
        request.put("client_secret", clientSecret);
        request.put("profile_id", profileId);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        HttpResponse response = null;
        try {
            response = HttpUtils.invokeHttpRequest(baseURL + "/vtss/service/ras/v1/login", "POST", 50000,
                    headers, Utils.gsTmp.toJson(request));

//            JsonObject jsonObject = Utils.gsTmp.fromJson(response.getMsg(), JsonObject.class);
//            String accessToken = jsonObject.get("access_token").getAsString();
//            log.info("accessToken: " + accessToken);
            ViettelLoginResponse signCloudResp = Utils.gsTmp.fromJson(response.getMsg(), ViettelLoginResponse.class);
            connectorLogRequest.setpURL(baseURL + "/vtss/service/ras/v1/login");
            connectorLogRequest.setpHTTP_VERB("POST");
            connectorLogRequest.setpSTATUS_CODE(signCloudResp.error);
            Gson gson = new Gson();
            connectorLogRequest.setpREQUEST(gson.toJson(request));
            connectorLogRequest.setpRESPONSE(gson.toJson(signCloudResp));
            if (signCloudResp.error != 0) {

                throw new APIException(signCloudResp.getError_description());
            }
            String accessToken = signCloudResp.getAccess_token();
//            VariableLocal.access_token = accessToken;
//            TokenStorage.saveToken(accessToken);



            return accessToken;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public CertDetail[] getCertificate(String accessToken,ConnectorLogRequest connectorLogRequest) throws APIException {
        Map<String, Object> request = new HashMap<>();
        request.put("client_id", VariableLocal.client_id);
        request.put("user_id", VariableLocal.user_id);
        request.put("client_secret", VariableLocal.client_secret);
        request.put("profile_id", VariableLocal.profile_id);
        request.put("certificates", "chain");
        request.put("certInfo", true);
        request.put("authInfo", true);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);
        List<Map<String, Object>> list = null;
        CertDetail[] signCloudResp = null;
        try {
            HttpResponse response = HttpUtils.invokeHttpRequest(baseURL + "/vtss/service/certificates/info", "POST", 50000,
                    headers, Utils.gsTmp.toJson(request));

            Gson gson = new Gson();
            JsonElement jsonElement = gson.fromJson(response.getMsg(), JsonElement.class);

            if (jsonElement.isJsonArray()) {
                // Trường hợp trả về một mảng
                signCloudResp = Utils.gsTmp.fromJson(response.getMsg(), CertDetail[].class);
                connectorLogRequest.setpURL(baseURL + "/vtss/service/certificates/info");
                connectorLogRequest.setpHTTP_VERB("POST");
                connectorLogRequest.setpSTATUS_CODE(response.getHttpCode());

                connectorLogRequest.setpREQUEST(gson.toJson(request));
                connectorLogRequest.setpRESPONSE(gson.toJson(signCloudResp));
                for (CertDetail cert : signCloudResp) {
                    cert.getCert().setIssuerDN(CommonFunction.getCommonnameInDN(cert.getCert().getIssuerDN()));
                    String nameCN = cert.getCert().getSubjectDN();
                    int startIndex = nameCN.indexOf("CN=") + 3; // +3 để bỏ qua ký tự "CN="
                    int endIndex = nameCN.indexOf(",", startIndex);

                    // Trích xuất giá trị của trường "CN"
                    String cnValue = nameCN.substring(startIndex, endIndex);
                    cert.getCert().setSubjectDN(cnValue);
                }

                return signCloudResp;
            } else if (jsonElement.isJsonObject()) {
                // Trường hợp trả về một đối tượng
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                connectorLogRequest.setpSTATUS_CODE(jsonObject.get("error").getAsInt());
                jsonObject.get("error").getAsString();
//                System.out.println("jsonElement: " + jsonObject.get("error").getAsString());
                throw new APIException(jsonObject.get("error_description").getAsString());
            } else {
                // Xử lý trường hợp không xác định được kiểu dữ liệu
//                System.out.println("Kiểu dữ liệu không hợp lệ");
            }

//            System.out.println("list: " + list);
        } catch (Exception e) {
//            System.out.println("error: " + e.getMessage());
            throw new APIException(e.getMessage());
        }
        return signCloudResp;
    }

    public List<String> signHash(String credentialID, List<Map<String, Object>> documents, List<String> hashRequestList, String accessToken, int async,ConnectorLogRequest connectorLogRequest) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("client_id", VariableLocal.client_id);
        request.put("client_secret", VariableLocal.client_secret);
        request.put("credentialID", credentialID);
        request.put("numSignatures", 1);
        request.put("documents", documents);
        request.put("hash", hashRequestList);
        request.put("hashAlgo", HashAlgorithmOID.SHA_256);
        request.put("signAlgo", SignAlgo.RSA_SHA256);
        request.put("async", async);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);

        VTSignResponse signCloudResp = null;
        try {
            HttpResponse response = HttpUtils.invokeHttpRequest(baseURL + "/vtss/service/signHash", "POST", 183000,
                    headers, Utils.gsTmp.toJson(request));
            System.out.println("response: " + response.getMsg());
            signCloudResp = Utils.gsTmp.fromJson(response.getMsg(), VTSignResponse.class);
//            log.info("response: " + signCloudResp.getError());
            if (signCloudResp.getError() != 0) {
//                log.info("response: " + signCloudResp.getError_description());
                throw new APIException(signCloudResp.getError_description());
            }
            connectorLogRequest.setpURL(baseURL + "/vtss/service/signHash");
            connectorLogRequest.setpHTTP_VERB("POST");
            connectorLogRequest.setpSTATUS_CODE(signCloudResp.getError());
            Gson gson = new Gson();
            connectorLogRequest.setpREQUEST(gson.toJson(request));
            connectorLogRequest.setpRESPONSE(gson.toJson(signCloudResp));
            return signCloudResp.getSignatures();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
