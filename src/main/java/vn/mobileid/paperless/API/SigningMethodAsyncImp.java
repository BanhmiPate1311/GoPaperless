package vn.mobileid.paperless.API;


import org.apache.commons.io.FileUtils;
import vn.mobileid.exsig.SigningMethodAsync;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class SigningMethodAsyncImp implements SigningMethodAsync {

    public List<String> certificateChain;
    public List<String> signatures;
    public List<String> hashList;

    public List<String> getCertificateChain() {
        return certificateChain;
    }

    public void setCertificateChain(List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public List<String> getHashList() {
        return hashList;
    }

    public void setHashList(List<String> hashList) {
        this.hashList = hashList;
    }

    @Override
    public void generateTempFile(List<String> list) throws Exception {
        this.hashList = list;
    }

    @Override
    public List<String> getCert() throws Exception {
        return this.certificateChain;
    }

    @Override
    public List<String> pack() throws Exception {
        return this.signatures;
    }

    public void saveTemporalData(byte[] temporalData) throws IOException {
        SigningMethodAsyncImp signingMethodAsyncImp = new SigningMethodAsyncImp();
        signingMethodAsyncImp.saveTemporalData(null, temporalData);

    }

    public void saveTemporalData(String owner, byte[] temporalData) throws IOException {
        String result = System.getProperty("java.io.tmpdir");
        String fileName = result + "/" + owner + ".temp";
        FileUtils.writeByteArrayToFile(new File(fileName), temporalData);
    }

    public byte[] loadTemporalData(String owner) throws IOException {
        String result = System.getProperty("java.io.tmpdir");
        String fileName = result + "/" + owner + ".temp";
        return Files.readAllBytes(Paths.get(fileName));
    }
}
