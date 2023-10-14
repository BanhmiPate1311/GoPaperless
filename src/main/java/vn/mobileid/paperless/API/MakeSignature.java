package vn.mobileid.paperless.API;

public class MakeSignature {
    
    String data;
    String key;
    String passKey;
    
    public MakeSignature(String data, String key, String passKey){
        this.data = data;
        this.key = key;
        this.passKey = passKey;
    }
    
}
