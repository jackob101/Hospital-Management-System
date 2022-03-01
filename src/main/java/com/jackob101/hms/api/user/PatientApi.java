package com.jackob101.hms.api.user;

import com.jackob101.hms.api.base.BaseController;
import com.jackob101.hms.converter.user.PatientFormConverter;
import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(PatientApi.REQUEST_MAPPING)
@RestController
public class PatientApi extends BaseController<Patient, PatientForm> {

    public static final String REQUEST_MAPPING = "patients";

    public PatientApi(IPatientService service, PatientFormConverter converter) {
        super(service, REQUEST_MAPPING, converter);
    }
}
