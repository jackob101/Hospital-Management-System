package com.jackob101.hms.unittests.service.allergy;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import com.jackob101.hms.service.allergy.implementation.AllergenService;
import com.jackob101.hms.service.allergy.implementation.AllergyTypeService;
import com.jackob101.hms.service.allergy.implementation.PatientAllergyService;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.unittests.service.base.BaseFormServiceUnitTest;
import com.jackob101.hms.unittests.service.base.TestFormCallbacks;
import com.jackob101.hms.unittests.service.base.TestName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumMap;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PatientAllergyServiceUnitTest extends BaseFormServiceUnitTest<PatientAllergy, PatientAllergyForm, IPatientAllergyService> {

    @Mock
    PatientAllergyRepository repository;

    @Mock
    AllergenService allergenService;

    @Mock
    AllergyTypeService allergyTypeService;

    @Mock
    IPatientService patientService;

    PatientAllergyForm patientAllergyForm;

    Allergen allergen;

    AllergyType allergyType;

    private PatientAllergy patientAllergy;

    @Override
    protected IPatientAllergyService configureService() {
        return new PatientAllergyService(getValidationUtils(), repository, patientService, allergenService, allergyTypeService);
    }

    @Override
    protected PatientAllergy configureEntity() {
        allergen = new Allergen("Test Allergen");
        allergen.setId(1L);

        allergyType = new AllergyType("test Allergy Type");
        allergyType.setId(1L);

        patientAllergy = new PatientAllergy();
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

    @Override
    protected PatientAllergyForm configureForm() {
        patientAllergyForm = new PatientAllergyForm();

        patientAllergyForm.setId(patientAllergy.getId());
        patientAllergyForm.setAllergyTypeId(patientAllergy.getAllergyType().getId());
        patientAllergyForm.setAllergenId(patientAllergy.getAllergen().getId());
        patientAllergyForm.setPatient(1L);
        return patientAllergyForm;
    }

    @Override
    protected void configureFormCallbacks(EnumMap<TestName, TestFormCallbacks<PatientAllergy, PatientAllergyForm>> formCallbacks) {
        TestFormCallbacks<PatientAllergy, PatientAllergyForm> createSuccessfully = new TestFormCallbacks<>();
        createSuccessfully.setBeforeForm((patientAllergy, patientAllergyForm1) -> convertMocks());

        formCallbacks.put(TestName.CREATE_FROM_FORM_SUCCESSFULLY, createSuccessfully);
        formCallbacks.put(TestName.UPDATE_FROM_FORM_SUCCESSFULLY, createSuccessfully);
    }

    private void convertMocks() {
        doReturn(new Patient())
                .when(patientService)
                .find(Mockito.anyLong());

        doReturn(allergen)
                .when(allergenService)
                .find(Mockito.anyLong());

        doReturn(allergyType)
                .when(allergyTypeService)
                .find(Mockito.anyLong());
    }
}