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
public class PostbackJson {

    @JsonProperty("action")
    public String action;
    @JsonProperty("status")
    public String status;
    @JsonProperty("token")
    public String token;
    @JsonProperty("file")
    public String file;
    @JsonProperty("signer")
    public String signer;
    @JsonProperty("file_digest")
    public String file_digest;
    @JsonProperty("valid_to")
    public String valid_to;
    @JsonProperty("signer_info")
    public SignerInfoJson signer_info;
}
