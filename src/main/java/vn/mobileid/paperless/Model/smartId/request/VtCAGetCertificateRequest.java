package vn.mobileid.paperless.Model.smartId.request;

public class VtCAGetCertificateRequest extends SigningBaseRequest{
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
