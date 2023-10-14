package vn.mobileid.paperless.API;

import vn.mobileid.paperless.Model.Enum.MobileDisplayTemplate;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.Model.Enum.SignedPropertyType;
import vn.mobileid.paperless.Model.Request.DocumentDigests;
import vn.mobileid.paperless.Model.Response.BaseCertificateInfo;
import vn.mobileid.paperless.Model.Response.CertificateInfo;
import vn.mobileid.paperless.object.ConnectorLogRequest;

import java.util.HashMap;
import java.util.List;

public interface ICertificate {

    BaseCertificateInfo baseCredentialInfo() throws Exception;

    //getCredentialInfo();
    CertificateInfo credentialInfo() throws Exception;

    CertificateInfo credentialInfo(String cetificate, boolean certInfoEnabled, boolean authInfoEnabled) throws Exception;

    //ask RSSP send OTP to email or phone of certificate
    String sendOTP(ConnectorLogRequest connectorLogRequest, String lang, String credentialID, String notificationTemplate, String notificationSubject) throws Throwable;

    //authorize
    //if certififate has auth_mode
    //          - PIN then authorizeCode is pin-code
    //          - OTP then authorizeCode is otp
    //          - TSE then authorizeCode is null
    //validIn in seconds
    String authorize(int numSignatures, DocumentDigests doc, SignAlgo signAlgo, String authorizeCode) throws Throwable;

    String authorize(ConnectorLogRequest connectorLogRequest, String lang, String credentailID, int numSignatures, DocumentDigests doc, SignAlgo signAlgo, String otpRequestID, String otp) throws Throwable;

    String authorize(int numSignatures, DocumentDigests doc, SignAlgo signAlgo, MobileDisplayTemplate displayTemplate) throws Throwable;

    String authorize(ConnectorLogRequest connectorLogRequest, int numSignatures, DocumentDigests doc, SignAlgo signAlgo, MobileDisplayTemplate displayTemplate) throws Throwable;

    String authorize(ConnectorLogRequest connectorLogRequest, String lang, String credentialID, int numSignatures, DocumentDigests doc, SignAlgo signAlgo, MobileDisplayTemplate displayTemplate) throws Throwable;

    //if DocumentDigests/SignAlgo is avaiable in authorize then they can missing
    List<byte[]> signHash(String credentialID, DocumentDigests documentDigest, SignAlgo signAlgo, String SAD) throws Exception;

    List<byte[]> signHash(ConnectorLogRequest connectorLogRequest, String lang, String credentialID, DocumentDigests documentDigest, SignAlgo signAlgo, String SAD) throws Exception;

    //sign document, now support sign pdf file
    List<byte[]> signDoc(HashMap<SignedPropertyType, Object> signedProps, List<byte[]> docs, String SAD);
}
