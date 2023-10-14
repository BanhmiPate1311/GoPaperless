/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.aws.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import vn.mobileid.paperless.aws.datatypes.ValidityResults;

import java.util.List;

/**
 * 2021/08/30
 * @author TuoiCM
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PadesResponse {
    public int status;
    public String message;
    public String dtis_id;
    public List<ValidityResults> validity_results;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDtis_id() {
        return dtis_id;
    }

    public void setDtis_id(String dtis_id) {
        this.dtis_id = dtis_id;
    }

    public List<ValidityResults> getValidity_results() {
        return validity_results;
    }

    public void setValidity_results(List<ValidityResults> validity_results) {
        this.validity_results = validity_results;
    }
    
}
