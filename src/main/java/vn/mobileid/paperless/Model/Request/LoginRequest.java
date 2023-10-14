package vn.mobileid.paperless.Model.Request;

public class LoginRequest extends Request {

    public String relyingParty;
    public Boolean rememberMeEnabled;

    public Boolean getRememberMeEnabled() {
        return rememberMeEnabled;
    }

    public void setRememberMeEnabled(Boolean rememberMeEnabled) {
        this.rememberMeEnabled = rememberMeEnabled;
    }

    public String getRelyingParty() {
        return relyingParty;
    }

    public void setRelyingParty(String relyingParty) {
        this.relyingParty = relyingParty;
    }
}
