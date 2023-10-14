package vn.mobileid.paperless.API;

import vn.mobileid.paperless.Model.Enum.MobileDisplayTemplate;
import vn.mobileid.paperless.Model.Enum.SignAlgo;
import vn.mobileid.paperless.Model.Request.DocumentDigests;
import vn.mobileid.paperless.Model.Request.SearchConditions;
import vn.mobileid.paperless.aws.datatypes.JwtModel;
import vn.mobileid.paperless.object.ConnectorLogRequest;

import java.util.List;

public interface IServerSession extends ISession {

    void login(ConnectorLogRequest connectorLogRequest) throws Exception;

    List<ICertificate> listCertificates() throws Exception;

    List<ICertificate> listCertificates(String agreementUUID) throws Exception;

    List<ICertificate> listCertificates(String agreementUUID, ConnectorLogRequest connectorLogRequest, String lang) throws Exception;

    List<ICertificate> listCertificates(String agreementUUID, String certificate, boolean certInfoEnabled, boolean authInfoEnabled, SearchConditions conditions) throws Exception;

    List<ICertificate> listCertificates(String agreementUUID, ConnectorLogRequest connectorLogRequest, String lang, String certificate, boolean certInfoEnabled,
                                        boolean authInfoEnabled, SearchConditions conditions) throws Exception;

    ICertificate certificateInfo(String credentialID, ConnectorLogRequest connectorLogRequest, String lang) throws Exception;

    ICertificate certificateInfo(String credentialID) throws Exception;

    ICertificate certificateInfo(String agreementUUID, String credentialID) throws Exception;

    ICertificate certificateInfo(String agreementUUID, String credentialID, String certificate, boolean certInfoEnabled, boolean authInfoEnabled) throws Exception;

    ICertificate certificateInfo(String agreementUUID, ConnectorLogRequest connectorLogRequest, String lang, String credentialID, String certificate,
                                 boolean certInfoEnabled, boolean authInfoEnabled) throws Exception;

    //ask rssp send otp to email/sms of certificate
    String sendOTP(ConnectorLogRequest connectorLogRequest, String lang, String agreementUUID, String credentialID, String notificationTemplate, String notificationSubject) throws Throwable;

    // agreementUUID là số CCCD
    //authorize
    //if certififate has auth_mode
    //          - PIN then authorizeCode is pin-code
    //          - OTP then authorizeCode is otp
    //          - TSE then authorizeCode is null
    //validIn in seconds
    String authorize(String agreementUUID, String credentialID, int numSignatures, DocumentDigests doc, SignAlgo signAlgo,
                     String authorizeCode) throws Throwable;

    String authorize(ConnectorLogRequest connectorLogRequest, String lang, String agreementUUID, String credentialID, int numSignatures, DocumentDigests doc, SignAlgo signAlgo,
                     String otpRequestID, String otp) throws Throwable;

    String authorize(String agreementUUID, String credentialID, int numSignatures, DocumentDigests doc, SignAlgo signAlgo,
                     MobileDisplayTemplate displayTemplate) throws Throwable;

    String authorize(ConnectorLogRequest connectorLogRequest, String lang, String agreementUUID, String credentialID, int numSignatures, DocumentDigests doc,
                     SignAlgo signAlgo, MobileDisplayTemplate displayTemplate) throws Exception;

    List<byte[]> signHash(String agreementUUID, String credentialID, DocumentDigests documentDigest, SignAlgo signAlgo, String SAD) throws Exception;

    List<byte[]> signHash(ConnectorLogRequest connectorLogRequest, String lang, String agreementUUID, String credentialID, DocumentDigests documentDigest,
                          SignAlgo signAlgo, String SAD) throws Exception;

    String ownerCreate(ConnectorLogRequest connectorLogRequest, JwtModel jwt, String lang) throws Exception;

    String credentialsIssue(ConnectorLogRequest connectorLogRequest, JwtModel jwt, String lang) throws Exception;
}
