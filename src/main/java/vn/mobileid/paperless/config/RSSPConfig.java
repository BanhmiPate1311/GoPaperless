/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.config;

import org.springframework.stereotype.Component;
import vn.mobileid.paperless.API.Property;

import java.io.IOException;

/**
 *
 * @author Mr Spider
 */
@Component
public class RSSPConfig {
    public Property loadRSSPConfig(String baseUrl, String relyingParty, String relyingPartyUser,
            String relyingPartyPassword, String relyingPartySignature, String relyingPartyKeyStoreValue,
            String relyingPartyKeyStorePassword) throws IOException {
        // ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // InputStream stream = loader.getResourceAsStream("file/rssp.ssl2");
        // if (stream == null) {
        // System.out.println("Can read config-file: ");
        // }
        // try (final InputStreamReader in = new InputStreamReader(stream,
        // StandardCharsets.UTF_8)) {
        // prop.load(in);
        // }
        // if (prop.keySet() == null) {
        // System.out.println("Not found keys ");
        // }

        // String baseUrl = "";// prop.getProperty("mobileid.rssp.baseurl");
        // String relyingParty = "";//prop.getProperty("mobileid.rssp.rp.name");
        // String relyingPartyUser = "";//prop.getProperty("mobileid.rssp.rp.user");
        // String relyingPartyPassword =
        // "";//prop.getProperty("mobileid.rssp.rp.password");
        // String relyingPartySignature =
        // "";//prop.getProperty("mobileid.rssp.rp.signature");
        //// String relyingPartyKeyStore =
        // prop.getProperty("mobileid.rssp.rp.keystore.file");
        // String relyingPartyKeyStoreValue = "";
        // String relyingPartyKeyStorePassword =
        // "";//prop.getProperty("mobileid.rssp.rp.keystore.password");

        // ObjectMapper objectMapper = new ObjectMapper();
        // COMNECTOR_ATTRIBUTE proParse = objectMapper.readValue(sIdentierWS,
        // COMNECTOR_ATTRIBUTE.class);
        // for (COMNECTOR_ATTRIBUTE.Attribute attribute : proParse.getAttributes()) {
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_RSSP_MOBILE_ID_URI))
        // {
        // baseUrl = attribute.getValue();
        // }
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_RSSP_MOBILE_ID_NAME))
        // {
        // relyingParty = attribute.getValue();
        // }
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_RSSP_MOBILE_ID_USERNAME))
        // {
        // relyingPartyUser = attribute.getValue();
        // }
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_RSSP_MOBILE_ID_PASSWORD))
        // {
        // relyingPartyPassword = attribute.getValue();
        // }
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_RSSP_MOBILE_ID_SIGNATURE))
        // {
        // relyingPartySignature = attribute.getValue();
        // }
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_RSSP_MOBILE_ID_KEYSTORE_FILE_URL))
        // {
        // relyingPartyKeyStoreValue = attribute.getValue();
        // }
        // if
        // (attribute.getName().equals(Difinitions.CONFIG_CONECTOR_DMS_MOBILE_ID_KEYSTORE_PASSWORD))
        // {
        // relyingPartyKeyStorePassword = attribute.getValue();
        // }
        // }
        // String relyingPartyKeyStore =
        // getClass().getClassLoader().getResource(relyingPartyKeyStoreValue).getFile();

        return new Property(baseUrl,
                relyingParty,
                relyingPartyUser,
                relyingPartyPassword,
                relyingPartySignature,
                relyingPartyKeyStoreValue,
                relyingPartyKeyStorePassword);
    }
}
