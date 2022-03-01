package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientAllergyService extends BaseService<PatientAllergy> implements IPatientAllergyService {

    public PatientAllergyService(ValidationUtils validator, PatientAllergyRepository patientAllergyRepository) {
        super(validator, PatientAllergy.class, patientAllergyRepository);
    }

}
