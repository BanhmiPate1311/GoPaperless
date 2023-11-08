package vn.mobileid.paperless.object;

public class LastFile {
    private int FIRST_PPL_FILE_SIGNED_ID;
    private int LAST_PPL_FILE_SIGNED_ID;
    private String LAST_PPL_FILE_NAME;
    private String LAST_PPL_FILE_UUID;
    private int FILE_SIZE;
    private String FILE_TYPE;
    private String UPLOAD_TOKEN;
    private String WORKFLOW_DOCUMENT_NAME;
    private String WORKFLOW_DOCUMENT_FORMAT;
    private int PPL_WORKFLOW_ID;
    private int ENTERPRISE_ID;
    private int DOCUMENT_ID;

    public int getFIRST_PPL_FILE_SIGNED_ID() {
        return FIRST_PPL_FILE_SIGNED_ID;
    }

    public void setFIRST_PPL_FILE_SIGNED_ID(int FIRST_PPL_FILE_SIGNED_ID) {
        this.FIRST_PPL_FILE_SIGNED_ID = FIRST_PPL_FILE_SIGNED_ID;
    }

    public int getLAST_PPL_FILE_SIGNED_ID() {
        return LAST_PPL_FILE_SIGNED_ID;
    }

    public void setLAST_PPL_FILE_SIGNED_ID(int LAST_PPL_FILE_SIGNED_ID) {
        this.LAST_PPL_FILE_SIGNED_ID = LAST_PPL_FILE_SIGNED_ID;
    }

    public String getLAST_PPL_FILE_NAME() {
        return LAST_PPL_FILE_NAME;
    }

    public void setLAST_PPL_FILE_NAME(String LAST_PPL_FILE_NAME) {
        this.LAST_PPL_FILE_NAME = LAST_PPL_FILE_NAME;
    }

    public String getLAST_PPL_FILE_UUID() {
        return LAST_PPL_FILE_UUID;
    }

    public void setLAST_PPL_FILE_UUID(String LAST_PPL_FILE_UUID) {
        this.LAST_PPL_FILE_UUID = LAST_PPL_FILE_UUID;
    }

    public int getFILE_SIZE() {
        return FILE_SIZE;
    }

    public void setFILE_SIZE(int FILE_SIZE) {
        this.FILE_SIZE = FILE_SIZE;
    }

    public String getFILE_TYPE() {
        return FILE_TYPE;
    }

    public void setFILE_TYPE(String FILE_TYPE) {
        this.FILE_TYPE = FILE_TYPE;
    }

    public String getUPLOAD_TOKEN() {
        return UPLOAD_TOKEN;
    }

    public void setUPLOAD_TOKEN(String UPLOAD_TOKEN) {
        this.UPLOAD_TOKEN = UPLOAD_TOKEN;
    }

    public String getWORKFLOW_DOCUMENT_NAME() {
        return WORKFLOW_DOCUMENT_NAME;
    }

    public void setWORKFLOW_DOCUMENT_NAME(String WORKFLOW_DOCUMENT_NAME) {
        this.WORKFLOW_DOCUMENT_NAME = WORKFLOW_DOCUMENT_NAME;
    }

    public String getWORKFLOW_DOCUMENT_FORMAT() {
        return WORKFLOW_DOCUMENT_FORMAT;
    }

    public void setWORKFLOW_DOCUMENT_FORMAT(String WORKFLOW_DOCUMENT_FORMAT) {
        this.WORKFLOW_DOCUMENT_FORMAT = WORKFLOW_DOCUMENT_FORMAT;
    }

    public int getPPL_WORKFLOW_ID() {
        return PPL_WORKFLOW_ID;
    }

    public void setPPL_WORKFLOW_ID(int PPL_WORKFLOW_ID) {
        this.PPL_WORKFLOW_ID = PPL_WORKFLOW_ID;
    }

    public int getENTERPRISE_ID() {
        return ENTERPRISE_ID;
    }

    public void setENTERPRISE_ID(int ENTERPRISE_ID) {
        this.ENTERPRISE_ID = ENTERPRISE_ID;
    }

    public int getDOCUMENT_ID() {
        return DOCUMENT_ID;
    }

    public void setDOCUMENT_ID(int DOCUMENT_ID) {
        this.DOCUMENT_ID = DOCUMENT_ID;
    }
}
