package com.jackob101.hms.converter.user;

import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.core.convert.converter.Converter;

public class PatientFormConverter implements Converter<PatientForm, Patient> {

    private final IUserDetailsService userDetailsService;

    public PatientFormConverter(IUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Patient convert(PatientForm source) {

        ModelMapper modelMapper = new ModelMapper();

        PropertyMap<PatientForm, Patient> propertyMap = new PropertyMap<>() {

            @Override
            protected void configure() {
                skip().setUserDetails(null);
            }

        };
        modelMapper.addMappings(propertyMap);

        Patient model = modelMapper.map(source, Patient.class);

        model.setUserDetails(userDetailsService.find(source.getUserDetailsId()));

        return model;
    }
}
