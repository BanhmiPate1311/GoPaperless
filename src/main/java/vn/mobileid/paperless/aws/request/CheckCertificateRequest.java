package vn.mobileid.paperless.aws.request;

public class CheckCertificateRequest extends ElectronicBaseRequest{
    private String jwt;
    private String credentialID;
    private String connectorNameRSSP;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }

    public String getConnectorNameRSSP() {
        return connectorNameRSSP;
    }

    public void setConnectorNameRSSP(String connectorNameRSSP) {
        this.connectorNameRSSP = connectorNameRSSP;
    }
}
