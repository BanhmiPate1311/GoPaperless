package vn.mobileid.paperless.Model.smartId.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.mobileid.paperless.fps.request.HashFileRequest;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignRequest extends SigningBaseRequest {
    private String requestID;
    private int lastFileId;
    private String fileName;
    private String signingOption;
    private String credentialID;
    private String signerId;
    private String certChain;
    private String prefixCode;
    private String relyingParty;
    private boolean codeEnable;
    private String fieldName;
    private String type;
    private int documentId;
}
