package vn.mobileid.paperless.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        String synchronizeUrl = "https://fps.mobile-id.vn/fps/v1/synchronize";
        String accessToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2OTgwNDE0MTY1NzEsImlhdCI6MTY5ODAzNzgxNjU3MSwiaXNzIjoiaHR0cHM6Ly9mcHMubW9iaWxlLWlkLnZuIiwiYXVkIjoiZW50ZXJwcmlzZSIsInN1YiI6IkZQUyIsInR5cCI6IkJlYXJlciIsInNjb3BlIjoiTUlfTW9iaWxlQXBwIiwic2lkIjoiMDkxMi01OTY0MS02MDg0OCIsImF6cCI6Ik1vYmlsZS1JRCBDb21wYW55IiwibW9iaWxlIjoiMTkwMCA2ODg0IiwiYWlkIjozLCJpY2kiOjF9.QqT0AxiTUgJLTT59tP7qeS_sTD5pwR6uEQVar_n1oiEIq5H-sr_xTDX7RAsmcUFhporNj3-liBGahFGgtBRKOANMAHXwxfTsPx0WWw-JukKMsnvwruDedASmgiUsV8SGS609rHjXC-y8IgHFnrXA7s8EzECLAvc6NyoRbVuBRsW-m3-A0hQUJo1b-Hy61Un4xnrQ_y2guyN7AEn1Obb4y1MMuZFXf8z0x9jthO8bFJLpr0vAi_moNqOX31QWa9TxSaBQWoz5ob-l3TwiC4JRBA7BjBd132FRT9FGUXRwDAx31lwAorC9ufHF8RzgTQ1j1M20MBjbYwVk-lfzUotj9w";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("uuid", "B1816F12447FB30D2AC233EE753BF8AD");

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData, headers);

        try {
//            ResponseEntity<SynchronizeDto> responseEntity = restTemplate.exchange(synchronizeUrl, HttpMethod.POST, httpEntity, SynchronizeDto.class);
//            return Objects.requireNonNull(responseEntity.getBody()).getDocument_id();
            ResponseEntity<String> responseEntity = restTemplate.exchange(synchronizeUrl, HttpMethod.POST, httpEntity, String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("responseBody: " + responseBody);

// Assuming the responseBody is a JSON string, you can parse it into a JsonNode
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
// Get the "document_id" field from the JsonNode as a JSON string

            System.out.println("jsonNode: " + jsonNode.get("document_id").asInt());


        } catch (HttpClientErrorException | JsonProcessingException e) {
            System.out.println("e: " + e);
        }
    }
}
