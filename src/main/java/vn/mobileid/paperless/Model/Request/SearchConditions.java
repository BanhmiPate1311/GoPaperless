package vn.mobileid.paperless.Model.Request;

public class SearchConditions {

    public String certificateStatus;
    public String cetificatePurpose;
    public String taxID;
    public String budgetID;
    public String personalID;
    public String passportID;
    public Identification identification;
    public String[] agreementStates;
    public String fromDate;
    public String toDate;

    public String getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(String certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    public String getCetificatePurpose() {
        return cetificatePurpose;
    }

    public void setCetificatePurpose(String cetificatePurpose) {
        this.cetificatePurpose = cetificatePurpose;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public String getBudgetID() {
        return budgetID;
    }

    public void setBudgetID(String budgetID) {
        this.budgetID = budgetID;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Identification getIdentification() {
        return identification;
    }

    public void setIdentification(Identification identification) {
        this.identification = identification;
    }

    public String[] getAgreementStates() {
        return agreementStates;
    }

    public void setAgreementStates(String[] agreementStates) {
        this.agreementStates = agreementStates;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}
