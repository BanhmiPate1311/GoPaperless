package vn.mobileid.paperless.Model.Request;

import vn.mobileid.paperless.API.Types.IdentificationType;

public class Identification {

    public IdentificationType type;
    public String value;

    public IdentificationType getType() {
        return type;
    }

    public void setType(IdentificationType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Identification(IdentificationType type, String value) {
        this.type = type;
        this.value = value;
    }

    public Identification() {
    }
}
