package vn.mobileid.paperless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.fps.*;
import vn.mobileid.paperless.fps.request.FpsSignRequest;
import vn.mobileid.paperless.fps.request.HashFileRequest;
import vn.mobileid.paperless.object.FirstFile;
import vn.mobileid.paperless.process.process;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class FpsService {

    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private process connect;

    @Autowired
    private GatewayAPI gatewayAPI;

    public void getAccessToken() {
        String authorizeUrl = "https://fps.mobile-id.vn/fps/v1/authenticate";

//        RestTemplate restTemplate = new RestTemplate();

        // Tạo HttpHeaders để đặt các headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo dữ liệu JSON cho yêu cầu POST
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("grant_type", "client_credentials");
        requestData.put("client_id", "Dokobit_Gateway");
        requestData.put("remember_me_enabled", false);
        requestData.put("client_secret", "TmFtZTogRG9rb2JpdCBHYXRld2F5IFdlYgpDcmVhdGVkIGF0OiAxNjk3NjAzNDE5CkNyZWF0ZWQgYnk6IEdpYVRLClZlcnNpb24gY2xpZW50IFNlY3JldDogMSA=");

        // Tạo HttpEntity với dữ liệu JSON và headers
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        ResponseEntity<AccessTokenDto> responseEntity = restTemplate.exchange(authorizeUrl, HttpMethod.POST, httpEntity, AccessTokenDto.class);
        this.accessToken = Objects.requireNonNull(responseEntity.getBody()).getAccess_token();
    }

    public int getDocumentId(String uuid) throws Exception {
        System.out.println("uuid: " + uuid);
//        String synchronizeUrl = "https://fps.mobile-id.vn/fps/v1/synchronize";

        String synchronizeUrl = "https://fps.mobile-id.vn/fps/v1/synchronize";
//        String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTgwNDE0MTY1NzEsImlhdCI6MTY5ODAzNzgxNjU3MSwiaXNzIjoiaHR0cHM6Ly9mcHMubW9iaWxlLWlkLnZuIiwiYXVkIjoiZW50ZXJwcmlzZSIsInN1YiI6IkZQUyIsInR5cCI6IkJlYXJlciIsInNjb3BlIjoiTUlfTW9iaWxlQXBwIiwic2lkIjoiMDkxMi01OTY0MS02MDg0OCIsImF6cCI6Ik1vYmlsZS1JRCBDb21wYW55IiwibW9iaWxlIjoiMTkwMCA2ODg0IiwiYWlkIjozLCJpY2kiOjF9.QqT0AxiTUgJLTT59tP7qeS_sTD5pwR6uEQVar_n1oiEIq5H-sr_xTDX7RAsmcUFhporNj3-liBGahFGgtBRKOANMAHXwxfTsPx0WWw-JukKMsnvwruDedASmgiUsV8SGS609rHjXC-y8IgHFnrXA7s8EzECLAvc6NyoRbVuBRsW-m3-A0hQUJo1b-Hy61Un4xnrQ_y2guyN7AEn1Obb4y1MMuZFXf8z0x9jthO8bFJLpr0vAi_moNqOX31QWa9TxSaBQWoz5ob-l3TwiC4JRBA7BjBd132FRT9FGUXRwDAx31lwAorC9ufHF8RzgTQ1j1M20MBjbYwVk-lfzUotj9w";

//        RestTemplate restTemplate = new RestTemplate();

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
            return jsonNode.get("document_id").asInt();

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

//        RestTemplate restTemplate = new RestTemplate();

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

    public InputStream getImagePdf(int documentId) throws Exception {

        String getImageBasse64Url = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(getImageBasse64Url, HttpMethod.GET, httpEntity, byte[].class);
            InputStream inputStreamFile = null;


            if (response.getBody() != null) {
                inputStreamFile = new ByteArrayInputStream(response.getBody());
            }

            return inputStreamFile;
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return getImagePdf(documentId);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String getFields(int documentId) throws Exception {

        String getImageBasse64Url = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields";

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(getImageBasse64Url, HttpMethod.GET, httpEntity, String.class);

            return response.getBody();
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

//        RestTemplate restTemplate = new RestTemplate();

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

//        RestTemplate restTemplate = new RestTemplate();

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

    public String addSignature(int documentId, String field, @NotNull BasicFieldAttribute data, boolean drag) throws Exception {
        String addSignatureUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields/" + field;

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        if (drag) {
            headers.set("x-dimension-unit", "percentage");
        }

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("field_name", data.getFieldName());
        requestData.put("page", data.getPage());
        requestData.put("dimension", data.getDimension());
        requestData.put("visible_enabled", data.getVisibleEnabled());
        List<String> list = new ArrayList<>();
        list.add("ESEAL");
        requestData.put("level_of_assurance", list);

        Gson gson = new Gson();
        System.out.println("addSignature: " + gson.toJson(requestData));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, String.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.out.println("Error  đây: ");
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return addSignature(documentId, field, data, drag);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String putSignature(int documentId, String field, @NotNull BasicFieldAttribute data) throws Exception {
        String putSignatureUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields/" + field;

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.set("x-dimension-unit", "percentage");

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("field_name", data.getFieldName());
        requestData.put("page", data.getPage());
        requestData.put("dimension", data.getDimension());
        requestData.put("visible_enabled", data.getVisibleEnabled());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(putSignatureUrl, HttpMethod.PUT, httpEntity, String.class);
            // Get the response body as a String

            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return putSignature(documentId, field, data);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String deleteSignatue(int documentId, String field_name) throws Exception {
        String deleteSignatureUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields";

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("field_name", field_name);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(deleteSignatureUrl, HttpMethod.DELETE, httpEntity, String.class);
            // Get the response body as a String

            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return deleteSignatue(documentId, field_name);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String hashSignatureField(int documentId, HashFileRequest data) throws Exception {
        String hashSignatureFieldUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/fields/hash";

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("field_name", data.getFieldName());
        requestData.put("signature_algorithm", "RSA");
        requestData.put("signed_hash", "SHA256");
        requestData.put("signing_reason", data.getSigningReason());
        requestData.put("signing_location", data.getSigningLocation());
        requestData.put("certificate_chain", data.getCertificateChain());

//        Gson gson = new Gson();
//        System.out.println("Request Data: " + gson.toJson(requestData));

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(hashSignatureFieldUrl, HttpMethod.POST, httpEntity, String.class);
            // Get the response body as a String
            String responseBody = response.getBody();

//            JsonObject jsonObject1 = gson.fromJson(responseBody, JsonObject.class);
//            return jsonObject1.get("hash_value").getAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            return jsonNode.get("hash_value").asText();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return hashSignatureField(documentId, data);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }

    public String signDocument(int documentId, FpsSignRequest data) throws Exception {
        String signDocumentUrl = "https://fps.mobile-id.vn/fps/v1/documents/" + documentId + "/sign";

//        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("field_name", data.getFieldName());
        requestData.put("hash_value", data.getHashValue());
        requestData.put("signature_value", data.getSignatureValue());
        requestData.put("certificate_chain", data.getCertificateChain());

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(addSignatureUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();

            ResponseEntity<String> response = restTemplate.exchange(signDocumentUrl, HttpMethod.POST, httpEntity, String.class);
            // Get the response body as a String
            System.out.println("check: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            System.out.println("HTTP Status Code: " + statusCode.value());
            if (statusCode.value() == 401) {
                getAccessToken();
                return signDocument(documentId, data);
            } else {
                throw new Exception(e.getMessage());
            }
        }
    }
}
