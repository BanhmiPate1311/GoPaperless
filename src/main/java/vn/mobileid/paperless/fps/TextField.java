package vn.mobileid.paperless.fps;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TextField extends BasicFieldAttribute {
    private String value;
    private boolean readOnly;
    private boolean multiline;
    private int maxLength;
    private String format; //For date
    private String color;
    private String align;
    private String formatType;
}
