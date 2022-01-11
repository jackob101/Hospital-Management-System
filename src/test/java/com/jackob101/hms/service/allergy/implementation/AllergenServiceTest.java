package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
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
class AllergenServiceTest {

    @Mock
    AllergenRepository allergenRepository;

    AllergenService allergenService;

    Allergen allergen;

    @BeforeEach
    void setUp() {
        openMocks(this);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        allergenService = new AllergenService(validator, allergenRepository);

        allergen = new Allergen("Test allergen");
        allergen.setId(1L);
    }

    @Test
    void create_allergen_successfully() {

        doAnswer(returnsFirstArg()).when(allergenRepository).save(allergen);

        Allergen saved = allergenService.create(this.allergen);

        assertNotNull(saved);
        assertEquals(allergen.getName(), saved.getName());
        assertEquals(allergen.getId(), saved.getId());
    }

    @Test
    void create_allergenNameBlank_throw() {

        allergen.setName("");

        assertThrows(RuntimeException.class, () -> allergenService.create(allergen));
    }

    @Test
    void create_allergenNameNull_throw() {

        allergen.setName(null);

        assertThrows(RuntimeException.class, () -> allergenService.create(allergen));
    }

    @Test
    void create_allergenNull_throwException() {

        assertThrows(RuntimeException.class, () -> allergenService.create(null));
    }

    @Test
    void update_allergen_successfully() {

        doAnswer(returnsFirstArg()).when(allergenRepository).save(allergen);
        doReturn(true).when(allergenRepository).existsById(anyLong());

        Allergen updated = allergenService.update(allergen);

        assertNotNull(updated);
        assertEquals(allergen.getId(), updated.getId());
        assertEquals(allergen.getName(), updated.getName());
    }

    @Test
    void update_allergenNameBlank_throwException() {
        allergen.setName("");

        assertThrows(RuntimeException.class, () -> allergenService.update(allergen));
    }

    @Test
    void update_allergenNameNull_throwException() {

        allergen.setName(null);

        assertThrows(RuntimeException.class, () -> allergenService.update(allergen));
    }

    @Test
    void update_allergenIdNull_throwException() {

        allergen.setId(null);

        assertThrows(RuntimeException.class, () -> allergenService.update(allergen));
    }

    @Test
    void update_allergenNull_throwException() {

        assertThrows(RuntimeException.class, () -> allergenService.update(null));
    }

    @Test
    void delete_id_null() {

        assertThrows(RuntimeException.class, () -> allergenService.delete(null));

    }

    @Test
    void delete_id_failed() {

        doReturn(true, true).when(allergenRepository).existsById(anyLong());

        assertFalse(allergenService.delete(1L));

    }

    @Test
    void delete_id_notFound() {

        assertThrows(RuntimeException.class, () -> allergenService.delete(1L));
    }

    @Test
    void find_id_null() {
        assertThrows(RuntimeException.class, () -> allergenService.find(null));
    }

    @Test
    void find_id_notFound() {

        doReturn(Optional.empty()).when(allergenRepository).findById(anyLong());

        assertThrows(RuntimeException.class, () -> allergenService.find(1L));

    }

    @Test
    void find_id_found() {

        doReturn(Optional.of(allergen)).when(allergenRepository).findById(anyLong());

        Allergen found = allergenService.find(1L);

        assertEquals(allergen.getId(), found.getId());
        assertEquals(allergen.getName(), found.getName());

    }
}