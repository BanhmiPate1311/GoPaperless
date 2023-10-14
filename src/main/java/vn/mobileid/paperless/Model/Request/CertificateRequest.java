package vn.mobileid.paperless.Model.Request;

public class CertificateRequest extends Request {
    public String userID;

    public String agreementUUID;
    public String certificates;
    public Boolean certInfoEnabled;
    public Boolean authInfoEnabled;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public Boolean getCertInfoEnabled() {
        return certInfoEnabled;
    }

    public void setCertInfoEnabled(Boolean certInfoEnabled) {
        this.certInfoEnabled = certInfoEnabled;
    }

    public Boolean getAuthInfoEnabled() {
        return authInfoEnabled;
    }

    public void setAuthInfoEnabled(Boolean authInfoEnabled) {
        this.authInfoEnabled = authInfoEnabled;
    }
}
   