///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.paperless.uat;
//
////import org.apache.logging.log4j.core.util.IOUtils;
//import java.util.ArrayList;
//
//import static vn.mobileid.paperless.controller.APIController.headerFooter;
//
///**
// *
// * @author PHY
// */
//public class maintest {
//
//    public static void main(String[] args) throws Exception {
////        process conect = new process();
////        PREFIX_UID[][] rsFile = new PREFIX_UID[1][];
////        conect.USP_PREFIX_PERSONAL_CODE_LIST(rsFile, "");
////        if (rsFile != null && rsFile[0].length > 0) {
////           for(int i =0; i < rsFile[0].length; i++){
////                System.err.println("prefix: "+rsFile[0][i].NAME);
////                System.err.println("remark: "+rsFile[0][i].REMARK);
////           }
//        ArrayList<Object> a = headerFooter();
//        System.out.println(a);
//    }
//}
//
////        String sUUID = "a89a19a2-7b35-4cf5-bad4-05de3c0da321";
////        String sJRB_Host = "http://192.168.2.245:8080/FileManagerSystem/server/";
////        String sJRB_UserID = "RWTRUONGNNT";
////        String sJRB_UserPass = "RWTRUONGNNT";
////        String sJRB_MaxSession = "128";
////        String sJRB_MaxFileFolder = "10000";
////        String sJRB_PrefixFolder = "247_client_";
////        String sJRB_WorkSpace = "TRUONGNNT";
////        JCRConfig jcrConfig = JackRabbitCommon.getJCRConfig(sJRB_Host, sJRB_UserID, sJRB_UserPass, Integer.parseInt(sJRB_MaxSession),
////                Integer.parseInt(sJRB_MaxFileFolder), sJRB_WorkSpace, sJRB_PrefixFolder);
////        JCRFile jrbFile = JackRabbitCommon.getInstance(jcrConfig).downloadFile(sUUID);
////        if (jrbFile.getStream() != null) {
////            System.out.println("hello: "+jrbFile.getStream());
////            //BufferedOutputStream bs = null;
////
////            byte[] bytes = IOUtils.toByteArray(jrbFile.getStream());
////            System.out.println("Byte: " + bytes.getClass().getSimpleName());
////
//////            FileOutputStream fs = new FileOutputStream(new File("E:\\Up_Temp\\NCCA\\050423\\201\\abc1.pdf"));
//////            bs = new BufferedOutputStream(fs);
//////            bs.write(bytes);
//////            bs.close();
//////            bs = null;
////        }
////    }
////}
////    private static String convertInputStreamToString(InputStream is) throws IOException {
////
////        ByteArrayOutputStream result = new ByteArrayOutputStream();
////        byte[] buffer = new byte[8192];
////        int length;
////        while ((length = is.read(buffer)) != -1) {
////            result.write(buffer, 0, length);
////        }
////
////        // Java 1.1
////        //return result.toString(StandardCharsets.UTF_8.name());
////
////        return result.toString("UTF-8");
////
////        // Java 10
////        //return result.toString(StandardCharsets.UTF_8);
////
////    }
////}
