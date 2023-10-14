/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.utils;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Mr Spider
 */
public class CommonHash {

    final public static String HASH_SHA1 = "SHA-1";
    final public static String HASH_SHA256 = "SHA-256";
    final public static String HASH_SHA384 = "SHA-384";
    final public static String HASH_SHA512 = "SHA-512";

//    public static void main(String[] args) throws Exception {
//        String signerToken = "daf4f4875bd52319dd608df60eb276bcdc762250";
//        System.out.println("SIGNATURE_ID: " + "S-" + hashPass(signerToken.getBytes()));
//    }

    public static String hashPass(byte[] data) {
        return DatatypeConverter.printHexBinary(hashData(hashData(data, HASH_SHA256), HASH_SHA256));
    }

    public static byte[] hashData(byte[] data, String algorithm) {
        byte[] result = null;
        try {
            if (!algorithm.equals(HASH_SHA256)
                    && !algorithm.equals(HASH_SHA384)) {
                algorithm = HASH_SHA1;
            }
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            result = md.digest();
        } catch (NoSuchAlgorithmException e) {
//            LogExceptionServlet(log, "No Such Algorithm Exception. Details: " + e.toString(), e);
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] hashPass(String input) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    public static String toHexString(byte[] hash)
    {
        BigInteger number = new BigInteger(1, hash);
        StringBuilder hexString = new StringBuilder(number.toString(16));
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}
