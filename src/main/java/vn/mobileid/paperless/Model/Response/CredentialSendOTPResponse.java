package vn.mobileid.paperless.Model.Response;

public class CredentialSendOTPResponse extends Response{
    private int tempLockoutDuration;

    public int getTempLockoutDuration() {
        return tempLockoutDuration;
    }

    public void setTempLockoutDuration(int tempLockoutDuration) {
        this.tempLockoutDuration = tempLockoutDuration;
    }
}
