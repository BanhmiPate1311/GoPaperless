package vn.mobileid.paperless.Model.smartId.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigningBaseRequest {
    private String lang;
    private String signingToken;
    private String signerToken;
    private String connectorName;
    private String codeNumber;
    private int enterpriseId;
    private int workFlowId;
}
