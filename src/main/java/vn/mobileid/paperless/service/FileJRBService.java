/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.service;

import java.util.ArrayList;
import java.util.Base64;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import vn.mobileid.paperless.object.ConnectorName;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;

/**
 *
 * @author PHY
 */
public class FileJRBService {

    // //@RequestMapping(value = {"/uploadFile"}, method = RequestMethod.POST)
    // public static String uploadFile(InputStream request) throws SQLException,
    // Exception {
    // byte[] bytes = null;
    // String idFile = null;
    // String value_mimeType;
    // String value_FileName;
    // String value_FileType;
    // //String value_UUID;
    //
    // ArrayList<String> results = new ArrayList<String>();
    // //String fileName = request.get("fileName");
    // // Path path = new File(fileName).toPath();
    //
    // //String fileType = FilenameUtils.getExtension(fileName); // file type
    // //fileName = fileName.replaceAll(".pdf", "");
    // String fileName = "File_signer.pdf"; //file name
    // Path path = new File(fileName).toPath();
    // String mimeType = Files.probeContentType(path);
    // String sMimeType = mimeType; //mimetype
    // //String base64 = request.get("base64");
    // // byte[] decodedBytes =Base64.getDecoder().decode(base64);
    // //InputStream inputStream = new ByteArrayInputStream(decodedBytes);
    //
    // // if( base64 != null ){
    // JCRFile jcrFile = uploadPdf(fileName, sMimeType, request);
    // if (jcrFile != null) {
    // idFile = jcrFile.getUuid(); //uuid
    //// value_mimeType = sMimeType;
    //// value_FileName = fileName_new;
    //// value_FileType = fileType;
    //
    //// results.add(idFile);
    //// results.add(value_mimeType);
    //// results.add(value_FileName);
    //// results.add(value_FileType);
    // // }
    // }
    // return idFile;
    //
    // }
    public static String getPropertiesFMS() throws Exception {

        String sPropertiesFMS = "";
        ConnectorName[][] object = new ConnectorName[1][];
        ArrayList<ConnectorName> connector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        if (connector.size() > 0) {
            for (int m = 0; m < connector.size(); m++) {
                if (connector.get(m).CONNECTOR_NAME.equals(Difinitions.CONFIG_CONNECTOR_DMS_MOBILE_ID)) {
                    sPropertiesFMS = connector.get(m).IDENTIFIER;
                }
            }
        }
        return sPropertiesFMS;
    }

    // up file lên jackrabit
//    public static JCRFile uploadPdf(String fileNameToSave, String sMimeType, InputStream stream,
//                                    String sPropertiesFMS, String pathFile) {
//        JCRFile jrbFile = null;
//        try {
//            byte[] bytes = null;
//            String encoded = null;
//            String sJRB_Host = "";
//            String sJRB_UserID = "";
//            String sJRB_UserPass = "";
//            String sJRB_MaxSession = "";
//            String sJRB_MaxFileFolder = "";
//            String sJRB_PrefixFolder = "";
//            String sJRB_WorkSpace = "";
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            COMNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sPropertiesFMS, COMNECTOR_ATTRIBUTE.class);
//            for (COMNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_URI)) {
//                    sJRB_Host = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_USERNAME)) {
//                    sJRB_UserID = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_PASSWORD)) {
//                    sJRB_UserPass = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_MAXSESSION)) {
//                    sJRB_MaxSession = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_MAXFILEINFOLDER)) {
//                    sJRB_MaxFileFolder = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_FOLDER_PREFIX)) {
//                    sJRB_PrefixFolder = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_WORKSPACE)) {
//                    sJRB_WorkSpace = attribute.getValue();
//                }
//            }
//
//            JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass,
//                    Integer.parseInt(sJRB_MaxSession),
//                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//            // reopen stream to upload
//            InputStream testStream = new FileInputStream(pathFile);
//            jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(fileNameToSave, sMimeType, testStream);
//        } catch (Exception e) {
////            System.out.println(e.getMessage());
//            jrbFile = null;
//        }
//        return jrbFile;
//    }
    // up file lên jackrabit
//    public static JCRFile downloadFMS(String sUUID, String sPropertiesFMS) {
//        JCRFile jrbFile = null;
//        try {
//            String sJRB_Host = "";
//            String sJRB_UserID = "";
//            String sJRB_UserPass = "";
//            String sJRB_MaxSession = "";
//            String sJRB_MaxFileFolder = "";
//            String sJRB_PrefixFolder = "";
//            String sJRB_WorkSpace = "";
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            COMNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sPropertiesFMS, COMNECTOR_ATTRIBUTE.class);
//            for (COMNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_URI)) {
//                    sJRB_Host = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_USERNAME)) {
//                    sJRB_UserID = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_PASSWORD)) {
//                    sJRB_UserPass = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_MAXSESSION)) {
//                    sJRB_MaxSession = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_MAXFILEINFOLDER)) {
//                    sJRB_MaxFileFolder = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_FOLDER_PREFIX)) {
//                    sJRB_PrefixFolder = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_WORKSPACE)) {
//                    sJRB_WorkSpace = attribute.getValue();
//                }
//            }
//            JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass,
//                    Integer.parseInt(sJRB_MaxSession),
//                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//            jrbFile = JackRabbitCommon.getInstance(jcrConfig).downloadFile(sUUID);
//        } catch (Exception e) {
////            System.out.println(e.getMessage());
//            jrbFile = null;
//        }
//        return jrbFile;
//    }
    public static String downloadFMS(String sUUID) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dev-fms.mobile-id.vn/rssp.FMS/download/" + sUUID;

        String result = "";
        ResponseEntity<byte[]> responseEntity = null;
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);
        if (responseEntity.getBody() != null) {
            byte[] encode = Base64.getEncoder().encode(responseEntity.getBody());
            result = new String(encode);
        } else {
            result = null;
        }
        return result;
    }

    public static byte[] downloadFMS2(String sUUID) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dev-fms.mobile-id.vn/rssp.FMS/download/" + sUUID;

        String result = "";
        ResponseEntity<byte[]> responseEntity = null;
        responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);

        return responseEntity.getBody();
    }

    //    public static JCRFile uploadPdfToken(String fileNameToSave, String sMimeType, InputStream stream,
//                                         String sPropertiesFMS, byte[] pdfFile) {
//        JCRFile jrbFile = null;
//        try {
//            byte[] bytes = null;
//            String encoded = null;
//            String sJRB_Host = "";
//            String sJRB_UserID = "";
//            String sJRB_UserPass = "";
//            String sJRB_MaxSession = "";
//            String sJRB_MaxFileFolder = "";
//            String sJRB_PrefixFolder = "";
//            String sJRB_WorkSpace = "";
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            COMNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sPropertiesFMS, COMNECTOR_ATTRIBUTE.class);
//            for (COMNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_URI)) {
//                    sJRB_Host = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_USERNAME)) {
//                    sJRB_UserID = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_PASSWORD)) {
//                    sJRB_UserPass = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_MAXSESSION)) {
//                    sJRB_MaxSession = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_MAXFILEINFOLDER)) {
//                    sJRB_MaxFileFolder = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_FOLDER_PREFIX)) {
//                    sJRB_PrefixFolder = attribute.getValue();
//                }
//                if (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_WORKSPACE)) {
//                    sJRB_WorkSpace = attribute.getValue();
//                }
//            }
//
//            JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass,
//                    Integer.parseInt(sJRB_MaxSession),
//                    Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
//            // reopen stream to upload
//            InputStream inputStreamSigned = new ByteArrayInputStream(pdfFile);
//
//            jrbFile = JackRabbitCommon.getInstance(jcrConfig).uploadFile(fileNameToSave, sMimeType, inputStreamSigned);
//        } catch (Exception e) {
////            System.out.println(e.getMessage());
//            jrbFile = null;
//        }
//        return jrbFile;
//    }
    public static String uploadPdfToken(byte[] fileContents) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://dev-fms.mobile-id.vn/rssp.FMS/upload";
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-file-name", "Công ty co phan dich vu MobileID");
        headers.set("x-mime-type", "pdf");
        headers.set("Content-Type", "application/pdf");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String result = "";
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileContents, headers);
        ResponseEntity<String> responseEntity = null;
        responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (responseEntity.getBody() != null) {
//            System.out.println("body: "+responseEntity.getBody());
            result = responseEntity.getBody();
        } else {
//            System.out.println("Error while upload file");
            result = null;
        }
        return result;

    }

}
