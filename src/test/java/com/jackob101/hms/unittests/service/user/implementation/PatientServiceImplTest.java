package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.user.definition.IPatientService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.PatientService;
import com.jackob101.hms.unittests.service.TestFormCallbacks;
import com.jackob101.hms.unittests.service.base.BaseFormServiceTest;
import com.jackob101.hms.unittests.service.base.TestName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.EnumMap;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@Import(ValidationAutoConfiguration.class)
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest extends BaseFormServiceTest<Patient, PatientForm, IPatientService> {

    @Mock
    PatientRepository repository;

    @Mock
    IUserDetailsService userDetailsService;

    UserDetails userDetails;

    @Override
    protected void configure() {
        PatientService patientService = new PatientService(repository, userDetailsService, getValidationUtils());
        configure(repository, patientService);
        configureFormCallbacks();
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

        Patient patient = Patient.builder()
                .id(1L)
                .language("Polish")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE)
                .nationality("Polish")
                .userDetails(userDetails)
                .religion("none")
                .build();

        PatientForm patientForm = new PatientForm(1L, userDetails.getId(), MaritalStatus.SINGLE, "none", "none", Gender.MALE, "Polish");

        setData(patient, patientForm);

    }

    private void configureFormCallbacks() {
        EnumMap<TestName, TestFormCallbacks<Patient, PatientForm>> callbacks = getFormCallbacks();

        TestFormCallbacks<Patient, PatientForm> createSuccessfully = new TestFormCallbacks<>();
        createSuccessfully.setBeforeForm((patient, patientForm) -> {
            doReturn(userDetails).when(userDetailsService).find(anyLong());
        });
        callbacks.put(TestName.CREATE_FROM_FORM_SUCCESSFULLY, createSuccessfully);

        TestFormCallbacks<Patient, PatientForm> updateSuccessfully = new TestFormCallbacks<>();
        updateSuccessfully.setBeforeForm(createSuccessfully.getBeforeForm());
        callbacks.put(TestName.UPDATE_FROM_FORM_SUCCESSFULLY, updateSuccessfully);
    }

}