package vn.mobileid.paperless.aws.request;

public class ElectronicBaseRequest {
    private String lang;
    private String connectorName;
    private int enterpriseId;
    private int workFlowId;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getConnectorName() {
        return connectorName;
    }

    public void setConnectorName(String connectorName) {
        this.connectorName = connectorName;
    }

    public int getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(int enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public int getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(int workFlowId) {
        this.workFlowId = workFlowId;
    }
}
