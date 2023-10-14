package vn.mobileid.paperless.SignFile;

import vn.mobileid.exsig.PdfProfile;
import vn.mobileid.exsig.PdfProfileCMS;
import vn.mobileid.paperless.API.IServerSession;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.SignAlgo;

import java.util.List;

public class PdfSignFile extends RSSP_RestfulSign implements IPdfSignFile {

    private final HashAlgorithmOID hashAlgo;

    public PdfSignFile(PdfProfile profile, HashAlgorithmOID alg) {
        super(profile, alg);
        hashAlgo = alg;
    }

    public PdfProfile getProfile() {
        return (PdfProfile) super._profile;
    }

    public PdfProfile getPdfProfile() {
        return this.getProfile();
    }

    public byte[] createBlankSignature(String agreementUUID, String credentialID, IServerSession session, List<byte[]> files) throws Exception {
        return getProfile().createTemporalFile(new RestfulSigningMethod(agreementUUID, credentialID, "", session, SignAlgo.RSA, hashAlgo), files);
    }

    public byte[] createBlankSignature(List<byte[]> files) throws Exception {
        return ((PdfProfileCMS) getProfile()).createTemporalFile(new RestfulSigningMethod(), files);
    }

    public byte[] addSignature(String agreementUUID, String credentialId, String pin, byte[] blankSignature, IServerSession session) throws Exception {
        RestfulSigningMethod rest = new RestfulSigningMethod(agreementUUID, credentialId, pin, session, SignAlgo.RSA, hashAlgo);
        return PdfProfileCMS.sign(rest, blankSignature).get(0);
    }
}
