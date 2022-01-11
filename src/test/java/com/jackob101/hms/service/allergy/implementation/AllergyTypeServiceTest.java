package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class AllergyTypeServiceTest {

    @Mock
    AllergyTypeRepository allergyTypeRepository;

    AllergyTypeService allergyTypeService;

    AllergyType allergyType;

    @BeforeEach
    void setUp() {
        openMocks(this);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        allergyTypeService = new AllergyTypeService(validator, allergyTypeRepository);

        allergyType = new AllergyType("Test allergen");
        allergyType.setId(1L);
    }

    @Test
    void create_allergyType_successfully() {

        doAnswer(returnsFirstArg()).when(allergyTypeRepository).save(allergyType);

        AllergyType saved = allergyTypeService.create(this.allergyType);

        assertNotNull(saved);
        assertEquals(allergyType.getName(), saved.getName());
        assertEquals(allergyType.getId(), saved.getId());
    }

    @Test
    void create_allergyTypeNameBlank_throw() {

        allergyType.setName("");

        assertThrows(RuntimeException.class, () -> allergyTypeService.create(allergyType));
    }

    @Test
    void create_allergyTypeNameNull_throw() {

        allergyType.setName(null);

        assertThrows(RuntimeException.class, () -> allergyTypeService.create(allergyType));
    }

    @Test
    void create_allergyTypeNull_throwException() {

        assertThrows(RuntimeException.class, () -> allergyTypeService.create(null));
    }

    @Test
    void update_allergyType_successfully() {

        doAnswer(returnsFirstArg()).when(allergyTypeRepository).save(allergyType);
        doReturn(true).when(allergyTypeRepository).existsById(anyLong());

        AllergyType updated = allergyTypeService.update(allergyType);

        assertNotNull(updated);
        assertEquals(allergyType.getId(), updated.getId());
        assertEquals(allergyType.getName(), updated.getName());
    }

    @Test
    void update_allergyTypeNameBlank_throwException() {
        allergyType.setName("");

        assertThrows(RuntimeException.class, () -> allergyTypeService.update(allergyType));
    }

    @Test
    void update_allergyTypeNameNull_throwException() {

        allergyType.setName(null);

        assertThrows(RuntimeException.class, () -> allergyTypeService.update(allergyType));
    }

    @Test
    void update_allergyTypeIdNull_throwException() {

        allergyType.setId(null);

        assertThrows(RuntimeException.class, () -> allergyTypeService.update(allergyType));
    }

    @Test
    void update_allergyTypeNull_throwException() {

        assertThrows(RuntimeException.class, () -> allergyTypeService.update(null));
    }

    @Test
    void delete_id_null() {

        assertThrows(RuntimeException.class, () -> allergyTypeService.delete(null));

    }

    @Test
    void delete_id_failed() {

        doReturn(true, true).when(allergyTypeRepository).existsById(anyLong());

        assertFalse(allergyTypeService.delete(1L));

    }

    @Test
    void delete_id_notFound() {

        assertThrows(RuntimeException.class, () -> allergyTypeService.delete(1L));
    }

    @Test
    void find_id_null() {
        assertThrows(RuntimeException.class, () -> allergyTypeService.find(null));
    }

    @Test
    void find_id_notFound() {

        doReturn(Optional.empty()).when(allergyTypeRepository).findById(anyLong());

        assertThrows(RuntimeException.class, () -> allergyTypeService.find(1L));

    }

    @Test
    void find_id_found() {

        doReturn(Optional.of(allergyType)).when(allergyTypeRepository).findById(anyLong());

        AllergyType found = allergyTypeService.find(1L);

        assertNotNull(found);
        assertEquals(allergyType.getId(), found.getId());
        assertEquals(allergyType.getName(), found.getName());

    }
}