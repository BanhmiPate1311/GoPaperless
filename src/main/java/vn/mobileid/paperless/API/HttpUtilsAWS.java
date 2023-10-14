package vn.mobileid.paperless.API;

import vn.mobileid.paperless.aws.MultipartUtility;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class HttpUtilsAWS {

    public static String invokeHttpRequest(URL endpointUrl,
                                           String httpMethod,
                                           int timeout,
                                           Map<String, String> headers,
                                           String requestBody) {

        if (httpMethod.equals("GET")
                && !Utils.isNullOrEmpty(requestBody)) {
            String url = endpointUrl.toString();
            try {
                endpointUrl = new URL(url.concat("?request_data_base64=").concat(Base64.getUrlEncoder().encodeToString(requestBody.getBytes("UTF-8"))));
                requestBody = null;
            } catch (Exception ex) {
                throw new RuntimeException("Request failed. " + ex.getMessage(), ex);
            }
        }

        HttpURLConnection connection = createHttpConnection(endpointUrl, httpMethod, timeout, headers);

        try {
//            if (requestBody != null) {
//                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
//                wr.writeBytes(requestBody);
//                wr.flush();
//                wr.close();
//            }
            if (requestBody != null) {
                try (BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8))) {

                    wr.write(requestBody);
                    wr.flush();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        }
        return executeHttpRequest(connection);
    }

    public static String executeHttpRequest(HttpURLConnection connection) {
        try {
            System.out.println(connection.getResponseCode());
            InputStream is;
            try {
                is = connection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                is = connection.getErrorStream();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException("Request failed. " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static HttpURLConnection createHttpConnection(URL endpointUrl,
                                                         String httpMethod,
                                                         int timeout,
                                                         Map<String, String> headers) {
        try {
            HttpURLConnection connection = (HttpURLConnection) endpointUrl.openConnection();
            connection.setRequestMethod(httpMethod);
            connection.setRequestProperty("User-Agent", "PostmanRuntime/7.32.2");
            if (headers != null) {
                for (String headerKey : headers.keySet()) {
                    connection.setRequestProperty(headerKey, headers.get(headerKey));
                }
            }
            //System.out.println("httpMethod: "+httpMethod);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create connection. " + e.getMessage(), e);
        }
    }

    public static String invokeHttpMutltiPartRequest(
            URL endpointUrl,
            String httpMethod,
            int timeout,
            Map<String, String> headers,
            String requestBody,
            File document, String boundary) throws Exception {
        String responseString = "";
        MultipartUtility multipart = new MultipartUtility(endpointUrl, headers, "UTF-8", boundary, httpMethod);
        if (document != null) {
            multipart.addFilePart("document", document);
        }
//        multipart.addFilePart("document", document);
        if (requestBody != null) {
            multipart.addFormField("payload", requestBody);
        }
//        multipart.addFormField("payload", requestBody);
        List<String> response = multipart.finish();
        for (String line : response) {
            responseString += line;
        }
        return responseString;
    }

//    public static String invokeHttpMutltiPartRequest(
//            URL endpointUrl,
//            String httpMethod,
//            int timeout,
//            Map<String, String> headers,
//            Map<String, File> filesForPayload,
//            Map<String, String> textsForPayload,
//            String boundary) throws Exception {
//        String responseString = "";
//        MultipartUtility multipart = new MultipartUtility(endpointUrl, headers, "UTF-8", boundary, httpMethod);
//
//        if (filesForPayload != null) {
//            for (Map.Entry<String, File> entry : filesForPayload.entrySet()) {
//                multipart.addFilePart(entry.getKey(), entry.getValue());
//            }
//        }
//
//        if (textsForPayload != null) {
//            for (Map.Entry<String, String> entry : textsForPayload.entrySet()) {
//                multipart.addFormField(entry.getKey(), entry.getValue());
//            }
//        }
//
//        List<String> response = multipart.finish();
//        for (String line : response) {
//            responseString += line;
//        }
//        return responseString;
//    }
}