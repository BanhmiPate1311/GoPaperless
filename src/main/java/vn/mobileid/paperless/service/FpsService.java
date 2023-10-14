package vn.mobileid.paperless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.paperless.fps.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class FpsService {
    private String accessToken;
    private String bearerToken;

    public void getAccessToken() {
        String authorizeUrl = "https://fps.mobile-id.vn/fps/v1/authenticate";

        RestTemplate restTemplate = new RestTemplate();

        // Tạo HttpHeaders để đặt các headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo dữ liệu JSON cho yêu cầu POST
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("grant_type", "client_credentials");
        requestData.put("client_id", "MI_MobileApp");
        requestData.put("remember_me_enabled", false);
        requestData.put("client_secret", "h9fSyjob8OF2SjlLSJY0");

        // Tạo HttpEntity với dữ liệu JSON và headers
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<AccessTokenDto> responseEntity = restTemplate.exchange(authorizeUrl, HttpMethod.POST, httpEntity, AccessTokenDto.class);
        this.accessToken = Objects.requireNonNull(responseEntity.getBody()).getAccess_token();
    }

    public String getDocumentId(String uuid) throws Exception {
        String synchronizeUrl = "https://fps.mobile-id.vn/fps/v1/synchronize";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("uuid", uuid);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(synchronizeUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();
            ResponseEntity<String> responseEntity = restTemplate.exchange(synchronizeUrl, HttpMethod.POST, httpEntity, String.class);
            String responseBody = responseEntity.getBody();

// Assuming the responseBody is a JSON string, you can parse it into a JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

// Get the "document_id" field from the JsonNode as a JSON string
            String documentIdJsonString = jsonNode.get("document_id").toString();

            return documentIdJsonString;
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return getDocumentId(uuid);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public byte[] getImageBasse64(int documentId, int page) throws Exception {

        String getImageBasse64Url = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/images/" + page;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(getImageBasse64Url, HttpMethod.GET, httpEntity, byte[].class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return getImageBasse64(documentId, page);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String getFields(int documentId) throws Exception {

        String getImageBasse64Url = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(getImageBasse64Url, HttpMethod.GET, httpEntity, String.class);
            // Get the response body as a String
            String responseBody = response.getBody();

// Convert the response body to a JsonNode using Jackson's ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(responseBody).toString();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return getFields(documentId);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String getDocumentDetails(int documentId) throws Exception {

        String getDocumentDetailsUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/details";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(getDocumentDetailsUrl, HttpMethod.GET, httpEntity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return getDocumentDetails(documentId);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public List<Signature> getVerification(int documentId) throws Exception {
        if (accessToken == null) {
            getAccessToken();
        }
        String verificationUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/verification";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        try {
            ParameterizedTypeReference<List<Signature>> responseType = new ParameterizedTypeReference<List<Signature>>() {
            };

            ResponseEntity<List<Signature>> response = restTemplate.exchange(verificationUrl, HttpMethod.GET, httpEntity, responseType);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return getVerification(documentId);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String addSignature(int documentId, String field, @NotNull BasicFieldAttribute data) throws Exception {
        String addSignatureUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields/" + field;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("field_name", data.getFieldName());
        requestData.put("page", data.getPage());
        requestData.put("dimension", data.getDimension());
        requestData.put("visible_enabled", data.getVisibleEnabled());


        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, String.class);
            // Get the response body as a String
            String responseBody = response.getBody();

// Convert the response body to a JsonNode using Gson's ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(responseBody).toString();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return addSignature(documentId, field, data);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }
}
