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
public class CertificateObject {

    @JsonProperty("subject")
    public String subject;
    @JsonProperty("issuer")
    public String issuer;
    @JsonProperty("valid_from")
    public String valid_from;
    @JsonProperty("value")
    public String value;
    @JsonProperty("valid_to")
    public String valid_to;
    @JsonProperty("name")
    public String name;
    @JsonProperty("type")
    public String type;
}
