package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.service.user.implementation.SpecializationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceTest {


    @Mock
    SpecializationRepository specializationRepository;

    SpecializationService specializationService;

    Specialization specialization;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        specializationService = new SpecializationService(specializationRepository, validator);

        specialization = new Specialization();

        specialization.setName("Doctor");
        specialization.setId(1L);

    }

    @Test
    void create_specialization_successfully() {
        doAnswer(returnsFirstArg()).when(specializationRepository).save(specialization);

        assertNotNull(specializationService.create(specialization));
    }

    @Test
    void create_specialization_validationError() {

        specialization.setName("");

        assertThrows(HmsException.class, () -> specializationService.create(specialization));
    }

    @Test
    void create_specialization_null() {

        assertThrows(HmsException.class, () -> specializationService.create(null));
    }

    @Test
    void update_specialization_successfully() {

        doAnswer(returnsFirstArg()).when(specializationRepository).save(specialization);
        doReturn(true).when(specializationRepository).existsById(anyLong());

        Specialization updated = specializationService.update(specialization);
        assertEquals(specialization.getId(), updated.getId());
        assertEquals(specialization.getName(), updated.getName());
    }

    @Test
    void update_specialization_validationError() {

        specialization.setId(null);

        assertThrows(HmsException.class, () -> specializationService.update(specialization));
    }

    @Test
    void update_specialization_null() {

        assertThrows(HmsException.class, () -> specializationService.update(null));
    }

    @Test
    void find_specialization_successfully() {

        doReturn(Optional.of(specialization)).when(specializationRepository).findById(anyLong());

        Specialization found = specializationService.find(1L);

        assertEquals(this.specialization.getName(), found.getName());
        assertEquals(specialization.getId(), found.getId());
    }

    @Test
    void find_specialization_idNull() {

        assertThrows(HmsException.class, () -> specializationService.find(null));
    }

    @Test
    void find_specialization_failed() {

        doReturn(Optional.empty()).when(specializationRepository).findById(anyLong());

        assertThrows(HmsException.class, () -> specializationService.find(1L));
    }

}