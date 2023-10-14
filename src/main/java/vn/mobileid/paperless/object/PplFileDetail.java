package vn.mobileid.paperless.object;

import java.sql.Date;

public class PplFileDetail {
    private int id;
    private int ppl_file_id;
    private int ppl_file_attr_type_id;
    private String value;
    private String hmac;
    private String created_by;
    private Date created_at;
    private String last_modified_by;
    private Date last_modified_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPpl_file_id() {
        return ppl_file_id;
    }

    public void setPpl_file_id(int ppl_file_id) {
        this.ppl_file_id = ppl_file_id;
    }

    public int getPpl_file_attr_type_id() {
        return ppl_file_attr_type_id;
    }

    public void setPpl_file_attr_type_id(int ppl_file_attr_type_id) {
        this.ppl_file_attr_type_id = ppl_file_attr_type_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getLast_modified_by() {
        return last_modified_by;
    }

    public void setLast_modified_by(String last_modified_by) {
        this.last_modified_by = last_modified_by;
    }

    public Date getLast_modified_at() {
        return last_modified_at;
    }

    public void setLast_modified_at(Date last_modified_at) {
        this.last_modified_at = last_modified_at;
    }
}
