package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.PatientApi;
import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.PatientGenerator;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .userDetailsId(userDetails.get(0).getId())
                .language("English")
                .maritalStatus(MaritalStatus.SINGLE)
                .religion(RandomStringUtils.randomAlphabetic(10))
                .gender(Gender.MALE)
                .build();
    }

    @Override
    protected Long configureId() {
        return patients.get(0).getId();
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
        callbacks.get(ITestName.CREATE_ENTITY_FAILED).setBefore(form -> form.setUserDetailsId(null));

        callbacks.get(ITestName.UPDATE_ENTITY_SUCCESSFULLY).setBefore(form -> {
            form.setId(patients.get(0).getId());
            form.setUserDetailsId(patients.get(0).getUserDetails().getId());
        });

        callbacks.get(ITestName.UPDATE_ENTITY_SUCCESSFULLY).setAfter(response -> {
            ResponseEntity<Patient> responseEntity = (ResponseEntity<Patient>) response;

            assertEquals(getForm().getId(), responseEntity.getBody().getId());
            assertEquals(getForm().getLanguage(), responseEntity.getBody().getLanguage());
        });

        callbacks.get(ITestName.UPDATE_ENTITY_FAILED).setBefore(form -> {
            form.setId(patients.get(0).getId());
            form.setUserDetailsId(null);
        });

        callbacks.get(ITestName.DELETE_ENTITY_NOT_FOUND).setBefore(form -> setId(Long.MAX_VALUE));

        callbacks.get(ITestName.FIND_ALL_SUCCESSFULLY).setAfter(response -> {
            ResponseEntity<Patient> responseEntity = (ResponseEntity<Patient>) response;

            assertEquals(patients.get(0).getId(), responseEntity.getBody().getId());
        });

        callbacks.get(ITestName.FIND_ENTITY_NOT_FOUND).setBefore(form -> setId(Long.MAX_VALUE));

        callbacks.get(ITestName.FIND_ALL_SUCCESSFULLY).setAfter(response -> {
            ResponseEntity<Object[]> responseEntity = (ResponseEntity<Object[]>) response;

            assertEquals(patients.size(), responseEntity.getBody().length);
        });
    }
}
