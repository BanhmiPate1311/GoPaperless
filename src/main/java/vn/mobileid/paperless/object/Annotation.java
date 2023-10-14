package vn.mobileid.paperless.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Annotation implements Serializable {
    private String page = "1"; // Page index for visible signature. Default: 1
    private String top = "1"; // 	Visible signature position from top. Default: 1
    private String left = "1"; // Visible signature position from left. Default: 1
    private String width = "100"; // Visible signature width. Default: 100
    private String height = "40"; // 	Visible signature height. Default: 40
    private String text; // Custom text for visible signature. Example: Signature: Jonas Jonaitis
    private String type; // Custom annotation type. Available types: text, image. Default: image
    private Boolean add_personal_code; // Render personal code in annotation. Default: false
}
