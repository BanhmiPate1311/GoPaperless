/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.process;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.mobileid.paperless.object.*;

import vn.mobileid.paperless.utils.CommonFunction;
import vn.mobileid.paperless.utils.Difinitions;
import vn.mobileid.paperless.utils.LoadParamSystem;

/**
 * @author PHY
 */
@Component
public class process {

    @Value("${db.driver.sql}")
    private String driver_sql;

    @Value("${db.url.sql}")
    private String url_sql;

    @Value("${db.username.sql}")
    private String username_sql;

    @Value("${db.password.sql}")
    private String password_sql;

    @Value("${db.connect.timeout}")
    private String connect_timeout;

    public Connection OpenDatabase() throws Exception {
        String Driver_Sql = driver_sql;
        String Url_Sql = url_sql;
        String Username_SQL = username_sql;
        String Password_SQL = password_sql;
        String DBConnect_Timeout = connect_timeout;
        Class.forName(Driver_Sql);
        DriverManager.setLoginTimeout(Integer.parseInt(DBConnect_Timeout));
        Connection connInner = DriverManager.getConnection(Url_Sql, Username_SQL, Password_SQL);
        return connInner;
    }

    public void CloseDatabase(Connection[] temp) throws Exception {
        if (temp[0] != null) {
            temp[0].close();
        }
    }

    public String USP_GW_PPL_FILE_GET(String pUPLOAD_TOKEN, FirstFileFromUpLoadToken firstFileFromUpLoadToken) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_FILE_GET(?,?) }");
            proc_stmt.setString(1, pUPLOAD_TOKEN);

            proc_stmt.registerOutParameter(2, java.sql.Types.INTEGER);
            proc_stmt.execute();
            convrtr = String.valueOf(proc_stmt.getInt(2));

//            System.out.println("USP_PPL_FILE_GET: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                firstFileFromUpLoadToken.setId(rs.getInt("ID"));
                firstFileFromUpLoadToken.setEnterpriseId(rs.getInt("ENTERPRISE_ID"));
                firstFileFromUpLoadToken.setEnabled(rs.getInt("ENABLED"));
                firstFileFromUpLoadToken.setFileName(rs.getString("FILE_NAME"));
                firstFileFromUpLoadToken.setFileSize(rs.getInt("FILE_SIZE"));
                firstFileFromUpLoadToken.setFileUuid(rs.getString("FILE_UUID"));

            }
            return convrtr;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

//    public String USP_GW_GET_FIRST_FILE_FROM_UPLOAD_TOKEN(String pUPLOAD_TOKEN, FirstFileFromUpLoadToken firstFileFromUpLoadToken) throws Exception {
//        String convrtr = "1";
//        Connection conns = null;
//        CallableStatement proc_stmt = null;
//        ResultSet rs = null;
//        try {
//            System.out.println("pUPLOAD_TOKEN: " + pUPLOAD_TOKEN);
//            conns = OpenDatabase();
//            proc_stmt = conns.prepareCall("{ call USP_GW_GET_FIRST_FILE_FROM_UPLOAD_TOKEN(?,?) }");
//            proc_stmt.setString("pUPLOAD_TOKEN", pUPLOAD_TOKEN);
//            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//
//            proc_stmt.execute();
//            convrtr = proc_stmt.getString("pRESPONSE_CODE");
//
//            rs = proc_stmt.executeQuery();
//            while (rs.next()) {
//                firstFileFromUpLoadToken.setId(rs.getInt("ID"));
//                firstFileFromUpLoadToken.setPplWorkflowId(rs.getInt("PPL_WORKFLOW_ID"));
//                firstFileFromUpLoadToken.setPplFileId(rs.getInt("PPL_FILE_ID"));
//                firstFileFromUpLoadToken.setFirstPplFilename(rs.getString("FIRST_PPL_FILE_NAME"));
//                firstFileFromUpLoadToken.setFirstPplFileUuid(rs.getString("FIRST_PPL_FILE_UUID"));
//                firstFileFromUpLoadToken.setEnterpriseId(rs.getInt("ENTERPRISE_ID"));
//                firstFileFromUpLoadToken.setFileSize(rs.getInt("FILE_SIZE"));
//            }
//            return convrtr;
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        } finally {
//            if (proc_stmt != null) {
//                proc_stmt.close();
//            }
//            Connection[] temp_connection = new Connection[]{conns};
//            CloseDatabase(temp_connection);
//        }

    public void USP_GW_ENTERPRISE_INFO_GET(ENTERPRISE[][] response, int pENTERPRISE_ID, String NAME) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<ENTERPRISE> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_ENTERPRISE_INFO_GET(?,?,?) }");
            proc_stmt.setInt(1, pENTERPRISE_ID);
            if (!"".equals(NAME)) {
                proc_stmt.setString(2, NAME);
            } else {
                proc_stmt.setString(2, null);
            }

            proc_stmt.registerOutParameter(3, java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString(3);

//            System.out.println("USP_ENTERPRISE_INFO_GET: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                ENTERPRISE tempItem = new ENTERPRISE();
                tempItem.METADATA_GATEWAY_VIEW = rs.getString("METADATA_GATEWAY_VIEW");
                tempItem.LOGO = rs.getString("LOGO");

                tempList.add(tempItem);
            }
            response[0] = new ENTERPRISE[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET(Participants[][] response, String pSIGNER_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<Participants> tempList = new ArrayList<>();
        String convrtr = "1";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_PARTICIPANTS_GET(?,?) }");
            proc_stmt.setString(1, pSIGNER_TOKEN);

            proc_stmt.registerOutParameter(2, java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString(2);

//            System.out.println("USP_PPL_WORKFLOW_PARTICIPANTS_GET: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                Participants tempItem = new Participants();
                tempItem.ID = rs.getInt("ID");
                tempItem.PPL_WORKFLOW_ID = rs.getInt("PPL_WORKFLOW_ID");
                tempItem.FIRST_NAME = rs.getString("FIRST_NAME");
                tempItem.LAST_NAME = rs.getString("LAST_NAME");
                tempItem.SIGNER_STATUS = rs.getInt("SIGNER_STATUS");
                tempItem.CERTIFICATE = rs.getString("CERTIFICATE");
                tempItem.SIGNING_OPTIONS = rs.getString("SIGNING_OPTIONS");
                tempItem.SIGNER_ID = rs.getString("SIGNER_ID");
                tempItem.CUSTOM_REASON = rs.getString("CUSTOM_REASON");
                tempItem.SIGNING_PURPOSE = rs.getString("SIGNING_PURPOSE");
                tempItem.META_INFORMATION = rs.getString("META_INFORMATION");
                tempItem.ANNOTATION = rs.getString("ANNOTATION");
                tempList.add(tempItem);
            }
            response[0] = new Participants[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    // Insert file PDF
    public String getIDENTIFIER() throws Exception {
        process connect = new process();
        ConnectorName[][] object = new ConnectorName[1][];
        String pCONNECTOR_NAME = Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID;
        connect.USP_GW_CONNECTOR_GET(object, pCONNECTOR_NAME);
        String intt = null;
//        System.out.println(object.length);
        if (object.length > 0) {
            for (int i = 0; i < object.length; i++) {
//                System.out.println(object[0][i].IDENTIFIER);
                return intt = object[0][i].IDENTIFIER;
            }
        }
        return intt;
    }

    public String USP_GW_PPL_FILE_ADD(int pENTERPRISE_ID, String pFILE_NAME, int pFILE_SIZE, int pFILE_STATUS, String pURL, String pFILE_TYPE,
                                      String pMIME_TYPE,
                                      String pDIGEST, String pCONTENT, String pFILE_UUID, String pDMS_PROPERTY, String pUPLOAD_TOKEN,
                                      String pHMAC,
                                      String pCREATED_BY, int[] pFILE_ID) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_FILE_ADD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

            proc_stmt.setInt("pENTERPRISE_ID", pENTERPRISE_ID); // pENTERPRISE_ID
            proc_stmt.setString("pFILE_NAME", pFILE_NAME); // file name
            // file size
            proc_stmt.setInt("pFILE_SIZE", pFILE_SIZE);

            // file status
            proc_stmt.setInt("pFILE_STATUS", pFILE_STATUS);

            // file URL
            if (!"".equals(pURL)) {
                proc_stmt.setString("pURL", pURL);
            } else {
                proc_stmt.setString("pURL", null);
            }

            // file type
            proc_stmt.setString("pFILE_TYPE", pFILE_TYPE);

            // file mime type
            proc_stmt.setString("pMIME_TYPE", pMIME_TYPE);

            proc_stmt.setString("pDIGEST", pDIGEST); // file digest

            // file content
            if (!"".equals(pCONTENT)) {
                proc_stmt.setString("pCONTENT", pCONTENT);
            } else {
                proc_stmt.setString("pCONTENT", null);
            }

            // uuid file
            proc_stmt.setString("pFILE_UUID", pFILE_UUID);

            // file DMS property
            proc_stmt.setString("pDMS_PROPERTY", pDMS_PROPERTY);

            // upload token
            proc_stmt.setString("pUPLOAD_TOKEN", pUPLOAD_TOKEN);

            // hmac
            if (!"".equals(pHMAC)) {
                proc_stmt.setString("pHMAC", pHMAC);
            } else {
                proc_stmt.setString("pHMAC", null);
            }
            // create by
            if (!"".equals(pCREATED_BY)) {
                proc_stmt.setString("pCREATED_BY", pCREATED_BY);
            } else {
                proc_stmt.setString("pCREATED_BY", null);
            }

            proc_stmt.registerOutParameter("pFILE_ID", java.sql.Types.BIGINT); // giá trị out file_id
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR); // giá trị out response code

//            System.out.println("USP_PPL_FILE_ADD: " + proc_stmt); // kiem tra
            proc_stmt.execute();
            pFILE_ID[0] = proc_stmt.getInt("pFILE_ID");
            convrtr = String.valueOf(proc_stmt.getInt("pRESPONSE_CODE"));
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    public void USP_GW_CONNECTOR_GET(ConnectorName[][] response, String pCONNECTOR_NAME) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<ConnectorName> tempList = new ArrayList<>();
        String convrtr = "1";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_CONNECTOR_GET(?,?) }");
            proc_stmt.setString(1, pCONNECTOR_NAME);

            proc_stmt.registerOutParameter(2, java.sql.Types.INTEGER);
            proc_stmt.execute();
            convrtr = String.valueOf(proc_stmt.getInt(2));

//            System.out.println("USP_CONNECTOR_GET: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                ConnectorName tempItem = new ConnectorName();
                tempItem.IDENTIFIER = CommonFunction.CheckTextNull(rs.getString("IDENTIFIER"));
                tempItem.PREFIX_CODE = CommonFunction.CheckTextNull(rs.getString("PREFIX_CODE"));
                // tempItem.LOGO = rs.getString("LOGO");

                tempList.add(tempItem);
            }
            response[0] = new ConnectorName[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_PPL_BATCH_FILE_GET_ENTERPRISE_INFO(ENTERPRISE[][] response, String pBATCH_FILE_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<ENTERPRISE> tempList = new ArrayList<>();
        String convrtr = "1";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_BATCH_FILE_GET_ENTERPRISE_INFO(?,?) }");
            proc_stmt.setString(1, pBATCH_FILE_TOKEN);

            proc_stmt.registerOutParameter(2, java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString(2);

//            System.out.println("USP_CONNECTOR_GET: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                ENTERPRISE tempItem = new ENTERPRISE();
                tempItem.LOGO = rs.getString("LOGO");
                tempItem.METADATA_GATEWAY_VIEW = rs.getString("METADATA_GATEWAY_VIEW");
                tempItem.ID = rs.getInt("ID");
                tempList.add(tempItem);
            }
            response[0] = new ENTERPRISE[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    // update signer status
    public String USP_GW_PPL_WORKFLOW_PARTICIPANTS_UPDATE_STATUS(String pSIGNER_TOKEN, int pSIGNER_STATUS,
                                                                 String pLAST_MODIFIED_BY, int intIS_SET_POSITION)
            throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_WORKFLOW_PARTICIPANTS_UPDATE_STATUS(?,?,?,?,?) }");

            proc_stmt.setString("pSIGNER_TOKEN", pSIGNER_TOKEN);
            proc_stmt.setInt("pSIGNER_STATUS", pSIGNER_STATUS);
            if (!"".equals(pLAST_MODIFIED_BY)) {
                proc_stmt.setString("pLAST_MODIFIED_BY", pLAST_MODIFIED_BY);
            } else {
                proc_stmt.setString("pLAST_MODIFIED_BY", null);
            }
            proc_stmt.setInt("IS_SET_POSITION", intIS_SET_POSITION);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            System.out.println("USP_GW_PPL_WORKFLOW_PARTICIPANTS_UPDATE_STATUS: " + proc_stmt.toString());
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    // update ppl file
    public String USP_GW_PPL_FILE_UPDATE(int pFILE_ID, int pFILE_STATUS, String pFILE_NAME,
                                         String pFILE_SIZE, String pURL, String pFILE_TYPE, String pMIME_TYPE, String pDIGEST,
                                         String pCONTENT, String pFILE_UUID, String pDMS_PROPERTY, String pHMAC,
                                         String pLAST_MOTIFIED_BY) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_FILE_UPDATE(?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

            proc_stmt.setInt("pFILE_ID", pFILE_ID);
            proc_stmt.setInt("pFILE_STATUS", pFILE_STATUS);

            if (!"".equals(pFILE_NAME)) {
                proc_stmt.setString("pFILE_NAME", pFILE_NAME);
            } else {
                proc_stmt.setString("pFILE_NAME", null);
            }

            if (!"".equals(pFILE_SIZE)) {
                proc_stmt.setInt("pFILE_SIZE", Integer.parseInt(pFILE_SIZE));
            } else {
                proc_stmt.setString("pFILE_SIZE", null);
            }
            if (!"".equals(pURL)) {
                proc_stmt.setString("pURL", pURL);
            } else {
                proc_stmt.setString("pURL", null);
            }
            if (!"".equals(pFILE_TYPE)) {
                proc_stmt.setString("pFILE_TYPE", pFILE_TYPE);
            } else {
                proc_stmt.setString("pFILE_TYPE", null);
            }
            if (!"".equals(pMIME_TYPE)) {
                proc_stmt.setString("pMIME_TYPE", pMIME_TYPE);
            } else {
                proc_stmt.setString("pMIME_TYPE", null);
            }
            if (!"".equals(pDIGEST)) {
                proc_stmt.setString("pDIGEST", pDIGEST);
            } else {
                proc_stmt.setString("pDIGEST", null);
            }
            if (!"".equals(pCONTENT)) {
                proc_stmt.setString("pCONTENT", pCONTENT);
            } else {
                proc_stmt.setString("pCONTENT", null);
            }

            proc_stmt.setString("pFILE_UUID", pFILE_UUID);
            proc_stmt.setString("pDMS_PROPERTY", pDMS_PROPERTY);

            // if(!"".equals(pUPLOAD_TOKEN)){
            // proc_stmt.setString("pUPLOAD_TOKEN", pUPLOAD_TOKEN);
            // } else {
            // proc_stmt.setString("pUPLOAD_TOKEN", null);
            // }
            if (!"".equals(pHMAC)) {
                proc_stmt.setString("pHMAC", pHMAC);
            } else {
                proc_stmt.setString("pHMAC", null);
            }
            if (!"".equals(pLAST_MOTIFIED_BY)) {
                proc_stmt.setString("pLAST_MODIFIED_BY", pLAST_MOTIFIED_BY);
            } else {
                proc_stmt.setString("pLAST_MODIFIED_BY", null);
            }
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            System.out.println("USP_PPL_FILE_UPDATE: " + proc_stmt);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    // insert workflow file
    public String USP_GW_PPL_WORKFLOW_FILE_ADD(int pPPL_WORKFLOW_ID, int pPPL_FILE_ID, String pTYPE,
                                               String pFILE_INFO, int pFROM_FILE_ID, String pHMAC, String pCREATED_BY) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_FILE_ADD(?,?,?,?,?,?,?,?) }");

            proc_stmt.setInt("pPPL_WORKFLOW_ID", pPPL_WORKFLOW_ID);
            proc_stmt.setInt("pPPL_FILE_ID", pPPL_FILE_ID);
            proc_stmt.setString("pTYPE", pTYPE);
            proc_stmt.setString("pFILE_INFO", pFILE_INFO);
            if (pFROM_FILE_ID != 0) {
                proc_stmt.setInt("pFROM_FILE_ID", pFROM_FILE_ID);
            } else {
                proc_stmt.setString("pFROM_FILE_ID", null);
            }
            proc_stmt.setString("pHMAC", pHMAC);
            proc_stmt.setString("pCREATED_BY", pCREATED_BY);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            System.out.println("USP_PPL_WORKFLOW_FILE_ADD: " + proc_stmt);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    public void USP_GW_PPL_WORKFLOW_GET_LAST_FILE(LastFile[][] response, String pSIGNING_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<LastFile> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_WORKFLOW_GET_LAST_FILE(?,?) }");
            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

            System.out.println("USP_PPL_WORKFLOW_GET_LAST_FILE: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                LastFile tempItem = new LastFile();
                tempItem.setFIRST_PPL_FILE_SIGNED_ID(rs.getInt("FIRST_PPL_FILE_SIGNED_ID"));
                tempItem.setPPL_WORKFLOW_ID(rs.getInt("PPL_WORKFLOW_ID"));
                tempItem.setLAST_PPL_FILE_SIGNED_ID(rs.getInt("LAST_PPL_FILE_SIGNED_ID"));
                tempItem.setLAST_PPL_FILE_NAME(rs.getString("LAST_PPL_FILE_NAME"));
                tempItem.setLAST_PPL_FILE_UUID(rs.getString("LAST_PPL_FILE_UUID"));
                tempItem.setFILE_SIZE(rs.getInt("FILE_SIZE"));
                tempItem.setFILE_TYPE(rs.getString("FILE_TYPE"));
                tempItem.setUPLOAD_TOKEN(rs.getString("UPLOAD_TOKEN"));
                tempItem.setWORKFLOW_DOCUMENT_NAME(rs.getString("WORKFLOW_DOCUMENT_NAME"));
                tempItem.setWORKFLOW_DOCUMENT_FORMAT(rs.getString("WORKFLOW_DOCUMENT_FORMAT"));
                tempItem.setENTERPRISE_ID(rs.getInt("ENTERPRISE_ID"));

                tempList.add(tempItem);
            }
            response[0] = new LastFile[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    // check workfllow signed?
    public String USP_GW_PPL_WORKFLOW_GET_STATUS(String pSIGNING_TOKEN, int[] sStatus, String[] sPostbackWFCheck)
            throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        String convrtr = "1";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_GET_STATUS(?,?) }");
            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

//            System.out.println("USP_PPL_WORKFLOW_GET_STATUS: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                sStatus[0] = rs.getInt("ID");
                sStatus[1] = rs.getInt("WORKFLOW_STATUS");
                sPostbackWFCheck[0] = CommonFunction.CheckTextNull(rs.getString("POSTBACK_URL"));
                sPostbackWFCheck[1] = CommonFunction.CheckTextNull(rs.getString("REDIRECT_URI"));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    public void USP_GW_PPL_WORKFLOW_GET_FIRST_FILE(FirstFile[][] response, String pSIGNING_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<FirstFile> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_WORKFLOW_GET_FIRST_FILE(?,?) }");
            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

//            System.out.println("USP_PPL_WORKFLOW_GET_FIRST_FILE: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                FirstFile tempItem = new FirstFile();
                // tempItem.ID = rs.getInt("FIRST_PPL_FILE_ID");
                tempItem.ID = rs.getInt("PPL_FILE_ID");
                tempItem.FILE_ID = rs.getInt("PPL_FILE_ID");
                tempItem.FILE_NAME = rs.getString("FIRST_PPL_FILE_NAME");
                tempItem.FILE_UUID = rs.getString("FIRST_PPL_FILE_UUID");
                tempItem.UPLOAD_TOKEN = rs.getString("UPLOAD_TOKEN");
                tempItem.ENTERPRISE_ID = rs.getInt("ENTERPRISE_ID");
                tempItem.WORKFLOW_DOCUMENT_NAME = rs.getString("WORKFLOW_DOCUMENT_NAME");
                tempItem.WORKFLOW_DOCUMENT_FORMAT = rs.getString("WORKFLOW_DOCUMENT_FORMAT");
                tempItem.WORKFLOW_ID = rs.getInt("PPL_WORKFLOW_ID");
                tempList.add(tempItem);
            }
            response[0] = new FirstFile[tempList.size()];

            response[0] = tempList.toArray(response[0]);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_PPL_WORKFLOW_PARTICIPANTS_LIST(Participants[][] response, String pSIGNING_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<Participants> tempList = new ArrayList<>();
        String convrtr = "1";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_PARTICIPANTS_LIST(?,?) }");
            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

//            System.out.println("USP_PPL_WORKFLOW_PARTICIPANTS_LIST: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                Participants tempItem = new Participants();
                tempItem.ID = rs.getInt("ID");
                tempItem.PPL_WORKFLOW_ID = rs.getInt("PPL_WORKFLOW_ID");
                tempItem.FIRST_NAME = rs.getString("FIRST_NAME");
                tempItem.LAST_NAME = rs.getString("LAST_NAME");
                tempItem.SIGNER_TOKEN = rs.getString("SIGNER_TOKEN");
                tempItem.SIGNER_STATUS = rs.getInt("SIGNER_STATUS");
                tempItem.SIGNED_TIME = rs.getTimestamp("SIGNED_TIME");
                tempItem.META_INFORMATION = rs.getString("META_INFORMATION");
                tempItem.SIGNING_PURPOSE = rs.getString("SIGNING_PURPOSE");
                tempItem.SIGNED_TYPE = rs.getString("SIGNED_TYPE");
                tempItem.SIGNING_OPTIONS = rs.getString("SIGNING_OPTIONS");
                tempItem.CERTIFICATE = CommonFunction.CheckTextNull(rs.getString("CERTIFICATE"));
                tempItem.SIGNER_ID = rs.getString("SIGNER_ID");
                tempList.add(tempItem);
            }
            response[0] = new Participants[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_PREFIX_PERSONAL_CODE_LIST(PREFIX_UID[][] response, String pTYPE, String pLANGUAGE_NAME) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<PREFIX_UID> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PREFIX_PERSONAL_CODE_LIST(?,?,?) }");
            if (!"".equals(pTYPE)) {
                proc_stmt.setString("pTYPE", pTYPE);
            } else {
                proc_stmt.setString("pTYPE", null);
            }
            if (!"".equals(pLANGUAGE_NAME)) {
                proc_stmt.setString("pLANGUAGE_NAME", pLANGUAGE_NAME);
            } else {
                proc_stmt.setString("pLANGUAGE_NAME", null);
            }

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

//            System.out.println("USP_PREFIX_PERSONAL_CODE_LIST: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                PREFIX_UID tempItem = new PREFIX_UID();
                tempItem.NAME = rs.getString("NAME");
                tempItem.TYPE = rs.getString("TYPE");
                tempItem.REMARK = rs.getString("REMARK");
                tempList.add(tempItem);
            }
            response[0] = new PREFIX_UID[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_CONNECTOR_GET_FROM_PROVIDER(ConnectorName[][] response, String pPROVIDER) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<ConnectorName> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_CONNECTOR_GET_FROM_PROVIDER(?,?) }");
            proc_stmt.setString(1, pPROVIDER);

            proc_stmt.registerOutParameter(2, java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            // convrtr = String.valueOf(proc_stmt.getInt(2));
            convrtr = proc_stmt.getString(2);

//            System.out.println("USP_CONNECTOR_GET_FROM_PROVIDER: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                ConnectorName tempItem = new ConnectorName();
                tempItem.CONNECTOR_NAME = rs.getString("CONNECTOR_NAME");
                tempItem.LOGO = rs.getString("LOGO");
                tempItem.REMARK = rs.getString("REMARK");

                tempList.add(tempItem);

            }
            response[0] = new ConnectorName[tempList.size()];
//            System.out.println(response[0]);
            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    // check workfllow participants signed token
    public String USP_GW_PPL_WORKFLOW_PARTICIPANTS_GET_STATUS(String pSIGNER_TOKEN, int[] sStatus) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        String convrtr = "1";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_PARTICIPANTS_GET_STATUS(?,?) }");
            proc_stmt.setString("pSIGNER_TOKEN", pSIGNER_TOKEN);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

//            System.out.println("USP_PPL_WORKFLOW_GET_STATUS: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                sStatus[0] = rs.getInt("SIGNER_STATUS");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    // update participans
    public String USP_GW_PPL_WORKFLOW_PARTICIPANTS_UPDATE(String pSIGNER_TOKEN, String pSIGNED_TYPE,
                                                          java.sql.Timestamp pSIGNED_TIME,
                                                          String pSIGNATURE_ID, String pSIGNED_ALGORITHM, String pCERTIFICATE, String pSIGNATURE_TYPE,
                                                          String pSIGNING_OPTION, String pLAST_MODIFIED_BY) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_PARTICIPANTS_UPDATE(?,?,?,?,?,?,?,?,?,?) }");

            proc_stmt.setString("pSIGNER_TOKEN", pSIGNER_TOKEN);
            proc_stmt.setString("pSIGNED_TYPE", pSIGNED_TYPE);
            if (pSIGNED_TIME != null) {
                proc_stmt.setObject("pSIGNED_TIME", pSIGNED_TIME);
            } else {
                proc_stmt.setString("pSIGNED_TIME", null);
            }
            proc_stmt.setString("pSIGNATURE_ID", pSIGNATURE_ID);
            proc_stmt.setString("pSIGNED_ALGORITHM", pSIGNED_ALGORITHM);
            proc_stmt.setString("pCERTIFICATE", pCERTIFICATE);
            proc_stmt.setString("pSIGNATURE_TYPE", pSIGNATURE_TYPE);
            proc_stmt.setString("pSIGNING_OPTION", pSIGNING_OPTION);
            proc_stmt.setString("pLAST_MODIFIED_BY", pLAST_MODIFIED_BY);

            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            System.out.println("USP_PPL_WORKFLOW_PARTICIPANTS_UPDATE: " + proc_stmt.toString());
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    // update WF status
    public String USP_GW_PPL_WORKFLOW_UPDATE_STATUS(String pSIGNING_TOKEN, int pWORKFLOW_STATUS, String pLAST_MODIFIED_BY) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_WORKFLOW_UPDATE_STATUS(?,?,?,?) }");

            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);
            proc_stmt.setInt("pWORKFLOW_STATUS", pWORKFLOW_STATUS);
            proc_stmt.setString("pLAST_MODIFIED_BY", pLAST_MODIFIED_BY);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            System.out.println("USP_PPL_WORKFLOW_UPDATE_STATUS: " + proc_stmt.toString());
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    public void USP_GW_PPL_WORKFLOW_GET(WorkFlowList[][] response, String pSIGNING_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<WorkFlowList> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_WORKFLOW_GET(?,?) }");
            if (!"".equals(pSIGNING_TOKEN)) {
                proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);
            } else {
                proc_stmt.setString("pSIGNING_TOKEN", null);
            }
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

//            System.out.println("USP_PPL_WORKFLOW_GET: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                WorkFlowList tempItem = new WorkFlowList();
                tempItem.ID = rs.getInt("ID");
                tempItem.POSTBACK_URL = CommonFunction.CheckTextNull(rs.getString("POSTBACK_URL"));
                tempItem.WORKFLOW_STATUS = rs.getInt("WORKFLOW_STATUS");
                tempItem.WORKFLOW_DOCUMENT_NAME = rs.getString("WORKFLOW_DOCUMENT_NAME");
                tempItem.WORKFLOW_DOCUMENT_FORMAT = rs.getString("WORKFLOW_DOCUMENT_FORMAT");
                tempItem.VISIBLE_HEADER_FOOTER = rs.getInt("VISIBLE_HEADER_FOOTER");

                tempList.add(tempItem);

            }
            response[0] = new WorkFlowList[tempList.size()];
//            System.out.println(response[0]);
            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_PPL_BATCH_FILE_GET(BATCH[][] response, String pBATCH_FILE_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<BATCH> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_BATCH_FILE_GET(?,?) }");

            proc_stmt.setString("pBATCH_FILE_TOKEN", pBATCH_FILE_TOKEN);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                BATCH tempItem = new BATCH();
//                tempItem.ID = rs.getInt("ID");

                tempItem.VISIBLE_HEADER_FOOTER = rs.getInt("VISIBLE_HEADER_FOOTER");
                tempList.add(tempItem);

            }
            response[0] = new BATCH[tempList.size()];
//            System.out.println(response[0]);
            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    // batch
    //truyền batch token lấy ID
    public void USP_GW_PPL_BATCH_FILE_GET_WORKFLOW(BATCH[][] response, String pBATCH_FILE_TOKEN) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<BATCH> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_PPL_BATCH_FILE_GET_WORKFLOW(?,?) }");

            proc_stmt.setString("pBATCH_FILE_TOKEN", pBATCH_FILE_TOKEN);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                BATCH tempItem = new BATCH();
//                tempItem.ID = rs.getInt("ID");
                tempItem.SIGNING_TOKEN = rs.getString("SIGNING_TOKEN");
                tempItem.SIGNER_TOKEN = rs.getString("SIGNER_TOKEN");
                tempList.add(tempItem);

            }
            response[0] = new BATCH[tempList.size()];
//            System.out.println(response[0]);
            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public String getIdentierConnector(String sConnectorName, String[] sResult) throws Exception {
        process conect = new process();
        String sPropertiesFMS = "0";
        ConnectorName[][] object = new ConnectorName[1][];
        ArrayList<ConnectorName> conector = LoadParamSystem.getParamStart(Difinitions.CONFIG_LOAD_PARAM_CONECTOR_NAME);
        if (conector.size() > 0) {
            for (int m = 0; m < conector.size(); m++) {
                if (conector.get(m).CONNECTOR_NAME.equals(sConnectorName)) {
                    sResult[0] = conector.get(m).IDENTIFIER;
                    sResult[1] = conector.get(m).PREFIX_CODE;
                }
            }
        }
        return sPropertiesFMS;
    }

    public String USP_GW_PPL_WORKFLOW_GET_SIGNED_COUNTER(int[] pSIGNED_COUNTER, String pSIGNING_TOKEN) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_WORKFLOW_GET_SIGNED_COUNTER(?,?,?) }");

            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);
            proc_stmt.registerOutParameter("pSIGNED_COUNTER", java.sql.Types.INTEGER);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            System.out.println("USP_PPL_WORKFLOW_UPDATE_STATUS: " + proc_stmt.toString());
            proc_stmt.execute();
            pSIGNED_COUNTER[0] = proc_stmt.getInt("pSIGNED_COUNTER");
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    public String USP_GW_SIGNER_CHECK_EXIST(int[] pIS_EXIST, String pSIGNING_TOKEN, String pSIGNER_TOKEN) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_SIGNER_CHECK_EXIST(?,?,?,?) }");

            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);
            proc_stmt.setString("pSIGNER_TOKEN", pSIGNER_TOKEN);

            proc_stmt.registerOutParameter("pIS_EXIST", java.sql.Types.INTEGER);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);

            proc_stmt.execute();
            pIS_EXIST[0] = proc_stmt.getInt("pIS_EXIST");
            convrtr = proc_stmt.getString("pRESPONSE_CODE");
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    //    @RequestMapping(value = {"/checkHeader"}, method = RequestMethod.POST)
//    public static Map<String, Object> checkHeader(@RequestBody Map<String, String> signingToken) throws Exception {
//        process conect = new process();
//        String pSIGNING_TOKEN = signingToken.get("signingToken");
////        ArrayList<Object> intt = new ArrayList<Object>();
//        WorkFlowList[][] rsParticipant = new WorkFlowList[1][];
//        conect.USP_GW_PPL_WORKFLOW_GET(rsParticipant, pSIGNING_TOKEN);
//        Map<String, Object> map = new HashMap();
//        if (rsParticipant != null && rsParticipant[0].length > 0) {
//            for (int j = 0; j < rsParticipant[0].length; j++) {
//
//                map.put("WORKFLOW_DOCUMENT_NAME", rsParticipant[0][j].WORKFLOW_DOCUMENT_NAME);
//                map.put("WORKFLOW_DOCUMENT_FORMAT", rsParticipant[0][j].WORKFLOW_DOCUMENT_FORMAT);
//                map.put("visibleHeaderFooter", rsParticipant[0][j].VISIBLE_HEADER_FOOTER);
////                intt.add(map);
//            }
//        }
//        return map;
//    }
//
//    @RequestMapping(value = {"/checkHeaderBS"}, method = RequestMethod.POST)
//    public int checkHeaderBS(@RequestBody Map<String, String> batchToken) throws Exception {
//        process conect = new process();
//        int isSet = 0;
//        String pBATCH_TOKEN = batchToken.get("batchToken");
//        BATCH[][] batch = new BATCH[1][];
//        ArrayList<Object> list = new ArrayList<Object>();
//        conect.USP_GW_PPL_BATCH_FILE_GET(batch, pBATCH_TOKEN);
//        if (batch != null && batch[0].length > 0) {
//            for (int i = 0; i < batch[0].length; i++) {
//                isSet = batch[0][i].VISIBLE_HEADER_FOOTER;
//            }
//        }
//        return isSet ;
//    }
    public void USP_GW_CONNECTOR_LIST(ConnectorName[][] response) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<ConnectorName> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_CONNECTOR_LIST() }");
//            proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);

//            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            proc_stmt.execute();
//            convrtr = proc_stmt.getString("pRESPONSE_CODE");
//            System.out.println("USP_PPL_WORKFLOW_PARTICIPANTS_LIST: " + proc_stmt.toString());
            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                ConnectorName tempItem = new ConnectorName();
                tempItem.ID = rs.getInt("ID");
                tempItem.IDENTIFIER = rs.getString("IDENTIFIER");
                tempItem.LOGO = rs.getString("LOGO");
                tempItem.CONNECTOR_NAME = rs.getString("CONNECTOR_NAME");
                tempItem.REMARK = rs.getString("REMARK");
                tempItem.PREFIX_CODE = rs.getString("PREFIX_CODE");
                tempItem.PROVIDER = rs.getString("PROVIDER");
                tempList.add(tempItem);
            }
            response[0] = new ConnectorName[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_ENTERPRISE_LIST(ENTERPRISE[][] response) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<ENTERPRISE> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_ENTERPRISE_LIST() }");

            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                ENTERPRISE tempItem = new ENTERPRISE();
                tempItem.ID = rs.getInt("ID");
                tempItem.METADATA_GATEWAY_VIEW = rs.getString("METADATA_GATEWAY_VIEW");
                tempItem.LOGO = rs.getString("LOGO");

                tempList.add(tempItem);
            }
            response[0] = new ENTERPRISE[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public void USP_GW_COUNTRY_WITH_ELECTRONIC_LIST(CountryModel[][] response) throws Exception {
        CallableStatement proc_stmt = null;
        Connection conns = null;
        ResultSet rs = null;
        ArrayList<CountryModel> tempList = new ArrayList<>();
        String convrtr = "0";
        try {
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_COUNTRY_WITH_ELECTRONIC_LIST() }");

            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                CountryModel tempItem = new CountryModel();
                tempItem.ID = rs.getInt("ID");
                tempItem.META_DATA = rs.getString("META_DATA");
                tempItem.REMARK_EN = rs.getString("REMARK_EN");
                tempItem.REMARK = rs.getString("REMARK");

                tempList.add(tempItem);
            }
            response[0] = new CountryModel[tempList.size()];

            response[0] = tempList.toArray(response[0]);
//            System.out.println(tempList);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public String USP_GW_PPL_CONNECTOR_LOG_ADD(
            String pUSER_EMAIL,
            String pCONNECTOR_NAME,
            int pENTERPRISE_ID,
            int pWORKFLOW_ID,
            String pAPP_NAME,
            String pAPI_KEY,
            String pVERSION,
            String pSERVICE_NAME,
            String pURL,
            String pHTTP_VERB,
            int pSTATUS_CODE,
            String pREQUEST,
            String pRESPONSE,
            String pHMAC,
            String pCREATED_BY) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;

        try {

            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_CONNECTOR_LOG_ADD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

            proc_stmt.setString("pUSER_EMAIL", pUSER_EMAIL);
            proc_stmt.setString("pCONNECTOR_NAME", pCONNECTOR_NAME);
            proc_stmt.setInt("pENTERPRISE_ID", pENTERPRISE_ID); // pENTERPRISE_ID            // file size
            proc_stmt.setInt("pWORKFLOW_ID", pWORKFLOW_ID);
            proc_stmt.setString("pAPP_NAME", pAPP_NAME);
            proc_stmt.setString("pAPI_KEY", pAPI_KEY);
            proc_stmt.setString("pVERSION", pVERSION);
            proc_stmt.setString("pSERVICE_NAME", pSERVICE_NAME);
            proc_stmt.setString("pURL", pURL);
            proc_stmt.setString("pHTTP_VERB", pHTTP_VERB);
            proc_stmt.setInt("pSTATUS_CODE", pSTATUS_CODE);
            proc_stmt.setString("pREQUEST", pREQUEST);
            proc_stmt.setString("pRESPONSE", pRESPONSE);
            proc_stmt.setString("pHMAC", pHMAC);
            proc_stmt.setString("pCREATED_BY", pCREATED_BY);

            proc_stmt.registerOutParameter("pPPL_CONNECTOR_LOG_ID", java.sql.Types.BIGINT); // giá trị out file_id
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.VARCHAR); // giá trị out response code

            proc_stmt.execute();
            int abc = proc_stmt.getInt("pPPL_CONNECTOR_LOG_ID");
            convrtr = String.valueOf(proc_stmt.getInt("pRESPONSE_CODE"));
            System.out.println("convrtr: " + convrtr); // kiem tra
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }

        return convrtr;
    }

    public String USP_GW_PPL_CALLBACK_LOG_ADD(
            String pUSER_EMAIL,
            int pENTERPRISE_ID,
            int pWORKFLOW_ID,
            String pAPP_NAME,
            String pAPI_KEY,
            String pVERSION,
            String pSERVICE_NAME,
            String pURL,
            String pHTTP_VERB,
            int pSTATUS_CODE,
            String pREQUEST,
            String pRESPONSE,
            int pRETRYING_COUNTER,
            Date pNEXT_RETRY_AT,
            String pHMAC,
            String pCREATED_BY) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        try {

            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_CALLBACK_LOG_ADD(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

            proc_stmt.setString("pUSER_EMAIL", pUSER_EMAIL);
            proc_stmt.setInt("pENTERPRISE_ID", pENTERPRISE_ID); // pENTERPRISE_ID            // file size
            proc_stmt.setInt("pWORKFLOW_ID", pWORKFLOW_ID);
            proc_stmt.setString("pAPP_NAME", pAPP_NAME);
            proc_stmt.setString("pAPI_KEY", pAPI_KEY);
            proc_stmt.setString("pVERSION", pVERSION);
            proc_stmt.setString("pSERVICE_NAME", pSERVICE_NAME);
            proc_stmt.setString("pURL", pURL);
            proc_stmt.setString("pHTTP_VERB", pHTTP_VERB);
            proc_stmt.setInt("pSTATUS_CODE", pSTATUS_CODE);
            proc_stmt.setString("pREQUEST", pREQUEST);
            proc_stmt.setString("pRESPONSE", pRESPONSE);
            proc_stmt.setInt("pRETRYING_COUNTER", pRETRYING_COUNTER);
            proc_stmt.setDate("pNEXT_RETRY_AT", pNEXT_RETRY_AT);
            proc_stmt.setString("pHMAC", pHMAC);
            proc_stmt.setString("pCREATED_BY", pCREATED_BY);

            proc_stmt.registerOutParameter("pPPL_CALLBACK_LOG_ID", java.sql.Types.BIGINT); // giá trị out file_id
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.VARCHAR); // giá trị out response code

//            System.out.println("USP_PPL_FILE_ADD: " + proc_stmt); // kiem tra
            proc_stmt.execute();
//            int abc =  proc_stmt.getInt("pPPL_CALLBACK_LOG_ID");
            convrtr = String.valueOf(proc_stmt.getInt("pRESPONSE_CODE"));
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
        return convrtr;
    }

    public String USP_GW_PPL_FILE_DETAIL_GET_SIGNATURE(int pPPL_FILE_ID, List<PplFileDetail> listPplFileDetail) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("pPPL_FILE_ID: " + pPPL_FILE_ID);
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_FILE_DETAIL_GET_SIGNATURE(?,?) }");
            proc_stmt.setInt("pPPL_FILE_ID", pPPL_FILE_ID);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);

            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                PplFileDetail pplFileDetail = new PplFileDetail();
                pplFileDetail.setId(rs.getInt("ID"));
                pplFileDetail.setPpl_file_id(rs.getInt("PPL_FILE_ID"));
                pplFileDetail.setPpl_file_attr_type_id(rs.getInt("PPL_FILE_ATTR_TYPE_ID"));
                pplFileDetail.setValue(rs.getString("VALUE"));
                pplFileDetail.setHmac(rs.getString("HMAC"));
                pplFileDetail.setCreated_by(rs.getString("CREATED_BY"));
                pplFileDetail.setCreated_at(rs.getDate("CREATED_AT"));
                pplFileDetail.setLast_modified_by(rs.getString("LAST_MODIFIED_BY"));
                pplFileDetail.setLast_modified_at(rs.getDate("LAST_MODIFIED_AT"));
                listPplFileDetail.add(pplFileDetail);
            }
            return convrtr;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }

    public String USP_GW_PPL_FILE_DETAIL_GET_FROM_UPLOAD_TOKEN(String pUPLOAD_TOKEN, List<PplFileDetail> listPplFileDetail) throws Exception {
        String convrtr = "1";
        Connection conns = null;
        CallableStatement proc_stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("pUPLOAD_TOKEN: " + pUPLOAD_TOKEN);
            conns = OpenDatabase();
            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_FILE_DETAIL_GET_FROM_UPLOAD_TOKEN(?,?) }");
            proc_stmt.setString("pUPLOAD_TOKEN", pUPLOAD_TOKEN);
            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);

            proc_stmt.execute();
            convrtr = proc_stmt.getString("pRESPONSE_CODE");

            rs = proc_stmt.executeQuery();
            while (rs.next()) {
                PplFileDetail pplFileDetail = new PplFileDetail();
                pplFileDetail.setId(rs.getInt("ID"));
                pplFileDetail.setPpl_file_id(rs.getInt("PPL_FILE_ID"));
                pplFileDetail.setPpl_file_attr_type_id(rs.getInt("PPL_FILE_ATTR_TYPE_ID"));
                pplFileDetail.setValue(rs.getString("VALUE"));
                pplFileDetail.setHmac(rs.getString("HMAC"));
                pplFileDetail.setCreated_by(rs.getString("CREATED_BY"));
                pplFileDetail.setCreated_at(rs.getDate("CREATED_AT"));
                pplFileDetail.setLast_modified_by(rs.getString("LAST_MODIFIED_BY"));
                pplFileDetail.setLast_modified_at(rs.getDate("LAST_MODIFIED_AT"));
                listPplFileDetail.add(pplFileDetail);
            }
            return convrtr;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            if (proc_stmt != null) {
                proc_stmt.close();
            }
            Connection[] temp_connection = new Connection[]{conns};
            CloseDatabase(temp_connection);
        }
    }


//    public void USP_GW_PPL_WORKFLOW_GET(WorkFlowList[][] response, String pSIGNING_TOKEN) throws Exception {
//        CallableStatement proc_stmt = null;
//        Connection conns = null;
//        ResultSet rs = null;
//        ArrayList<WorkFlowList> tempList = new ArrayList<>();
//        String convrtr = "0";
//        try {
//            conns = OpenDatabase();
//            proc_stmt = conns.prepareCall("{ call USP_GW_PPL_WORKFLOW_GET(?,?) }");
//            if (!"".equals(pSIGNING_TOKEN)) {
//                proc_stmt.setString("pSIGNING_TOKEN", pSIGNING_TOKEN);
//            } else {
//                proc_stmt.setString("pSIGNING_TOKEN", null);
//            }
//            proc_stmt.registerOutParameter("pRESPONSE_CODE", java.sql.Types.NVARCHAR);
//            proc_stmt.execute();
//            convrtr = proc_stmt.getString("pRESPONSE_CODE");
//
////            System.out.println("USP_PPL_WORKFLOW_GET: " + proc_stmt.toString());
//            rs = proc_stmt.executeQuery();
//            while (rs.next()) {
//                WorkFlowList tempItem = new WorkFlowList();
//                tempItem.ID = rs.getInt("ID");
//                tempItem.POSTBACK_URL = CommonFunction.CheckTextNull(rs.getString("POSTBACK_URL"));
//                tempItem.WORKFLOW_STATUS = rs.getInt("WORKFLOW_STATUS");
//                tempItem.WORKFLOW_DOCUMENT_NAME = rs.getString("WORKFLOW_DOCUMENT_NAME");
//                tempItem.WORKFLOW_DOCUMENT_FORMAT = rs.getString("WORKFLOW_DOCUMENT_FORMAT");
//                tempItem.VISIBLE_HEADER_FOOTER = rs.getInt("VISIBLE_HEADER_FOOTER");
//
//                tempList.add(tempItem);
//
//            }
//            response[0] = new WorkFlowList[tempList.size()];
////            System.out.println(response[0]);
//            response[0] = tempList.toArray(response[0]);
////            System.out.println(tempList);
//        } finally {
//            if (rs != null) {
//                rs.close();
//            }
//            if (proc_stmt != null) {
//                proc_stmt.close();
//            }
//            Connection[] temp_connection = new Connection[]{conns};
//            CloseDatabase(temp_connection);
//        }
//    }
}
