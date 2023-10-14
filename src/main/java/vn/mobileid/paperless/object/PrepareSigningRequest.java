package vn.mobileid.paperless.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class PrepareSigningRequest {
    private String signing_token;
    private String signer_token;
    private String signing_option;
    private String signer_id;
    private String certificate; // base64(certificate)
    private String connector_name;
    private PdfDto pdf;

    public PrepareSigningRequest(String signing_token, String signer_token, String signing_option, String signer_id, String certificate, String connector_name, PdfDto pdf) {
        this.signing_token = signing_token;
        this.signer_token = signer_token;
        this.signing_option = signing_option;
        this.signer_id = signer_id;
        this.certificate = certificate;
        this.connector_name = connector_name;
        this.pdf = pdf;
    }

    public String getSigning_token() {
        return signing_token;
    }

    public void setSigning_token(String signing_token) {
        this.signing_token = signing_token;
    }

    public String getSigner_token() {
        return signer_token;
    }

    public void setSigner_token(String signer_token) {
        this.signer_token = signer_token;
    }

    public String getSigning_option() {
        return signing_option;
    }

    public void setSigning_option(String signing_option) {
        this.signing_option = signing_option;
    }

    public String getSigner_id() {
        return signer_id;
    }

    public void setSigner_id(String signer_id) {
        this.signer_id = signer_id;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getConnector_name() {
        return connector_name;
    }

    public void setConnector_name(String connector_name) {
        this.connector_name = connector_name;
    }

    public PdfDto getPdf() {
        return pdf;
    }

    public void setPdf(PdfDto pdf) {
        this.pdf = pdf;
    }
}
