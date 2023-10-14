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
public class DocumentCustomPage {
    private int pageStart;
    private int pageEnd;
    private int pageRotate;
    private int pageHeight;
    private int pageWidth;
}
