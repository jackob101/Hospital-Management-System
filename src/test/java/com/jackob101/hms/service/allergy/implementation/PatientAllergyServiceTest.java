package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.user.definition.IPatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class PatientAllergyServiceTest {

    @Mock
    PatientAllergyRepository patientAllergyRepository;

    @Mock
    AllergenService allergenService;

    @Mock
    AllergyTypeService allergyTypeService;

    @Mock
    IPatientService patientService;

    PatientAllergyService patientAllergyService;

    PatientAllergy patientAllergy;

    PatientAllergyForm patientAllergyForm;

    @BeforeEach
    void setUp() {
        openMocks(this);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        patientAllergyService = new PatientAllergyService(validator, patientAllergyRepository, patientService, allergenService, allergyTypeService);

        Allergen allergen = new Allergen("Test Allergen");
        allergen.setId(1L);

        AllergyType testAllergyType = new AllergyType("test Allergy Type");
        testAllergyType.setId(1L);

        patientAllergy = new PatientAllergy();
        patientAllergy.setId(1L);
        patientAllergy.setAllergyType(testAllergyType);
        patientAllergy.setAllergen(allergen);

        patientAllergyForm = new PatientAllergyForm();

        patientAllergyForm.setId(patientAllergy.getId());
        patientAllergyForm.setAllergyTypeId(patientAllergy.getAllergyType().getId());
        patientAllergyForm.setAllergenId(patientAllergy.getAllergen().getId());
    }

    @Test
    void create_patientAllergy_successfully() {

        doAnswer(returnsFirstArg()).when(patientAllergyRepository).save(patientAllergy);

        PatientAllergy saved = patientAllergyService.create(this.patientAllergy);

        assertNotNull(saved);
        assertEquals(patientAllergy.getId(), saved.getId());
    }

    @Test
    void create_patientAllergyAllergenNull_throw() {

        patientAllergy.setAllergen(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.create(patientAllergy));
    }

    @Test
    void create_patientAllergyAllergyTypeNull_throw() {

        patientAllergy.setAllergyType(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.create(patientAllergy));
    }

    @Test
    void create_allergenNull_throwException() {

        assertThrows(RuntimeException.class, () -> patientAllergyService.create(null));
    }

    @Test
    void createFromForm_patientAllergyAllergenIdNull_throwException() {

        patientAllergyForm.setAllergenId(null);

        doThrow(RuntimeException.class).when(allergenService).find(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.createFromForm(patientAllergyForm));
    }

    @Test
    void createFromForm_patientAllergyAllergyTypeIdNull_throwException() {

        patientAllergyForm.setAllergyTypeId(null);

        doThrow(RuntimeException.class).when(allergyTypeService).find(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.createFromForm(patientAllergyForm));
    }

    @Test
    void update_patientAllergy_successfully() {

        doAnswer(returnsFirstArg()).when(patientAllergyRepository).save(patientAllergy);
        doReturn(true).when(patientAllergyRepository).existsById(anyLong());

        PatientAllergy updated = patientAllergyService.update(patientAllergy);

        assertNotNull(updated);
        assertEquals(patientAllergy.getId(), updated.getId());
    }

    @Test
    void update_patientAllergyAllergenNull_throwException() {
        patientAllergy.setAllergen(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.update(patientAllergy));
    }

    @Test
    void update_patientAllergyAllergyTypeNull_throwException() {

        patientAllergy.setAllergyType(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.update(patientAllergy));
    }

    @Test
    void update_patientAllergyIdNull_throwException() {

        patientAllergy.setId(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.update(patientAllergy));
    }

    @Test
    void update_patientAllergyNull_throwException() {

        assertThrows(RuntimeException.class, () -> patientAllergyService.update(null));
    }

    @Test
    void updateFromForm_patientAllergyAllergenIdNull_throwException() {

        patientAllergyForm.setAllergenId(null);

        doThrow(RuntimeException.class).when(allergenService).find(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.updateFromForm(patientAllergyForm));
    }

    @Test
    void updateFromForm_patientAllergyAllergyTypeIdNull_throwException() {

        patientAllergyForm.setAllergyTypeId(null);

        doThrow(RuntimeException.class).when(allergyTypeService).find(null);

        assertThrows(RuntimeException.class, () -> patientAllergyService.updateFromForm(patientAllergyForm));
    }

    @Test
    void delete_id_null() {

        assertThrows(RuntimeException.class, () -> patientAllergyService.delete(null));

    }

    @Test
    void delete_id_failed() {

        doReturn(true, true).when(patientAllergyRepository).existsById(anyLong());

        assertFalse(patientAllergyService.delete(1L));

    }

    @Test
    void delete_id_notFound() {

        assertThrows(RuntimeException.class, () -> patientAllergyService.delete(1L));
    }

    @Test
    void find_id_null() {
        assertThrows(RuntimeException.class, () -> patientAllergyService.find(null));
    }

    @Test
    void find_id_notFound() {

        doReturn(Optional.empty()).when(patientAllergyRepository).findById(anyLong());

        assertThrows(RuntimeException.class, () -> patientAllergyService.find(1L));

    }

    @Test
    void find_id_found() {

        doReturn(Optional.of(patientAllergy)).when(patientAllergyRepository).findById(anyLong());

        PatientAllergy found = patientAllergyService.find(1L);

        assertNotNull(found);
        assertEquals(patientAllergy.getId(), found.getId());

    }
}