package vn.mobileid.paperless.fps;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BasicFieldAttribute {
    private String fieldName;
    private int page;
    private String type;
    private String embedded;
    private String processStatus;
    private String processOn;
    private String processBy;
    private Dimension dimension;
    private Boolean visibleEnabled;
    private Boolean required;
    private String renamedAs;
}
