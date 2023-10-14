package vn.mobileid.paperless.viettelca.response;

import vn.mobileid.paperless.Model.Response.Response;

import java.util.List;

public class VTCertResponse extends Response {
    private CertDetail[] data;
    public String error_description;
    public String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }

    public CertDetail[] getData() {
        return data;
    }

    public void setData(CertDetail[] data) {
        this.data = data;
    }
}
