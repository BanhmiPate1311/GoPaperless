package vn.mobileid.paperless.Model.Request;

public class OwnerCreateRequest extends Request {

    public String username;
    public String password;
    public String fullname;
    public String email;
    public String phone;
    public String identificationType;
    public String identification;
    public String twoFactorMethod;
    public Boolean registerTSEEnabled;
    public String loa;
    public String kycEvidence;
    public Boolean registerTrialCert;
    public CertificateDetails certDetails;
    public String address;
    public String stateOrProvince;
    public String country;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getTwoFactorMethod() {
        return twoFactorMethod;
    }

    public void setTwoFactorMethod(String twoFactorMethod) {
        this.twoFactorMethod = twoFactorMethod;
    }

    public Boolean getRegisterTSEEnabled() {
        return registerTSEEnabled;
    }

    public void setRegisterTSEEnabled(Boolean registerTSEEnabled) {
        this.registerTSEEnabled = registerTSEEnabled;
    }

    public String getLoa() {
        return loa;
    }

    public void setLoa(String loa) {
        this.loa = loa;
    }

    public String getKycEvidence() {
        return kycEvidence;
    }

    public void setKycEvidence(String kycEvidence) {
        this.kycEvidence = kycEvidence;
    }

    public Boolean getRegisterTrialCert() {
        return registerTrialCert;
    }

    public void setRegisterTrialCert(Boolean registerTrialCert) {
        this.registerTrialCert = registerTrialCert;
    }

    public CertificateDetails getCertDetails() {
        return certDetails;
    }

    public void setCertDetails(CertificateDetails certDetails) {
        this.certDetails = certDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}