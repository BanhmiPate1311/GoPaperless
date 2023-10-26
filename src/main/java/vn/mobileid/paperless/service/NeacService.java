package vn.mobileid.paperless.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.paperless.aws.datatypes.PadesConstants;
import vn.mobileid.paperless.object.ConnectorName;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NeacService {
    private String BaseURL = "https://esign.neac.gov.vn/";
    private String spId = "BacNinh";
    private String spPassWord = "eb6a66d2-1d03-4dd3-b565-10dafffba156";
    private String caName = "FPT-CA";

    @Autowired
    private process connect;

    public void getIdentifier() throws Exception {
        String connectorName = "SMART_ID_FPT-CA";

        String sPropertiesFMS = "";
        ArrayList<ConnectorName> connector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        if (connector.size() > 0) {
            for (int m = 0; m < connector.size(); m++) {
                if (connector.get(m).CONNECTOR_NAME.equals(connectorName)) {
                    sPropertiesFMS = connector.get(m).IDENTIFIER;
                }
            }

        }
        JsonNode jsonObject = new ObjectMapper().readTree(sPropertiesFMS);
        JsonNode attributes = jsonObject.get("attributes");

        for (JsonNode att : attributes) {
            JsonNode nameNode = att.get("name");
            JsonNode valueNode = att.get("value");

            if (nameNode != null && valueNode != null) {
                String name = nameNode.asText();
                String value = valueNode.asText();

                if ("URI".equals(name)) {
                    BaseURL = value;
                }
                if ("SP_ID".equals(name)) {
                    spId = value;
                }
                if ("SP_PASSWORD".equals(name)) {
                    spPassWord = value;
                }
                if ("CA_NAME".equals(name)) {
                    caName = value;
                }
            }
        }
    }

    public String getCertificate() throws Exception {
        String userId = "046088000025";

        String getCertificateURL = BaseURL + "sign_v2/get_certificate";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("sp_id", spId);
        requestData.put("sp_password", spPassWord);
        requestData.put("user_id", userId);
        requestData.put("ca_name", caName);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData);

        try {
            ResponseEntity<String> response = restTemplate.exchange(getCertificateURL, HttpMethod.POST, httpEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String sign() throws Exception {
        String userId = "046088000025";

        String timestamp = "";
        String SerialNumber = "28B59FA2C2C603079714B61F";
        String docId = "doc-2309271425";
        String fileType = "pdf";
        String signType = "hash";
        String dataToBeSigned = "9kO5fGipcF+6BZhh2lpteEHw3nab73hNpo4kXoddK1k=";

        String signUrl = BaseURL + "sign_v2/sign";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> signData = new HashMap<>();
        signData.put("doc_id", docId);
        signData.put("file_type", fileType);
        signData.put("sign_type", signType);
        signData.put("data_to_be_signed", dataToBeSigned);

        List<Map<String, Object>> signDataList = new ArrayList<>();
        signDataList.add(signData);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("sp_id", spId);
        requestData.put("sp_password", spPassWord);
        requestData.put("user_id", userId);
        requestData.put("ca_name", caName);
        requestData.put("sign_files", signDataList);

        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestData);

        try {
            ResponseEntity<String> response = restTemplate.exchange(signUrl, HttpMethod.POST, httpEntity, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
