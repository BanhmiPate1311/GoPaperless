package vn.mobileid.paperless.fps;

public class AccessTokenDto {
    private String access_token;
    private String token_type;
    private int expires_in;
    private int refresh_expires_in;
    private boolean remember_me_enable;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public int getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public void setRefresh_expires_in(int refresh_expires_in) {
        this.refresh_expires_in = refresh_expires_in;
    }

    public boolean isRemember_me_enable() {
        return remember_me_enable;
    }

    public void setRemember_me_enable(boolean remember_me_enable) {
        this.remember_me_enable = remember_me_enable;
    }
}
