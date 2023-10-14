/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.Model;

/**
 *
 * @author Admin
 */
public class APIException extends Exception {

    private final int error;

    private String agreementUUID;

    public APIException(int code, String msg) {
        super(msg);
        this.error = code;
    }

    public APIException(String msg) {
        super(msg);
        this.error = -1;
    }

    public APIException(int code, String msg, String agreement) {
        super(msg + ". Agreement is [" + agreement + "]");
        this.error = code;
        this.agreementUUID = agreement;
    }

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public int getError() {
        return error;
    }

}
