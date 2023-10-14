package vn.mobileid.paperless.SignFile;

import vn.mobileid.exsig.PdfProfile;
import vn.mobileid.paperless.API.IServerSession;

import java.util.List;

public interface IPdfSignFile extends ISignFile {

    PdfProfile getProfile();

    @Deprecated
    PdfProfile getPdfProfile();

    byte[] createBlankSignature(String agreementUUID, String credentialID, IServerSession session, List<byte[]> files) throws Exception;

    byte[] createBlankSignature(List<byte[]> files) throws Exception;

    byte[] addSignature(String agreementUUID, String credentialId, String pin, byte[] blankSignature, IServerSession session) throws Exception;

}
