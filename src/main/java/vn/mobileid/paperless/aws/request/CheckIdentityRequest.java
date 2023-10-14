package vn.mobileid.paperless.aws.request;

public class CheckIdentityRequest extends ElectronicBaseRequest {
    private String code;
    private String type;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
