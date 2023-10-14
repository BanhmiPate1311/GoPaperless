package vn.mobileid.paperless.aws.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SignatureDto {
    public String indication;
    public String subIndication;
    public List<String> errors;
    public String signingReason;
    public String signingTime;
    public String qualifiedTimestamp;
    public String signatureFormat;
    public String signatureScope;
    public String certificateOwner;
    public String certificateIssuer;
    public String certificateValidityPeriod;
    public String certificateType;

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getSubIndication() {
        return subIndication;
    }

    public void setSubIndication(String subIndication) {
        this.subIndication = subIndication;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getSigningReason() {
        return signingReason;
    }

    public void setSigningReason(String signingReason) {
        this.signingReason = signingReason;
    }

    public String getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(String signingTime) {
        this.signingTime = signingTime;
    }

    public String getQualifiedTimestamp() {
        return qualifiedTimestamp;
    }

    public void setQualifiedTimestamp(String qualifiedTimestamp) {
        this.qualifiedTimestamp = qualifiedTimestamp;
    }

    public String getSignatureFormat() {
        return signatureFormat;
    }

    public void setSignatureFormat(String signatureFormat) {
        this.signatureFormat = signatureFormat;
    }

    public String getSignatureScope() {
        return signatureScope;
    }

    public void setSignatureScope(String signatureScope) {
        this.signatureScope = signatureScope;
    }

    public String getCertificateOwner() {
        return certificateOwner;
    }

    public void setCertificateOwner(String certificateOwner) {
        this.certificateOwner = certificateOwner;
    }

    public String getCertificateIssuer() {
        return certificateIssuer;
    }

    public void setCertificateIssuer(String certificateIssuer) {
        this.certificateIssuer = certificateIssuer;
    }

    public String getCertificateValidityPeriod() {
        return certificateValidityPeriod;
    }

    public void setCertificateValidityPeriod(String certificateValidityPeriod) {
        this.certificateValidityPeriod = certificateValidityPeriod;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }
}
