/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.uat;

import vn.mobileid.exsig.Algorithm;
import vn.mobileid.exsig.PdfProfileCMS;
import vn.mobileid.paperless.API.SigningMethodAsyncImp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author PHY
 */
public class tínhHash {

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
}
