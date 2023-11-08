package vn.mobileid.paperless.controller;

import com.google.gson.Gson;
import org.jmrtd.lds.icao.DG2File;
import org.jmrtd.lds.iso19794.FaceImageInfo;
import org.jmrtd.lds.iso19794.FaceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.mobileid.paperless.aws.dto.CertResponse;
import vn.mobileid.paperless.aws.dto.GetSubjectDto;
import vn.mobileid.paperless.aws.dto.PersonalDto;
import vn.mobileid.paperless.aws.request.*;
import vn.mobileid.paperless.aws.response.PerformResponse;
import vn.mobileid.paperless.aws.response.SubjectResponse;
import vn.mobileid.paperless.service.ElectronicIdService;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/elec")
public class ElectronicController {

    @Autowired
    private ElectronicIdService electronicIdService;

    private Gson gson = new Gson();

    private Logger logger = LoggerFactory.getLogger(ElectronicController.class);

    @PostMapping("/checkPersonalCode")
    public ResponseEntity<?> checkPersonalCode(
            @RequestBody CheckIdentityRequest checkIdentityRequest
            ) throws IOException {
//        String token = electronicIdService.checkPersonalCode(lang, code, type, connectorName);
        SubjectResponse response = electronicIdService.checkExisted(checkIdentityRequest);

        GetSubjectDto getSubjectDto = new GetSubjectDto();
        getSubjectDto.setStatus(response.getStatus());
        getSubjectDto.setMessage(response.getMessage());
        getSubjectDto.setDtis_id(response.getDtis_id());
        if (response.getPersonal_informations() != null) {

            // Giải mã chuỗi Base64 thành mảng byte
            byte[] byteArray = Base64.getDecoder().decode(response.getPersonal_informations().getDg2_value());

            // DG2
            byte[] portrait = null;
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            DG2File dg2File = new DG2File(bais);
            bais.close();
            List<FaceInfo> faceInfos = dg2File.getFaceInfos();
            List<FaceImageInfo> allFaceImageInfos = new ArrayList<>();
            for (FaceInfo faceInfo : faceInfos) {
                allFaceImageInfos.addAll(faceInfo.getFaceImageInfos());
            }
            if (!allFaceImageInfos.isEmpty()) {
                FaceImageInfo faceImageInfo = allFaceImageInfos.iterator().next();
                int imageLength = faceImageInfo.getImageLength();
                DataInputStream dataInputStream = new DataInputStream(faceImageInfo.getImageInputStream());
                byte[] buffer = new byte[imageLength];
                dataInputStream.readFully(buffer, 0, imageLength);
                portrait = buffer;
            }

            String base64Image = Base64.getEncoder().encodeToString(portrait);

            PersonalDto personalDto = new PersonalDto();
            personalDto.setPersonalNumber(response.getPersonal_informations().getPersonal_number());
            personalDto.setFullName(response.getPersonal_informations().getFull_name());
            personalDto.setBirthDate(response.getPersonal_informations().getBirth_date());
            personalDto.setGender(response.getPersonal_informations().getGender());
            personalDto.setNationality(response.getPersonal_informations().getNationality());
            personalDto.setEthnic(response.getPersonal_informations().getEthnic());
            personalDto.setReligion(response.getPersonal_informations().getReligion());
            personalDto.setPlaceOfOrigin(response.getPersonal_informations().getPlace_of_origin());
            personalDto.setPersonalIdentification(response.getPersonal_informations().getPersonal_identification());
            personalDto.setIssuanceDate(response.getPersonal_informations().getIssuance_date());
            personalDto.setExpiryDate(response.getPersonal_informations().getExpiry_date());
            personalDto.setEmail(response.getPersonal_informations().getEmail());
            personalDto.setMobile(response.getPersonal_informations().getMobile());
            personalDto.setDg1(response.getPersonal_informations().getDg1_value());
            personalDto.setDg2(base64Image);
            personalDto.setSubject_id(response.getPersonal_informations().getSubject_id());
            personalDto.setIdentity_document_type(response.getPersonal_informations().getIdentity_document_type());
            getSubjectDto.setPersonal_informations(personalDto);
        }

        return new ResponseEntity<>(getSubjectDto, HttpStatus.OK);
    }

    @PostMapping("/faceAndCreate")
    public ResponseEntity<?> faceAndCreate(
            @RequestBody FaceAndCreateRequest faceAndCreateRequest) throws Exception {
        PerformResponse performResponse = electronicIdService.faceAndCreate(faceAndCreateRequest);
//        PerformResponse performResponse = gson.fromJson(response, PerformResponse.class);
        if (performResponse.getStatus() == 0) {
            performResponse.setJwt(performResponse.getPerform_result().getFinal_result().getClaim_sources().getJWT());
        }
        return new ResponseEntity<>(performResponse, HttpStatus.OK);
    }

    @PostMapping("/updateSubject")
    public ResponseEntity<?> updateSubject(
            @RequestBody UpdateSubjectRequest updateSubjectRequest) throws Exception {

        String response = electronicIdService.updateSubject(updateSubjectRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/processPerForm")
    public ResponseEntity<?> processPerForm(
            @RequestBody ProcessPerFormRequest processPerFormRequest) throws Exception {
        System.out.println("processPerForm");

        PerformResponse performResponse = electronicIdService.processPerForm(processPerFormRequest);
//        PerformResponse performResponse = gson.fromJson(response, PerformResponse.class);
        if (performResponse.getStatus() == 0) {
            performResponse.setJwt(performResponse.getPerform_result().getFinal_result().getClaim_sources().getJWT());
        }
        return new ResponseEntity<>(performResponse, HttpStatus.OK);
    }

    @PostMapping("/processOTPResend")
    public ResponseEntity<?> processOTPResend(
            @RequestBody ProcessPerFormRequest processPerFormRequest) throws Exception {
        System.out.println("processPerForm");
        String response = electronicIdService.processOTPResend(processPerFormRequest);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/checkCertificate")
    public ResponseEntity<?> checkCertificate(
            @RequestBody CheckCertificateRequest checkCertificateRequest) throws Exception {
        System.out.println("checkCertificate");
        List<CertResponse> certResponses = electronicIdService.checkCertificate(checkCertificateRequest);
        return new ResponseEntity<>(certResponses, HttpStatus.OK);
    }

    @PostMapping("/createCertificate")
    public ResponseEntity<?> createCertificate(
            @RequestBody CheckCertificateRequest checkCertificateRequest) throws Throwable {
//        String jwt = "eyJraWQiOiI5NTBhNmM2YTgwZjk0NTVmM2Y3NjU3ZDZmMTUyZGQyMjkwYjk2Y2U3IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJiNWNiZjY2ZS1lMmVlLTQwM2EtYmEzZS04MTk3ZjQzYTBjZmEiLCJtYXRjaF90aHJlc2hvbGQiOjcwLCJkb2N1bWVudF9udW1iZXIiOiIwNzkwODMwMTEzMTUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm1hdGNoX2NvbmZpZGVuY2UiOjk2LCJnZW5kZXIiOiJOYW0iLCJpc3MiOiJodHRwczpcL1wvaWQubW9iaWxlLWlkLnZuIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjp0cnVlLCJwbGFjZV9vZl9yZXNpZGVuY2UiOiI2NDlcLzU4XC81NiDEkGnhu4duIEJpw6puIFBo4bunLCBQaMaw4budbmcgMjUsIELDrG5oIFRo4bqhbmgsIFRQLkjhu5MgQ2jDrSBNaW5oIiwiY2VydGlmaWNhdGVzX3F1ZXJ5X3BhdGgiOiJcL2R0aXNcL3YxXC9lLWlkZW50aXR5XC9jZXJ0aWZpY2F0ZXMiLCJjaXR5X3Byb3ZpbmNlIjoixJBJ4buGTiBCScOKTiIsInBsYWNlX29mX29yaWdpbiI6IlRQLkjhu5MgQ2jDrSBNaW5oIiwibmF0aW9uYWxpdHkiOiJWaeG7h3QgTmFtIiwiaXNzdWluZ19jb3VudHJ5IjoiVmnhu4d0IE5hbSIsIm1hdGNoX3Jlc3VsdCI6dHJ1ZSwibmFtZSI6Ikh14buzbmggUXVhbmcgQ8aw4budbmciLCJwaG9uZV9udW1iZXIiOiI4NDkwMTc5MDc2NyIsImV4cCI6MTY5MTQ3MTI1MiwiaWF0IjoxNjkxNDcwOTUyLCJhc3N1cmFuY2VfbGV2ZWwiOiJFWFRFTkRFRCIsImR0aXNfaWQiOiJJU0FQUC0yMzA4MDgxMjAyMzItNTkyMTE1LTU2OTY2NSIsImp0aSI6IjgwODM5NzAxLWFkN2UtNDljYy1hNzIyLWQ2NWJlMjcxYWUzNCIsImVtYWlsIjoiIiwiZG9jdW1lbnRfdHlwZSI6IkNJVElaRU5DQVJEIn0.cFDH6ndS5rWBUBXC52NcrJ1bmNJcQ4h9tzrLz6ixKHLEmQj_rQdyI5JAoOoURJrgapZjAWmsMja-j4y8xIXz8LRx1YWWFZ3lgaCRZhs_ultrhYk1SjSXSF5Gt-tM9feJrY20BBOwjQ-n90UnUZJH-6J2hG-p4jPn8S1OTcHImkE0P5OX6v1inqplqqwD8z0YBEcj_-OQrInceclui_aJV3WQAQvltNpYOW9e9v60sMGAbIFDB3pCRr92zGZtJz0oNfyfH8KVoHgpr9l2m0J_62eXcuVaQ5BpkiH9RGien5MwgiitfctbKDqxKq904Vi65SkTl68dmvDml0MlSQ2Elg";
        CertResponse response = electronicIdService.createCertificate(checkCertificateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/credentialOTP")
    public ResponseEntity<?> sendOtp(
            @RequestBody CheckCertificateRequest checkCertificateRequest) throws Throwable {
        System.out.println("sendOtp");
        String response = electronicIdService.credentialOTP(checkCertificateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("authorizeOTP")
    public ResponseEntity<?> authorizeOTP(
            @RequestBody AuthorizeOTPRequest authorizeOTPRequest,
            HttpServletRequest request) throws Throwable {
        System.out.println("authorizeOTP ");
        String response = electronicIdService.authorizeOTPFps(authorizeOTPRequest, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
