/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vn.mobileid.fms.client.JCRConfig;
import vn.mobileid.fms.client.JCRException;
import vn.mobileid.fms.client.JCRFile;
import vn.mobileid.fms.client.JCRManager;
import vn.mobileid.fms.client.loadbalancing.HealcheckConfig;

import java.io.InputStream;
import java.sql.SQLException;


/**
 *
 * @author PHY
 */
public class JackRabbitCommon {
    
    //public static JCRConfig jcrConfig;
    private static final Logger log = LogManager.getLogger(JackRabbitCommon.class.getName());
//    static final String CONFIG_FILE = "localconfig.properties";

   public JCRManager jcrManager;

    //private static JackRabbitCommon INSTANCE;

//    public static JackRabbitCommon newInstance(JCRConfig jcrConfig) throws Exception{
//        if(INSTANCE != null){
//            throw new Exception("FMS instance is exist");
//        }
//        INSTANCE = new JackRabbitCommon();
//        
//        INSTANCE.jcrManager = JCRManager.getInstance(jcrConfig, new HealcheckConfig());
//        
//        return INSTANCE;
//    }
    public synchronized static JackRabbitCommon getInstance(JCRConfig jcrConfig) throws Exception {
//        if (INSTANCE == null) {
//            INSTANCE = new JackRabbitCommon();
//            INSTANCE.jcrManager = JCRManager.getInstance(jcrConfig, new HealcheckConfig());
//        }
//        return INSTANCE;
        JackRabbitCommon jrc = new JackRabbitCommon();
        jrc.jcrManager = JCRManager.getInstance(jcrConfig, new HealcheckConfig());
        return jrc;
    }

    public boolean changePasswordWorkSpace(String workspace, String loginId, String loginPsw, String userId, String newPsw) throws JCRException {
        return jcrManager.changePasswordForWorkspace(workspace, loginId, loginPsw, userId, newPsw);
    }

    public boolean changePasswordAdmin(String oldPsw, String newPsw) throws JCRException {
        return jcrManager.changePasswordAdmin(oldPsw, newPsw);
    }

    public boolean createWorkSpace(String userAdmin, String pswAdmin, String wsName) throws JCRException {
        return JCRManager.createWorkspace(userAdmin, pswAdmin, wsName);
    }
//    public void uploadFileThread(String fileNameToSave, String sMimeType, InputStream stream, FILE_PROFILE_DATA mhIP,
//        String sJRBConfig, String pCERTIFICATION_ID, String hdfOwnerID, String loginUID, int[] pFILE_MANAGER_ID) {
//        ThreadPoolExecutor threadPoolExecutor = ThreadPoolManagement.getThreadPoolExecutorForEverything();
//        threadPoolExecutor.submit(new Runnable() {
//            @Override
//            public void run() {
//                String fileNameToSaveSS = "";
//                if (fileNameToSave != null && !fileNameToSave.isEmpty()) {
//                    fileNameToSaveSS = CommonFunction.clearUnicodeFontString(fileNameToSave);
//                }
//                try {
//                    if(!"".equals(fileNameToSaveSS)){
//                        CommonFunction.LogDebugString(log, "uploadFile-JRB", "; fileNameToSave: " + fileNameToSaveSS);
//                        JCRFile uploadFile = new JCRFile();
//                        uploadFile.setFilePath(fileNameToSaveSS);
//                        uploadFile.setMimeType(sMimeType);
//                        uploadFile.setStream(stream);
//                        JCRFile jrbFile = jcrManager.uploadJCR(uploadFile);
//                        if(jrbFile != null) {
//                            
//                            process db = new process();
//                            
//                            db.S_BO_FILE_MANAGER_INSERT_WITH_OWNER(mhIP.FILE_PROFILE, jrbFile.getUuid(), sJRBConfig, EscapeUtils.CheckTextNull(mhIP.FILE_MIMETYPE),
//                                jrbFile.getFileName(), (int) mhIP.FILE_SIZE, pCERTIFICATION_ID, hdfOwnerID, loginUID, pFILE_MANAGER_ID);
//                            System.out.println("pFILE_MANAGER_ID inner: " + pFILE_MANAGER_ID[0]);
//                        }
//                    } else {
//                        CommonFunction.LogErrorServlet(log, "uploadFileThread: FileName is empty");
//                    }
//                } catch (Exception ex) {
//                    CommonFunction.LogExceptionServlet(log, "FileName: " + fileNameToSaveSS + "; " + ex.getMessage(), ex);
//                }
//            }
//        });
//    }

    public JCRFile uploadFile(String fileNameToSave, String sMimeType, InputStream stream) {
        JCRFile referFile = null;
        try {
            if (fileNameToSave != null && !fileNameToSave.isEmpty()) {
              //  fileNameToSave = CommonFunction.clearUnicodeFontString(fileNameToSave);
            } else {
                fileNameToSave = "file_upload";
            }
           // CommonFunction.LogDebugString(log, "uploadFile-JRB", "; fileNameToSave: " + fileNameToSave);
            JCRFile uploadFile = new JCRFile();
            uploadFile.setFilePath(fileNameToSave); // + ".pdf"
            uploadFile.setMimeType(sMimeType); //"application/pdf"
            uploadFile.setStream(stream);
            referFile = jcrManager.uploadJCR(uploadFile);
            //        Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //        log.info("jcrFile is: " + gson.toJson(referFile, JCRFile.class));
        } catch (Exception ex) {
            System.out.println("JCRManager.destroyAllJCR()");
            jcrManager.destroyAllJCR();
            JCRManager.destroyAllJCR();
            //CommonFunction.LogExceptionServlet(log, "FileName: " + fileNameToSave + "; " + ex.getMessage(), ex);
//            if (jcrManager != null) {
//                jcrManager.destroy();
//                CommonFunction.LogDebugString(log, "uploadFile-JRB", "Destroy");
//            }
//            if(jcrConfig != null) {
//                jcrConfig = null;
//                CommonFunction.LogDebugString(log, "uploadFile-JRB-jcrConfig", "null");
//            }
            //JCRManager.destroyAllJCR();
        }
//        finally {
//            System.out.println("JCRManager.destroyAllJCR()");
//            JCRManager.destroyAllJCR();
//        }
        return referFile;
    }

    public JCRFile downloadFile(String uuid) {
        JCRFile downloadFile = null;
        try {
          //  CommonFunction.LogDebugString(log, "downloadFile-JRB", "; uuid: " + uuid);
            //        LOG.info("Get JCRManager instance successful...");
            downloadFile = jcrManager.downloadJCR(uuid);
            //        LOG.info("UUID : " + downloadFile.getUuid());
            //        LOG.info("File Path : " + downloadFile.getFilePath());
            //        LOG.info("Mime Type : " + downloadFile.getMimeType());
            //        LOG.info("Last Modified : " + downloadFile.getLastModified());
            //        LOG.info("Last Modified By : " + downloadFile.getLastModifiedBy());
            //        LOG.info("File Name : " + downloadFile.getFileName());

            //        OutputStream fOut = new FileOutputStream("E:/Project/TMS CA/TMS EFY/Demo JackRabbit/" + downloadFile.getFileName());
            //        IOUtils.write(IOUtils.toByteArray(downloadFile.getStream()), fOut);
            //        fOut.close();
        } catch (Exception ex) {
            System.out.println("JCRManager.destroyAllJCR()");
            jcrManager.destroyAllJCR();
            JCRManager.destroyAllJCR();
       //     CommonFunction.LogExceptionServlet(log, "UUID: " + uuid + "; " + ex.getMessage(), ex);
//            if (jcrManager != null) {
//                jcrManager.destroy();
//                CommonFunction.LogDebugString(log, "downloadFile-JRB", "Destroy");
//            }
//            if(jcrConfig != null) {
//                jcrConfig = null;
//                CommonFunction.LogDebugString(log, "downloadFile-JRB-jcrConfig", "null");
//            }
//            JCRManager.destroyAllJCR();
        }
//        finally {
//            System.out.println("JCRManager.destroyAllJCR()");
//            JCRManager.destroyAllJCR();
//        }

//        finally {
//            if(jcrConfig != null) {
//                jcrConfig = null;
//                CommonFunction.LogDebugString(log, "downloadFile-JRB-jcrConfig", "null");
//            }
//        }
        return downloadFile;
    }

//    static final String JCR_HOST = "jcr.config.host";
//    static final String JCR_USER_ID = "jcr.config.userid";
//    static final String JCR_PWD = "jcr.config.password";
//    static final String JCR_MAX_SESSION = "jcr.config.maxSession";
//    static final String JCR_MAX_FILE_IN_FOLDER = "jcr.config.maxFileInFolder";
//    static final String JCR_MAX_PREFIX_FOLDER = "jcr.config.prefixFolder";
//    static final String JCR_WORKSPACE = "jcr.config.workspace";
    public static JCRConfig getJCRConfig(String sJCR_HOST, String sJCR_USER_ID, String sJCR_PWD,
            int sJCR_MAX_SESSION, int sJCR_MAX_FILE_IN_FOLDER, String sJCR_WORKSPACE,
            String sJCR_MAX_PREFIX_FOLDER) throws SQLException, JCRException {
//        CommonFunction.LogDebugString(log, "getJCRConfig-Request", "sJCR_HOST: " + sJCR_HOST + "; sJCR_USER_ID: " + sJCR_USER_ID
//                + "; sJCR_PWD: " + sJCR_PWD + "; sJCR_MAX_SESSION: " + sJCR_MAX_SESSION + "; sJCR_MAX_FILE_IN_FOLDER: " + sJCR_MAX_FILE_IN_FOLDER
//                + "; sJCR_WORKSPACE: " + sJCR_WORKSPACE + "; sJCR_MAX_PREFIX_FOLDER: " + sJCR_MAX_PREFIX_FOLDER);
        JCRConfig jcrConfig = new JCRConfig();
        if (sJCR_MAX_SESSION == 0) {
            sJCR_MAX_SESSION = 50;
        }
        if (sJCR_MAX_FILE_IN_FOLDER == 0) {
            sJCR_MAX_FILE_IN_FOLDER = 10000;
        }
        if ("".equals(sJCR_WORKSPACE)) {
            sJCR_WORKSPACE = "default";
        }
        if ("".equals(sJCR_MAX_PREFIX_FOLDER)) {
            sJCR_MAX_PREFIX_FOLDER = "247_client_";
        }
        jcrConfig.setHost(sJCR_HOST);
        jcrConfig.setUserID(sJCR_USER_ID);
        jcrConfig.setPassword(sJCR_PWD);
        jcrConfig.setMaxSession(sJCR_MAX_SESSION);
        jcrConfig.setMaxFileInFolder(sJCR_MAX_FILE_IN_FOLDER);
        jcrConfig.setWorkSpaceName(sJCR_WORKSPACE);
        jcrConfig.setFolderPrefix(sJCR_MAX_PREFIX_FOLDER);

        return jcrConfig;
    }
}
