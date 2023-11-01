package vn.mobileid.paperless.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.aws.dto.ValidViewDto;
import vn.mobileid.paperless.aws.request.ValidPostBackRequest;
import vn.mobileid.paperless.aws.request.ValidationResquest;
import vn.mobileid.paperless.process.process;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValidationService {
    @Autowired
    private GatewayAPI gatewayAPI;

    @Autowired
    private process connect;

    public String getView(ValidationResquest validationResquest) {
        return gatewayAPI.ValidView(validationResquest);
    }

    public String postback(ValidPostBackRequest validPostBackRequest) throws Exception {

        String postbackUrl = validPostBackRequest.getPostBackUrl();

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("status", validPostBackRequest.getStatus());
        requestData.put("upload_token ", validPostBackRequest.getUploadToken());

        Gson gson = new Gson();
        System.out.println("postback Data: " + gson.toJson(requestData));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(postbackUrl, HttpMethod.POST, httpEntity, String.class);
            connect.USP_GW_PPL_FILE_VALIDATION_UPDATE_POSTBACK_STATUS(validPostBackRequest.getFileValidationId(), 2, "GoPaperLess");

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error  đây: ");
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            throw new Exception(e.getMessage());
        }

    }

    public int checkStatus(ValidPostBackRequest validPostBackRequest) throws Exception {

        return connect.USP_GW_PPL_FILE_VALIDATION_GET_POSTBACK_STATUS(validPostBackRequest.getFileValidationId());

    }
}
