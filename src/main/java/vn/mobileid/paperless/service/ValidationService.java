package vn.mobileid.paperless.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.mobileid.paperless.API.GatewayAPI;
import vn.mobileid.paperless.aws.dto.ValidViewDto;
import vn.mobileid.paperless.aws.request.ValidationResquest;

@Service
public class ValidationService {
    @Autowired
    private GatewayAPI gatewayAPI;

    public ValidViewDto getView(ValidationResquest validationResquest) {
        return gatewayAPI.ValidView(validationResquest);
    }
}
