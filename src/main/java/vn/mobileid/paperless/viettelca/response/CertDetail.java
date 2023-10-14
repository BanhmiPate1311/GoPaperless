package vn.mobileid.paperless.viettelca.response;

public class CertDetail {
    private String description;
    private Key key;
    private VtCert cert;
    private String authMode;
    private String multiSign;
    private String credential_id;
    private String SCAL;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public VtCert getCert() {
        return cert;
    }

    public void setCert(VtCert cert) {
        this.cert = cert;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public String getMultiSign() {
        return multiSign;
    }

    public void setMultiSign(String multiSign) {
        this.multiSign = multiSign;
    }

    public String getCredential_id() {
        return credential_id;
    }

    public void setCredential_id(String credential_id) {
        this.credential_id = credential_id;
    }

    public String getSCAL() {
        return SCAL;
    }

    public void setSCAL(String SCAL) {
        this.SCAL = SCAL;
    }
}
