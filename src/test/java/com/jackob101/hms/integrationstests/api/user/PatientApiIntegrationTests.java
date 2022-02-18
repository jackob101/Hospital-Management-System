package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.PatientApi;
import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.PatientGenerator;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatientApiIntegrationTests extends BaseIntegrationTest<Patient, PatientForm> {

    @Autowired
    PatientRepository patientRepository;

    List<UserDetails> userDetails;

    List<Patient> patients;

    PatientForm patientForm;

    @BeforeEach
    void setUp() {


        patients = patientRepository.saveAll(new PatientGenerator().generate(5));
        userDetails = patients.stream().map(Patient::getUserDetails).collect(Collectors.toList());
        patientForm = PatientForm.builder()
                .userDetailsId(userDetails.get(0).getId())
                .language("English")
                .maritalStatus(MaritalStatus.SINGLE)
                .religion(RandomStringUtils.randomAlphabetic(10))
                .gender(Gender.MALE)
                .build();

        configure(PatientApi.REQUEST_MAPPING);
        setId(patients.get(0).getId());
        setForm(patientForm);
        setModelClass(Patient.class);
        setArrayModelClass(Patient[].class);

        callbacks.setCreateFailedBefore(form -> form.setUserDetailsId(null));

        callbacks.setUpdateSuccessfullyBefore(form -> {
            form.setId(patients.get(0).getId());
            form.setUserDetailsId(patients.get(0).getUserDetails().getId());
        });
        callbacks.setUpdateSuccessfullyAfter(response -> {
            assertEquals(patientForm.getId(), response.getBody().getId());
            assertEquals(patientForm.getLanguage(), response.getBody().getLanguage());
        });

        callbacks.setUpdateFailedBefore(form -> {
            form.setId(patients.get(0).getId());
            form.setUserDetailsId(null);
        });

        callbacks.setDeleteFailedBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.setFindSuccessfullyAfter(response -> {
            assertEquals(patients.get(0).getId(), response.getBody().getId());
        });

        callbacks.setFindFailedBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.setFindAllSuccessfullyAfter(response -> {
            assertEquals(patients.size(), response.getBody().length);
        });
    }

    @AfterEach
    void tearDown() {

        patientRepository.deleteAll();

    }
}
