package vn.mobileid.paperless.Model.Response;

import vn.mobileid.paperless.Model.Enum.AuthMode;

public class CertificateInfo extends BaseCertificateInfo {

    public String sharedMode;
    public String createdRP;
    public String[] authModes;
    public AuthMode authMode;
    //public String authMode ;
    public int SCAL;
    public String contractExpirationDate;
    public Boolean defaultPassphraseEnabled;
    public Boolean trialEnabled;

    //public int multisign { get; set; }
    //public int remainingSigningCounter { get; set; }
    //public string authorizationEmail { get; set; }
    //public string authorizationPhone { get; set; }
    public String getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(String sharedMode) {
        this.sharedMode = sharedMode;
    }

    public String getCreatedRP() {
        return createdRP;
    }

    public void setCreatedRP(String createdRP) {
        this.createdRP = createdRP;
    }

    public String[] getAuthModes() {
        return authModes;
    }

    public void setAuthModes(String[] authModes) {
        this.authModes = authModes;
    }

    public AuthMode getAuthMode() {
        return authMode;
    }

    public void setAuthMode(AuthMode authMode) {
        this.authMode = authMode;
    }

    public int getSCAL() {
        return SCAL;
    }

    public void setSCAL(int SCAL) {
        this.SCAL = SCAL;
    }

    public String getContractExpirationDate() {
        return contractExpirationDate;
    }

    public void setContractExpirationDate(String contractExpirationDate) {
        this.contractExpirationDate = contractExpirationDate;
    }

    public Boolean getDefaultPassphraseEnabled() {
        return defaultPassphraseEnabled;
    }

    public void setDefaultPassphraseEnabled(Boolean defaultPassphraseEnabled) {
        this.defaultPassphraseEnabled = defaultPassphraseEnabled;
    }

    public Boolean getTrialEnabled() {
        return trialEnabled;
    }

    public void setTrialEnabled(Boolean trialEnabled) {
        this.trialEnabled = trialEnabled;
    }
}
