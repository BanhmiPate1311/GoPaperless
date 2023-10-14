package vn.mobileid.paperless.aws.response;

public class PerformResponse extends AWSResponse{
    private String jwt;
    private String subject_id;
    private String process_type;
    private PerformResult perform_result;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public PerformResult getPerform_result() {
        return perform_result;
    }

    public void setPerform_result(PerformResult perform_result) {
        this.perform_result = perform_result;
    }
}
