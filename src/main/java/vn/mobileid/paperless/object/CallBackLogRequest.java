package vn.mobileid.paperless.object;

import java.sql.Date;

public class CallBackLogRequest {
    private String pUSER_EMAIL;
    private int pENTERPRISE_ID;
    private int pWORKFLOW_ID;
    private String pAPP_NAME;
    private String API_KEY;
    private String pVERSION;
    private String pSERVICE_NAME;
    private String pURL;
    private String pHTTP_VERB;
    private int pSTATUS_CODE;
    private String pREQUEST;
    private String pRESPONSE;
    private int pRETRYING_COUNTER;
    private Date pNEXT_RETRY_AT;
    private String pHMAC;
    private String pCREATED_BY;

    public String getpUSER_EMAIL() {
        return pUSER_EMAIL;
    }

    public void setpUSER_EMAIL(String pUSER_EMAIL) {
        this.pUSER_EMAIL = pUSER_EMAIL;
    }

    public int getpENTERPRISE_ID() {
        return pENTERPRISE_ID;
    }

    public void setpENTERPRISE_ID(int pENTERPRISE_ID) {
        this.pENTERPRISE_ID = pENTERPRISE_ID;
    }

    public int getpWORKFLOW_ID() {
        return pWORKFLOW_ID;
    }

    public void setpWORKFLOW_ID(int pWORKFLOW_ID) {
        this.pWORKFLOW_ID = pWORKFLOW_ID;
    }

    public String getpAPP_NAME() {
        return pAPP_NAME;
    }

    public void setpAPP_NAME(String pAPP_NAME) {
        this.pAPP_NAME = pAPP_NAME;
    }

    public String getAPI_KEY() {
        return API_KEY;
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public String getpVERSION() {
        return pVERSION;
    }

    public void setpVERSION(String pVERSION) {
        this.pVERSION = pVERSION;
    }

    public String getpSERVICE_NAME() {
        return pSERVICE_NAME;
    }

    public void setpSERVICE_NAME(String pSERVICE_NAME) {
        this.pSERVICE_NAME = pSERVICE_NAME;
    }

    public String getpURL() {
        return pURL;
    }

    public void setpURL(String pURL) {
        this.pURL = pURL;
    }

    public String getpHTTP_VERB() {
        return pHTTP_VERB;
    }

    public void setpHTTP_VERB(String pHTTP_VERB) {
        this.pHTTP_VERB = pHTTP_VERB;
    }

    public int getpSTATUS_CODE() {
        return pSTATUS_CODE;
    }

    public void setpSTATUS_CODE(int pSTATUS_CODE) {
        this.pSTATUS_CODE = pSTATUS_CODE;
    }

    public String getpREQUEST() {
        return pREQUEST;
    }

    public void setpREQUEST(String pREQUEST) {
        this.pREQUEST = pREQUEST;
    }

    public String getpRESPONSE() {
        return pRESPONSE;
    }

    public void setpRESPONSE(String pRESPONSE) {
        this.pRESPONSE = pRESPONSE;
    }

    public int getpRETRYING_COUNTER() {
        return pRETRYING_COUNTER;
    }

    public void setpRETRYING_COUNTER(int pRETRYING_COUNTER) {
        this.pRETRYING_COUNTER = pRETRYING_COUNTER;
    }

    public Date getpNEXT_RETRY_AT() {
        return pNEXT_RETRY_AT;
    }

    public void setpNEXT_RETRY_AT(Date pNEXT_RETRY_AT) {
        this.pNEXT_RETRY_AT = pNEXT_RETRY_AT;
    }

    public String getpHMAC() {
        return pHMAC;
    }

    public void setpHMAC(String pHMAC) {
        this.pHMAC = pHMAC;
    }

    public String getpCREATED_BY() {
        return pCREATED_BY;
    }

    public void setpCREATED_BY(String pCREATED_BY) {
        this.pCREATED_BY = pCREATED_BY;
    }
}
