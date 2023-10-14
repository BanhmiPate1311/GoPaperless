package vn.mobileid.paperless.SignFile;

import vn.mobileid.exsig.Algorithm;
import vn.mobileid.exsig.PdfForm;
import vn.mobileid.exsig.PdfProfile;
import vn.mobileid.exsig.PdfProfileCMS;
import vn.mobileid.paperless.Model.APIException;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;

public class SignFileFactory {

    public enum SignType {
        CMS,
        PAdES
    }

    public IPdfSignFile createPdfSignFile(SignType signType, Algorithm alg, PdfForm form) throws APIException {
        PdfProfile profile;
        switch (signType) {
            case CMS:
                profile = new PdfProfileCMS(alg);
                break;
            case PAdES:
                profile = new PdfProfile(form, alg);
                break;
            default:
                throw new APIException("Not support SignType [" + signType + "]");
        }
        return new PdfSignFile(profile, getHashAlgorithmOID(alg));
    }

    private HashAlgorithmOID getHashAlgorithmOID(Algorithm alg) throws APIException {
        switch (alg) {
            case SHA1:
                return HashAlgorithmOID.SHA_1;
            case SHA256:
                return HashAlgorithmOID.SHA_256;
            default:
                throw new APIException("Not support HashAlgorithmOID [" + alg + "]");
        }
    }
}
