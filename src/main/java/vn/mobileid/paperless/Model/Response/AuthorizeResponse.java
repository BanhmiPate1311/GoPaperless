package vn.mobileid.paperless.Model.Response;

public class AuthorizeResponse extends Response{
    public String SAD;
    public int expiresIn;
    public int remainingCounter;
    public int tempLockoutDuration;

    public String getSAD() {
        return SAD;
    }

    public void setSAD(String SAD) {
        this.SAD = SAD;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
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
