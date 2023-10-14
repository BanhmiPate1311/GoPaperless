package vn.mobileid.paperless.SignFile;

import vn.mobileid.exsig.SigningMethodAsync;
import vn.mobileid.exsig.SigningMethodSync;
import vn.mobileid.paperless.API.ICertificate;
import vn.mobileid.paperless.API.IServerSession;
import vn.mobileid.paperless.API.Utils;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.Model.Request.DocumentDigests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestfulSigningMethod implements SigningMethodSync, SigningMethodAsync {

    private String _agreementUUID;
    private String _credentialID;
    private String _pin;
    private List<String> _chain;
    private IServerSession _session;
    //private readonly List<byte[]> _files;
    private SignAlgo _signAlgo;
    private HashAlgorithmOID _hashAlgo;
    private List<String> hashList;
    private List<String> signatures;

    public RestfulSigningMethod(String _agreementUUID, String _credentialID, String _pin, IServerSession _session, SignAlgo _signAlgo, HashAlgorithmOID _hashAlgo) {
        this._agreementUUID = _agreementUUID;
        this._credentialID = _credentialID;
        this._pin = _pin;
        this._session = _session;
        this._signAlgo = _signAlgo;
        this._hashAlgo = _hashAlgo;
    }

    public RestfulSigningMethod() {
    }

    @Override
    public List<String> getCert() throws Exception {

        ICertificate iCert = null;
        try {
            iCert = _session.certificateInfo(_agreementUUID, _credentialID, "chain", false, false);
        } catch (Throwable ex) {
        }
        _chain = new ArrayList<String>(Arrays.asList(iCert.baseCredentialInfo().getCertificates()));
        return _chain;
    }

    @Override
    public List<String> sign(List<String> hashList) throws Exception {
        DocumentDigests doc = new DocumentDigests();
        doc.setHashAlgorithmOID(_hashAlgo);
        doc.setHashes(new ArrayList<byte[]>());
        for (String el : hashList) {
            try {
                doc.getHashes().add(Utils.base64Decode(el));
            } catch (Throwable ex) {
            }
        }
        String sad = null;
        try {
            sad = _session.authorize(_agreementUUID, _credentialID, hashList.size(), doc, _signAlgo, _pin);
        } catch (Throwable ex) {
            Logger.getLogger(RestfulSigningMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<byte[]> signatures = null;
        try {
            signatures = _session.signHash(_agreementUUID, _credentialID, doc, _signAlgo, sad);
        } catch (Throwable ex) {
            Logger.getLogger(RestfulSigningMethod.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < hashList.size(); i++) {
            byte[] sig = signatures.get(i);
            try {
                ret.add(Utils.base64Encode(sig));
            } catch (Throwable ex) {

            }
        }
        return ret;
    }

    @Override
    public List<String> pack() throws Exception {
        return this.signatures;
    }

    @Override
    public void generateTempFile(List<String> list) throws Exception {
        this.hashList = list;
    }

}
