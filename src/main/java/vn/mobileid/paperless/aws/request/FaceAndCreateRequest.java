package vn.mobileid.paperless.aws.request;

public class FaceAndCreateRequest extends ElectronicBaseRequest {
    private String code;
    private String type;
    private String facialImage;
    private String imageFace;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFacialImage() {
        return facialImage;
    }

    public void setFacialImage(String facialImage) {
        this.facialImage = facialImage;
    }

    public String getImageFace() {
        return imageFace;
    }

    public void setImageFace(String imageFace) {
        this.imageFace = imageFace;
    }
}
