package vn.mobileid.paperless.Model.Request;

import vn.mobileid.paperless.Model.Enum.OperationMode;

public class CredentialIssueRequest extends Request {

    public String user;
    public String userType;
    public String agreementUUID;
    public String authorizeCode;
    public String certificateProfile;
    public String signingProfile;
    public int signingProfileValue;
    public String sharedMode;
    public int SCAL;
    public String authMode;
    public int multisign;
    public String email;
    public String phone;
    public CertificateDetails certDetails;
    public String notBefore;
    public String notAfter;
    public OperationMode operationMode;

    public String responseURI;
    //public int validityPeriod { get; set; }
    public String certificates;
    public int hsmProfileID;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    public String getAuthorizeCode() {
        return authorizeCode;
    }

    public void setAuthorizeCode(String authorizeCode) {
        this.authorizeCode = authorizeCode;
    }

    public String getCertificateProfile() {
        return certificateProfile;
    }

    public void setCertificateProfile(String certificateProfile) {
        this.certificateProfile = certificateProfile;
    }

    public String getSigningProfile() {
        return signingProfile;
    }

    public void setSigningProfile(String signingProfile) {
        this.signingProfile = signingProfile;
    }

    public int getSigningProfileValue() {
        return signingProfileValue;
    }

    public void setSigningProfileValue(int signingProfileValue) {
        this.signingProfileValue = signingProfileValue;
    }

    public String getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(String sharedMode) {
        this.sharedMode = sharedMode;
    }

    public int getSCAL() {
        return SCAL;
    }

    public void setSCAL(int SCAL) {
        this.SCAL = SCAL;
    }

    public String getAuthMode() {
        return authMode;
    }

    public void setAuthMode(String authMode) {
        this.authMode = authMode;
    }

    public int getMultisign() {
        return multisign;
    }

    public void setMultisign(int multisign) {
        this.multisign = multisign;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CertificateDetails getCertDetails() {
        return certDetails;
    }

    public void setCertDetails(CertificateDetails certDetails) {
        this.certDetails = certDetails;
    }

    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public String getResponseURI() {
        return responseURI;
    }

    public void setResponseURI(String responseURI) {
        this.responseURI = responseURI;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public int getHsmProfileID() {
        return hsmProfileID;
    }

    public void setHsmProfileID(int hsmProfileID) {
        this.hsmProfileID = hsmProfileID;
    }
}
