package vn.mobileid.paperless.Model.Request;

import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;

import java.util.List;

public class DocumentDigests {

    public List<byte[]> hashes;
    public HashAlgorithmOID hashAlgorithmOID;

    public List<byte[]> getHashes() {
        return hashes;
    }

    public void setHashes(List<byte[]> hashes) {
        this.hashes = hashes;
    }

    public HashAlgorithmOID getHashAlgorithmOID() {
        return hashAlgorithmOID;
    }

    public void setHashAlgorithmOID(HashAlgorithmOID hashAlgorithmOID) {
        this.hashAlgorithmOID = hashAlgorithmOID;
    }
}
