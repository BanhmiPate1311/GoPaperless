package vn.mobileid.paperless.SignFile;

import vn.mobileid.exsig.Profile;
import vn.mobileid.paperless.API.IServerSession;

import java.util.List;

public interface ISignFile {

    Profile getProfile();

    List<byte[]> sign(String agreementUUID, String credentialId, String pin, List<byte[]> files, IServerSession session) throws Exception;
}
