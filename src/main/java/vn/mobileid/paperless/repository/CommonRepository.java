package vn.mobileid.paperless.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.pdf.BaseFont;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.mobileid.exsig.*;
import vn.mobileid.paperless.API.SigningMethodAsyncImp;
import vn.mobileid.paperless.entity.SignPosition;
import vn.mobileid.paperless.object.*;
import vn.mobileid.paperless.process.process;
import vn.mobileid.paperless.service.FileJRBService;
import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.Difinitions;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CommonRepository {

    @Autowired
    private process connect;

    @Value("${dev.mode}")
    private boolean devMode;

    public byte[] packFile(String certEncode, List<String> signatures, String cerId) throws Exception {
        SigningMethodAsyncImp signFinal = new SigningMethodAsyncImp();
        List<String> chain = new ArrayList<>();
        chain.add(certEncode);
        signFinal.certificateChain = chain;
        signFinal.signatures = signatures;

        byte[] temp = signFinal.loadTemporalData(cerId);

        List<byte[]> results = PdfProfileCMS.sign(signFinal, temp);
        byte[] pdfSigned = null;
        for (byte[] result : results) {
//             save file signed
            if (devMode) {
                OutputStream OS = Files.newOutputStream(Paths.get("d:/project/file/sample.signed.pdf"));

                IOUtils.write(result, OS);
                System.out.println("Signed File Successfully ! Save in:d:/project/file/sample.signed.pdf");
                OS.close();
            }

            pdfSigned = result;

        }
        return pdfSigned;
    }

    public void connectorLog(
            ConnectorLogRequest connectorLogRequest
    ) throws Exception {
        try {

            String result = connect.USP_GW_PPL_CONNECTOR_LOG_ADD(
                    null,
                    connectorLogRequest.getpCONNECTOR_NAME(),
                    connectorLogRequest.getpENTERPRISE_ID(),
                    connectorLogRequest.getpWORKFLOW_ID(),
                    null, null, null, null,
                    connectorLogRequest.getpURL(),
                    connectorLogRequest.getpHTTP_VERB(),
                    connectorLogRequest.getpSTATUS_CODE(),
                    connectorLogRequest.getpREQUEST(),
                    connectorLogRequest.getpRESPONSE(), null, "Gateway");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void callbackLog(
            CallBackLogRequest callBackLogRequest
    ) throws Exception {
        try {
            String result = connect.USP_GW_PPL_CALLBACK_LOG_ADD(null,
                    callBackLogRequest.getpENTERPRISE_ID(),
                    callBackLogRequest.getpWORKFLOW_ID(),
                    null, null, null, null,
                    callBackLogRequest.getpURL(),
                    callBackLogRequest.getpHTTP_VERB(),
                    callBackLogRequest.getpSTATUS_CODE(),
                    callBackLogRequest.getpREQUEST(),
                    callBackLogRequest.getpRESPONSE(),
                    0, null, null, "Gateway");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void postBack(
            CallBackLogRequest callBackLogRequest,
            Participants[][] rsParticipant,
            byte[] pdfSigned,
            String fileName,
            String signingToken,
            String pDMS_PROPERTY,
            String signatureId,
            String signerToken,
            Timestamp tsTimeSigned,
            WorkFlowList[][] rsWFList,
            int sFileID_Last,
            String certEncode,
            String serialNumber,
            String signingOption,
            String sType,
            HttpServletRequest request
    ) throws Exception {

        String meta = rsParticipant[0][0].META_INFORMATION;
        JsonObject jsonObject = new Gson().fromJson(meta, JsonObject.class);

        int isSetPosition = 0;
        if (jsonObject != null && jsonObject.has("pdf")) {
            JsonObject pdfObject = jsonObject.getAsJsonObject("pdf");

            JsonElement annotationElement = pdfObject.get("annotation");
            if (annotationElement != null) {
                JsonObject annotationObject = annotationElement.getAsJsonObject();
                if (annotationObject.has("top") && annotationObject.has("left")) {
                    isSetPosition = 1;
                }
            }
        }

        InputStream inputStreamSigned = new ByteArrayInputStream(pdfSigned);

        String fileName1 = fileName.replace(".pdf", "");

        String fileName_Signed = fileName1 + "_" + CommonFunction.generateNumberDays() + "_signed"
                + ".pdf";

        Path path = new File(fileName_Signed).toPath();
        String mimeType = Files.probeContentType(path);

        String sBase64 = "";
        try {
            sBase64 = DatatypeConverter.printBase64Binary(pdfSigned);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String fileDigest = Hex.encodeHexString(
                CommonFunction.hashData(pdfSigned, CommonFunction.HASH_SHA256));
        String sUUID = "";
        UUID uuid = UUID.randomUUID();
        String uploadToken = CommonFunction.getCryptoHash(uuid.toString());
        String fileType = FilenameUtils.getExtension(fileName_Signed);
        String pHMAC = "";
        String pCREATED_BY = "";
        String pURL = "";
        String SIGNATURE_TYPE = "qes";
        int enteriprise_id = 0;
        int[] pFILE_ID = new int[1];
        FirstFile[][] file = new FirstFile[1][];

        connect.USP_GW_PPL_WORKFLOW_GET_FIRST_FILE(file, signingToken);
        if (file != null && file[0].length > 0) {
            enteriprise_id = file[0][0].ENTERPRISE_ID;
        }

        String sInsertFile = connect.USP_GW_PPL_FILE_ADD(enteriprise_id, fileName_Signed, pdfSigned.length,
                Difinitions.CONFIG_PPL_FILE_STATUS_PENDING,
                pURL, fileType, mimeType, fileDigest, sBase64, sUUID, pDMS_PROPERTY, uploadToken,
                pHMAC, pCREATED_BY, pFILE_ID);
        if ("1".equals(sInsertFile)) {
            // Upload jackrabit
            String jcr = FileJRBService.uploadPdfToken(pdfSigned);
            Gson gson = new Gson();
            if (jcr != null) {
//                JsonObject json = new JsonParser().parse(jcr).getAsJsonObject();
//                System.out.println("json check: " + json);
//                sUUID = json.getAsJsonObject().get("UUID").toString();
//                System.out.println("UUID: " + sUUID);

                JsonObject jsonObject1 = gson.fromJson(jcr, JsonObject.class);
                sUUID = jsonObject1.get("UUID").getAsString();
                System.out.println("UUID: " + sUUID);
//                if (jsonObject.get("status").getAsInt() != 0) {
//                    throw new Exception(jsonObject.get("message").getAsString());
//                }
//                                    String signingOption = "";
                String sAction = "signer_signed";
                String sSigner = CommonFunction.CheckTextNull(rsParticipant[0][0].SIGNER_ID);
                String sStatus = "ok";
                String sFileSigner = "";
                String sFileComplete = "";
                String sCountryCode = "vn";
                // String pSIGNED_TIME = "";
                String pSIGNED_ALGORITHM = "";
//                                    String[] sResultConnector = new String[2];
//                                    String pIdentierConnector = connectDB.getIdentierConnector(connectorName, sResultConnector);
//                                    String prefixCode = sResultConnector[1];
//                                    long millis = System.currentTimeMillis();
//                                    String sSignatureHash = signerToken + millis;
                String sSignature_id = signatureId;
                try {
//                                        byte[] byteSigned = IOUtils.toByteArray(new FileInputStream(sFileSigned));
                    List<VerifyResult> vrf = PdfProfile.verify(pdfSigned, false);
                    for (VerifyResult veryfy : vrf) {
                        //pSIGNED_TIME = veryfy.getSigningTimes();
                        pSIGNED_ALGORITHM = veryfy.getAlgorithm();
                        //System.out.println("Time Signed: " + pSIGNED_TIME);
                        System.out.println("ALGORITHM Signed: " + pSIGNED_ALGORITHM);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                String uuid2 = sUUID.replace("\"", "");
                System.out.println("uuid2: " + uuid2);
                // update file signed
                String sUpdateFile = connect.USP_GW_PPL_FILE_UPDATE(pFILE_ID[0],
                        Difinitions.CONFIG_PPL_FILE_STATUS_UPLOADED,
                        "", "", "", "", "", "", "", sUUID, pDMS_PROPERTY, "", "");
                connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_UPDATE_STATUS(signerToken,
                        Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_SIGNED, "", isSetPosition);
                int pPPL_WORKFLOW_ID = rsWFList[0][0].ID;// sStatusWFCheck[0];
                connect.USP_GW_PPL_WORKFLOW_FILE_ADD(pPPL_WORKFLOW_ID, pFILE_ID[0], Difinitions.CONFIG_WORKFLOW_FILE_SIGNED_FILE, "", sFileID_Last, "", "");

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String sDateSign = "";
                    if (tsTimeSigned != null) {
                        sDateSign = formatter.format(tsTimeSigned);
                    }
                    rsWFList = new WorkFlowList[1][];
                    connect.USP_GW_PPL_WORKFLOW_GET(rsWFList, signingToken);
                    // khởi tạo request
                    String protocol = request.getHeader("X-Forwarded-Proto");
                    if (protocol == null) {
                        protocol = request.getScheme(); // fallback to default scheme
                    }

                    sFileSigner = protocol + "://" + request.getHeader("host") + "/api/signing/"
                            + signingToken + "/download/" + sSigner;
//                    String sType = Difinitions.CONFIG_PREFIX_UID_TOKEN;
                    String sJsonCertResult = CommonFunction.JsonCertificateObject(certEncode,
                            sType, serialNumber, sDateSign, signingOption, sAction, signingToken,
                            sSigner, sStatus, sFileSigner, fileDigest, sSignature_id, sCountryCode);
                    if (!"".equals(rsWFList[0][0].POSTBACK_URL)) {
                        CommonFunction.PostBackJsonCertificateObject(callBackLogRequest, rsWFList[0][0].POSTBACK_URL, certEncode,
                                sType, serialNumber, sDateSign, signingOption, sAction, signingToken,
                                sSigner, sStatus, sFileSigner, fileDigest, sSignature_id, sCountryCode);
                        callbackLog(callBackLogRequest);
                    }
                    String signedType = "NORMAL";
                    connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_UPDATE(signerToken,
                            signedType, tsTimeSigned, sSignature_id, pSIGNED_ALGORITHM, sJsonCertResult, SIGNATURE_TYPE, signingOption, "");
                    if (rsWFList[0] != null && rsWFList[0].length > 0) {
                        if (rsWFList[0][0].WORKFLOW_STATUS != Difinitions.CONFIG_PPL_WORKFLOW_STATUS_PENDING) {
                            connect.USP_GW_PPL_WORKFLOW_UPDATE_STATUS(signingToken, Difinitions.CONFIG_PPL_WORKFLOW_STATUS_COMPLETED, "");
                            if (!"".equals(rsWFList[0][0].POSTBACK_URL)) {
                                sAction = "signing_completed";
//                                log.info(
//                                        "link download : " + protocol + "://"
//                                                + request.getHeader("host") + "/api/signing/"
//                                                + signingToken
//                                                + "/download/");
                                sFileComplete = protocol + "://" + request.getHeader("host") + "/api/signing/"
                                        + signingToken + "/download/";
                                CommonFunction.PostBackJsonObject(callBackLogRequest, rsWFList[0][0].POSTBACK_URL, certEncode, sType,
                                        serialNumber, signingOption, sAction, signingToken, sSigner, sStatus, sFileComplete, sCountryCode, fileDigest);
                                callbackLog(callBackLogRequest);
                            }
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        }
    }

    public List<String> createHashList(String signerToken, String signingToken, String certChain, String cerId, String signName, String sSignature_id) throws Exception {
        // get UUID of last file

        // get participant to check signer status
        Participants[][] rsParticipant = new Participants[1][];
        connect.USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(rsParticipant, signerToken);
        if (rsParticipant[0] != null && rsParticipant[0].length > 0) {

            String sUUID_Last = "";
            int sFileID_Last = 0;
            LastFile[][] rsFile = new LastFile[1][];
            connect.USP_GW_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingToken);
            if (rsFile[0].length > 0) {
                sFileID_Last = rsFile[0][0].getLAST_PPL_FILE_SIGNED_ID();
                sUUID_Last = rsFile[0][0].getLAST_PPL_FILE_UUID();
            }

            // download file
            String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();
            byte[] jrbFile = FileJRBService.downloadFMS2(sUUID_Last);
            InputStream inputStreamNotSigned = null;
            if (jrbFile != null) {
//                inputStreamNotSigned = new ByteArrayInputStream(jrbFile.getBytes(StandardCharsets.UTF_8));
                //hash file
                byte[] pdfFile = jrbFile;

                int pageHeight = CommonFunction.calculateHeight(pdfFile);
//                    System.out.println("height: " + pageHeight);

                //<editor-fold defaultstate="collapsed" desc="### COUNTER SIGN GET">
                int count = 0;
                int[] pSIGNED_COUNTER = new int[1];
                String sGetCounter = connect.USP_GW_PPL_WORKFLOW_GET_SIGNED_COUNTER(pSIGNED_COUNTER, signingToken);
                if ("1".equals(sGetCounter)) {
                    count = pSIGNED_COUNTER[0];
                }
                //</editor-fold>

                String meta = rsParticipant[0][0].META_INFORMATION;
                JsonObject jsonObject = new Gson().fromJson(meta, JsonObject.class);

//                    JsonElement signingPurposeElement = jsonObject.get("signing_purpose");
                String signingPurpose = "Signature";

                if (jsonObject != null && jsonObject.has("signing_purpose")) {
                    signingPurpose = jsonObject.get("signing_purpose").getAsString();
                    // Tiếp tục xử lý dữ liệu trong signingPurposeElement
                } else {
                    // Xử lý khi không tìm thấy thuộc tính "signing_purpose"
                }

                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                InputStream input = loader.getResourceAsStream("file/Roboto-Bold.ttf");
                // convert inputstream to bytearray
                byte[] fontTitle = IOUtils.toByteArray(input);

                InputStream input2 = loader.getResourceAsStream("file/Roboto-Regular.ttf");
                byte[] fontData = IOUtils.toByteArray(input2);

                InputStream input3 = loader.getResourceAsStream("file/D-Times.ttf");
                byte[] fontCMS = IOUtils.toByteArray(input3);

                List<byte[]> src = new ArrayList<>();
                src.add(pdfFile);

                String reason = "Purpose: " + signingPurpose; // Mặc định là "Purpose: " + signingPurpose
                String page = "1";
                String top = null;
                String left = null;
                String width = "135";
                String height = "39";
                String text = "Sample text";
                String type = "image";

//                    JsonElement pdfElement = jsonObject.get("pdf");
                if (jsonObject != null && jsonObject.has("pdf")) {
                    JsonObject pdfObject = jsonObject.getAsJsonObject("pdf");
                    if (pdfObject.has("reason")) {
                        reason = pdfObject.get("reason").getAsString();
                    }
                    JsonElement annotationElement = pdfObject.get("annotation");
                    if (annotationElement != null) {
                        JsonObject annotationObject = annotationElement.getAsJsonObject();
                        if (annotationObject.has("page")) {
                            page = annotationObject.get("page").getAsString();
                        }
                        if (annotationObject.has("top")) {
                            top = annotationObject.get("top").getAsString();
                        }
                        if (annotationObject.has("left")) {
                            left = annotationObject.get("left").getAsString();
                        }
                        if (annotationObject.has("width")) {
                            width = annotationObject.get("width").getAsString();
                        }
                        if (annotationObject.has("height")) {
                            height = annotationObject.get("height").getAsString();
                        }
                        if (annotationObject.has("text")) {
                            text = annotationObject.get("text").getAsString();
                        }
                        if (annotationObject.has("type")) {
                            type = annotationObject.get("type").getAsString();
                        }
                    }

                }
                if (type.equals("text")) {
                    PdfProfileCMS profile = new PdfProfileCMS(Algorithm.SHA256);
                    profile.setTextContent(text);
                    profile.setReason(signingPurpose);
                    profile.setSignatureName(sSignature_id);
//                        profile.setTextContent("Ký bởi: " + signName + "\nNgày ký: {date}");
                    profile.setCheckText(false);
                    profile.setCheckMark(false);
                    profile.setSigningTime(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss");
                    profile.setBorder(Color.BLACK);
                    if (top == null || left == null) {
                        SignPosition signPosition = new SignPosition(page, count, pageHeight);
                        profile.setVisibleSignature(page, signPosition.getPos());
                    } else {
                        SignPosition signPosition = new SignPosition(page, Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(width), Integer.parseInt(height), pageHeight);
                        profile.setVisibleSignature(page, signPosition.getPos());
                    }

                    profile.setFont(
                            fontCMS,
                            BaseFont.IDENTITY_H,
                            true,
                            0,
                            0,
                            TextAlignment.ALIGN_LEFT,
                            Color.BLACK);

                    SigningMethodAsyncImp signInit = new SigningMethodAsyncImp();
                    byte[] temporalData = profile.createTemporalFile(signInit, src);
                    signInit.saveTemporalData(cerId, temporalData);
                    List<String> hashList = signInit.hashList;

                    return hashList;
//                        return Hex.encodeHexString(decoded);
                }
                if (type.equals("image")) {
                    PdfEsealCMS profileCMS = new PdfEsealCMS(PdfForm.B, Algorithm.SHA256);
                    profileCMS.setReason(signingPurpose);
                    profileCMS.setSignatureName(sSignature_id);
//                        profileCMS.setTitleEseal("Qualified Electronic Signature");

                    PdfEsealCMS.EsealTitle title = new PdfEsealCMS.EsealTitle();
                    title.setSize(PdfEsealCMS.EsealTitle.defaultSize);
                    title.setPaddingleft(0);
                    title.setPaddingtop(0);
                    title.setTitle("Qualified Electronic Signature");
                    profileCMS.setEsealTitle(title);

                    profileCMS.setSigningTime(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss");
                    SignPosition signPosition = null;
                    if (top == null || left == null) {
                        signPosition = new SignPosition(page, count, pageHeight);
                    } else {
                        signPosition = new SignPosition(page, Integer.parseInt(top), Integer.parseInt(left), pageHeight);
                    }
                    if (!certChain.equals("")) {
                        profileCMS.createEseal(Integer.parseInt(signPosition.getPageNumber()), signPosition.getX1(), signPosition.getY1(), "{signby}", "{date}\n" + reason);
                        profileCMS.setSignerCertificate(certChain);
                    } else {
                        profileCMS.createEseal(Integer.parseInt(signPosition.getPageNumber()), signPosition.getX1(), signPosition.getY1(), signName, "{date}\n" + reason);
                    }

                    profileCMS.setEsealTitleFont(
                            fontTitle,
                            BaseFont.IDENTITY_H,
                            true,
                            0,
                            0,
                            TextAlignment.ALIGN_LEFT,
                            Color.YELLOW);

                    profileCMS.setEsealContentFont(
                            fontData,
                            BaseFont.IDENTITY_H,
                            true,
                            0,
                            0,
                            TextAlignment.ALIGN_LEFT,
                            Color.YELLOW);

                    SigningMethodAsyncImp signInit = new SigningMethodAsyncImp();
                    byte[] temporalData = profileCMS.createTemporalFile(signInit, src);
//                        System.out.println("BBBB: " + cerId);
                    signInit.saveTemporalData(cerId, temporalData);
                    List<String> hashList = signInit.hashList;

                    return hashList;
                }
            } else {
                return null;
            }

        }
        return null;
    }

}
