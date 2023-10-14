package vn.mobileid.paperless.fps;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Signature extends BasicFieldAttribute {
    private List<String> levelOfAssurance;
    //Temporal
    private String fieldName;
    //Official
    private String signatureValue;
    private String signingLocation;
    private String signingReason;
    private boolean imageEnabled;
    private List<String> certificateChain;
    private String signerName;
    private byte[] signerPhoto;
    private String signatureStatus;
    private String signingTime;
    private boolean ltv;
    private boolean qualified;
    private boolean certified;
    private String certifiedPermission;
    private String timestampAt;
    private String timestampAuthority;
    private String subjectDn;
    private String issuerDn;
    private String certValidFrom;
    private String certValidTo;
    private String signatureType;
    private String signatureApplication;
    private String signatureAlgorithm;
    private String signedHash; //tên hash
    private String hashData; //dữ liệu hash
}
