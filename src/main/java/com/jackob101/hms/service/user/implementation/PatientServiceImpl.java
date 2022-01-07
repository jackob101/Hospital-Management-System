package com.jackob101.hms.service.user.implementation;

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
        super(validator, "patient.null", "patient.validation.failed");
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
            throw new HmsException("patient.null");

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
    public boolean delete(Long id) {

        if (id == null)
            throw new HmsException("service.delete.id_null", "Patient");

        boolean isFound = patientRepository.existsById(id);

        if (!isFound)
            throw new HmsException("service.delete.id_not_found", HttpStatus.BAD_REQUEST, "Patient", id);

        patientRepository.deleteById(id);

        return !patientRepository.existsById(id);
    }

    @Override
    public Patient find(Long id) {

        if (id == null)
            throw new HmsException("id.null");

        Optional<Patient> optionalPatient = patientRepository.findById(id);

        return optionalPatient.orElseThrow(() ->
                new HmsException("patient.not_found", HttpStatus.BAD_REQUEST, id));


    }

    @Override
    public List<Patient> findAll() {

        return patientRepository.findAll();
    }


}
