package vn.mobileid.paperless.aws.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DetailsDto {
    private String validationReportID;
    private String validatedDocumentHash;
    private String diagnosticData;
    private String detailedValidationReport;

    public String getValidationReportID() {
        return validationReportID;
    }

    public void setValidationReportID(String validationReportID) {
        this.validationReportID = validationReportID;
    }

    public String getValidatedDocumentHash() {
        return validatedDocumentHash;
    }

    public void setValidatedDocumentHash(String validatedDocumentHash) {
        this.validatedDocumentHash = validatedDocumentHash;
    }

    public String getDiagnosticData() {
        return diagnosticData;
    }

    public void setDiagnosticData(String diagnosticData) {
        this.diagnosticData = diagnosticData;
    }

    public String getDetailedValidationReport() {
        return detailedValidationReport;
    }

    public void setDetailedValidationReport(String detailedValidationReport) {
        this.detailedValidationReport = detailedValidationReport;
    }
}
