package vn.mobileid.paperless.Model.smartId.request;

public class IsRequest extends SigningBaseRequest{
    private String signature;
    private String hashList;
    private String certChain;
    private String fieldName;
    private int documentId;
    private String signerId;
    private String fileName;
    private String signatureId;
    private int lastFileId;
    private String signingOption;
    private String serialNumber;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHashList() {
        return hashList;
    }

    public void setHashList(String hashList) {
        this.hashList = hashList;
    }

    public String getCertChain() {
        return certChain;
    }

    public void setCertChain(String certChain) {
        this.certChain = certChain;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    public int getLastFileId() {
        return lastFileId;
    }

    public void setLastFileId(int lastFileId) {
        this.lastFileId = lastFileId;
    }

    public String getSigningOption() {
        return signingOption;
    }

    public void setSigningOption(String signingOption) {
        this.signingOption = signingOption;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
