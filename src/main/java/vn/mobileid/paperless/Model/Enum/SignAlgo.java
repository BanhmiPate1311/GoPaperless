package vn.mobileid.paperless.Model.Enum;

import com.google.gson.annotations.SerializedName;

public enum SignAlgo {
    @SerializedName("1.2.840.113549.1.1.1")
    RSA("1.2.840.113549.1.1.1"),
    @SerializedName("1.2.840.113549.1.1.5")
    RSA_SHA1("1.2.840.113549.1.1.5"),
    @SerializedName("1.2.840.113549.1.1.11")
    RSA_SHA256("1.2.840.113549.1.1.11"),
    @SerializedName("1.2.840.113549.1.1.12")
    SHA_384("1.2.840.113549.1.1.12"),
    @SerializedName("1.2.840.113549.1.1.13")
    SHA_512("1.2.840.113549.1.1.13"),;
    public final String Value;

    private SignAlgo(String Value) {
        this.Value = Value;
    }
}
