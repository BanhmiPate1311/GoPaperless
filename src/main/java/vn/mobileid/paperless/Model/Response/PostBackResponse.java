/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.Model.Response;

/**
 *
 * @author Mr Spider
 */
public class PostBackResponse extends Response {
    public String status;
    public String token;
    public String action;
    public String file;
    public String signer;
    public SignerInfo signerInfo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public SignerInfo getSignerInfo() {
        return signerInfo;
    }

    public void setSignerInfo(SignerInfo signerInfo) {
        this.signerInfo = signerInfo;
    }
}
