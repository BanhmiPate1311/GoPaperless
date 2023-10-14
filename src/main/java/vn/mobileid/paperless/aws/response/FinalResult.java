package vn.mobileid.paperless.aws.response;

public class FinalResult {
    private String email;
    private String mobile;
    private ClaimSource claim_sources;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ClaimSource getClaim_sources() {
        return claim_sources;
    }

    public void setClaim_sources(ClaimSource claim_sources) {
        this.claim_sources = claim_sources;
    }
}
