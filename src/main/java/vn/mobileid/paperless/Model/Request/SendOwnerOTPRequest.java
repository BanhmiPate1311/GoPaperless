package vn.mobileid.paperless.Model.Request;

import vn.mobileid.paperless.Model.Enum.OTPType;
import vn.mobileid.paperless.Model.Enum.UserType;

public class SendOwnerOTPRequest extends Request {

    public String agreementUUID;
    public String user;
    public UserType userType;
    public OTPType otpType;

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

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

    public OTPType getOtpType() {
        return otpType;
    }

    public void setOtpType(OTPType otpType) {
        this.otpType = otpType;
    }
}
