package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.exceptions.ExceptionCode;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.PatientService;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl extends BaseService<Patient> implements PatientService {

    private final PatientRepository patientRepository;
    private final UserDetailsService userDetailsService;

    public PatientServiceImpl(PatientRepository patientRepository, UserDetailsService userDetailsService, Validator validator) {
        super(validator, "Patient cannot be null", "Patient validation failed", ExceptionCode.PATIENT_VALIDATION_ERROR);
        this.patientRepository = patientRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Patient create(Patient entity) {

        validate(entity, OnCreate.class);

        return patientRepository.save(entity);
    }

    @Override
    public Patient create(Patient patient, Long userDetailsId) {

        if (patient == null)
            throw new HmsException("Patient cannot be null", ExceptionCode.PATIENT_VALIDATION_ERROR, HttpStatus.BAD_REQUEST);

        UserDetails userDetails = userDetailsService.find(userDetailsId);
        patient.setUserDetails(userDetails);

        return create(patient);
    }

    @Override
    public Patient update(Patient patient) {

        validate(patient, OnUpdate.class);

        return create(patient);
    }

    @Override
    public boolean delete(Patient patient) {

        boolean isFound = patientRepository.existsById(patient.getId());

        if (!isFound)
            throw new RuntimeException("Patient not found");

        patientRepository.delete(patient);

        return !patientRepository.existsById(patient.getId());
    }

    @Override
    public Patient find(Long id) {

        if (id == null)
            throw new HmsException("Id cannot be null", ExceptionCode.PATIENT_ID_NULL, HttpStatus.BAD_REQUEST);

        Optional<Patient> optionalPatient = patientRepository.findById(id);

        return optionalPatient.orElseThrow(() ->
                new HmsException("Patient with id: " + id + " was not found",
                        ExceptionCode.PATIENT_NOT_FOUND,
                        HttpStatus.BAD_REQUEST));

    }

    @Override
    public List<Patient> findAll() {

        return patientRepository.findAll();
    }


}
