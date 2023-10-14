package vn.mobileid.paperless.fps;

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
public class DocumentDetails {
    private int documentId;
    private String documentName;
    private String documentType;
    private int documentHeight;
    private int documentWidth;
    private int documentPages;
    private int documentRevision;
    private String uploadedBy;
    private List<DocumentCustomPage> documentCustomPage;
}
