package vn.mobileid.paperless.Model.Request;

public class AgreementCreateRequest extends Request {

    public String agreementUUID;
    
    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }
}
