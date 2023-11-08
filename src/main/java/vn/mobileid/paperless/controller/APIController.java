/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;

import vn.mobileid.fms.client.JCRException;

import vn.mobileid.paperless.repository.CommonRepository;
import vn.mobileid.paperless.service.FileJRBService;
import vn.mobileid.paperless.service.FpsService;
import vn.mobileid.paperless.service.VCStoringService;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.Itext7CommonFunction;
import vn.mobileid.paperless.utils.LoadParamSystem;

/**
 * @author PHY
 */
@RestController
public class APIController {

    public static String getSigningtoken;
    public static String value;
    public static int test;

    @Autowired
    private GatewayAPI gatewayAPI;

    @Autowired
    private VCStoringService vcStoringService;

    @Autowired
    private CommonRepository commonRepository;

    @Autowired
    private process connect;

    @Autowired
    private FpsService fpsService;

    private Logger logger = LoggerFactory.getLogger(APIController.class);

    @RequestMapping(value = {"/getVC"}, method = RequestMethod.GET)
    public String getVC(@RequestParam String requestID) {
        Long startTime = System.currentTimeMillis();
        try {
            while (true) {
                String VC = vcStoringService.get(requestID);
                if (VC != null) {
                    vcStoringService.remove(requestID);
                    return VC;
                } else {
                    Long endTime = System.currentTimeMillis();
                    if (endTime - startTime > 60000) {
                        return VC;
                    }
                    Thread.sleep(5000);
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = {"/getPrefix"}, method = RequestMethod.POST)
    public ArrayList getPrefix(@RequestBody Map<String, String> language) throws Exception {
        String sType = Difinitions.CONFIG_PREFIX_UID_PERSONAL_ID;
        String lang = language.get("lang");

        ArrayList list = new ArrayList();
        PREFIX_UID[][] rsFile = new PREFIX_UID[1][];
        connect.USP_GW_PREFIX_PERSONAL_CODE_LIST(rsFile, sType, lang);
        if (rsFile != null && rsFile[0].length > 0) {
            for (int i = 0; i < rsFile[0].length; i++) {
                Map<String, String> item = new HashMap<>();
                item.put("prefix", rsFile[0][i].NAME);
                item.put("type", rsFile[0][i].TYPE);
                item.put("remark", rsFile[0][i].REMARK);
                list.add(item);
            }

        }
        return list;

    }

    @RequestMapping(value = {"/getPrefixPhone"}, method = RequestMethod.POST)
    public ArrayList getPrefixPhone(@RequestBody Map<String, String> language) throws Exception {
        String sType = Difinitions.CONFIG_PREFIX_UID_PHONE_ID;
        String lang = language.get("lang");

        ArrayList list = new ArrayList();
        PREFIX_UID[][] rsFile = new PREFIX_UID[1][];
        connect.USP_GW_PREFIX_PERSONAL_CODE_LIST(rsFile, sType, lang);
        if (rsFile != null && rsFile[0].length > 0) {
            for (int i = 0; i < rsFile[0].length; i++) {
                Map<String, String> item = new HashMap<>();
                item.put("prefix", rsFile[0][i].NAME);
                item.put("type", rsFile[0][i].TYPE);
                item.put("remark", rsFile[0][i].REMARK);
                list.add(item);
            }

        }
        return list;

    }

    @RequestMapping(value = {"/base64Logo"}, method = RequestMethod.POST)
    public ArrayList<Object> base64Logo(@RequestBody Map<String, String> pPROVIDER) throws Exception {

        String connector_name;
        String logo;
        int a = 0;
        ArrayList<Object> list = new ArrayList<Object>();
        ConnectorName[][] rsFile = new ConnectorName[1][];
        String pPROVIDERs = pPROVIDER.get("param");
        ArrayList<ConnectorName> provider = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        ArrayList<CountryModel> countryList = LoadParamSystem.getParamCountry(Difinitions.CONFIG_LOAD_PARAM_COUNTRY);
        if (provider.size() > 0) {
            for (int m = 0; m < provider.size(); m++) {
                if (pPROVIDERs.equals(provider.get(m).PROVIDER) && !provider.get(m).PROVIDER.equals("ELECTRONIC_ID")) {
                    Map<String, String> map = new HashMap();
                    map.put("connector_name", provider.get(m).CONNECTOR_NAME);
                    map.put("logo", provider.get(m).LOGO);
                    map.put("remark", provider.get(m).REMARK);
                    list.add(map);
                }
            }
        }
        if (countryList.size() > 0) {
            for (int m = 0; m < countryList.size(); m++) {
                if ("ELECTRONIC_ID".equals(pPROVIDERs)) {
                    Map<String, String> map = new HashMap();
                    map.put("connector_name", "MOBILE_ID_IDENTITY");
                    map.put("logo", countryList.get(m).META_DATA);
                    map.put("remark", countryList.get(m).REMARK_EN);
                    list.add(map);
                }
            }
        }
        return list;
    }

    @RequestMapping(value = {"/headerFooter"}, method = RequestMethod.POST)
    public ArrayList<Object> headerFooter(@RequestBody Map<String, String> signingToken) throws Exception {

        String connector_name;
//        String logo;
        int a = 0;
//        String value = "";
        ENTERPRISE[][] rsFile = new ENTERPRISE[1][];
        ArrayList<Object> list = new ArrayList<Object>();
        String Signing_Token = signingToken.get("signingToken");
//        System.out.println("signingToken:" + Signing_Token);
        int enteriprise_id = 0;
        FirstFile[][] file = new FirstFile[1][];
        connect.USP_GW_PPL_WORKFLOW_GET_FIRST_FILE(file, Signing_Token);
        if (file != null && file[0].length > 0) {
            enteriprise_id = file[0][0].ENTERPRISE_ID;
        }
        ArrayList<ENTERPRISE> enterpriseID = LoadParamSystem.getParamEnterpriseStart(Difinitions.CONFIG_LOAD_PARAM_ENTERPRISE);
        if (enterpriseID.size() > 0) {
            for (int m = 0; m < enterpriseID.size(); m++) {
                if (enterpriseID.get(m).ID == enteriprise_id) {
                    Map<String, String> map = new HashMap();

                    String value = enterpriseID.get(m).METADATA_GATEWAY_VIEW;
                    String logo = enterpriseID.get(m).LOGO;
                    map.put("value", value);
                    map.put("logo", logo);
                    list.add(map);
                }
            }
        }

        return list;
    }

    @RequestMapping(value = {"/headerFooterOpen"}, method = RequestMethod.POST)
    public ArrayList<Object> headerFooterOpen(@RequestBody Map<String, String> uploadToken) throws Exception {

        String connector_name;
//        String logo;
        int enabled = 0;
//        String value = "";
        ENTERPRISE[][] rsFile = new ENTERPRISE[1][];
        //String pPROVIDERs = pPROVIDER.get("param");
        // System.out.println("pPROVIDERs" + pPROVIDERs);
        ArrayList<Object> list = new ArrayList<Object>();
        String upload_Token = uploadToken.get("upload_token");
        int enteriprise_id = 0;
        PPLFile[][] file = new PPLFile[1][];
        FirstFileFromUpLoadToken firstFileFromUpLoadToken = new FirstFileFromUpLoadToken();
        connect.USP_GW_PPL_FILE_GET(upload_Token, firstFileFromUpLoadToken);
        if (firstFileFromUpLoadToken != null && firstFileFromUpLoadToken.getEnterpriseId() > 0) {
            enteriprise_id = firstFileFromUpLoadToken.getEnterpriseId();
            enabled = firstFileFromUpLoadToken.getEnabled();
        }
        ArrayList<ENTERPRISE> enterpriseID = LoadParamSystem.getParamEnterpriseStart(Difinitions.CONFIG_LOAD_PARAM_ENTERPRISE);
        if (enterpriseID.size() > 0) {
            for (int m = 0; m < enterpriseID.size(); m++) {
                if (enterpriseID.get(m).ID == enteriprise_id) {
                    Map<String, String> map = new HashMap();
                    String value = enterpriseID.get(m).METADATA_GATEWAY_VIEW;
                    String logo = enterpriseID.get(m).LOGO;
                    map.put("value", value);
                    map.put("logo", logo);
                    map.put("enabled", String.valueOf(enabled));
                    list.add(map);
                }
            }
        }
        return list;
    }

    @RequestMapping(value = {"/CheckheaderFooterOpen"}, method = RequestMethod.POST)
    public int CheckheaderFooterOpen(@RequestBody Map<String, String> uploadToken) throws Exception {
        int enabled = 0;
        ENTERPRISE[][] rsFile = new ENTERPRISE[1][];
        ArrayList<Object> list = new ArrayList<Object>();
        String upload_Token = uploadToken.get("upload_token");
        FirstFileFromUpLoadToken firstFileFromUpLoadToken = new FirstFileFromUpLoadToken();
        connect.USP_GW_PPL_FILE_GET(upload_Token, firstFileFromUpLoadToken);
        if (firstFileFromUpLoadToken != null && firstFileFromUpLoadToken.getEnterpriseId() > 0) {
            enabled = firstFileFromUpLoadToken.getEnabled();
        }

        return enabled;
    }

    @RequestMapping(value = {"/headerfooterBatch"}, method = RequestMethod.POST)
    public ArrayList<Object> headerfooterBatch(@RequestBody Map<String, String> batchToken) throws Exception {

        String pBATCH_FILE_TOKEN = batchToken.get("batchToken");
        ENTERPRISE[][] rsFile = new ENTERPRISE[1][];
        ArrayList<Object> list = new ArrayList<Object>();
        connect.USP_GW_PPL_BATCH_FILE_GET_ENTERPRISE_INFO(rsFile, pBATCH_FILE_TOKEN);
        int idEnterprise = 0;
        ArrayList<ENTERPRISE> enterpriseID = LoadParamSystem.getParamEnterpriseStart(Difinitions.CONFIG_LOAD_PARAM_ENTERPRISE);
        if (enterpriseID.size() > 0) {
            idEnterprise = rsFile[0][0].ID;
            for (int m = 0; m < enterpriseID.size(); m++) {
                if (enterpriseID.get(m).ID == idEnterprise) {
                    Map<String, String> map = new HashMap();

                    String value = enterpriseID.get(m).METADATA_GATEWAY_VIEW;
                    String logo = enterpriseID.get(m).LOGO;
                    map.put("value", value);
                    map.put("logo", logo);
                    list.add(map);
                }
            }
        }

        return list;
    }

    @RequestMapping(value = {"/getSigning"}, method = RequestMethod.POST)
    public ResponseEntity<?> getSigning(@RequestBody Map<String, String> signingToken) throws Exception {

        ResponseEntity<byte[]> responseEntity = null;
        BATCH[][] rsFile = new BATCH[1][];

        String pBATCH_FILE_TOKEN = signingToken.get("batch_token");
        connect.USP_GW_PPL_BATCH_FILE_GET_WORKFLOW(rsFile, pBATCH_FILE_TOKEN);

        List<Map<String, Object>> workFlowList = new ArrayList<>();
        if (rsFile != null && rsFile[0].length > 0) {
//            ArrayList<Participants> listParticipants = new ArrayList<Participants>();
            for (int i = 0; i < rsFile[0].length; i++) {

                Participants[][] objectParticipants = new Participants[1][];
                LastFile[][] objectPPLFile = new LastFile[1][];

                connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_LIST(objectParticipants, rsFile[0][i].SIGNING_TOKEN);

                connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(objectPPLFile, rsFile[0][i].SIGNING_TOKEN);
                if (objectPPLFile[0].length > 0) {
                    for (int j = 0; j < objectPPLFile[0].length; j++) {
                        int documentId = objectPPLFile[0][j].getDOCUMENT_ID();
//                        byte[] jrbFile = FileJRBService.downloadFMS2(sUUID);
                        Map<String, Object> response = new HashMap<>();
                        response.put("fileId", objectPPLFile[0][j].getFIRST_PPL_FILE_SIGNED_ID());
                        response.put("lastFileId", objectPPLFile[0][j].getLAST_PPL_FILE_SIGNED_ID());
                        response.put("fileName", objectPPLFile[0][j].getLAST_PPL_FILE_NAME());
                        response.put("fileSize", objectPPLFile[0][j].getFILE_SIZE());
                        response.put("enterpriseId", objectPPLFile[0][j].getENTERPRISE_ID());

                        response.put("workFlowId", objectParticipants[0][j].PPL_WORKFLOW_ID);
                        response.put("documentName", objectPPLFile[0][j].getWORKFLOW_DOCUMENT_NAME());
                        response.put("signingToken", rsFile[0][i].SIGNING_TOKEN);
                        response.put("signerToken", rsFile[0][i].SIGNER_TOKEN);
                        response.put("documentId", documentId);
                        List<Map<String, Object>> listParticipants = new ArrayList<>();

                        if (objectParticipants[0].length > 0) {
                            for (int k = 0; k < objectParticipants[0].length; k++) {
                                Map<String, Object> participant = new HashMap<>();
                                participant.put("id", objectParticipants[0][j].ID);
                                participant.put("ppLWorkFlowId", objectParticipants[0][k].PPL_WORKFLOW_ID);
                                participant.put("firstName", objectParticipants[0][k].FIRST_NAME);
                                participant.put("lastName", objectParticipants[0][k].LAST_NAME);
                                participant.put("signerToken", objectParticipants[0][k].SIGNER_TOKEN);
                                participant.put("signingToken", rsFile[0][i].SIGNING_TOKEN);
                                participant.put("signerStatus", objectParticipants[0][k].SIGNER_STATUS);
                                participant.put("signedTime", objectParticipants[0][k].SIGNED_TIME == null ? null : new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(objectParticipants[0][k].SIGNED_TIME));
                                participant.put("metaInformation", objectParticipants[0][k].META_INFORMATION);
                                participant.put("signingPurpose", objectParticipants[0][k].SIGNING_PURPOSE);
                                participant.put("signedType", objectParticipants[0][k].SIGNED_TYPE);
                                participant.put("signingOptions", objectParticipants[0][k].SIGNING_OPTIONS);
                                participant.put("signerId", objectParticipants[0][k].SIGNER_ID);

                                String sIssue = "";
                                String sOwner = "";
                                String sFrom = "";
                                String sTo = "";
                                String sCertificate = CommonFunction.CheckTextNull(objectParticipants[0][j].CERTIFICATE);
                                if (!"".equals(sCertificate)) {
                                    ObjectMapper oMapperParse = new ObjectMapper();
                                    CertificateJson itemParse = oMapperParse.readValue(sCertificate, CertificateJson.class);
                                    if (itemParse != null) {
                                        sIssue = CommonFunction.CheckTextNull(itemParse.signer_info.certificate.issuer);
                                        sOwner = CommonFunction.CheckTextNull(itemParse.signer_info.certificate.subject);
                                        sFrom = itemParse.signer_info.certificate.valid_from;
                                        sTo = itemParse.signer_info.certificate.valid_to;
                                    }
                                }
                                if (!"".equals(sIssue)) {
                                    sIssue = CommonFunction.getCommonNameInDN(sIssue);
                                }
                                if (!"".equals(sOwner)) {
                                    sOwner = CommonFunction.getCommonNameInDN(sOwner);
                                }
//                            Map<String, Object> certificate = new HashMap<>();
                                participant.put("issuer", sIssue);
                                participant.put("owner", sOwner);
                                participant.put("validFrom", sFrom);
                                participant.put("validTo", sTo);

//                            listCertificate.add(certificate);
                                listParticipants.add(participant);
                            }
                        }

//                    response.put("certificates", listCertificate);
                        response.put("participants", listParticipants);

                        workFlowList.add(response);
                    }
                }
            }
            return new ResponseEntity<>(workFlowList, HttpStatus.OK);
        }
//        ArrayList<BATCH> tempList;
//        tempList = new ArrayList<>();
//        tempList.addAll(Arrays.asList(rsFile[0]));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = {"/getSigningWorkFlow"}, method = RequestMethod.POST)
    public ResponseEntity<?> getSigningWorkFlow(@RequestBody Map<String, String> signingToken) throws Exception {

        String pSIGNING_TOKEN = signingToken.get("signingToken");
        System.out.println("pSIGNING_TOKEN:" + pSIGNING_TOKEN);
//        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<byte[]> responseEntity = null;
        String result = "";
        Participants[][] objectParticipants = new Participants[1][];
        LastFile[][] objectPPLFile = new LastFile[1][];

        connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_LIST(objectParticipants, pSIGNING_TOKEN);

//        FileFirst[][] rsFile = new FileFirst[1][];
//        String pSIGNING_TOKEN = signingToken.get("signingToken");
        connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(objectPPLFile, pSIGNING_TOKEN);

        if (objectPPLFile[0].length > 0) {
            for (int i = 0; i < objectPPLFile[0].length; i++) {

//                int documentId = fpsService.getDocumentId(sUUID);
                int documentId = objectPPLFile[0][i].getDOCUMENT_ID();

//                byte[] jrbFile = FileJRBService.downloadFMS2(sUUID);
//                byte[] jrbFile = gatewayAPI.getFileFromUploadToken(uploadToken);
//                String base64Document = fpsService.getImageBasse64(documentId).getFile_data();
//                if (jrbFile != null) {
//                    byte[] bytes = IOUtils.toByteArray(jrbFile.getStream());
//                    byte[] newBytes = Itext7CommonFunction.RemoveSignaturesFromDocument(jrbFile);
//                    String base64Encoded = Base64.getEncoder().encodeToString(jrbFile);
//                    String base64Encoded = jrbFile;
//                    int fileSize = Base64.getDecoder().decode(base64Document).length;
//                    String sFileSize = Integer.toString(fileSize);
                Map<String, Object> response = new HashMap<>();

                response.put("fileId", objectPPLFile[0][i].getFIRST_PPL_FILE_SIGNED_ID());

//                    response.put("uuid", sUUID);
//                    response.put("base64", base64Encoded);
//                    response.put("base64", base64Document);
                response.put("lastFileId", objectPPLFile[0][i].getLAST_PPL_FILE_SIGNED_ID());
                response.put("fileName", objectPPLFile[0][i].getLAST_PPL_FILE_NAME());
                response.put("fileSize", objectPPLFile[0][i].getFILE_SIZE());
                response.put("enterpriseId", objectPPLFile[0][i].getENTERPRISE_ID());
                response.put("workFlowId", objectParticipants[0][i].PPL_WORKFLOW_ID);
                response.put("documentName", objectPPLFile[0][i].getWORKFLOW_DOCUMENT_NAME());
                response.put("signingToken", pSIGNING_TOKEN);
                response.put("documentId", documentId);

                List<Map<String, Object>> listParticipants = new ArrayList<>();
                List<Map<String, Object>> listCertificate = new ArrayList<>();

                if (objectParticipants[0].length > 0) {
                    for (int j = 0; j < objectParticipants[0].length; j++) {
                        Map<String, Object> participant = new HashMap<>();
                        participant.put("id", objectParticipants[0][j].ID);
                        participant.put("ppLWorkFlowId", objectParticipants[0][j].PPL_WORKFLOW_ID);
                        participant.put("firstName", objectParticipants[0][j].FIRST_NAME);
                        participant.put("lastName", objectParticipants[0][j].LAST_NAME);
                        participant.put("signerToken", objectParticipants[0][j].SIGNER_TOKEN);
                        participant.put("signingToken", pSIGNING_TOKEN);
                        participant.put("signerStatus", objectParticipants[0][j].SIGNER_STATUS);
                        participant.put("signedTime", objectParticipants[0][j].SIGNED_TIME == null ? null : new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(objectParticipants[0][j].SIGNED_TIME));
                        participant.put("metaInformation", objectParticipants[0][j].META_INFORMATION);
                        participant.put("signingPurpose", objectParticipants[0][j].SIGNING_PURPOSE);
                        participant.put("signedType", objectParticipants[0][j].SIGNED_TYPE);
                        participant.put("signingOptions", objectParticipants[0][j].SIGNING_OPTIONS);
                        participant.put("signerId", objectParticipants[0][j].SIGNER_ID);

                        String sIssue = "";
                        String sOwner = "";
                        String sFrom = "";
                        String sTo = "";
                        String sCertificate = CommonFunction.CheckTextNull(objectParticipants[0][j].CERTIFICATE);
//                            System.out.println("sCertificate:" + sCertificate);
                        if (!"".equals(sCertificate)) {
                            ObjectMapper oMapperParse = new ObjectMapper();
                            CertificateJson itemParse = oMapperParse.readValue(sCertificate, CertificateJson.class);
                            if (itemParse != null) {
                                sIssue = CommonFunction.CheckTextNull(itemParse.signer_info.certificate.issuer);
                                sOwner = CommonFunction.CheckTextNull(itemParse.signer_info.certificate.subject);
                                sFrom = itemParse.signer_info.certificate.valid_from;
                                sTo = itemParse.signer_info.certificate.valid_to;
                            }
                        }
                        if (!"".equals(sIssue)) {
                            sIssue = CommonFunction.getCommonNameInDN(sIssue);
                        }
                        if (!"".equals(sOwner)) {
                            sOwner = CommonFunction.getCommonNameInDN(sOwner);
                        }
//                            Map<String, Object> certificate = new HashMap<>();
                        participant.put("issuer", sIssue);
                        participant.put("owner", sOwner);
                        participant.put("validFrom", sFrom);
                        participant.put("validTo", sTo);

//                            listCertificate.add(certificate);
                        listParticipants.add(participant);
                    }
                }

//                    response.put("certificates", listCertificate);
                response.put("participants", listParticipants);

                return new ResponseEntity<>(response, HttpStatus.OK);
//                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = {"/getSignedInfo"}, method = RequestMethod.POST)
    public ResponseEntity<?> getSignedInfo(@RequestBody Map<String, String> fileId) throws Exception {
        int pFileId = Integer.parseInt(fileId.get("fileId"));
//        System.out.println("pSIGNING_TOKEN:" + pSIGNING_TOKEN);
        List<PplFileDetail> listPplFileDetail = new ArrayList<>();

        connect.USP_GW_PPL_FILE_DETAIL_GET_SIGNATURE(pFileId, listPplFileDetail);

        return new ResponseEntity<>(listPplFileDetail, HttpStatus.OK);
    }

    @RequestMapping(value = {"/getFileInfo"}, method = RequestMethod.POST)
    public ResponseEntity<?> getFileInfo(@RequestBody Map<String, String> upload_token) throws Exception {
        String pUpload_token = upload_token.get("upload_token");
//        System.out.println("pSIGNING_TOKEN:" + pSIGNING_TOKEN);
        List<PplFileDetail> listPplFileDetail = new ArrayList<>();

        connect.USP_GW_PPL_FILE_DETAIL_GET_FROM_UPLOAD_TOKEN(pUpload_token, listPplFileDetail);

        return new ResponseEntity<>(listPplFileDetail, HttpStatus.OK);
    }

    @RequestMapping(value = {"/getFirstFileFromUploadToken"}, method = RequestMethod.POST)
    public ResponseEntity<?> getFirstFileFromUploadToken(@RequestBody Map<String, String> upload_token) throws Exception {
        String pUpload_token = upload_token.get("upload_token");
//        System.out.println("pSIGNING_TOKEN:" + pSIGNING_TOKEN);
        FirstFileFromUpLoadToken firstFileFromUpLoadToken = new FirstFileFromUpLoadToken();

//        byte[] file = gatewayAPI.getFileFromUploadToken(pUpload_token);
//
//        if(file != null){
//            byte[] newBytes = Itext7CommonFunction.RemoveSignaturesFromDocument(file);
//            firstFileFromUpLoadToken.setBase64(Base64.getEncoder().encodeToString(newBytes));
//        }
        connect.USP_GW_PPL_FILE_GET(pUpload_token, firstFileFromUpLoadToken);
        System.out.println("Uuid:" + firstFileFromUpLoadToken.getFileUuid());
//        String uploadToken = firstFileFromUpLoadToken.getUploadToken();

        if (firstFileFromUpLoadToken != null) {
            String sPropertiesFMS = FileJRBService.getPropertiesFMS();
//            byte[] jrbFile = FileJRBService.downloadFMS2(firstFileFromUpLoadToken.getFileUuid());
            byte[] jrbFile = gatewayAPI.getFileFromUploadToken(pUpload_token);

            if (jrbFile != null) {
                // convert inputstream to base64

//                byte[] bytes = IOUtils.toByteArray(jrbFile.getStream());
//
                byte[] newBytes = Itext7CommonFunction.RemoveSignaturesFromDocument(jrbFile);

                firstFileFromUpLoadToken.setBase64(Base64.getEncoder().encodeToString(newBytes));
//                firstFileFromUpLoadToken.setBase64(jrbFile);

            }
        }
        return new ResponseEntity<>(firstFileFromUpLoadToken, HttpStatus.OK);
    }

    @RequestMapping(value = {"/getDLLUSBToken"}, method = RequestMethod.POST)
    public String getDLLUSBToken(@RequestBody Map<String, String> connectorName)
            throws JCRException, SQLException, Exception {

        String dllUSBToken = "";
        String sPropertiesFMS = "";

        ConnectorName[][] object = new ConnectorName[1][];
        String cnt = connectorName.get("connector");
        ArrayList<ConnectorName> conector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        if (conector.size() > 0) {
            for (int m = 0; m < conector.size(); m++) {
                if (conector.get(m).CONNECTOR_NAME.equals(cnt)) {
                    sPropertiesFMS = conector.get(m).IDENTIFIER;
                }
            }
            JsonObject jsonObject = new JsonParser().parse(sPropertiesFMS).getAsJsonObject();
            JsonArray arr = jsonObject.getAsJsonArray("attributes");
            ArrayList list = new ArrayList();
            for (int j = 0; j < arr.size(); j++) {
                dllUSBToken = arr.get(j).getAsJsonObject().get("value").toString();
            }
        }
        return dllUSBToken; //
    }

    @RequestMapping(value = {"/checkHeader"}, method = RequestMethod.POST)
    public Map<String, Object> checkHeader(@RequestBody Map<String, String> signingToken) throws Exception {

        String pSIGNING_TOKEN = signingToken.get("signingToken");
        logger.info("pSIGNING_TOKEN : " + pSIGNING_TOKEN);
//        ArrayList<Object> intt = new ArrayList<Object>();
        WorkFlowList[][] rsParticipant = new WorkFlowList[1][];
        connect.USP_GW_PPL_WORKFLOW_GET(rsParticipant, pSIGNING_TOKEN);
        Map<String, Object> map = new HashMap();
        if (rsParticipant != null && rsParticipant[0].length > 0) {
            for (int j = 0; j < rsParticipant[0].length; j++) {

                map.put("documentName", rsParticipant[0][j].WORKFLOW_DOCUMENT_NAME);
                map.put("documentFormat", rsParticipant[0][j].WORKFLOW_DOCUMENT_FORMAT);
                map.put("visibleHeaderFooter", rsParticipant[0][j].VISIBLE_HEADER_FOOTER);
//                intt.add(map);
            }
        }
        return map;
    }

    @RequestMapping(value = {"/checkHeaderBS"}, method = RequestMethod.POST)
    public int checkHeaderBS(@RequestBody Map<String, String> batchToken) throws Exception {

        int isSet = 0;
        String pBATCH_TOKEN = batchToken.get("batchToken");
        BATCH[][] batch = new BATCH[1][];
        ArrayList<Object> list = new ArrayList<Object>();
        connect.USP_GW_PPL_BATCH_FILE_GET(batch, pBATCH_TOKEN);
        if (batch != null && batch[0].length > 0) {
            for (int i = 0; i < batch[0].length; i++) {
                isSet = batch[0][i].VISIBLE_HEADER_FOOTER;
            }
        }
        return isSet;
    }

    @RequestMapping(value = {"/getCheckValid"}, method = RequestMethod.POST)
    public int getCheckValid(@RequestBody Map<String, String> param) throws Exception {

        int[] pIS_EXIST = new int[1];
        String signingToken = param.get("signingToken");
        String signerToken = param.get("signerToken");
        String a = connect.USP_GW_SIGNER_CHECK_EXIST(pIS_EXIST, signingToken, signerToken);

        return pIS_EXIST[0];
    }

    @RequestMapping(value = {"/getVCEnabled"}, method = RequestMethod.POST)
    public static boolean getVCEnabled(@RequestBody Map<String, String> connectorName)
            throws JCRException, SQLException, Exception {
        boolean VERIFICATION_CODE_ENABLED = false;
        String sPropertiesFMS = "";
        String cnt = connectorName.get("connectorName");
        ArrayList<ConnectorName> provider = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONNECTOR_NAME);
        if (provider.size() > 0) {
            for (int m = 0; m < provider.size(); m++) {
                if (provider.get(m).CONNECTOR_NAME.equals(cnt)) {
                    sPropertiesFMS = provider.get(m).IDENTIFIER;
                }
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        CONNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sPropertiesFMS, CONNECTOR_ATTRIBUTE.class);
        for (CONNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
            if (attribute.getName().equals(Difinitions.CONFIG_WORKFLOW_VERIFICATION_CODE_ENABLED)) {
                VERIFICATION_CODE_ENABLED = Boolean.parseBoolean(attribute.getValue());
            }
        }
        return VERIFICATION_CODE_ENABLED;
    }

    @RequestMapping(value = {"/connectorLogAdd"}, method = RequestMethod.POST)
    public void connectorLogAdd(@RequestBody ConnectorLogRequest connectorLogRequest) throws Exception {
        commonRepository.connectorLog(connectorLogRequest);
    }

    @RequestMapping(value = {"/download/checkid"}, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadCheckId() throws IOException {
        // Đọc file checkid.exe từ thư mục tài nguyên tĩnh
        Resource resource = new ClassPathResource("static/checkid_client_installer.exe");
//        Resource resource = new UrlResource("static/checkid.zip");
        // Trả về file dưới dạng response để người dùng tải về
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"checkid_client_installer.exe\"")
                .body(resource);
    }

}
