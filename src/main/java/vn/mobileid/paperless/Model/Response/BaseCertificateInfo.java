package vn.mobileid.paperless.Model.Response;

public class BaseCertificateInfo {
    
        public String status;
        public String statusDesc ;
        public String[] certificates ;
        public String csr ;
        public String credentialID ;
        public String issuerDN ;
        public String serialNumber ;
        public String thumbprint ;
        public String subjectDN ;
        public String validFrom ;
        public String validTo ;
        public String purpose ;
        public int version ;
        public String multisign ;
        public int numSignatures ;
        public int remainingSigningCounter ;
        public String authorizationEmail ;
        public String authorizationPhone ;
        public CertificateProfile certificateProfile ;
        public CertificateAuthority certificateAuthority ;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String[] getCertificates() {
        return certificates;
    }

    public void setCertificates(String[] certificates) {
        this.certificates = certificates;
    }

    public String getCsr() {
        return csr;
    }

    public void setCsr(String csr) {
        this.csr = csr;
    }

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }

    public String getIssuerDN() {
        return issuerDN;
    }

    public void setIssuerDN(String issuerDN) {
        this.issuerDN = issuerDN;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getThumbprint() {
        return thumbprint;
    }

    public void setThumbprint(String thumbprint) {
        this.thumbprint = thumbprint;
    }

    public String getSubjectDN() {
        return subjectDN;
    }

    public void setSubjectDN(String subjectDN) {
        this.subjectDN = subjectDN;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMultisign() {
        return multisign;
    }

    public void setMultisign(String multisign) {
        this.multisign = multisign;
    }

    public int getNumSignatures() {
        return numSignatures;
    }

    public void setNumSignatures(int numSignatures) {
        this.numSignatures = numSignatures;
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

    public CertificateProfile getCertificateProfile() {
        return certificateProfile;
    }

    public void setCertificateProfile(CertificateProfile certificateProfile) {
        this.certificateProfile = certificateProfile;
    }

    public CertificateAuthority getCertificateAuthority() {
        return certificateAuthority;
    }

    public void setCertificateAuthority(CertificateAuthority certificateAuthority) {
        this.certificateAuthority = certificateAuthority;
    }
}
