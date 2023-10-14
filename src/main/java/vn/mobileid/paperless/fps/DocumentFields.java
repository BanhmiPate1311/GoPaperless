package vn.mobileid.paperless.fps;

import java.util.List;


public class DocumentFields {
    private List<TextField> TEXTBOX;
    private List<Signature> SIGNATURE;

    public List<TextField> getTEXTBOX() {
        return TEXTBOX;
    }

    public void setTEXTBOX(List<TextField> TEXTBOX) {
        this.TEXTBOX = TEXTBOX;
    }

    public List<Signature> getSIGNATURE() {
        return SIGNATURE;
    }

    public void setSIGNATURE(List<Signature> SIGNATURE) {
        this.SIGNATURE = SIGNATURE;
    }
}
