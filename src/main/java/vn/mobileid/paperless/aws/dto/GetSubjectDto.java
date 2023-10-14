package vn.mobileid.paperless.aws.dto;

import vn.mobileid.paperless.aws.response.AWSResponse;
import vn.mobileid.paperless.aws.response.PersonalResponse;

public class GetSubjectDto extends AWSResponse {
    private PersonalDto personal_informations;

    public PersonalDto getPersonal_informations() {
        return personal_informations;
    }

    public void setPersonal_informations(PersonalDto personal_informations) {
        this.personal_informations = personal_informations;
    }
}
