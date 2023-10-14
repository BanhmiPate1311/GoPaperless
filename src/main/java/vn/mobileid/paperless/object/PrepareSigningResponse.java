package vn.mobileid.paperless.object;

public class PrepareSigningResponse {
    private String status;
    private String dtbs;
    private String dtbs_hash;
    private String algorithm;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDtbs() {
        return dtbs;
    }

    public void setDtbs(String dtbs) {
        this.dtbs = dtbs;
    }

    public String getDtbs_hash() {
        return dtbs_hash;
    }

    public void setDtbs_hash(String dtbs_hash) {
        this.dtbs_hash = dtbs_hash;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
