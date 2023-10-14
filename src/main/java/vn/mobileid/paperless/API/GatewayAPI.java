package vn.mobileid.paperless.API;

import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.paperless.aws.dto.ValidViewDto;
import vn.mobileid.paperless.aws.request.ValidationResquest;
import vn.mobileid.paperless.object.PrepareSigningRequest;
import vn.mobileid.paperless.object.PrepareSigningResponse;

import java.net.MalformedURLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayAPI {
    private String baseUrl = "https://uat-paperless-gw.mobile-id.vn";
    private int timeOut = 50000;

    public byte[] previewFile(String uploadToken, String signerToken) throws MalformedURLException {
        String previewUrl = baseUrl + "/file/" + uploadToken + "/preview/" + signerToken;

        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<byte[]> responseEntity = null;
        responseEntity = restTemplate.exchange(previewUrl, HttpMethod.GET, null, byte[].class);

        return responseEntity.getBody();
    }

    public String PrepareSign(PrepareSigningRequest prepareSigningRequest) {
        System.out.println("PrepareSign");

//        String prepareUrl = baseUrl + "/apigw/signing/" + prepareSigningRequest.getSigning_token() + "/prepare.json?access_token=" + prepareSigningRequest.getSigner_token();
        // convert String to base64
        String prepareUrl = baseUrl + "/internalusage/api/signing/"+ prepareSigningRequest.getSigning_token() + "/prepare.json?access_token=" + prepareSigningRequest.getSigner_token();


        StringBuilder pemBuilder = new StringBuilder();
        pemBuilder.append("-----BEGIN CERTIFICATE-----\n");

        int index = 0;
        int chunkSize = 64;
        while (index < prepareSigningRequest.getCertificate().length()) {
            pemBuilder.append(prepareSigningRequest.getCertificate(), index, Math.min(index + chunkSize, prepareSigningRequest.getCertificate().length()));
            pemBuilder.append("\n");
            index += chunkSize;
        }

        pemBuilder.append("-----END CERTIFICATE-----");

        String pemString = pemBuilder.toString();
//        System.out.println(pemString);

        String base64CertChain = Base64.getEncoder().encodeToString(pemString.getBytes());
//        System.out.println("base64CertChain: " + base64CertChain);
        RestTemplate restTemplate = new RestTemplate();

        // Tạo HttpHeaders để đặt các headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer YourAccessToken"); // Thay thế bằng header bạn muốn gửi

        // Tạo dữ liệu JSON cho yêu cầu POST
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("signer_id", prepareSigningRequest.getSigner_id());
        requestData.put("certificate", base64CertChain);
        requestData.put("connector_name", prepareSigningRequest.getConnector_name());
        requestData.put("signing_option", prepareSigningRequest.getSigning_option());

        // Tạo HttpEntity với dữ liệu JSON và headers
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);


        // Gửi yêu cầu POST và nhận phản hồi
        ResponseEntity<String> responseEntity = restTemplate.exchange(prepareUrl, HttpMethod.POST, httpEntity, String.class);
        Gson gson = new Gson();
        PrepareSigningResponse prepareSigningResponse = gson.fromJson(responseEntity.getBody(), PrepareSigningResponse.class);
        return prepareSigningResponse.getDtbs_hash();

    }

    public String sign(String signingToken, String SignerToken, String signerId, String signature) {
        String prepareUrl = baseUrl + "/internalusage/api/signing/" + signingToken + "/sign?access_token=" + SignerToken;
        // convert String to base64
        System.out.println("signerId: " + signerId);
        System.out.println("signature_value: " + signature);

        RestTemplate restTemplate = new RestTemplate();

        // Tạo HttpHeaders để đặt các headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer YourAccessToken"); // Thay thế bằng header bạn muốn gửi

        // Tạo dữ liệu JSON cho yêu cầu POST
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("signer_id", signerId);
        requestData.put("signature_value", signature);
Gson gson = new Gson();
        System.out.println("requestData: " + gson.toJson(requestData));

        // Tạo HttpEntity với dữ liệu JSON và headers
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);


        // Gửi yêu cầu POST và nhận phản hồi
        ResponseEntity<String> responseEntity = restTemplate.exchange(prepareUrl, HttpMethod.POST, httpEntity, String.class);

        return responseEntity.getBody();

    }

    public byte[] getFileFromUploadToken(String uploadToken) {
        String getFileUrl = baseUrl + "/internalusage/api/open/" + uploadToken;

        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<byte[]> responseEntity = null;
        responseEntity = restTemplate.exchange(getFileUrl, HttpMethod.GET, null, byte[].class);

        return responseEntity.getBody();
    }

    public byte[] getLastFile(String signingToken, String signerToken) {
        String getLastFileUrl = baseUrl + "/internalusage/api/signing/" + signingToken + "/download";

        //{{url}}/apigw/signing/{{signing_token}}/download?access_token={{signer_token}}

        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<byte[]> responseEntity = null;
        responseEntity = restTemplate.exchange(getLastFileUrl, HttpMethod.GET, null, byte[].class);

        return responseEntity.getBody();
    }

    public ValidViewDto ValidView(ValidationResquest validationResquest) {
        String getValidViewUrl = baseUrl + "/internalusage/api/validation/" + validationResquest.getUploadToken() + "/view.json";
        RestTemplate restTemplate = new RestTemplate();


        ResponseEntity<ValidViewDto> responseEntity = null;
        responseEntity = restTemplate.exchange(getValidViewUrl, HttpMethod.GET, null, ValidViewDto.class);

        return responseEntity.getBody();
    }
}
