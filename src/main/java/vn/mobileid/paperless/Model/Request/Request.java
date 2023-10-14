package vn.mobileid.paperless.Model.Request;

public class Request {
    public String profile;
    public String lang;
    public String requestID;
    public String rpRequestID;

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getRpRequestID() {
        return rpRequestID;
    }

    public void setRpRequestID(String rpRequestID) {
        this.rpRequestID = rpRequestID;
    }
    
    public Request(){
        this.profile = "rssp-119.432-v2.0";
        this.lang    = "VN";
    }
}
