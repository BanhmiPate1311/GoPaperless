package vn.mobileid.paperless.Model.Request;

public class CredentialInfoRequest extends CertificateRequest {

    public String credentialID;

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }
}
