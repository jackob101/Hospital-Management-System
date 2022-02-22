package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import com.jackob101.hms.service.base.BaseFormService;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.validation.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Service;

@Service
public class PatientAllergyService extends BaseFormService<PatientAllergy, PatientAllergyForm> implements IPatientAllergyService {

    private final IPatientService patientService;
    private final IAllergenService allergenService;
    private final IAllergyTypeService allergyTypeService;

    public PatientAllergyService(ValidationUtils validator, PatientAllergyRepository patientAllergyRepository, IPatientService patientService, IAllergenService allergenService, IAllergyTypeService allergyTypeService) {
        super(validator, PatientAllergy.class, patientAllergyRepository);
        this.patientService = patientService;
        this.allergenService = allergenService;
        this.allergyTypeService = allergyTypeService;
    }

    @Override
    public PatientAllergy convert(PatientAllergyForm form) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PatientAllergyForm, PatientAllergy>() {

            @Override
            protected void configure() {
                skip().setPatient(null);
            }

        });

        Patient patient = null;

        if (form.getPatient() != null)
            patient = patientService.find(form.getPatient());

        PatientAllergy mapped = modelMapper.map(form, PatientAllergy.class);


        AllergyType allergyType = allergyTypeService.find(form.getAllergyTypeId());
        Allergen allergen = allergenService.find(form.getAllergenId());

        mapped.setAllergen(allergen);
        mapped.setAllergyType(allergyType);
        mapped.setPatient(patient);

        return mapped;
    }

}
