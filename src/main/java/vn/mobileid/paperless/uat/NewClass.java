///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.paperless.uat;
//
//import vn.mobileid.paperless.object.PPLFile;
//import vn.mobileid.paperless.process.process;
//
///**
// *
// * @author PHY
// */
//public class NewClass {
//
//    public static void main(String[] args) throws Exception {
//        process conect = new process();
//        PPLFile[][] object = new PPLFile[1][];
//        String pUPLOAD_TOKEN = "4645fb94ed8e9de0fd57ec83c3e50824538f617d";
//        //String pRESPONSE_CODE = "@response";
//        conect.USP_PPL_FILE_GET(object, pUPLOAD_TOKEN);
//        System.out.println(object);
//        if (object.length > 0) {
//            for (int i = 0; i < object.length; i++) {
//                System.out.println(object[0][i].FILE_NAME);
//            }
//        }
//        System.out.println("fdags");
//    }
//}
