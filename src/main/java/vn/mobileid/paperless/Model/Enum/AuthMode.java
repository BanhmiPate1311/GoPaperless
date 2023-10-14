package vn.mobileid.paperless.Model.Enum;

import com.google.gson.annotations.SerializedName;

public enum AuthMode {

    @SerializedName("EXPLICIT/PIN")
    EXPLICIT_PIN("EXPLICIT/PIN"),
    @SerializedName("EXPLICIT/OTP-SMS")
    EXPLICIT_OTP_SMS("EXPLICIT/OTP-SMS"),
    @SerializedName("EXPLICIT/OTP-EMAIL")
    EXPLICIT_OTP_EMAIL("EXPLICIT/OTP-EMAIL"),
    @SerializedName("IMPLICIT/TSE")
    IMPLICIT_TSE("IMPLICIT/TSE"),
    @SerializedName("IMPLICIT/BIP-CATTP")
    IMPLICIT_BIP_CATTP("IMPLICIT/BIP-CATTP"),;

    public final String Value;

    private AuthMode(String Value) {
        this.Value = Value;
    }
}
