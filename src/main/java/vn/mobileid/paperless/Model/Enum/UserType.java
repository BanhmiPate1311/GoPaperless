package vn.mobileid.paperless.Model.Enum;

import com.google.gson.annotations.SerializedName;

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
    TAX_CODE("TAX-CODE"),;
    
    
    public final String name;

    private UserType(String name) {
        this.name = name;
    }
    
}
