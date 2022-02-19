package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService extends BaseService<Patient> implements IPatientService {

    private final PatientRepository patientRepository;
    private final IUserDetailsService userDetailsService;

    public PatientService(PatientRepository patientRepository, IUserDetailsService userDetailsService, Validator validator) {
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
            throw HmsException.code("Could not create patient because given entity is null");

        UserDetails userDetails = userDetailsService.find(userDetailsId);
        patient.setUserDetails(userDetails);

        return create(patient);
    }

    @Override
    public Patient createFromForm(PatientForm patientForm) {

        return create(convert(patientForm));
    }


    @Override
    public Patient update(Patient patient) {

        validate(patient, OnUpdate.class);

        return create(patient);
    }

    @Override
    public Patient updateFromForm(PatientForm patientForm) {

        return update(convert(patientForm));
    }

    @Override
    public boolean delete(Long id) {

        if (id == null)
            throw HmsException.code("Couldn't delete Patient because given ID is null");

        boolean isFound = patientRepository.existsById(id);

        if (!isFound)
            throw HmsException.params(id).code("Couldn't delete Patient because entity with ID %s was not found");

        patientRepository.deleteById(id);

        return !patientRepository.existsById(id);
    }

    @Override
    public Patient find(Long id) {

        if (id == null)
            throw HmsException.code("Couldn't delete Patient because ID was null");

        Optional<Patient> optionalPatient = patientRepository.findById(id);

        return optionalPatient.orElseThrow(() ->
                HmsException.badRequest().params(id).code("Couldn't find Patient with ID %s"));


    }

    @Override
    public List<Patient> findAll() {

        return patientRepository.findAll();
    }

    @Override
    public Patient convert(PatientForm patientForm) {

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<PatientForm, Patient>() {

            @Override
            protected void configure() {
                skip().setUserDetails(null);
            }

        });

        Patient model = modelMapper.map(patientForm, Patient.class);

        model.setUserDetails(userDetailsService.find(patientForm.getUserDetailsId()));

        return model;

    }

}
