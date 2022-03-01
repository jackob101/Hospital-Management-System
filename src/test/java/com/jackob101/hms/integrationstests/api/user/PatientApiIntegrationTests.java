package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.TestUtils.data.user.PatientGenerator;
import com.jackob101.hms.api.user.PatientApi;
import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class PatientApiIntegrationTests extends BaseApiIntegrationTest<Patient, PatientForm> {

    @Autowired
    PatientRepository patientRepository;

    List<UserDetails> userDetails;

    List<Patient> patients;

    @Override
    protected String configureRequestMapping() {
        return PatientApi.REQUEST_MAPPING;
    }

    @Override
    protected PatientForm configureForm() {
        return PatientForm.builder()
                .id(patients.get(0).getId())
                .userDetailsId(userDetails.get(0).getId())
                .language("English")
                .maritalStatus(MaritalStatus.SINGLE)
                .religion(RandomStringUtils.randomAlphabetic(10))
                .gender(Gender.MALE)
                .build();
    }

    @Override
    protected void createMockData() {
        patients = patientRepository.saveAll(new PatientGenerator().generate(5));
        userDetails = patients.stream().map(Patient::getUserDetails).collect(Collectors.toList());
    }

    @Override
    protected void clearMockData() {

        patientRepository.deleteAll();

    }

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<Patient, PatientForm>.TestCallbacks> callbacks) {

        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setUserDetailsId(null));

        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setUserDetailsId(null));
    }
}
