/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.uat;

import vn.mobileid.paperless.utils.CommonFunction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author PHY
 */
public class NewClass3 {

    private Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }

    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
//        String sFolderSave = "D:/project/file/23427182421118_signed.pdf";
//        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//        InputStream filePdf = new FileInputStream(sFolderSave);
//            PdfReader reader = new PdfReader(filePdf,null);
//            AcroFields af = reader.getAcroFields();
//            ArrayList<String> names = af.getSignatureNames();
//            for(String name : names) {
//                PdfPKCS7 pdfPKCS7 = af.verifySignature(name);
//                Date signTime = pdfPKCS7.getSignDate().getTime();
//                System.out.println("signTime: " + signTime);
//                Timestamp ts=new Timestamp(signTime.getTime());
//                System.out.println(ts);
//                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                System.out.println(formatter.format(ts));
//
//            }
        System.out.println("OK");

//        Participants[][] rsParticipant = new Participants[1][];
//        ObjectMapper oMapperParse = null;
//        process cconect = new process();
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        cconect.USP_PPL_WORKFLOW_PARTICIPANTS_LIST(rsParticipant, "42b179c60ec476daab126e81b51616cf098bf3c8");
//        if (rsParticipant != null && rsParticipant[0].length > 0) {
//            for (int j = 0; j < rsParticipant[0].length; j++) {
//                Map<String, String> object = new HashMap();
//                Map<String, Object> map = new HashMap();
//                map.put("FIRST_NAME", rsParticipant[0][j].FIRST_NAME);
//                map.put("LAST_NAME", rsParticipant[0][j].LAST_NAME);
//                map.put("SIGNER_TOKEN", rsParticipant[0][j].SIGNER_TOKEN);
//                map.put("SIGNER_STATUS", rsParticipant[0][j].SIGNER_STATUS);
//                // map.put("CERTIFICATE", rsParticipant[0][j].CERTIFICATE);
//                System.out.println("rsParticipant[0][j].SIGNER_STATUS : " + rsParticipant[0][j].SIGNER_STATUS);
//                if (rsParticipant[0][j].SIGNER_STATUS != Difinitions.CONFIG_WORKFLOW_PARTICIPANTS_SIGNER_STATUS_ID_PENDING) {
////                    dateToCalendar
//                    String sTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rsParticipant[0][j].SIGNED_TIME);
//                    System.out.printf("date", sTime);
//                }
//            }
//        }
        String sIssuer = "CN=CMC Certification Authority, OU=CMC Technology and Solution, O=CMC Corporation, ST=Hà Nội, C=VN";
        String sOwner = CommonFunction.CheckTextNull(sIssuer);
        if (!"".equals(sOwner)) {
            sOwner = CommonFunction.getCommonNameInDN(sOwner);
        }
        System.out.println("sOwner: " + sOwner);

//        process connectDB = new process();
//        String signingTokenRequest = "34d8066e2399e28d8275e0912cf360bded87cd5e";
//        System.out.println("giatrisigningtojekn: " + signingTokenRequest);
//        String pDMS_PROPERTY = FileJRBService.getPropertiesFMS();
//        String sUUID_Last = "";
////        String sPathFileUp = "D:/project/file/23422172339545_signed.pdf";
////        Path path = new File("2342216465130_signed.pdf").toPath();
////        String mimeType = Files.probeContentType(path);
////        InputStream inStreamSigned = new FileInputStream(sPathFileUp);
////        JCRFile jcr = FileJRBService.uploadPdf("23422164651301_signed.pdf", mimeType, inStreamSigned, pDMS_PROPERTY);
////        if(jcr != null){
////            sUUID_Last = jcr.getUuid();
////        }
//        sUUID_Last = "ea985c5a-da4e-42fe-82e6-3f9c6a51f040";
//        /*PPLFile[][] rsFile = new PPLFile[1][];
//        connectDB.USP_PPL_WORKFLOW_GET_LAST_FILE(rsFile, signingTokenRequest);
//        if(rsFile != null && rsFile[0].length >0){
//            sUUID_Last = rsFile[0][0].FILE_UUID;
//        }*/
//        JCRFile jrbFile = FileJRBService.downloadFMS(sUUID_Last, pDMS_PROPERTY);
//        InputStream pdffile = null;
//        if (jrbFile.getStream() != null) {
//            pdffile = jrbFile.getStream();
//            byte[] bytes = IOUtils.toByteArray(jrbFile.getStream());
//            System.out.println("Byte: " + bytes.length);
//            FileOutputStream fs = new FileOutputStream(new File("D:/project/file/abc11.pdf"));
//            BufferedOutputStream bs = new BufferedOutputStream(fs);
//            bs.write(bytes);
//            bs.close();
//            bs = null;
//            System.out.println("OK");
//        }
    }

    public static String hashFile(File file) throws NoSuchAlgorithmException, IOException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        fis.close();

        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
