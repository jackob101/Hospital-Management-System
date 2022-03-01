package com.jackob101.hms.api.allergy;

import com.jackob101.hms.api.BaseController;
import com.jackob101.hms.converter.allergy.PatientAllergyFormConverter;
import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = PatientAllergyApi.REQUEST_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
public class PatientAllergyApi extends BaseController<PatientAllergy, PatientAllergyForm> {

    public final static String REQUEST_MAPPING = "patient_allergies";

    public PatientAllergyApi(IPatientAllergyService patientAllergyService, PatientAllergyFormConverter converter) {
        super(patientAllergyService, REQUEST_MAPPING, converter);
    }

}
