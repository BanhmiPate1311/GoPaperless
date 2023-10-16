package vn.mobileid.paperless.fps.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HashFileRequest {
    private String fieldName;
    private String signatureAlgorithm;
    private String signedHash;
    private String signingReason;
    private String signingLocation;
    private List<String> certificateChain;
}
