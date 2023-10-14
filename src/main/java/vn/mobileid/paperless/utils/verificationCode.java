/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.utils;

import vn.mobileid.exsig.Algorithm;
import vn.mobileid.exsig.PdfProfileCMS;
import vn.mobileid.paperless.API.SigningMethodAsyncImp;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author PHY
 */
public class verificationCode {

    // Tính mã hash
    public String getValueSignHash(String base64String) throws IOException, Exception {
        PdfProfileCMS profile = new PdfProfileCMS(Algorithm.SHA256);
//        File f = new File("file\\test.pdf");
//        byte[] fileContent = FileUtils.readFileToByteArray(f);

        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        List<byte[]> src = new ArrayList<>();
        src.add(decodedBytes);

        SigningMethodAsyncImp signInit = new SigningMethodAsyncImp();
        byte[] temporalData = profile.createTemporalFile(signInit, src);
        List<String> hashList = signInit.hashList;
        System.out.print("hashList: " + hashList.get(0));
        return hashList.get(0);
    }

    public static String computeVC(List<byte[]> hashesList) throws NoSuchAlgorithmException {

        byte[][] hashes = new byte[hashesList.size()][];
        for (int i = 0; i < hashesList.size(); i++) {
            hashes[i] = hashesList.get(i);
        }
        if (hashes == null || hashes.length == 0) {
            throw new RuntimeException("The input is null or empty");
        }
        //single hash
        byte[] vcData = new byte[hashes[0].length];
        System.arraycopy(hashes[0], 0, vcData, 0, vcData.length);

        if (hashes.length > 1) {
            padding(hashes);

            for (int ii = 1; ii < hashes.length; ii++) {
                if (hashes[ii].length > vcData.length) {
                    byte[] tmp = new byte[hashes[ii].length];
                    System.arraycopy(vcData, 0, tmp, 0, vcData.length);
                    for (int ttt = vcData.length; ttt < hashes[ii].length; ttt++) {
                        tmp[ttt] = (byte) 0xFF;
                    }
                    vcData = new byte[tmp.length];
                    System.arraycopy(tmp, 0, vcData, 0, tmp.length);
                }
                for (int idx = 0; idx < hashes[ii].length; idx++) {
                    vcData[idx] |= hashes[ii][idx];
                }
            }
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(vcData);
        byte[] vc = md.digest();
        short first = (short) (vc[0] << 8 | vc[1] & 0x00FF);
        short last = (short) (vc[vc.length - 2] << 8 | vc[vc.length - 1] & 0x00FF);
        return String.format("%04X-%04X", first, last);
    }

    public static byte[][] padding(byte[][] hashes) {
        int max = findMaxLen(hashes);
        byte[][] rsp = new byte[hashes.length][];

        for (int idx = 0; idx < hashes.length; idx++) {
            int len = hashes[idx].length;
            if (len < max) {
                byte[] tmp = new byte[len];
                System.arraycopy(hashes[idx], 0, tmp, 0, len);
                hashes[idx] = new byte[max];
                System.arraycopy(tmp, 0, hashes[idx], 0, len);
                for (int ii = len; ii < max; ii++) {
                    hashes[idx][ii] = (byte) 0xFF;
                }
            }
        }
        return rsp;
    }

    private static int findMaxLen(byte[][] hashes) {
        int max = 0;
        for (byte[] hh : hashes) {
            if (max < hh.length) {
                max = hh.length;
            }
        }
        return max;
    }

    public static byte[] base64Decode(String s) {
        return Base64.getMimeDecoder().decode(s);
    }

}
