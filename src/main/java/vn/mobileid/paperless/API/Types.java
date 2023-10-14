package vn.mobileid.paperless.API;

import com.google.gson.annotations.SerializedName;

public final class Types {
    public enum Function {
        INFO,
        AUTH_LOGIN,
        AUTH_LOGIN_SSL_ONLY,
        DEFAULT
    }
    
    public enum OTPType {
        EMAIL,
        SMS,
    }
    
    public enum SessionType {
        SERVER,
        USER,
    }
    
    public enum UserType {
        @SerializedName("USERNAME")
        USERNAME("USERNAME"),
        @SerializedName("PERSONAL-ID")
        PERSONAL_ID("PERSONAL-ID"),
        @SerializedName("PASSPORT-ID")
        PASSPORT_ID("PASSPORT-ID"),
        @SerializedName("CITIZEN-IDENTITY-CARD")
        CITIZEN_IDENTITY_CARD("CITIZEN-IDENTITY-CARD"),
        @SerializedName("BUDGET-CODE")
        BUDGET_CODE("BUDGET-CODE"),
        @SerializedName("TAX-CODE")
        TAX_CODE("TAX-CODE"),
        @SerializedName("UUID")
        UUID("UUID");

        private final String value;

        public String getValue() {
            return this.value;
        }

        private UserType(String value) {
            this.value = value;
        }
    }
    
    public enum IdentificationType {
        @SerializedName("PERSONAL-ID")
        PERSONAL_ID("PERSONAL-ID"),
        @SerializedName("PASSPORT-ID")
        PASSPORT_ID("PASSPORT-ID"),
        @SerializedName("CITIZEN-IDENTITY-CARD")
        CITIZEN_IDENTITY_CARD("CITIZEN-IDENTITY-CARD"),
        @SerializedName("BUDGET-CODE")
        BUDGET_CODE("BUDGET-CODE"),
        @SerializedName("TAX-CODE")
        TAX_CODE("TAX-CODE"),
        @SerializedName("DECISION-CODE")
        DECISION_CODE("DECISION-CODE"), //  DECISION_CODE("DECISION-CODE", "ENTERPRISE_ID", "DEC:", "QĐ:", "", "MST"),
        @SerializedName("SOCIAL-INSURANCE") 
        SOCIAL_INSURANCE("SOCIAL-INSURANCE"), //SOCIAL_INSURANCE("SOCIAL-INSURANCE", "ENTERPRISE_ID", "SIC:", "BHXH:", "SOCIALINSURANCE", "MST"),
        @SerializedName("UNIT-CODE")
        UNIT_CODE("UNIT-CODE"); // UNIT_CODE("UNIT-CODE", "ENTERPRISE_ID", " UNC:", "MDV:", "UNITCODE", "MST"),

        private final String value;

        public String getValue() {
            return this.value;
        }

        private IdentificationType(String value) {
            this.value = value;
        }
    }
    
    public enum HashAlgorithmOID {
        @SerializedName("1.3.14.3.2.26")
        SHA_1("1.3.14.3.2.26"),
        @SerializedName("2.16.840.1.101.3.4.2.1")
        SHA_256("2.16.840.1.101.3.4.2.1"),
        @SerializedName("2.16.840.1.101.3.4.2.2")
        SHA_384("2.16.840.1.101.3.4.2.2"),
        @SerializedName("2.16.840.1.101.3.4.2.3")
        SHA_512("2.16.840.1.101.3.4.2.3");

        private final String value;

        public String getValue() {
            return this.value;
        }

        private HashAlgorithmOID(String value) {
            this.value = value;
        }
    }
    
    public enum SignAlgo {
        @SerializedName("1.2.840.113549.1.1.1")
        RSA("1.2.840.113549.1.1.1"),
        @SerializedName("1.2.840.113549.1.1.5")
        RSA_SHA1("1.2.840.113549.1.1.5"),
        @SerializedName("1.2.840.113549.1.1.11")
        RSA_SHA256("1.2.840.113549.1.1.11"),
        @SerializedName("1.2.840.113549.1.1.12")
        RSA_SHA384("1.2.840.113549.1.1.12"),
        @SerializedName("1.2.840.113549.1.1.13")
        RSA_SHA512("1.2.840.113549.1.1.13");

        private final String value;

        public String getValue() {
            return this.value;
        }

        private SignAlgo(String value) {
            this.value = value;
        }
    }
    
    public enum OperationMode {
        S,
        A
    }
    
    public enum AuthMode {
        @SerializedName("EXPLICIT/PIN")
        EXPLICIT_PIN("EXPLICIT/PIN"),
        @SerializedName("EXPLICIT/OTP-SMS")
        EXPLICIT_OTP_SMS("EXPLICIT/OTP-SMS"),
        @SerializedName("EXPLICIT/OTP-EMAIL")
        EXPLICIT_OTP_EMAIL("EXPLICIT/OTP-EMAIL"),
        @SerializedName("IMPLICIT/TSE")
        IMPLICIT_TSE("IMPLICIT/TSE");
//        @SerializedName("IMPLICIT/BIP-CATTP")
//        IMPLICIT_BIP_CATTP("IMPLICIT/BIP-CATTP"),;

        private final String value;

        public String getValue() {
            return this.value;
        }

        private AuthMode(String value) {
            this.value = value;
        }
    }
    
    public enum SharedMode {
        PRIVATE_MODE,
        AGREEMENT_SHARED_MODE
    }
    
    public enum SignatureFormat {
        C,
        X,
        P,
    }
    
    public enum ConformanceLevel {
        @SerializedName("AdES-B-B")
        B_B("AdES-B-B"),
        @SerializedName("AdES-B-T")
        B_T("AdES-B-T"),
        @SerializedName("AdES-B-LT")
        B_LT("AdES-B-LT"),
        @SerializedName("AdES-B-LTA")
        B_LTA("AdES-B-LTA"),
        @SerializedName("AdES-B")
        B("AdES-B"),
        @SerializedName("AdES-T")
        T("AdES-T"),
        @SerializedName("AdES-LT")
        LT("AdES-LT"),
        @SerializedName("AdES-LTA")
        LTA("AdES-LTA");

        private final String value;

        public String getValue() {
            return this.value;
        }

        private ConformanceLevel(String value) {
            this.value = value;
        }
    }
    
    public enum SignedPropertyType {
        APPEARANCES,
        VISIBLESIGNATURE,
        VISUALSTATUS,
        //IMAGEANDTEXT,
        TEXTALIGNMENT,
        TEXTDIRECTION,
        TEXTCOLOR,
        TEXTSIZE,
        SHOWSIGNERINFO,
        SIGNERINFOPREFIX,
        SHOWDATETIME,
        DATETIMEPREFIX,
        DATETIMEFORMAT,
        SHOWREASON,
        SIGNREASONPREFIX,
        REASON,
        SHOWLOCATION,
        LOCATIONPREFIX,
        LOCALTION,
        SHOWTELEPHONE,
        TELEPHONEPREFIX,
        SHOWENTERPRISEID,
        ENTERPRISEIDPREFIX,
        SHOWPERSONALID,
        PERSONALIDPREFIX,
        SHOWSERIALNUMBER,
        SERIALNUMBERPREFIX,
        SHOWORGANIZATION,
        ORGANIZATIONPREFIX,
        SHOWTITLE,
        TITLEPREFIX,
        SHOWISSUERINFO,
        ISSUERINFOPREFIX,
        ONLYCOUNTERSIGNENABLED,
        BACKGROUNDIMAGE,
        IMAGE,
        //PAGESFORINITIALSIGNATURE,
        //SHADOWSIGNATUREPROPERTIES,
        PDFPASSWORD,
        MIMETYPE,
        FILENAME,
    }
    
    public enum ImageTextDirection {
        LEFTTORIGHT,
        RIGHTTOLEFT,
        TOPTOBOTTOM,
        BOTTOMTOTOP,
        OVERLAP
    }
    
    public enum TextAlignment {
        LEFT,
        CENTER,
        RIGHT,
        JUSTIFIED,
        JUSTIFIED_ALL
    }
    
    public enum Color {
        yellow,
        blue,
        cyan,
        dark_gray,
        gray,
        green,
        light_gray,
        magenta,
        orange,
        pink,
        red,
        white
    }
}
