package vn.mobileid.paperless.Model.Enum;

import com.google.gson.annotations.SerializedName;

public enum HashAlgorithmOID {
    @SerializedName("1.3.14.3.2.26")
    SHA_1("1.3.14.3.2.26"),
    @SerializedName("2.16.840.1.101.3.4.2.1")
    SHA_256("2.16.840.1.101.3.4.2.1"),
    @SerializedName("2.16.840.1.101.3.4.2.2")
    SHA_384("2.16.840.1.101.3.4.2.2"),
    @SerializedName("2.16.840.1.101.3.4.2.3")
    SHA_512("2.16.840.1.101.3.4.2.3"),;
    public final String name;

    private HashAlgorithmOID(String Value) {
        this.name = Value;
    }
}
