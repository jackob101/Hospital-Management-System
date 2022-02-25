package com.jackob101.hms.unittests.service.user;

import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.PatientService;
import com.jackob101.hms.unittests.service.base.BaseFormServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.EnumMap;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@Import(ValidationAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PatientServiceImplUnitTest extends BaseFormServiceUnitTest<Patient, PatientForm, IPatientService> {

    @Mock
    PatientRepository repository;

    @Mock
    IUserDetailsService userDetailsService;

    UserDetails userDetails;

    @Override
    protected IPatientService configureService() {
        return new PatientService(repository, userDetailsService, getValidationUtils());
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

    @Override
    protected PatientForm configureForm() {
        return new PatientForm(1L, userDetails.getId(), MaritalStatus.SINGLE, "none", "none", Gender.MALE, "Polish");
    }

    @Override
    protected void configureFormCallbacks(EnumMap<FormTestNames, TestFormCallbacks> formCallbacks) {

        formCallbacks.get(FormTestNames.CREATE_FROM_FORM_SUCCESSFULLY).setBeforeForm((patient, patientForm) ->
                doReturn(userDetails).when(userDetailsService).find(anyLong()));

        formCallbacks.get(FormTestNames.UPDATE_FROM_FORM_SUCCESSFULLY).setBeforeForm((patient, patientForm) ->
                doReturn(userDetails).when(userDetailsService).find(anyLong()));

    }
}