/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import vn.mobileid.paperless.object.*;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static vn.mobileid.paperless.API.Crypto.*;

/**
 * @author PHY
 */

public class CommonFunction {

    final public static String HASH_SHA256 = "SHA-256";
    final public static String HASH_SHA1 = "SHA-1";
    final public static OkHttpClient httpClient = new OkHttpClient();
    public static String generateNumberDays() {
        String result;
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);
        result = String.valueOf(year).substring(2) + String.valueOf(month) + String.valueOf(day)
                + String.valueOf(hour) + String.valueOf(minute) + String.valueOf(second)
                + String.valueOf(millis);
        return result;
    }

    public static byte[] hashData(byte[] data, String algorithm) {
        byte[] result = null;
        try {
            if (algorithm.compareToIgnoreCase(HASH_MD5) == 0) {
                algorithm = HASH_MD5;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA1) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA1_) == 0) {
                algorithm = HASH_SHA1;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA256) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA256_) == 0) {
                algorithm = HASH_SHA256;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA384) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA384_) == 0) {
                algorithm = HASH_SHA384;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA512) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA512_) == 0) {
                algorithm = HASH_SHA512;
            } else {
                algorithm = HASH_SHA256;
            }
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // log.error(e.getMessage());
        }
        return result;
    }

    // get upload token
    // String uuid = UUID.randomUUID().toString();
    // String uploadToken = GatewayUtils.getCryptoHash(uuid,
    // GatewayUtils.HASH_SHA1);
    public static String getCryptoHash(String input) {
        try {
            String algorithm = HASH_SHA1;
            if (algorithm.compareToIgnoreCase(HASH_MD5) == 0) {
                algorithm = HASH_MD5;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA1) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA1_) == 0) {
                algorithm = HASH_SHA1;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA256) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA256_) == 0) {
                algorithm = HASH_SHA256;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA384) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA384_) == 0) {
                algorithm = HASH_SHA384;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA512) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA512_) == 0) {
                algorithm = HASH_SHA512;
            } else {
                algorithm = HASH_SHA256;
            }
            // MessageDigest classes Static getInstance method is called with MD5 hashing
            MessageDigest msgDigest = MessageDigest.getInstance(algorithm);

            // digest() method is called to calculate message digest of the input
            // digest() return array of byte.
            byte[] inputDigest = msgDigest.digest(input.getBytes());

            // Convert byte array into signum representation
            // BigInteger class is used, to convert the resultant byte array into its signum
            // representation
            BigInteger inputDigestBigInt = new BigInteger(1, inputDigest);

            // Convert the input digest into hex value
            String hashtext = inputDigestBigInt.toString(16);

            // Add preceding 0's to pad the hashtext to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // Catch block to handle the scenarios when an unsupported message digest
        // algorithm is provided.
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String convertInputStreamToBase64(InputStream input) {
        try {
            byte[] byteArray = IOUtils.toByteArray(input);
            String sBase = Base64.getEncoder().encodeToString(byteArray);
            return sBase;
        } catch (Exception e) {
            return "";
        }
    }

    public static String JsonCertificateObject(String sCertificate, String sCode, String signingTime,
                                               String signingOption,
                                               String sAction, String sToken, String sSigner, String sStatus, String sFile, String sFileSigest,
                                               String sSignature_id, String sCountryCode) {
        String sJson = "";
        try {
            Object[] info = new Object[3];
            String[] time = new String[2];
            int[] intRes = new int[1];
            CertificateObject certObj = null;
            SignerInfoJson signerJson = new SignerInfoJson();
            VoidCertificateComponents(sCertificate, info, time, intRes);
            if (intRes[0] == 0) {
                certObj = new CertificateObject();
                certObj.subject = info[0].toString();
                certObj.issuer = info[1].toString();
                certObj.valid_from = time[0];
                certObj.valid_to = time[1];
                certObj.value = sCertificate;
            }
            // signerJson.type = sType;
            signerJson.code = sCode;
            signerJson.certificate = certObj;
            signerJson.signing_time = signingTime;
            signerJson.signing_option = signingOption;
            signerJson.country_code = sCountryCode;
            CertificateJson certJson = new CertificateJson();
            certJson.action = sAction;
            certJson.token = sToken;
            certJson.signer = sSigner;
            certJson.signer_info = signerJson;
            certJson.status = sStatus;
            certJson.file = sFile;
            certJson.file_digest = sFileSigest;
            certJson.valid_to = time[1];
            certJson.signature_id = sSignature_id;
            ObjectMapper oMapperParse = new ObjectMapper();
            sJson = oMapperParse.writeValueAsString(certJson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sJson;
    }

    public static String PostBackJsonCertificateObject(CallBackLogRequest callBackLogRequest, String url, String sCertificate, String sCode,
                                                       String signingTime, String signingOption,
                                                       String sAction, String sToken, String sSigner, String sStatus, String sFile, String sFileSigest,
                                                       String sSignature_id, String sCountryCode) {
        String sResult = "0";
        try {
            Object[] info = new Object[3];
            String[] time = new String[2];
            int[] intRes = new int[1];
            CertificateObject certObj = null;
            SignerInfoJson signerJson = new SignerInfoJson();
            VoidCertificateComponents(sCertificate, info, time, intRes);
            if (intRes[0] == 0) {
                certObj = new CertificateObject();
                certObj.subject = info[0].toString();
                certObj.issuer = info[1].toString();
                certObj.valid_from = time[0];
                certObj.valid_to = time[1];
                certObj.value = sCertificate;
            }
            // signerJson.type = sType;
            signerJson.code = sCode;
            signerJson.country_code = sCountryCode;
            // signerJson.certificate = certObj;
            signerJson.signing_time = signingTime;
            signerJson.signing_option = signingOption;
            CertificateJson certJson = new CertificateJson();
            certJson.action = sAction;
            certJson.token = sToken;
            certJson.signer = sSigner;
            certJson.signer_info = signerJson;
            certJson.status = sStatus;
            certJson.file = sFile;
            certJson.file_digest = sFileSigest;
            certJson.valid_to = time[1];
            certJson.signature_id = sSignature_id;
            ObjectMapper oMapperParse = new ObjectMapper();
            String sJson = oMapperParse.writeValueAsString(certJson);
            System.err.println("UrlPostBack: " + url);
            System.err.println("Requet: " + sJson);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sJson);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = httpClient.newCall(request).execute();
            System.out.println("requestbody PostBackJsonCertificateObject " + response.toString());

            callBackLogRequest.setpURL(url);
            callBackLogRequest.setpHTTP_VERB("https");
            callBackLogRequest.setpSTATUS_CODE(response.code());
            System.out.println("response.code() " + response.code());
            Gson gson = new Gson();
            callBackLogRequest.setpREQUEST(sJson);
            callBackLogRequest.setpRESPONSE(response.body().string());

            // HttpPost request = new HttpPost(url);
            // StringEntity params = new StringEntity(sJson);
            // request.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            // request.setEntity(params);
            // HttpResponse response = httpClient.execute(request);
            // System.out.println("requestbody PostBackJsonCertificateObject " + response.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            sResult = e.getMessage();
        }
        return sResult;
    }

    public static String PostBackJsonObject(CallBackLogRequest callBackLogRequest,String url, String sCertificate, String sCode,
                                            String signingOption, String sAction, String sToken,
                                            String sSigner, String sStatus, String sFile, String sCountryCode, String file_digest) {
        String sResult = "0";
        try {
            // SignerInfoJson signerJson = new SignerInfoJson();
            // signerJson.type = sType;
            // signerJson.code = sCode;
            // signerJson.signing_option = signingOption;
            // signerJson.country_code = sCountryCode;
            Object[] info = new Object[3];
            String[] time = new String[2];
            int[] intRes = new int[1];
            CertificateObject certObj = null;
            SignerInfoJson signerJson = new SignerInfoJson();
            VoidCertificateComponents(sCertificate, info, time, intRes);
            PostbackJson certJson = new PostbackJson();
            certJson.action = sAction;
            certJson.token = sToken;
            certJson.status = sStatus;
            certJson.file = sFile;
            certJson.file_digest = file_digest;
            certJson.valid_to = CommonFunction.CheckTextNull(time[1]);
            // certJson.signer_info = signerJson;
            ObjectMapper oMapperParse = new ObjectMapper();
            String sJson = oMapperParse.writeValueAsString(certJson);
            System.err.println("UrlPostBack: " + url);
            System.err.println("Request: " + sJson);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sJson);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = httpClient.newCall(request).execute();
            System.out.println("requestbody PostBackJsonCertificateObject " + response.toString());

            callBackLogRequest.setpURL(url);
            callBackLogRequest.setpHTTP_VERB("https");
            callBackLogRequest.setpSTATUS_CODE(response.code());
            System.out.println("response.code() duoi" + response.code());
            Gson gson = new Gson();
            callBackLogRequest.setpREQUEST(sJson);
            callBackLogRequest.setpRESPONSE(response.body().string());
            // HttpPost request = new HttpPost(url);
            // StringEntity params = new StringEntity(sJson);
            // request.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            // request.setEntity(params);
            // HttpResponse response = httpClient.execute(request);
            // System.out.println("requestbody PostBackJsonObject " + response.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            sResult = e.getMessage();
        }
        return sResult;
    }

    public static void VoidCertificateComponents(String certstr, Object[] info, String[] time, int[] intRes) {
        try {
            if (certstr.toUpperCase().contains("BEGIN CERTIFICATE")) {
                certstr = certstr.replace("-----BEGIN CERTIFICATE-----", "");
            }
            if (certstr.toUpperCase().contains("END CERTIFICATE")) {
                certstr = certstr.replace("-----END CERTIFICATE-----", "");
            }
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            CertificateFactory certFactory1 = CertificateFactory.getInstance("X.509");
//            InputStream in = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certstr));
            byte[] certBytes = Base64.getDecoder().decode(certstr);
            InputStream in = new ByteArrayInputStream(certBytes);
            X509Certificate cert = (X509Certificate) certFactory1.generateCertificate(in);
            info[0] = cert.getSubjectDN();
            info[0] = info[0].toString().replace("\\", "");
            info[1] = cert.getIssuerDN();
            info[2] = cert.getSerialNumber().toString(16);
            time[0] = formatter.format(cert.getNotBefore());
            time[1] = formatter.format(cert.getNotAfter());
            intRes[0] = 0;
        } catch (Exception e) {
            System.out.print("VoidCertificateComponents: " + e.getMessage());
            intRes[0] = 1;
        }
    }

    public static String CheckTextNull(String sValue) {
        if (sValue == null) {
            sValue = "";
        } else {
            if (Difinitions.CONFIG_EXCEPTION_STRING_ERROR_NULL.equals(sValue.trim().toUpperCase())) {
                sValue = "";
            }
        }
        return sValue.trim();
    }

    public static final String OID_CN = "2.5.4.3";

    public static String getCommonNameInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_CN)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    private static int findMaxLen(byte[][] hashes) {
        int max = 0;
        for (byte[] hh : hashes) {
            if (max < hh.length) {
                max = hh.length;
            }
        }
        return max;
    }

    public static byte[][] padding(byte[][] hashes) {
        int max = findMaxLen(hashes);
        byte[][] rsp = new byte[hashes.length][];

        for (int idx = 0; idx < hashes.length; idx++) {
            int len = hashes[idx].length;
            if (len < max) {
                byte[] tmp = new byte[len];
                System.arraycopy(hashes[idx], 0, tmp, 0, len);
                hashes[idx] = new byte[max];
                System.arraycopy(tmp, 0, hashes[idx], 0, len);
                for (int ii = len; ii < max; ii++) {
                    hashes[idx][ii] = (byte) 0xFF;
                }
            }
        }
        return rsp;
    }

    public static String computeVC(List<byte[]> hashesList) throws NoSuchAlgorithmException {

        byte[][] hashes = new byte[hashesList.size()][];
        for (int i = 0; i < hashesList.size(); i++) {
            hashes[i] = hashesList.get(i);
        }
        if (hashes == null || hashes.length == 0) {
            throw new RuntimeException("The input is null or empty");
        }
        //single hash
        byte[] vcData = new byte[hashes[0].length];
        System.arraycopy(hashes[0], 0, vcData, 0, vcData.length);

        if (hashes.length > 1) {
            padding(hashes);

            for (int ii = 1; ii < hashes.length; ii++) {
                if (hashes[ii].length > vcData.length) {
                    byte[] tmp = new byte[hashes[ii].length];
                    System.arraycopy(vcData, 0, tmp, 0, vcData.length);
                    for (int ttt = vcData.length; ttt < hashes[ii].length; ttt++) {
                        tmp[ttt] = (byte) 0xFF;
                    }
                    vcData = new byte[tmp.length];
                    System.arraycopy(tmp, 0, vcData, 0, tmp.length);
                }
                for (int idx = 0; idx < hashes[ii].length; idx++) {
                    vcData[idx] |= hashes[ii][idx];
                }
            }
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(vcData);
        byte[] vc = md.digest();
        short first = (short) (vc[0] << 8 | vc[1] & 0x00FF);
        short last = (short) (vc[vc.length - 2] << 8 | vc[vc.length - 1] & 0x00FF);
        return String.format("%04X-%04X", first, last);
    }

    public static final String OID_O = "2.5.4.3";

    public static String getCommonnameInDN(String dn) {
        X500Name subject = new X500Name(dn);
        RDN[] rdn = subject.getRDNs();
        for (int j = 0; j < rdn.length; j++) {
            AttributeTypeAndValue[] attributeTypeAndValue = rdn[j].getTypesAndValues();
            if (attributeTypeAndValue[0].getType().toString().equals(OID_O)) {
                return attributeTypeAndValue[0].getValue().toString();
            }
        }
        return "";
    }

    public static String convertBase64(String sValue) {
        String sResult = "";
        try {
            sResult = Base64.getEncoder().encodeToString(sValue.getBytes());
        } catch (Exception e) {
            System.out.println("convertBase64: " + e.getMessage());
        }
        return sResult;
    }

    public static int calculateHeight(byte[] pdfData) throws Exception {
//        byte[] pdfData = IOUtils.readFully(inputStream,inputStream.available(),true);
        PdfReader inputPdfReader = new PdfReader(pdfData);
        int height= (int) inputPdfReader.getPageSize(1).getHeight();
        int width= (int) inputPdfReader.getPageSize(1).getWidth();

        inputPdfReader.close();

//        PdfReader reader = new PdfReader(inputStream);
//        int height= (int) reader.getPageSize(1).getHeight();
//
//        // Đảm bảo đóng tài nguyên sau khi hoàn thành
//        reader.close();
        return height;
    }

    public static java.util.Date getSigningTime(byte[] pdf) throws IOException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        InputStream filePdf = new FileInputStream(sFolderSave);
        PdfReader reader = new PdfReader(pdf,null);
        AcroFields af = reader.getAcroFields();
        ArrayList<String> names = af.getSignatureNames();
        Date signTime = null;
        for(String name : names) {
            PdfPKCS7 pdfPKCS7 = af.verifySignature(name);
            signTime = pdfPKCS7.getSignDate().getTime();

        }
        return signTime;
    }

    // Hàm chuyển đổi mảng byte thành chuỗi hex
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static boolean checkSign(byte[] pdf) throws IOException {
        boolean valid = false;
        PdfReader reader = new PdfReader(pdf);
        AcroFields acro = reader.getAcroFields();
        List<String> signame = acro.getSignatureNames();
        System.out.println("Signature names: " + signame.size());
        if(signame.size() == 0) {
            System.out.println("No signature.");
            return false;
        }
        return true;
    }

    // Hàm chuyển đổi chuỗi hex thành mảng bytes
    public static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }
}
