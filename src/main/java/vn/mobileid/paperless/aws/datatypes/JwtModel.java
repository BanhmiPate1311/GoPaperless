package vn.mobileid.paperless.aws.datatypes;

public class JwtModel {
    private String sub;
    private int match_threshold;
    private int match_confidence;
    private boolean match_result;
    private String gender;
    private String place_of_residence;
    private String place_of_origin;
    private String nationality;
    private String issuing_country;
    private boolean email_verified;
    private String birthdate;
    private String document_number;
    private String expiry_date;
    private String iss;
    private boolean phone_number_verified;
    private String certificates_query_path;
    private String city_province;
    private String issurance_date;
    private String name;
    private String phone_number;
    private String exp;
    private String iat;
    private String dtis_id;
    private String jti;
    private String assurance_level;
    private String email;
    private String document_type;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public boolean getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(boolean email_verified) {
        this.email_verified = email_verified;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public boolean getPhone_number_verified() {
        return phone_number_verified;
    }

    public void setPhone_number_verified(boolean phone_number_verified) {
        this.phone_number_verified = phone_number_verified;
    }

    public String getCertificates_query_path() {
        return certificates_query_path;
    }

    public void setCertificates_query_path(String certificates_query_path) {
        this.certificates_query_path = certificates_query_path;
    }

    public String getCity_province() {
        return city_province;
    }

    public void setCity_province(String city_province) {
        this.city_province = city_province;
    }

    public String getIssurance_date() {
        return issurance_date;
    }

    public void setIssurance_date(String issurance_date) {
        this.issurance_date = issurance_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getIat() {
        return iat;
    }

    public void setIat(String iat) {
        this.iat = iat;
    }

    public String getDtis_id() {
        return dtis_id;
    }

    public void setDtis_id(String dtis_id) {
        this.dtis_id = dtis_id;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getAssurance_level() {
        return assurance_level;
    }

    public void setAssurance_level(String assurance_level) {
        this.assurance_level = assurance_level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public int getMatch_threshold() {
        return match_threshold;
    }

    public void setMatch_threshold(int match_threshold) {
        this.match_threshold = match_threshold;
    }

    public int getMatch_confidence() {
        return match_confidence;
    }

    public void setMatch_confidence(int match_confidence) {
        this.match_confidence = match_confidence;
    }

    public boolean isMatch_result() {
        return match_result;
    }

    public void setMatch_result(boolean match_result) {
        this.match_result = match_result;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPlace_of_residence() {
        return place_of_residence;
    }

    public void setPlace_of_residence(String place_of_residence) {
        this.place_of_residence = place_of_residence;
    }

    public String getPlace_of_origin() {
        return place_of_origin;
    }

    public void setPlace_of_origin(String place_of_origin) {
        this.place_of_origin = place_of_origin;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIssuing_country() {
        return issuing_country;
    }

    public void setIssuing_country(String issuing_country) {
        this.issuing_country = issuing_country;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public boolean isPhone_number_verified() {
        return phone_number_verified;
    }
}
