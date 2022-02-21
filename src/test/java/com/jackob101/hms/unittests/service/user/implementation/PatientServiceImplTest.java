package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.PatientService;
import com.jackob101.hms.unittests.TestConfiguration;
import com.jackob101.hms.unittests.service.BaseTests;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Import(ValidationAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest extends BaseServiceTest<Patient, PatientForm> {

    @Mock
    PatientRepository patientRepository;

    @Mock
    IUserDetailsService userDetailsService;

    UserDetails userDetails;

    @Override
    protected void configure() {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        PatientService patientService = new PatientService(patientRepository, userDetailsService, validator);
        configure(patientRepository, Patient.class, patientService);
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

    @Override
    protected void setUpCallbacks(Map<BaseTests, TestConfiguration<Patient, PatientForm>> configs) {

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