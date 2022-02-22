package com.jackob101.hms.unittests.service.allergy.implementation;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.allergy.implementation.AllergenService;
import com.jackob101.hms.service.allergy.implementation.AllergyTypeService;
import com.jackob101.hms.service.allergy.implementation.PatientAllergyService;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientAllergyServiceTest extends BaseServiceTest<PatientAllergy> {

    @Mock
    PatientAllergyRepository patientAllergyRepository;

    @Mock
    AllergenService allergenService;

    @Mock
    AllergyTypeService allergyTypeService;

    @Mock
    IPatientService patientService;

    PatientAllergyService patientAllergyService;

    PatientAllergyForm patientAllergyForm;

    @Override
    protected void configure() {
        PatientAllergyService service = new PatientAllergyService(validationUtils, patientAllergyRepository, patientService, allergenService, allergyTypeService);

        configure(patientAllergyRepository, PatientAllergy.class, service);
    }

    @Override
    protected void setUpData() {

        Allergen allergen = new Allergen("Test Allergen");
        allergen.setId(1L);

        AllergyType testAllergyType = new AllergyType("test Allergy Type");
        testAllergyType.setId(1L);

        PatientAllergy patientAllergy = new PatientAllergy();
        patientAllergy.setId(1L);
        patientAllergy.setAllergyType(testAllergyType);
        patientAllergy.setAllergen(allergen);
        patientAllergy.setPatient(new Patient());

        this.entity = patientAllergy;

        patientAllergyForm = new PatientAllergyForm();

        patientAllergyForm.setId(patientAllergy.getId());
        patientAllergyForm.setAllergyTypeId(patientAllergy.getAllergyType().getId());
        patientAllergyForm.setAllergenId(patientAllergy.getAllergen().getId());
        patientAllergyForm.setPatient(1L);
    }

}