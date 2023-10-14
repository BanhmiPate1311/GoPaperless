package vn.mobileid.paperless.aws.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OverviewDto {
    private String status;
    private int totalSignatures;
    private int totalValidSignatures;
    private int totalSeals;
    private int totalValidSeal;
    private String validationTime;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalSignatures() {
        return totalSignatures;
    }

    public void setTotalSignatures(int totalSignatures) {
        this.totalSignatures = totalSignatures;
    }

    public int getTotalValidSignatures() {
        return totalValidSignatures;
    }

    public void setTotalValidSignatures(int totalValidSignatures) {
        this.totalValidSignatures = totalValidSignatures;
    }

    public int getTotalSeals() {
        return totalSeals;
    }

    public void setTotalSeals(int totalSeals) {
        this.totalSeals = totalSeals;
    }

    public int getTotalValidSeal() {
        return totalValidSeal;
    }

    public void setTotalValidSeal(int totalValidSeal) {
        this.totalValidSeal = totalValidSeal;
    }

    public String getValidationTime() {
        return validationTime;
    }

    public void setValidationTime(String validationTime) {
        this.validationTime = validationTime;
    }
}
