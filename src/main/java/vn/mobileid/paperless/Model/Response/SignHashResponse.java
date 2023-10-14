package vn.mobileid.paperless.Model.Response;

import java.util.List;

public class SignHashResponse extends Response {

    public List<byte[]> signatures;
    public int remainingSigningCounter;

    public int remainingCounter;
    public int tempLockoutDuration;

    public List<byte[]> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<byte[]> signatures) {
        this.signatures = signatures;
    }

    public int getRemainingSigningCounter() {
        return remainingSigningCounter;
    }

    public void setRemainingSigningCounter(int remainingSigningCounter) {
        this.remainingSigningCounter = remainingSigningCounter;
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
