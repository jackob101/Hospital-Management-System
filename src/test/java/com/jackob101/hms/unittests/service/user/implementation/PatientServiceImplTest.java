package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.PatientService;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(ValidationAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest extends BaseServiceTest<Patient, IPatientService> {

    @Mock
    PatientRepository repository;

    @Mock
    IUserDetailsService userDetailsService;

    UserDetails userDetails;

    @Override
    protected void configure() {
        PatientService patientService = new PatientService(repository, userDetailsService, validationUtils);
        configure(repository, Patient.class, patientService);
    }

    @Override
    protected void setUpData() {
        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();

        this.entity = Patient.builder()
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
    void update_patient_when_id_is_less_than_zero() {

        entity.setId(-10L);

        assertThrows(RuntimeException.class, () -> getService().update(entity));
    }

    @Test
    void find_patient_when_id_is_less_than_zero() {

        assertThrows(RuntimeException.class, () -> getService().find(-10L));

    }
}