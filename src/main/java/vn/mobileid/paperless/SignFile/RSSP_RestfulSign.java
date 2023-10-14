package vn.mobileid.paperless.SignFile;

import vn.mobileid.exsig.Profile;
import vn.mobileid.paperless.API.IServerSession;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.SignAlgo;

import java.util.List;

public class RSSP_RestfulSign implements ISignFile {

    protected final Profile _profile;
    private final HashAlgorithmOID hashAlgo;

    public RSSP_RestfulSign(Profile profile, HashAlgorithmOID alg) {
        _profile = profile;
        hashAlgo = alg;
    }

    public Profile getProfile() {
        return _profile;
    }

    public List<byte[]> sign(String agreementUUID, String credentialId, String pin, List<byte[]> files, IServerSession session) throws Exception {
        RestfulSigningMethod signingMethod = new RestfulSigningMethod(agreementUUID, credentialId, pin, session, SignAlgo.RSA, hashAlgo);
        return _profile.sign(signingMethod, files);
    }

}
