package vn.mobileid.paperless.repository;

import org.springframework.stereotype.Repository;
import ua_parser.Client;
import ua_parser.Parser;
import vn.mobileid.paperless.API.IServerSession;
import vn.mobileid.paperless.API.Property;
import vn.mobileid.paperless.API.Utils;
import vn.mobileid.paperless.Model.Enum.HashAlgorithmOID;
import vn.mobileid.paperless.Model.Enum.MobileDisplayTemplate;
import vn.mobileid.paperless.Model.Request.DocumentDigests;
import vn.mobileid.paperless.Model.SessionFactory;
import vn.mobileid.paperless.object.ConnectorLogRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RsspRepository {

    public IServerSession Handshake_func(Property property, String lang, ConnectorLogRequest connectorLogRequest) throws Exception {

        SessionFactory factory = new SessionFactory(property, lang);
        return factory.getServerSession(connectorLogRequest);

    }

    public DocumentDigests getDoc(HttpServletRequest request, String fileName, String codeEnable,List<String> hashList,String relyingParty) throws Exception {
        // get user-agent
        String userAgent = request.getHeader("User-Agent");
        Parser parser = new Parser();
        Client c = parser.parse(userAgent);
        // set app interface
        String rpName = "{\"OPERATING SYSTEM\":\"" + c.os.family + " " + c.os.major + "\",\"BROWSER\":\"" + c.userAgent.family + " " + c.userAgent.major + "\",\"RP NAME\":\"" + relyingParty + "\"}";

        String fileType2 = fileName.substring(fileName.lastIndexOf(".") + 1);
        String message = " {\"FILE NAME\":\"" + fileName + "\", \"FILE TYPE\":\"" + fileType2 + "\"}";

        MobileDisplayTemplate template = new MobileDisplayTemplate();
        template.setScaIdentity("PAPERLESS GATEWAY");
        template.setMessageCaption("DOCUMENT SIGNING");
        template.setNotificationMessage("PAPERLESS GATEWAY ACTIVITES");
        template.setMessage(message);
        template.setRpName(rpName);
        template.setVcEnabled(Boolean.parseBoolean(codeEnable));
        template.setAcEnabled(true);

        HashAlgorithmOID hashAlgo = HashAlgorithmOID.SHA_256;
        DocumentDigests doc = new DocumentDigests();
        doc.hashAlgorithmOID = hashAlgo;
        doc.hashes = new ArrayList<>();
        doc.hashes.add(Utils.base64Decode(hashList.get(0)));

        return doc;
    }

}
