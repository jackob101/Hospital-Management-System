package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.base.BaseFormService;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.validation.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

@Service
public class PatientService extends BaseFormService<Patient, PatientForm> implements IPatientService {

    private final IUserDetailsService userDetailsService;

    public PatientService(PatientRepository patientRepository, IUserDetailsService userDetailsService, ValidationUtils validator) {
        super(validator, Patient.class, patientRepository);
        this.userDetailsService = userDetailsService;
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
