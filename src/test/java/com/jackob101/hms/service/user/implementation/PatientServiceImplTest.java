package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    PatientRepository patientRepository;

    @InjectMocks
    PatientServiceImpl patientService;

    Patient patient;

    UserDetails userDetails;

    @BeforeEach
    void setUp() {

        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();

        patient = Patient.builder()
                .id(1L)
                .language("Polish")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE)
                .nationality("Polish")
                .userDetails(userDetails)
                .religion("none")
                .build();
    }

    @Test
    void save_patient_when_patient_is_null() {

        assertThrows(RuntimeException.class, () -> patientService.create(null));

    }

    @Test
    void save_patient_when_user_details_is_null() {

        patient.setUserDetails(null);

        assertThrows(RuntimeException.class, () -> patientService.create(patient));

    }

    @Test
    void save_patient_successfully() {

        doAnswer(returnsFirstArg()).when(patientRepository).save(patient);

        Patient save = patientService.create(patient);

        assertNotNull(save);

    }

    @Test
    void update_patient_when_id_is_null() {

        Patient patient = Patient.builder()
                .id(null)
                .build();

        assertThrows(RuntimeException.class, () -> patientService.update(patient));
    }

    @Test
    void update_patient_when_user_details_is_null() {

        Patient patient = Patient.builder()
                .id(1L)
                .userDetails(null)
                .build();

        assertThrows(RuntimeException.class,() -> patientService.update(patient));
    }

    @Test
    void update_patient_when_patient_is_null() {

        assertThrows(RuntimeException.class,() -> patientService.update(null));
    }

    @Test
    void update_patient_successfully() {

        doAnswer(returnsFirstArg()).when(patientRepository).save(any(Patient.class));

        Patient updated = patientService.update(patient);

        assertNotNull(updated);
        assertEquals(patient.getId(), updated.getId());
        assertEquals(patient.getGender(),updated.getGender());
        assertEquals(patient.getNationality(),updated.getNationality());
        assertEquals(patient.getMaritalStatus(),updated.getMaritalStatus());
        assertEquals(patient.getLanguage(),updated.getLanguage());
    }

    @Test
    void update_patient_when_id_is_less_than_zero() {

        patient.setId(-10L);

        assertThrows(RuntimeException.class,() -> patientService.update(patient));
    }

    @Test
    void update_patient_failed() {

        doReturn(null).when(patientRepository).save(any(Patient.class));

        assertThrows(RuntimeException.class,() -> patientService.update(patient));
    }

    @Test
    void delete_patient_when_id_is_null() {
        patient.setId(null);
       assertThrows(RuntimeException.class,() -> patientService.delete(patient));
    }

    @Test
    void delete_patient_when_patient_is_null() {

        assertThrows(RuntimeException.class,() -> patientService.delete(null));

    }

    @Test
    void delete_patient_when_patient_not_found() {

        doReturn(false).when(patientRepository).existsById(anyLong());

        assertThrows(RuntimeException.class,() -> patientService.delete(patient));
    }

    @Test
    void delete_patient_failed() {

        doReturn(true).when(patientRepository).existsById(anyLong());

        assertFalse(patientService.delete(patient));

    }

    @Test
    void delete_patient_successfully() {

        doReturn(true, false).when(patientRepository).existsById(anyLong());

        assertTrue(patientService.delete(patient));

    }

    @Test
    void find_patient_when_id_is_null() {

        assertThrows(RuntimeException.class,() -> patientService.find(null));

    }

    @Test
    void find_patient_when_id_is_less_than_zero() {

        assertThrows(RuntimeException.class,() -> patientService.find(-10L));

    }

    @Test
    void find_patient_when_patient_not_found() {

        doReturn(Optional.empty()).when(patientRepository).findById(anyLong());

        assertThrows(RuntimeException.class,() -> patientService.find(1L));

    }

    @Test
    void find_patient_successfully() {

        doReturn(Optional.of(patient)).when(patientRepository).findById(anyLong());

        assertEquals(patient, patientService.find(1L));
    }
}