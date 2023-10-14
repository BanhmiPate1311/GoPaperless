package vn.mobileid.paperless.Model.Response;

import java.util.List;

public class CredentialListResponse extends Response{
      public List<BaseCertificateInfo> certs ;

    public List<BaseCertificateInfo> getCerts() {
        return certs;
    }

    public void setCerts(List<BaseCertificateInfo> certs) {
        this.certs = certs;
    }
}
