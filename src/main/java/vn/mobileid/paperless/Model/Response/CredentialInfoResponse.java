package vn.mobileid.paperless.Model.Response;

import vn.mobileid.paperless.Model.Enum.AuthMode;

public class CredentialInfoResponse extends Response {

    public CertificateInfo cert;
    public String sharedMode;
    public String createdRP;
    public String[] authModes;
    public AuthMode authMode; //doi kieu string
    //public String authMode;
    public int SCAL;
    public String contractExpirationDate;
    public Boolean defaultPassphraseEnabled;
    public Boolean trialEnabled;
    public int multisign;
    public int remainingSigningCounter;
    public String authorizationEmail;
    public String authorizationPhone;

    public CertificateInfo getCert() {
        return cert;
    }

    public void setCert(CertificateInfo cert) {
        this.cert = cert;
    }

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

    public int getMultisign() {
        return multisign;
    }

    public void setMultisign(int multisign) {
        this.multisign = multisign;
    }

    public int getRemainingSigningCounter() {
        return remainingSigningCounter;
    }

    public void setRemainingSigningCounter(int remainingSigningCounter) {
        this.remainingSigningCounter = remainingSigningCounter;
    }

    public String getAuthorizationEmail() {
        return authorizationEmail;
    }

    public void setAuthorizationEmail(String authorizationEmail) {
        this.authorizationEmail = authorizationEmail;
    }

    public String getAuthorizationPhone() {
        return authorizationPhone;
    }

    public void setAuthorizationPhone(String authorizationPhone) {
        this.authorizationPhone = authorizationPhone;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getResponseID() {
        return responseID;
    }

    public void setResponseID(String responseID) {
        this.responseID = responseID;
    }

}
