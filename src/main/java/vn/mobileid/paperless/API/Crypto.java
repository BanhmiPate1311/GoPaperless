package vn.mobileid.paperless.API;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestAlgorithmIdentifierFinder;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.x509.extension.X509ExtensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.util.DerOutputStream;
import sun.security.x509.AlgorithmId;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 *
 * @author VUDP
 */
public class Crypto {

    private static final Logger LOG = LoggerFactory.getLogger(Crypto.class);

    final public static String HASH_MD5 = "MD5";
    final public static String HASH_SHA1 = "SHA-1";
    final public static String HASH_SHA256 = "SHA-256";
    final public static String HASH_SHA384 = "SHA-384";
    final public static String HASH_SHA512 = "SHA-512";

    final public static String HASH_SHA1_ = "SHA1";
    final public static String HASH_SHA256_ = "SHA256";
    final public static String HASH_SHA384_ = "SHA384";
    final public static String HASH_SHA512_ = "SHA512";

    final public static int HASH_MD5_LEN = 16;
    final public static int HASH_MD5_LEN_PADDED = 34;

    final public static int HASH_SHA1_LEN = 20;
    final public static int HASH_SHA1_LEN_PADDED = 35;

    final public static int HASH_SHA256_LEN = 32;
    final public static int HASH_SHA256_LEN_PADDED = 51;

    final public static int HASH_SHA384_LEN = 48;
    final public static int HASH_SHA384_LEN_PADDED = 67;

    final public static int HASH_SHA512_LEN = 64;
    final public static int HASH_SHA512_LEN_PADDED = 83;

    final public static String KEY_ALGORITHM_RSA = "RSA";
    final public static String KEY_ALGORITHM_DSA = "DSA";

    final public static String CHARSET_UTF8 = "UTF-8";
    final public static String CHARSET_UTF16LE = "UTF-16LE";
    final public static String CHARSET_UTF16BE = "UTF-16BE";

    final public static String SECURE_BLACKBOX_LICENSE = "A6FF3228BE7138FECDEC31C2C99A5AA8F210D38478CD1C257489A48892330D033BF93983DC971DBB8F6665BCB6298984EE82265EE5C4416B7EB7396E33150675C69BF663B9EAE3D2A96D8C523BF1C5A2B4A09D16A8CD905C87A05EE80726DC0491382879DC4E23DF64888841704169E5CDD8157A7A9A782211A31EBA8531406FD3AF310E3AF618070CC280E98EDB522F57C9A8A5A3BE2A60E0B55486512A44B12B014E8B3C499D082D9F84FCD62FA560C29F54513F1B76DC7B92116CE741BD17080040C65F838E4DEE7744F5D7A6257740E8077EFF01C1B57A661AD51C83D94BA962707FFAE0C25EBFDBBDF7DC5A3A92DBD8C60FCED08AF7F874F3A02805C3D7";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static long crc32(String data) {
        byte bytes[] = data.getBytes();
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        long checksumValue = checksum.getValue();
        return checksumValue;
    }

    public static byte[] hashData(byte[] data, String algorithm) {
        byte[] result = null;
        try {
            if (algorithm.compareToIgnoreCase(HASH_MD5) == 0) {
                algorithm = HASH_MD5;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA1) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA1_) == 0) {
                algorithm = HASH_SHA1;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA256) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA256_) == 0) {
                algorithm = HASH_SHA256;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA384) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA384_) == 0) {
                algorithm = HASH_SHA384;
            } else if (algorithm.compareToIgnoreCase(HASH_SHA512) == 0
                    || algorithm.compareToIgnoreCase(HASH_SHA512_) == 0) {
                algorithm = HASH_SHA512;
            } else {
                algorithm = HASH_SHA256;
            }
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("No Such Algorithm Exception. Details: " + e.toString());
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] hashData(InputStream stream, String algorithm) throws Exception {

        if (algorithm.compareToIgnoreCase(HASH_MD5) == 0) {
            algorithm = HASH_MD5;
        } else if (algorithm.compareToIgnoreCase(HASH_SHA1) == 0
                || algorithm.compareToIgnoreCase(HASH_SHA1_) == 0) {
            algorithm = HASH_SHA1;
        } else if (algorithm.compareToIgnoreCase(HASH_SHA256) == 0
                || algorithm.compareToIgnoreCase(HASH_SHA256_) == 0) {
            algorithm = HASH_SHA256;
        } else if (algorithm.compareToIgnoreCase(HASH_SHA384) == 0
                || algorithm.compareToIgnoreCase(HASH_SHA384_) == 0) {
            algorithm = HASH_SHA384;
        } else if (algorithm.compareToIgnoreCase(HASH_SHA512) == 0
                || algorithm.compareToIgnoreCase(HASH_SHA512_) == 0) {
            algorithm = HASH_SHA512;
        } else {
            algorithm = HASH_SHA256;
        }

        MessageDigest md = MessageDigest.getInstance(algorithm);
//            DigestOutputStream diss = new DigestOutputStream(stream., md);
        try (DigestInputStream dis = new DigestInputStream(stream, md)) {
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) > -1) {
            }
            if (stream.markSupported()) {
                stream.reset();
            }
            return md.digest();
        }

    }

    public static String hashPass(byte[] data) {
        return Hex.toHexString(hashData(hashData(data, HASH_SHA384), HASH_SHA384));
    }

    public static byte[] hashPassToBytes(byte[] data) {
        return hashData(hashData(data, HASH_SHA384), HASH_SHA384);
    }

    public static PublicKey getPublicKeyInPemFormat(String data) {
        data = data.replace("-----BEGIN PUBLIC KEY-----\n", "");
        data = data.replace("\n-----END PUBLIC KEY-----", "");
        PublicKey pubKeyString = null;
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Utils.base64Decode(data));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pubKeyString = kf.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKeyString;
    }

    public static PublicKey getPublicKeyInHexFormat(byte[] data) {
        PublicKey pubKeyString = null;
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pubKeyString = kf.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKeyString;
    }

    public static X509Certificate getX509Object(byte[] der) {
        X509Certificate x509 = null;
        try {
            CertificateFactory certFactoryChild = CertificateFactory
                    .getInstance("X.509", "BC");
            InputStream inChild = new ByteArrayInputStream(der);
            x509 = (X509Certificate) certFactoryChild
                    .generateCertificate(inChild);
        } catch (Exception e) {
            LOG.error("Error occurs when generate certificate, caused by ", e);
        }
        return x509;
    }

    public static X509Certificate getX509Object(String pem) throws Exception {
        X509Certificate x509 = null;
//        try {
        CertificateFactory certFactoryChild = CertificateFactory
                .getInstance("X.509", "BC");
        InputStream inChild = new ByteArrayInputStream(getX509Der(pem));
        x509 = (X509Certificate) certFactoryChild
                .generateCertificate(inChild);
//        } catch (Exception e) {
//            LOG.error("Error occurs when generate certificate, caused by ", e);
//        }
        return x509;
    }

    public static X509Certificate getX509Object(String pem, String provider) {
        X509Certificate x509 = null;
        try {
            CertificateFactory certFactoryChild = CertificateFactory.getInstance("X.509", provider);
            InputStream inChild = new ByteArrayInputStream(getX509Der(pem));
            x509 = (X509Certificate) certFactoryChild.generateCertificate(inChild);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return x509;
    }

    public static byte[] getX509CertificateEncoded(X509Certificate x509) {
        byte[] data = null;
        try {
            data = x509.getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error while getting X509Certificate encoded data");
        }
        return data;
    }

    public static byte[] getPublicKeyEncoded(X509Certificate x509) {
        byte[] data = null;
        try {
            data = x509.getPublicKey().getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Error while getting X509Certificate encoded data");
        }
        return data;
    }

    public static PublicKey getPublicKey(String cert) throws Exception {
        X509Certificate x509 = getX509Object(cert);
        return x509.getPublicKey();
    }

    public static int CERT_EXPIRED = 1;
    public static int CERT_VALID = 0;
    public static int CERT_NOT_YET_VALID = -1;

    public static int checkCertificateValidity(X509Certificate x509) {
        int status;
        try {
            x509.checkValidity();
            status = CERT_VALID;
        } catch (CertificateExpiredException e) {
            LOG.error("Error occurs when validity the certificate, caused by ", e);
            status = CERT_EXPIRED;
        } catch (CertificateNotYetValidException e) {
            LOG.error("Error occurs when validity the certificate, caused by ", e);
            status = CERT_NOT_YET_VALID;
        }
        return status;
    }

    private static byte[] getX509Der(String base64Str)
            throws Exception {
        byte[] binary;
        if (base64Str.contains("-----BEGIN CERTIFICATE-----")) {
            binary = base64Str.getBytes();
        } else {
            binary = Utils.base64Decode(base64Str.replace("\n", ""));
        }
        return binary;
    }

    public static SecretKey computeSecretKey(String keyType, byte[] rawSecretKey) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(rawSecretKey, keyType);
        return (SecretKey) secretKeySpec;
    }

    public static byte[] wrapSecrectKey(String algWrapping, SecretKey wrappingKey, byte[] wrappingIv, Key keyToBeWrapped) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, IllegalBlockSizeException, NoSuchProviderException {
        LOG.debug("Wrapping AsymmetricKey (algWrapping): " + algWrapping);
        Cipher wrappingCipher = Cipher.getInstance(algWrapping);
        String[] list = algWrapping.split("/");
        AlgorithmParameters algParams = AlgorithmParameters.getInstance(list[0]);
        algParams.init(new IvParameterSpec(wrappingIv));
        wrappingCipher.init(Cipher.WRAP_MODE, wrappingKey, algParams);
        return wrappingCipher.wrap(keyToBeWrapped);
    }

    public static Key unwrapSecrectKey(String algWrap, String wrappedKeyAlgorithm, SecretKey wrappingKey, byte[] wrappingIv, byte[] wrappedKey, int wrappedKeyType) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, InvalidParameterSpecException, IllegalBlockSizeException, NoSuchProviderException {
        LOG.debug("Unwrapping AsymmetricKey (algWrap/wrappedKeyAlgorithm/wrappedKeyType): " + algWrap + "/" + wrappedKeyAlgorithm + "/" + wrappedKeyType);
        Cipher wrappingCipher = Cipher.getInstance(algWrap);
        String[] list = algWrap.split("/");
        AlgorithmParameters algParams = AlgorithmParameters.getInstance(list[0]);
        algParams.init(new IvParameterSpec(wrappingIv));
        wrappingCipher.init(Cipher.UNWRAP_MODE, wrappingKey, algParams);
        return wrappingCipher.unwrap(wrappedKey, wrappedKeyAlgorithm, wrappedKeyType);
    }

    public static byte[] encrypt(String encryptType, SecretKey key, byte[] initVector, byte[] data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv = new IvParameterSpec(initVector);
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encrypted = cipher.doFinal(data);
        return encrypted;

    }

    public static byte[] decrypt(String encryptType, SecretKey key, byte[] initVector, byte[] encoded)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec iv = new IvParameterSpec(initVector);
        Cipher cipher = Cipher.getInstance(encryptType);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] data = cipher.doFinal(encoded);
        return data;
    }

//    public static List<Certificate> getCertificate(String caCert1) throws IOException, CertificateException {
//        Collection<Certificate> certChain = Certificat.getCertsFromPEM(new ByteArrayInputStream(caCert1.getBytes()));
//        List<Certificate> certificates = new ArrayList();
//        certificates.addAll(certChain);
//        return certificates;
//    }
//
//    public static List<Certificate> getCertificateChain(String caCert1, String caCert2, X509Certificate cert) {
//        X509Certificate endCert = null;
//        X509Certificate ca1 = null;
//        X509Certificate ca2 = null;
//        endCert = cert;
//        ca1 = getX509Object(caCert1);
//        try {
//            endCert.verify(ca1.getPublicKey());
//            Collection<Certificate> certChain = CertTools.getCertsFromPEM(new ByteArrayInputStream(caCert1.getBytes()));
//
//            List<Certificate> certificates = new ArrayList();
//            certificates.add(endCert);
//            certificates.addAll(certChain);
//
//            return certificates;
//        } catch (Exception e) {
//            LOG.warn("First CA certificate isn't the one who issues end-user certificate. Try the second one");
//            ca2 = getX509Object(caCert2);
//            try {
//                endCert.verify(ca2.getPublicKey());
//                Collection<Certificate> certChain = CertTools.getCertsFromPEM(new ByteArrayInputStream(caCert2.getBytes()));
//
//                List<Certificate> certificates = new ArrayList();
//                certificates.add(endCert);
//                certificates.addAll(certChain);
//
//                return certificates;
//            } catch (Exception exx) {
//                LOG.warn("Cert-Chain is invalid", exx);
//                return null;
//            }
//        }
//    }
//    
//    public static String getCRLDistributionPoint(final Certificate certificate) {
//        String crlUri = null;
//        try {
//            crlUri = CertTools.getCrlDistributionPoint(certificate).toString();
//        } catch (Exception e) {
//            LOG.error("Error while getting CRL URI. Details: " + e.toString());
//            e.printStackTrace();
//        }
//        return crlUri;
//    }
    public static List<Certificate> getCertificate(String caCert1) throws IOException, CertificateException {
        //Collection<Certificate> certChain = Certificat.getCertsFromPEM(new ByteArrayInputStream(caCert1.getBytes()));
        org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory cf = new org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory();
        Collection<Certificate> certChain = cf.engineGenerateCertificates(new ByteArrayInputStream(caCert1.getBytes()));

        List<Certificate> certificates = new ArrayList();
        certificates.addAll(certChain);
        return certificates;
    }

    public static List<X509Certificate> getCertificateChain(String caCert1, String caCert2, X509Certificate cert) throws Exception {
        X509Certificate endCert = null;
        X509Certificate ca1 = null;
        X509Certificate ca2 = null;
        endCert = cert;
        ca1 = getX509Object(caCert1);

        org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory cf = new org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory();
        try {
            endCert.verify(ca1.getPublicKey());
            Collection certChain = cf.engineGenerateCertificates(new ByteArrayInputStream(caCert1.getBytes()));
            //Collection<Certificate> certChain = CertTools.getCertsFromPEM(new ByteArrayInputStream(caCert1.getBytes()));

            List<X509Certificate> certificates = new ArrayList();
            certificates.add(endCert);
            certificates.addAll(certChain);

            return certificates;
        } catch (Exception e) {
            LOG.warn("First CA certificate isn't the one who issues end-user certificate. Try the second one");
            try {
                ca2 = getX509Object(caCert2);
                endCert.verify(ca2.getPublicKey());
                Collection certChain = cf.engineGenerateCertificates(new ByteArrayInputStream(caCert2.getBytes()));
                //Collection<Certificate> certChain = CertTools.getCertsFromPEM(new ByteArrayInputStream(caCert2.getBytes()));

                List<X509Certificate> certificates = new ArrayList();
                certificates.add(endCert);
                certificates.addAll(certChain);

                return certificates;
            } catch (Exception exx) {
                LOG.warn("Cert-Chain is invalid", exx);
                return null;
            }
        }
    }

    public static String sign(String data, String keystorePath, String keystorePassword, String keystoreType) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance(keystoreType);
        Signature sig;
        try (InputStream is = new FileInputStream(keystorePath)) {
            keystore.load(is, keystorePassword.toCharArray());
            Enumeration<String> e = keystore.aliases();
            String aliasName;
            PrivateKey key = null;
            while (e.hasMoreElements()) {
                aliasName = e.nextElement();
                key = (PrivateKey) keystore.getKey(aliasName,
                        keystorePassword.toCharArray());
                if (key != null) {
                    break;
                }
            }
            sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(key);
            sig.update(data.getBytes());
        }
        return Utils.base64Encode(sig.sign());
    }

    public static String sign(String data, String keystr, String mimeType) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        PrivateKey key = getPrivateKeyFromString(keystr, mimeType);
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return Utils.base64Encode(sig.sign());
    }

    public static PrivateKey getPrivateKeyFromString(String key, String mimeType) throws IOException, GeneralSecurityException {
        byte[] encoded = null;
        if (mimeType.toLowerCase().contains("base64")) {
            String privateKeyPEM = key;
            privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----\n", "");
            privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
            encoded = Utils.base64Decode(privateKeyPEM);
        } else {
            encoded = Utils.base64Decode(key);
        }
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        PrivateKey privKey = (PrivateKey) kf.generatePrivate(keySpec);
        return privKey;
    }

    public static boolean[] getKeyUsage(X509Certificate x509) {
        /*
         * digitalSignature        (0),
         nonRepudiation          (1),
         keyEncipherment         (2),
         dataEncipherment        (3),
         keyAgreement            (4),
         keyCertSign             (5),  --> true ONLY for CAs
         cRLSign                 (6),
         encipherOnly            (7),
         decipherOnly            (8)
         *
         **/
        return x509.getKeyUsage();
    }

    public static int getBasicConstraint(X509Certificate x509) {
        return x509.getBasicConstraints();
    }

    //            ASN1ObjectIdentifier digestOid = new ASN1ObjectIdentifier("2.16.840.1.101.3.4.2.1");
//            AlgorithmIdentifier sha256oid = new AlgorithmIdentifier(digestOid, DERNull.INSTANCE);
//            DigestInfo di = new DigestInfo(sha256oid, data);
//            byte[] hashWithOID = di.getEncoded(ASN1Encoding.DER);
    public static byte[] padSHA1Oid(byte[] hashedData) throws Exception {
        ASN1ObjectIdentifier sha1oid_ = new ASN1ObjectIdentifier("1.3.14.3.2.26");
        AlgorithmIdentifier sha1aid_ = new AlgorithmIdentifier(sha1oid_, DERNull.INSTANCE);
        DigestInfo di = new DigestInfo(sha1aid_, hashedData);
        byte[] plainSig = di.getEncoded(ASN1Encoding.DER);
        return plainSig;
    }

    public static boolean checkCertificateRelation(String childCert,
            String parentCert) {
        boolean isOk = false;
        try {
            CertificateFactory certFactoryChild = CertificateFactory
                    .getInstance("X.509", "BC");
            InputStream inChild = new ByteArrayInputStream(
                    getX509Der(childCert));
            X509Certificate certChild = (X509Certificate) certFactoryChild
                    .generateCertificate(inChild);

            CertificateFactory certFactoryParent = CertificateFactory
                    .getInstance("X.509", "BC");
            InputStream inParent = new ByteArrayInputStream(
                    getX509Der(parentCert));
            X509Certificate certParent = (X509Certificate) certFactoryParent
                    .generateCertificate(inParent);

            certChild.verify(certParent.getPublicKey());

            isOk = true;
        } catch (SignatureException e) {
            LOG.error("Invalid certficate. Signature exception");
        } catch (CertificateException e) {
            LOG.error("Invalid certficate. Certificate exception");
        } catch (Exception e) {
            LOG.error("Invalid certficate. Something wrong exception");
        }
        return isOk;
    }

    public static boolean checkCertificateRelation(X509Certificate childCert,
            X509Certificate parentCert) {
        boolean isOk = false;
        try {
            childCert.verify(parentCert.getPublicKey());
            isOk = true;
        } catch (SignatureException e) {
            LOG.error("Invalid certficate. Signature exception");
            e.printStackTrace();
        } catch (CertificateException e) {
            LOG.error("Invalid certficate. Certificate exception");
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error("Invalid certficate. Something wrong exception");
            e.printStackTrace();
        }
        return isOk;
    }

    public static boolean checkCertificateAndCsr(String certificate, String csr) throws Exception {
        boolean isOk = false;
        try {
            X509Certificate x509Certificate = getX509Object(certificate);
            byte[] certPubkeyHash = hashData(x509Certificate.getPublicKey().getEncoded(), HASH_SHA1);

            PKCS10CertificationRequest pKCS10CertificationRequest
                    = new PKCS10CertificationRequest(Base64.getMimeDecoder().decode(csr));
            byte[] csrPubkeyHash = hashData(pKCS10CertificationRequest.getPublicKey().getEncoded(), HASH_SHA1);
            if (Arrays.equals(certPubkeyHash, csrPubkeyHash)) {
                isOk = true;
            } else {
                isOk = false;
            }
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Invalid certficate. NoSuchAlgorithmExceptionn");
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            LOG.error("Invalid certficate. NoSuchAlgorithmException");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            LOG.error("Invalid certficate. InvalidKeyException");
            e.printStackTrace();
        }
        return isOk;
    }

    public static PublicKey checkCsr(String csr) throws Exception {
        PKCS10CertificationRequest pKCS10CertificationRequest
                = new PKCS10CertificationRequest(Utils.base64Decode(csr));
        if (!pKCS10CertificationRequest.verify()) {
            throw new InvalidKeyException("Verify CSR is false");
        }
        return pKCS10CertificationRequest.getPublicKey();
    }

    public static PublicKey getPublicKeyFromCsr(String csr) throws Exception {
        PKCS10CertificationRequest pKCS10CertificationRequest
                = new PKCS10CertificationRequest(Utils.base64Decode(csr));
        return pKCS10CertificationRequest.getPublicKey();
    }

    public static String encryptRSA(String message, PublicKey publicKey) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            result = Utils.base64Encode(cipher.doFinal(message.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decryptRSA(String message, PrivateKey privateKey) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = new String(cipher.doFinal(Utils.base64Decode(message)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean validateHashData(String hash) {
        if ((hash.length() % 2) != 0) {
            LOG.error("Invalid HashData=" + hash + " modulus of 2 should be ZERO");
            return false;
        }
        byte[] binraryHash = Utils.base64Decode(hash);
        if (binraryHash.length > 83) { // 83 is SHA-512 padded
            LOG.error("Hash length is greater than 64 bytes. Wtf?");
            return false;
        }
        return true;
    }

    public static String getHashAlgorithm(byte[] hashData) {
        int len = hashData.length;
        switch (len) {
            case HASH_MD5_LEN:
                return HASH_MD5;
            case HASH_MD5_LEN_PADDED:
                return HASH_MD5;
            case HASH_SHA1_LEN:
                return HASH_SHA1;
            case HASH_SHA1_LEN_PADDED:
                return HASH_SHA1;
            case HASH_SHA256_LEN:
                return HASH_SHA256;
            case HASH_SHA256_LEN_PADDED:
                return HASH_SHA256;
            case HASH_SHA384_LEN:
                return HASH_SHA384;
            case HASH_SHA384_LEN_PADDED:
                return HASH_SHA384;
            case HASH_SHA512_LEN:
                return HASH_SHA512;
            case HASH_SHA512_LEN_PADDED:
                return HASH_SHA512;
            default:
                return HASH_SHA1;
        }
    }

    public static byte[] getBytes(String data, String charset) {
        byte[] bytes;
        try {
            bytes = data.getBytes(charset);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Invalid charset " + charset + ". Using the default one. It maybe got the unicode issue");
            bytes = data.getBytes();
        }
        return bytes;
    }

    public static String generatePKCS1Signature(
            String data,
            String keyStorePath,
            String keyStorePassword,
            String keystoreType) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance(keystoreType);
        Signature sig;
        try (InputStream is = new FileInputStream(keyStorePath)) {
            keystore.load(is, keyStorePassword.toCharArray());
            Enumeration<String> e = keystore.aliases();
            String aliasName;
            PrivateKey key = null;
            while (e.hasMoreElements()) {
                aliasName = e.nextElement();
                key = (PrivateKey) keystore.getKey(aliasName,
                        keyStorePassword.toCharArray());
                if (key != null) {
                    break;
                }
            }
            sig = Signature.getInstance("SHA1withRSA");
            sig.initSign(key);
            sig.update(data.getBytes());
        }
        return Utils.base64Encode(sig.sign());
    }

    public static PublicKey computePublicKey(BigInteger modulus, BigInteger exponent) {
        PublicKey pubKey = null;
        try {
            pubKey = (PublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new RSAPublicKeySpec(modulus, exponent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pubKey;
    }

    public static byte[] paddingHashOID(String hashName, byte[] hashedData) throws Exception {
//        LOG.debug("Padding [" + hashName + "] OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(hashName);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA1OID(byte[] hashedData) throws Exception {
        LOG.debug("Padding SHA-1 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(HASH_SHA1);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA256OID(byte[] hashedData) throws Exception {
        LOG.debug("Padding SHA-256 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(HASH_SHA256);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA384OID(byte[] hashedData) throws Exception {
        LOG.debug("Padding SHA-384 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(HASH_SHA384);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingSHA512OID(byte[] hashedData) throws Exception {
        LOG.debug("Padding SHA-512 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(HASH_SHA512);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static byte[] paddingMD5OID(byte[] hashedData) throws Exception {
        LOG.debug("Padding MD5 OID");
        DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
        AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(HASH_MD5);
        DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashedData);
        return digestInfo.getEncoded();
    }

    public static String getPKCS1Signature(String data, String relyingPartyKeyStore, String relyingPartyKeyStorePassword) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(relyingPartyKeyStore);
        keystore.load(is, relyingPartyKeyStorePassword.toCharArray());

        Enumeration<String> e = keystore.aliases();
        String aliasName = "";
        while (e.hasMoreElements()) {
            aliasName = e.nextElement();
        }
        PrivateKey key = (PrivateKey) keystore.getKey(aliasName,
                relyingPartyKeyStorePassword.toCharArray());

        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initSign(key);
        sig.update(data.getBytes());
        return Utils.base64Encode(sig.sign());
    }

    public static byte[] md5(byte[] data) {
        byte[] result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("No Such Algorithm Exception. Details: " + e.toString());
            e.printStackTrace();
        }
        return result;
    }

    public static String getOcspUri(X509Certificate certificate) {
        String ocspUri = null;
        try {
            ASN1Object obj = getExtensionValue(certificate, org.bouncycastle.asn1.x509.X509Extension.authorityInfoAccess.getId());
            if (obj == null) {
                return null;
            }
            ASN1Sequence AccessDescriptions = (ASN1Sequence) obj;
            for (int i = 0; i < AccessDescriptions.size(); i++) {
                ASN1Sequence AccessDescription = (ASN1Sequence) AccessDescriptions.getObjectAt(i);
                if (AccessDescription.size() != 2) {
                    continue;
                } else {
                    if ((AccessDescription.getObjectAt(0) instanceof ASN1ObjectIdentifier) && ((ASN1ObjectIdentifier) AccessDescription.getObjectAt(0)).getId().equals("1.3.6.1.5.5.7.48.1")) {
                        String AccessLocation = getStringFromGeneralName((ASN1Object) AccessDescription.getObjectAt(1));
                        if (AccessLocation == null) {
                            return null;
                        } else {
                            return AccessLocation;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error while getting OCSP URI. Details: " + e.toString());
            e.printStackTrace();
        }
        return ocspUri;
    }

    public static ASN1Object getExtensionValue(X509Certificate cert, String oid) throws IOException {
        byte[] bytes = cert.getExtensionValue(oid);
        if (bytes == null) {
            return null;
        }
        ASN1OctetString octs1;
        try (ASN1InputStream aIn1 = new ASN1InputStream(new ByteArrayInputStream(bytes))) {
            octs1 = (ASN1OctetString) aIn1.readObject();
        }
        ASN1Object octs2;
        try (ASN1InputStream aIn2 = new ASN1InputStream(new ByteArrayInputStream(octs1.getOctets()))) {
            octs2 = aIn2.readObject();
        }
        return octs2;
    }

    private static String getStringFromGeneralName(ASN1Object names) throws IOException {
        DERTaggedObject taggedObject = (DERTaggedObject) names;
        return new String(ASN1OctetString.getInstance(taggedObject, false).getOctets(), "ISO-8859-1");
    }

//    public static boolean verifyRsaSignature(String hashAlg, String signAlgo, boolean isPss, byte[] data, byte[] signature, PublicKey publicKey) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException, RSSP_InvalidParamException1 {
//
//        boolean hashed = false;
//        String[] sss = signAlgo.split("with");
//        if (sss.length != 2) {
//            throw new RSSP_InvalidParamException1("signAlgo must have format {hash}with{RSA}");
//        }
//        if (!sss[1].equalsIgnoreCase("RSA")) {
//            throw new RSSP_InvalidParamException1("signAlgo must have format {hash}with{RSA}");
//        }
//        if (sss[0].equalsIgnoreCase("NONE")) {
//            hashed = true;
//        } else if (hashAlg == null) {
//            hashAlg = sss[0].toUpperCase();
//        } else if (!sss[0].equalsIgnoreCase(hashAlg)) {
//            throw new RSSP_InvalidParamException1("signAlgo not matched with HashAlgo, SignAlgo is [" + signAlgo + "], but HashAlgo is [" + hashAlg + "]");
//        }
////        LOG.debug("hashed: " + hashed + ". isPss: " + isPss);
////        LOG.debug("hashAlg: " + hashAlg + ". signAlgo: " + signAlgo);
//        if (!hashed) {
//            if (!isPss) {
//                Signature privateSignature = Signature.getInstance(signAlgo);
//                privateSignature.initVerify(publicKey);
//                privateSignature.update(data);
//
//                return privateSignature.verify(signature);
//            } else {
////                final String RAWRSASSA_PSS = "NONEWITHRSASSA-PSS";
////                Signature sign = Signature.getInstance(RAWRSASSA_PSS, "BC");
////                sign.initVerify(publicKey);
////                sign.update(data);
////
////                return sign.verify(signature);
//                final String MGF1 = "MGF1";
//                final String RAWRSASSA_PSS = "NONEWITHRSASSA-PSS";
//                Digest digest = DigestFactory.getDigest(hashAlg);
//                //final int saltLen = signature.length - 2 - digest.getDigestSize();
//                final int saltLen = digest.getDigestSize();
//                digest.update(data, 0, data.length);
//                byte[] hash = new byte[saltLen];
//                digest.doFinal(hash, 0);
//
//                Signature sign = Signature.getInstance(RAWRSASSA_PSS, "BC");
//                PSSParameterSpec pssPrams = new PSSParameterSpec(hashAlg, MGF1, new MGF1ParameterSpec(hashAlg), saltLen, 1);
//                sign.setParameter(pssPrams);
//                sign.initVerify(publicKey);
//                sign.update(hash);
//
//                return sign.verify(signature);
//            }
//        } else {
//
//            if (!isPss) {
//                DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
//                AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(hashAlg);
//                DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, data);
//                byte[] hashWithOID = digestInfo.getEncoded();
//
////            ASN1ObjectIdentifier digestOid = new ASN1ObjectIdentifier("2.16.840.1.101.3.4.2.1");
////            AlgorithmIdentifier sha256oid = new AlgorithmIdentifier(digestOid, DERNull.INSTANCE);
////            DigestInfo di = new DigestInfo(sha256oid, data);
////            byte[] hashWithOID = di.getEncoded(ASN1Encoding.DER);
//                Signature privateSignature = Signature.getInstance("NonewithRSA");
//                privateSignature.initVerify(publicKey);
//                privateSignature.update(hashWithOID);
//                return privateSignature.verify(signature);
//            } else {
//                final String MGF1 = "MGF1";
//                final String RAWRSASSA_PSS = "NONEWITHRSASSA-PSS";
//                //final int saltLen = signature.length - 2 - DigestFactory.getDigest(hashAlg).getDigestSize();
//                final int saltLen = DigestFactory.getDigest(hashAlg).getDigestSize();
//                Signature sign = Signature.getInstance(RAWRSASSA_PSS, "BC");
//                PSSParameterSpec pssPrams = new PSSParameterSpec(hashAlg, MGF1, new MGF1ParameterSpec(hashAlg), saltLen, 1);
//                sign.setParameter(pssPrams);
//                sign.initVerify(publicKey);
//                sign.update(data);
//
//                return sign.verify(signature);
//            }
//        }
//    }
//
//    public static boolean verifyRsaSignature(String hashAlgOID, String signAlgOID, String signAlgoParam, byte[] data, byte[] signature, String certificate) throws Exception {
//
//        LOG.info("Verify AuthorizationSignature, HashOID: [" + hashAlgOID
//                + "] SignAlgo: [" + signAlgOID + "], SignAlgoParams: [" + signAlgoParam + "]");
//        String hashAlg = null;
//        if (!Utils.isNullOrEmpty(hashAlgOID)) {
//            hashAlg = HashAlgorithmOID.valueOfOID(hashAlgOID).name;
//        }
//        boolean isPss = false;
//        String signAlgoString = SignAlgo.valueOfOID(signAlgOID).algName;
//
//        if (!Utils.isNullOrEmpty(signAlgoParam)) {
//            if (SignAlgoParams.RSASSA_PSS.derAsn1.equals(signAlgoParam)) {
//                isPss = true;
//            } else {
//                throw new IllegalArgumentException("Unknown SignAlgoParams: [" + signAlgoParam + "]");
//            }
//        }
//
//        return verifyRsaSignature(hashAlg, signAlgoString, isPss, data, signature, getPublicKey(certificate));
//    }
    public static byte[] getCertificateRequestInfo(X500Name x500Name, PublicKey pubKey) throws IOException {
        ASN1Primitive p;
        try (ASN1InputStream input = new ASN1InputStream(pubKey.getEncoded())) {
            p = input.readObject();
        }
        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(p);
        CertificationRequestInfo csr = new CertificationRequestInfo(x500Name, pubInfo, null);
        return csr.getEncoded();
    }

    public static byte[] getCertificateRequestInfo(X500Name x500Name, byte[] pubKey) throws IOException {
        ASN1Primitive p;
        try (ASN1InputStream input = new ASN1InputStream(pubKey)) {
            p = input.readObject();
        }
        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(p);
        CertificationRequestInfo csr = new CertificationRequestInfo(x500Name, pubInfo, null);
        return csr.getEncoded();
    }

    public static SubjectPublicKeyInfo getSubjectPublicKeyInfo(PublicKey pubKey) throws IOException {
        try (ASN1InputStream input = new ASN1InputStream(pubKey.getEncoded())) {
            ASN1Primitive p = input.readObject();
            return SubjectPublicKeyInfo.getInstance(p);
        }
    }

//    public static byte[] getCertificateRequestInfo(X500Name x500Name, byte[] modulus, byte[] exponent) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
//        PublicKey pub = RSAUtils.getInstance().calcRsaPublicKey(new BigInteger(1, modulus), new BigInteger(1, exponent));
//
////        ASN1InputStream input = new ASN1InputStream(pub.getEncoded());
////        ASN1Primitive p = input.readObject();
//        SubjectPublicKeyInfo pubInfo = SubjectPublicKeyInfo.getInstance(pub.getEncoded());
//        CertificationRequestInfo csr = new CertificationRequestInfo(x500Name, pubInfo, null);
//        return csr.getEncoded();
//    }
    public static byte[] createCertificationSignatureRequest(byte[] certReqInfo, String alg,
            byte[] signature) throws IOException, NoSuchAlgorithmException {
        final DerOutputStream der1 = new DerOutputStream();
        der1.write(certReqInfo);

        // add signature algorithm identifier, and a digital signature on the
        // certification request information
        AlgorithmId.get(alg).encode(der1);
        der1.putBitString(signature);

        // final DER encoded output
        final DerOutputStream der2 = new DerOutputStream();
        der2.write((byte) 48, der1);
        return der2.toByteArray();
    }

    public static byte[] createCertificationSignatureRequest(byte[] certReqInfo, String hash, String keyAlg,
            byte[] signature) throws IOException, NoSuchAlgorithmException {
        final DerOutputStream der1 = new DerOutputStream();
        der1.write(certReqInfo);

        // add signature algorithm identifier, and a digital signature on the
        // certification request information
        AlgorithmId.get(hash.concat("with").concat(keyAlg)).encode(der1);
        der1.putBitString(signature);

        // final DER encoded output
        final DerOutputStream der2 = new DerOutputStream();
        der2.write((byte) 48, der1);
        return der2.toByteArray();
    }

    private static String bytesToString(
            byte[] data) {
        char[] cs = new char[data.length];

        for (int i = 0; i != cs.length; i++) {
            cs[i] = (char) (data[i] & 0xff);
        }

        return new String(cs);
    }

    public static Map<String, String[]> subjectDnToMap(String sub) throws Exception {
        try {
            Map<String, String[]> map = new HashMap();
            LdapName dn = new LdapName(sub);
            for (int i = 0; i < dn.size(); i++) {
                String[] sItem = dn.get(i).trim().split("=");
                if (map.containsKey(sItem[0])) {
                    String[] lst = map.get(sItem[0]);
                    String[] newVal = new String[lst.length + 1];
                    System.arraycopy(lst, 0, newVal, 0, lst.length);
                    newVal[lst.length] = sItem[1];
                    map.put(sItem[0], newVal);
                } else {
                    map.put(sItem[0], new String[]{sItem[1]});
                }
            }
            return map;
        } catch (InvalidNameException e) {
            //System.out.println("Cannot parse name: " + sub);
            LOG.debug("Error when parser SubjectDn", e);
            throw e;
        }
    }

    public static String valueToString(ASN1Encodable value) {
        StringBuffer vBuf = new StringBuffer();
        if (value instanceof ASN1String && !(value instanceof DERUniversalString)) {
            String v = ((ASN1String) value).getString();
            if (v.length() > 0 && v.charAt(0) == '#') {
                vBuf.append("\\").append(v);
            } else {
                vBuf.append(v);
            }
        } else {
            try {
                vBuf.append("#" + bytesToString(Hex.encode(value.toASN1Primitive().getEncoded(ASN1Encoding.DER))));
            } catch (IOException e) {
                throw new IllegalArgumentException("Other value has no encoded form");
            }
        }

        int end = vBuf.length();
        int index = 0;

        if (vBuf.length() >= 2 && vBuf.charAt(0) == '\\' && vBuf.charAt(1) == '#') {
            index += 2;
        }

//        while (index != end) {
//            if ((vBuf.charAt(index) == ',')
//                    || (vBuf.charAt(index) == '"')
//                    || (vBuf.charAt(index) == '\\')
//                    || (vBuf.charAt(index) == '+')
//                    || (vBuf.charAt(index) == '=')
//                    || (vBuf.charAt(index) == '<')
//                    || (vBuf.charAt(index) == '>')
//                    || (vBuf.charAt(index) == ';')) {
//                vBuf.insert(index, "\\");
//                index++;
//                end++;
//            }
//
//            index++;
//        }
        int start = 0;
        if (vBuf.length() > 0) {
            while (vBuf.length() > start && vBuf.charAt(start) == ' ') {
                vBuf.insert(start, "\\");
                start += 2;
            }
        }

        int endBuf = vBuf.length() - 1;

        while (endBuf >= 0 && vBuf.charAt(endBuf) == ' ') {
            vBuf.insert(endBuf, '\\');
            endBuf--;
        }

        return vBuf.toString();
    }

    public static String getStringFromX500Name(X500Name x500Name, ASN1ObjectIdentifier oid) {
        RDN[] rdns = x500Name.getRDNs(oid);
        if (rdns == null || rdns.length == 0) {
            return "";
        }
        ASN1Primitive asnp = rdns[0].getFirst().getValue().toASN1Primitive();

        return valueToString(asnp);
//        return DERUTF8String.getInstance(asnp).toString();
    }

    public static String[] getStringFromX500NameAsArray(X500Name x500Name, ASN1ObjectIdentifier oid) {
        RDN[] rdns = x500Name.getRDNs(oid);
        if (rdns == null || rdns.length == 0) {
            return null;
        }
        String[] resp = new String[rdns.length];
        for (int i = 0; i < rdns.length; i++) {
            ASN1Primitive asnp = rdns[i].getFirst().getValue().toASN1Primitive();
            resp[i] = valueToString(asnp);
        }
        return resp;
    }

    public static String getThumbprintOfCertificate(X509Certificate crt) throws CertificateEncodingException {
        return Hex.toHexString(hashData(crt.getEncoded(), HASH_SHA1)).toUpperCase();
    }

    public static byte[] getAuthorityKeyIdentifier(byte[] oidVal) throws IOException {
        AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(oidVal));

        return aki.getKeyIdentifier();
    }

    public static byte[] getAuthorityKeyIdentifier(X509Certificate crt) throws IOException {

        byte[] authKeyID = crt.getExtensionValue("2.5.29.35");
        //AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(authKeyID));

        //return aki.getKeyIdentifier();
        return getAuthorityKeyIdentifier(authKeyID);
    }

    public static String getAuthorityKeyIdentifierAsHex(X509Certificate crt) {
        try {
            return Hex.toHexString(getAuthorityKeyIdentifier(crt)).toUpperCase();
        } catch (Exception ex) {
            LOG.error("", ex);
            return null;
        }
    }

    public static byte[] getSubjectKeyIdentifier(X509Certificate crt) throws IOException {

        byte[] subKeyID = crt.getExtensionValue("2.5.29.14");
        SubjectKeyIdentifier aki = SubjectKeyIdentifier.getInstance(X509ExtensionUtil.fromExtensionValue(subKeyID));

        return aki.getKeyIdentifier();
    }

    public static SubjectKeyIdentifier getSubjectKeyIdentifier1(X509Certificate x509) throws IOException {
//        byte[] subKeyID = crt.getExtensionValue(Extension.subjectKeyIdentifier.getId());
//        SubjectKeyIdentifier aki = SubjectKeyIdentifier.getInstance(getASN1Primitive(subKeyID));
//        return aki;

        //SubjectPublicKeyInfo.getInstance(x509.getPublicKey().getEncoded()).
        return new BcX509ExtensionUtils()
                .createSubjectKeyIdentifier(SubjectPublicKeyInfo.getInstance(x509.getPublicKey().getEncoded()));
    }

//    public static AuthorityKeyIdentifier getAuthorityKeyIdentifier1(X509Certificate x509) throws IOException {
//        byte[] val = x509.getExtensionValue(Extension.authorityKeyIdentifier.getId());
//        
//        ASN1InputStream aIn = new ASN1InputStream(new ByteArrayInputStream(val));
//                DEROctetString octs = (DEROctetString) aIn.readObject();
//                
//        AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.getInstance(octs.getEncoded());
//        return aki;
//    }
    public static List<String> getCrlDistributionPoints(X509Certificate cert)
            throws IOException {
        byte[] crldpExt = cert.getExtensionValue(Extension.cRLDistributionPoints.getId());
        if (crldpExt == null) {
            return null;
        }
        ASN1Primitive derObjCrlDP;
        try (ASN1InputStream oAsnInStream = new ASN1InputStream(crldpExt)) {
            derObjCrlDP = oAsnInStream.readObject();
        }
        if (!(derObjCrlDP instanceof ASN1OctetString)) {
            LOG.warn("CRL distribution points for certificate subject "
                    + cert.getSubjectX500Principal().getName()
                    + " should be an octet string, but is " + derObjCrlDP);
            return null;
        }
        ASN1OctetString dosCrlDP = (ASN1OctetString) derObjCrlDP;
        byte[] crldpExtOctets = dosCrlDP.getOctets();
        ASN1Primitive derObj2;
        try (ASN1InputStream oAsnInStream2 = new ASN1InputStream(crldpExtOctets)) {
            derObj2 = oAsnInStream2.readObject();
        }
        CRLDistPoint distPoint = CRLDistPoint.getInstance(derObj2);
        List<String> crlUrls = new ArrayList<>();
        for (DistributionPoint dp : distPoint.getDistributionPoints()) {
            DistributionPointName dpn = dp.getDistributionPoint();
            // Look for URIs in fullName
            if (dpn != null && dpn.getType() == DistributionPointName.FULL_NAME) {
                // Look for an URI
                for (GeneralName genName : GeneralNames.getInstance(dpn.getName()).getNames()) {
                    if (genName.getTagNo() == GeneralName.uniformResourceIdentifier) {
                        String url = DERIA5String.getInstance(genName.getName()).getString();
                        crlUrls.add(url);
                    }
                }
            }
        }
        return crlUrls;
    }

    public static AuthorityInformationAccess getAuthorityInformationAccess(X509Certificate x509cert) throws IOException {
        ASN1Object obj = getExtensionValue(x509cert, Extension.authorityInfoAccess.getId());
        if (obj == null) {
            return null;
        }
        return AuthorityInformationAccess.getInstance(obj);
    }

    public static BasicConstraints getBasicConstraints(X509Certificate x509cert) throws IOException {
        ASN1Object obj = getExtensionValue(x509cert, Extension.basicConstraints.getId());
        if (obj == null) {
            return null;
        }

        return BasicConstraints.getInstance(obj);
    }

    public static ExtendedKeyUsage getExtendedKeyUsage(X509Certificate x509cert) throws IOException {
        ASN1Object obj = getExtensionValue(x509cert, Extension.extendedKeyUsage.getId());
        if (obj == null) {
            return null;
        }

        return ExtendedKeyUsage.getInstance(obj);
    }

    public static CertificatePolicies getCertificatePolicies(X509Certificate x509cert) throws IOException {
        ASN1Object obj = getExtensionValue(x509cert, Extension.certificatePolicies.getId());
        if (obj == null) {
            return null;
        }

        return CertificatePolicies.getInstance(obj);
    }

    public static AccessDescription getSubjectInfoAccess(X509Certificate x509cert) throws IOException {
        ASN1Object obj = getExtensionValue(x509cert, Extension.subjectInfoAccess.getId());
        if (obj == null) {
            return null;
        }
        return AccessDescription.getInstance(obj);
    }

    public static void getSubjectAlternativeNames(X509Certificate certificate, List<String> emails, List<String> dns, List<String> ips, List<String> others) {
        try {
            Collection<List<?>> altNames = certificate.getSubjectAlternativeNames();
            if (altNames == null) {
                return;
            }
            for (List item : altNames) {
                Integer type = (Integer) item.get(0);
                if (null == type) {
                    LOG.warn("SubjectAltName of invalid type found: " + certificate);
                } else {
                    switch (type) {
                        case 0:
                            getSubjectAltName(item, others);
                            break;
                        case 1:
                            getSubjectAltName(item, emails);
                            break;
                        case 2:
                            getSubjectAltName(item, dns);
                            break;
                        case 7:
                            getSubjectAltName(item, ips);
                            break;
                        default:
                            LOG.warn("SubjectAltName of invalid type found: " + certificate);
                            break;
                    }
                }
            }
        } catch (CertificateParsingException e) {
            LOG.error("Error parsing SubjectAltName in certificate: " + certificate + "\r\nerror:" + e.getLocalizedMessage(), e);
        }
    }

    private static void getSubjectAltName(List item, List<String> out) {
        try {
            ASN1InputStream decoder = null;
            if (item.toArray()[1] instanceof byte[]) {
                decoder = new ASN1InputStream((byte[]) item.toArray()[1]);
            } else if (item.toArray()[1] instanceof String) {
                out.add((String) item.toArray()[1]);
            }
            if (decoder == null) {
                return;
            }
            ASN1Encodable encoded = decoder.readObject();
            encoded = ((ASN1Sequence) encoded).getObjectAt(1);
            encoded = ((DERTaggedObject) encoded).getObject();
            encoded = ((DERTaggedObject) encoded).getObject();
            String identity = ((DERUTF8String) encoded).getString();
            out.add(identity);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Error decoding subjectAltName" + e.getLocalizedMessage(), e);
        } catch (Exception e) {
            LOG.error("Error decoding subjectAltName" + e.getLocalizedMessage(), e);
        }
    }

    public static ASN1Primitive getASN1Primitive(byte[] iii) throws IOException {
        try (ASN1InputStream input = new ASN1InputStream(iii)) {
            return input.readObject().toASN1Primitive();
        }
    }

    public static String getSubjectKeyIdentifierAsHex(X509Certificate crt) throws IOException {
        return Hex.toHexString(getSubjectKeyIdentifier(crt)).toUpperCase();
    }

    public static String toHexString(byte[] in) throws IOException {
        return Hex.toHexString(in);
    }

    public static int getKeyLength(final PublicKey pk) {
        int len = -1;
        if (pk instanceof RSAPublicKey) {
            final RSAPublicKey rsapub = (RSAPublicKey) pk;
            len = rsapub.getModulus().bitLength();
        } else if (pk instanceof JCEECPublicKey) {
            final JCEECPublicKey ecpriv = (JCEECPublicKey) pk;
            final org.bouncycastle.jce.spec.ECParameterSpec spec = ecpriv.getParameters();
            if (spec != null) {
                len = spec.getN().bitLength();
            } else {
                // We support the key, but we don't know the key length
                len = 0;
            }
        } else if (pk instanceof ECPublicKey) {
            final ECPublicKey ecpriv = (ECPublicKey) pk;
            final java.security.spec.ECParameterSpec spec = ecpriv.getParams();
            if (spec != null) {
                len = spec.getOrder().bitLength(); // does this really return something we expect?
            } else {
                // We support the key, but we don't know the key length
                len = 0;
            }
        } else if (pk instanceof DSAPublicKey) {
            final DSAPublicKey dsapub = (DSAPublicKey) pk;
            if (dsapub.getParams() != null) {
                len = dsapub.getParams().getP().bitLength();
            } else {
                len = dsapub.getY().bitLength();
            }
        }
        return len;
    }
}
