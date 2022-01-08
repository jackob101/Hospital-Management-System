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
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl extends BaseService<Patient> implements PatientService {

    private final PatientRepository patientRepository;
    private final UserDetailsService userDetailsService;

    public PatientServiceImpl(PatientRepository patientRepository, UserDetailsService userDetailsService, Validator validator) {
        super(validator, "Patient");
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
            throw HmsException.params("Patient").code("service.create.entity_null");

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
            throw HmsException.params("Patient").code("service.delete.id_null");

        boolean isFound = patientRepository.existsById(id);

        if (!isFound)
            throw HmsException.badRequest().params("Patient", id + "").code("service.delete.id_not_found");

        patientRepository.deleteById(id);

        return !patientRepository.existsById(id);
    }

    @Override
    public Patient find(Long id) {

        if (id == null)
            throw HmsException.badRequest().params("Patient").code("service.find.id_null");

        Optional<Patient> optionalPatient = patientRepository.findById(id);

        return optionalPatient.orElseThrow(() ->
                HmsException.badRequest().params("Patient", id).code("service.find.not_found"));


    }

    @Override
    public List<Patient> findAll() {

        return patientRepository.findAll();
    }


}
