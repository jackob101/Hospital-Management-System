package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientService extends BaseService<Patient> implements IPatientService {


    public PatientService(PatientRepository patientRepository, ValidationUtils validator) {
        super(validator, Patient.class, patientRepository);
    }
}
