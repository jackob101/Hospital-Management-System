package com.jackob101.hms.converter.allergy;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.user.definition.IPatientService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PatientAllergyFormConverter implements Converter<PatientAllergyForm, PatientAllergy> {

    private final IPatientService patientService;
    private final IAllergenService allergenService;
    private final IAllergyTypeService allergyTypeService;

    public PatientAllergyFormConverter(IPatientService patientService, IAllergenService allergenService, IAllergyTypeService allergyTypeService) {
        this.patientService = patientService;
        this.allergenService = allergenService;
        this.allergyTypeService = allergyTypeService;
    }

    @Override
    public PatientAllergy convert(PatientAllergyForm source) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PatientAllergyForm, PatientAllergy>() {

            @Override
            protected void configure() {
                skip().setPatient(null);
            }

        });

        Patient patient = null;

        if (source.getPatient() != null)
            patient = patientService.find(source.getPatient());

        PatientAllergy mapped = modelMapper.map(source, PatientAllergy.class);


        AllergyType allergyType = allergyTypeService.find(source.getAllergyTypeId());
        Allergen allergen = allergenService.find(source.getAllergenId());

        mapped.setAllergen(allergen);
        mapped.setAllergyType(allergyType);
        mapped.setPatient(patient);

        return mapped;
    }

}
