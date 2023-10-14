package vn.mobileid.paperless.aws.response;

public class SubjectResponse extends AWSResponse {
    private PersonalResponse personal_informations;

    public PersonalResponse getPersonal_informations() {
        return personal_informations;
    }

    public void setPersonal_informations(PersonalResponse personal_informations) {
        this.personal_informations = personal_informations;
    }
}
