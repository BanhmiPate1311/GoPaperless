package vn.mobileid.paperless.uat;///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package vn.mobileid.gateway.process;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// *
// * @author PHY
// */
//public class NewClass2 {
//     protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException, Exception {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//           String strResult="";
//           String sName= request.getParameter("kName");
//           String sRemark= request.getParameter("kRemark");
//           String sRemarkEN= request.getParameter("kRemarkEN");
//           Connect myDB= new Connect();
//           String pUsername = "admin";
//           String sInsert= myDB.S_BO_PROVINCE_INSERT(sName, sRemark, sRemarkEN, pUsername);
//           if ("0".equals(sInsert)) {
//                strResult = "0";
//            } else {
//                strResult = "1";
//            }
//            out.print(strResult);
//        }
//    }
//
//}
