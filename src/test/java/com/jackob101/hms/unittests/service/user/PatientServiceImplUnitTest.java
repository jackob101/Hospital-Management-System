package com.jackob101.hms.unittests.service.user;

import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.service.user.implementation.PatientService;
import com.jackob101.hms.unittests.service.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

@Import(ValidationAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PatientServiceImplUnitTest extends BaseServiceUnitTest<Patient, IPatientService> {

    @Mock
    PatientRepository repository;

    UserDetails userDetails;

    @Override
    protected IPatientService configureService() {
        return new PatientService(repository, getValidationUtils());
    }

    @Override
    protected Patient configureEntity() {

        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();

        return Patient.builder()
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
    protected JpaRepository<Patient, Long> configureRepository() {
        return repository;
    }

}