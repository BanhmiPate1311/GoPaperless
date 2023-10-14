package vn.mobileid.paperless.Model.Request;

import vn.mobileid.paperless.Model.Enum.OperationMode;
import vn.mobileid.paperless.Model.Enum.SignAlgo;

public class SignHashRequest extends Request {

    public String agreementUUID;
    public String credentialID;
    public String SAD;
    public DocumentDigests documentDigests;
    public String signAlgoParams;
    public OperationMode operationMode;
    public String scaIdentity;
    public String responseURI;
    public int validityPeriod;
    public ClientInfo clientInfo;
    public SignAlgo signAlgo;

    public SignAlgo getSignAlgo() {
        return signAlgo;
    }

    public void setSignAlgo(SignAlgo signAlgo) {
        this.signAlgo = signAlgo;
    }

    public SignHashRequest() {
        this.validityPeriod = 300;
        this.operationMode = OperationMode.S;
    }

    public String getAgreementUUID() {
        return agreementUUID;
    }

    public void setAgreementUUID(String agreementUUID) {
        this.agreementUUID = agreementUUID;
    }

    public String getCredentialID() {
        return credentialID;
    }

    public void setCredentialID(String credentialID) {
        this.credentialID = credentialID;
    }

    public String getSAD() {
        return SAD;
    }

    public void setSAD(String SAD) {
        this.SAD = SAD;
    }

    public DocumentDigests getDocumentDigests() {
        return documentDigests;
    }

    public void setDocumentDigests(DocumentDigests documentDigests) {
        this.documentDigests = documentDigests;
    }

    public String getSignAlgoParams() {
        return signAlgoParams;
    }

    public void setSignAlgoParams(String signAlgoParams) {
        this.signAlgoParams = signAlgoParams;
    }

    public OperationMode getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperationMode operationMode) {
        this.operationMode = operationMode;
    }

    public String getScaIdentity() {
        return scaIdentity;
    }

    public void setScaIdentity(String scaIdentity) {
        this.scaIdentity = scaIdentity;
    }

    public String getResponseURI() {
        return responseURI;
    }

    public void setResponseURI(String responseURI) {
        this.responseURI = responseURI;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
}
