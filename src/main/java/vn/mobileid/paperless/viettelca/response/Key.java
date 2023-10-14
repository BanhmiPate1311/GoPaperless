package vn.mobileid.paperless.viettelca.response;

import vn.mobileid.paperless.Model.Enum.SignAlgo;

import java.util.List;

public class Key {
    private String status;
    private List<String> algo;
    private int len;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getAlgo() {
        return algo;
    }

    public void setAlgo(List<String> algo) {
        this.algo = algo;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
