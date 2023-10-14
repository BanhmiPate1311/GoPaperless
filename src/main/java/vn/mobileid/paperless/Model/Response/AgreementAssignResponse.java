package vn.mobileid.paperless.Model.Response;

public class AgreementAssignResponse extends Response {

    public String claims;
    public String agreementUUID;
    public int remainingCounter;
    public int tempLockoutDuration;

    public String getClaims() {
        return claims;
    }

    public void setClaims(String claims) {
        this.claims = claims;
    }

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    public int getRemainingCounter() {
        return remainingCounter;
    }

    public void setRemainingCounter(int remainingCounter) {
        this.remainingCounter = remainingCounter;
    }

    public int getTempLockoutDuration() {
        return tempLockoutDuration;
    }

    public void setTempLockoutDuration(int tempLockoutDuration) {
        this.tempLockoutDuration = tempLockoutDuration;
    }
}
