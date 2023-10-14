package vn.mobileid.paperless.Model.Request;

//import RestfulFactory.Model.CertificateDetails;
import vn.mobileid.paperless.API.Types.AuthMode;
import vn.mobileid.paperless.API.Types.SharedMode;
import vn.mobileid.paperless.API.Types.UserType;

public class IssueCertificateRequest extends Request {

    private String user;
    private UserType userType;
    private String agreementUUID;
    private String authorizeCode;
    private String certificateProfile;
    private String signingProfile;
    private String signingProfileValue;

    private SharedMode sharedMode;
    private int SCAL;
    private AuthMode authMode;
    private int multisign;

    private String email;
    private String phone;
    private CertificateDetails certDetails;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
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

    public String getSigningProfileValue() {
        return signingProfileValue;
    }

    public void setSigningProfileValue(String signingProfileValue) {
        this.signingProfileValue = signingProfileValue;
    }

    public SharedMode getSharedMode() {
        return sharedMode;
    }

    public void setSharedMode(SharedMode sharedMode) {
        this.sharedMode = sharedMode;
    }

    public int getSCAL() {
        return SCAL;
    }

    public void setSCAL(int SCAL) {
        this.SCAL = SCAL;
    }

    public AuthMode getAuthMode() {
        return authMode;
    }

    public void setAuthMode(AuthMode authMode) {
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

}
