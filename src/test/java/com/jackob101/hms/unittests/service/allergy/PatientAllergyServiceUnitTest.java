package com.jackob101.hms.unittests.service.allergy;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import com.jackob101.hms.service.allergy.implementation.PatientAllergyService;
import com.jackob101.hms.unittests.service.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
class PatientAllergyServiceUnitTest extends BaseServiceUnitTest<PatientAllergy, IPatientAllergyService> {

    @Mock
    PatientAllergyRepository repository;

    Allergen allergen;

    AllergyType allergyType;

    @Override
    protected IPatientAllergyService configureService() {
        return new PatientAllergyService(getValidationUtils(), repository);
    }

    @Override
    protected PatientAllergy configureEntity() {
        allergen = new Allergen("Test Allergen");
        allergen.setId(1L);

        allergyType = new AllergyType("test Allergy Type");
        allergyType.setId(1L);

        PatientAllergy patientAllergy = new PatientAllergy();
        patientAllergy.setId(1L);
        patientAllergy.setAllergyType(allergyType);
        patientAllergy.setAllergen(allergen);
        patientAllergy.setPatient(new Patient());

        return patientAllergy;
    }

    @Override
    protected JpaRepository<PatientAllergy, Long> configureRepository() {
        return repository;
    }

}