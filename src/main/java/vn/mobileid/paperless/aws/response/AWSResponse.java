package vn.mobileid.paperless.aws.response;

public class AWSResponse {
    private int status;
    private String message;
    private String dtis_id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDtis_id() {
        return dtis_id;
    }

    public void setDtis_id(String dtis_id) {
        this.dtis_id = dtis_id;
    }
}
