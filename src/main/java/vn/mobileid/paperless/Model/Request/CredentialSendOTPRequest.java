package vn.mobileid.paperless.Model.Request;

public class CredentialSendOTPRequest extends Request {

    public String agreementUUID;
    public String credentialID;
    public String notificationTemplate;
    public String notificationSubject;

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }

    public String getNotificationTemplate() {
        return notificationTemplate;
    }

    public void setNotificationTemplate(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
    }

    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }
}
