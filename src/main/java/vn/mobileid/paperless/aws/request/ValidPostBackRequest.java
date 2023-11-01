package vn.mobileid.paperless.aws.request;

public class ValidPostBackRequest {
    private String postBackUrl;
    private String status;
    private String uploadToken;
    private int fileValidationId;

    public String getPostBackUrl() {
        return postBackUrl;
    }

    public void setPostBackUrl(String postBackUrl) {
        this.postBackUrl = postBackUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;
    }

    public int getFileValidationId() {
        return fileValidationId;
    }

    public void setFileValidationId(int fileValidationId) {
        this.fileValidationId = fileValidationId;
    }
}
