package vn.mobileid.paperless.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import vn.mobileid.aws.client.AWSV4Auth;
import vn.mobileid.aws.client.AWSV4Constants;
import vn.mobileid.paperless.aws.datatypes.PadesConstants;
import vn.mobileid.paperless.aws.datatypes.PrintResponse;
import vn.mobileid.paperless.aws.response.TokenResponse;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

@Component
public class AWSCall {

    final protected static String FILE_DIRECTORY_PDF = "file/";
    private URL url;
    private String httpMethod;
    private String accessKey;
    private String secretKey;
    private String regionName;
    private String serviceName;
    private int timeOut;
    private String xApiKey;
    private String contentType;
    public String sessionToken;
    public String bearerToken;
    private TreeMap<String, String> awsHeaders;
    private AWSV4Auth.Builder builder;
    private Gson gson = new Gson();

    public AWSCall() {
    }

    public AWSCall(String baseUrl, String httpMethod, String accessKey,
                   String secretKey, String regionName,
                   String serviceName, int timeOut,
                   String xApiKey, String contentType, TreeMap<String, String> queryParametes) throws MalformedURLException {
        this.httpMethod = httpMethod;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.regionName = regionName;
        this.serviceName = serviceName;
        this.timeOut = timeOut;
        this.xApiKey = xApiKey;
        this.contentType = contentType;
        this.url = new URL(baseUrl);

        this.awsHeaders = new TreeMap<>();
        awsHeaders.put(AWSV4Constants.X_API_KEY, this.xApiKey);
        awsHeaders.put(AWSV4Constants.CONTENT_TYPE, this.contentType);

        this.builder = new AWSV4Auth.Builder(accessKey, secretKey)
                .regionName(regionName)
                .serviceName(serviceName)
                .httpMethodName(httpMethod)// GET, PUT, POST, DELETE
                .queryParametes(queryParametes) // query parameters if any
                .awsHeaders(awsHeaders); // aws header parameters
    }

    //AWS4Auth
    public Map<String, String> getAWSV4Auth(String payload, String function, String token) throws MalformedURLException {
        this.awsHeaders.put(PadesConstants.SESSION_TOKEN, token);
        System.out.println("url: " + this.url);
        AWSV4Auth aWSV4Auth = this.builder
                .endpointURI(new URL(this.url.toString())) //https://id.mobile-id.vn/dtis/v1/e-verification/oidc/token
                .payload(payload)
                .build();
        return aWSV4Auth.getHeaders();
    }

    public Map<String, String> getAWSV4Auth(String payload, String function, String token, File file) throws MalformedURLException {
        this.awsHeaders.put(PadesConstants.SESSION_TOKEN, token);
        AWSV4Auth aWSV4Auth = this.builder
                .endpointURI(new URL(this.url.toString())) //https://id.mobile-id.vn/dtis/v1/e-verification/oidc/token
                .payload(payload)
                .addFile("document", file)
                .build();
        return aWSV4Auth.getHeaders();
    }

    // v1/e-verification/oidc/token
    public String v1VeriOidcToken(String function, String token) throws MalformedURLException, IOException {
        //System.out.println(this.bearerToken);
        //Send Post
        String response = HttpUtilsAWS.invokeHttpRequest(
                this.url = new URL(PadesConstants.BASE_URL + function),
                this.httpMethod,
                this.timeOut,
                getAWSV4Auth(null, function, token),
                null);

        //Response
        System.out.println("response" + response);
        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse tokenResponse = gson.fromJson(response, TokenResponse.class);
        //Past Bearer for Step 2 (E-verification/pades)
        this.bearerToken = "Bearer " + tokenResponse.access_token;
        System.out.println("Bearer Token: " + this.bearerToken);
        PrintResponse.printRespOdicToken(tokenResponse);
        return this.bearerToken;
    }


    public Map<String, String> getAWSV4AuthForFormData(String payload, String token, File file) throws MalformedURLException {
        this.awsHeaders.put(PadesConstants.SESSION_TOKEN, token);
        AWSV4Auth aWSV4Auth = this.builder
                .endpointURI(new URL(this.url.toString())) //https://id.mobile-id.vn/dtis/v1/e-verification/oidc/token
                .payload(payload)
                .addFile("document", file)
                .build();
        return aWSV4Auth.getHeaders();
    }

    public Map<String, String> getAWSV4AuthForFormDataReal(String payload, String token, File file, String boundary) throws MalformedURLException {
        this.awsHeaders.put(PadesConstants.SESSION_TOKEN, token);
        this.awsHeaders.put(AWSV4Constants.CONTENT_TYPE, "multipart/form-data; boundary=" + boundary);
        System.out.println("CONTENT_TYPE: " + this.awsHeaders.get(AWSV4Constants.CONTENT_TYPE));
        AWSV4Auth aWSV4Auth = this.builder
                .endpointURI(new URL(this.url.toString())) //https://id.mobile-id.vn/dtis/v1/e-verification/oidc/token
                .payload(payload)
//                .addFile("document", file)
                .build();
        return aWSV4Auth.getHeaders();
    }

}
