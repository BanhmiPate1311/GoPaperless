package vn.mobileid.paperless.API;


import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import vn.mobileid.paperless.aws.datatypes.JwtModel;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

public class test {
    private String baseUrl = "https://uat-paperless-gw.mobile-id.vn";
    private int timeOut = 50000;
    public static void main(String[] args) throws ParseException, MalformedURLException {
        String baseUrl = "https://uat-paperless-gw.mobile-id.vn";
        int timeOut = 50000;
        String uploadToken = "18aa470c7c5a8af31b93989b646ece72672c2208";
        String signerToken = "5ef6e380d52f299518d3bc87924db12b590e7052";

        String previewUrl = baseUrl + "/file/" + uploadToken + "/preview/" + signerToken;

        String response = HttpUtilsAWS.invokeHttpRequest(
                new URL(previewUrl),
                "GET",
                timeOut,
                null,
                null);
        System.out.println("createSubject: " + response);
    }
}
