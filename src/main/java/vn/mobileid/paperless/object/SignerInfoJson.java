/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author DELL
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignerInfoJson {

    @JsonProperty("type")
    public String type;
    @JsonProperty("code")
    public String code;
    @JsonProperty("certificate")
    public CertificateObject certificate;
    @JsonProperty("signing_time")
    public String signing_time;
    @JsonProperty("signing_option")
    public String signing_option;
    @JsonProperty("country_code")
    public String country_code;
}
