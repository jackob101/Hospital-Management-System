package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.dto.user.PatientDTO;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PatientApiIntegrationTests extends BaseIntegrationTest {

    @Autowired
    PatientRepository patientRepository;

    List<UserDetails> userDetails;

    List<Patient> patients;

    PatientDTO patientDTO;

    @BeforeEach
    void setUp() {

        configure("/patient");

        patients = patientRepository.saveAll(new PatientGenerator().generate(5));
        userDetails = patients.stream().map(Patient::getUserDetails).collect(Collectors.toList());
        patientDTO = PatientDTO.builder()
                .userDetailsId(userDetails.get(0).getId())
                .language("English")
                .maritalStatus(MaritalStatus.SINGLE)
                .religion(RandomStringUtils.randomAlphabetic(10))
                .gender(Gender.MALE)
                .build();
    }

    @AfterEach
    void tearDown() {

        patientRepository.deleteAll();

    }

    @Test
    void create_patient_successfully() throws JsonProcessingException {

        ResponseEntity<Patient> responseEntity = utils.createEntity(patientDTO, Patient.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void create_patient_bindingError() throws JsonProcessingException {

        patientDTO.setUserDetailsId(null);

        ResponseEntity<String> responseEntity = utils.createEntity(patientDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void update_patient_successfully() {

        patientDTO.setId(patients.get(0).getId());

        ResponseEntity<Patient> responseEntity = utils.updateEntity(patientDTO, Patient.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(patientDTO.getId(), responseEntity.getBody().getId());
        assertEquals(patientDTO.getLanguage(), responseEntity.getBody().getLanguage());

    }

    @Test
    void update_patient_bindingError() {

        patientDTO.setId(1L);
        patientDTO.setUserDetailsId(null);

        ResponseEntity<String> responseEntity = utils.updateEntity(patientDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @Test
    void delete_patient_successfully() {


        Long id = patients.get(0).getId();
        ResponseEntity<String> responseEntity = utils.deleteEntity(id, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        ResponseEntity<String> afterDeletionResponse = utils.findEntity(id, String.class);

        assertNotNull(afterDeletionResponse);
        assertEquals(HttpStatus.BAD_REQUEST, afterDeletionResponse.getStatusCode());
        assertNotNull(afterDeletionResponse.getBody());
    }

    @Test
    void delete_patient_notFound() {

        ResponseEntity<String> responseEntity = utils.deleteEntity(Long.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void find_patient_successfully() {

        Long id = patients.get(0).getId();
        ResponseEntity<Patient> responseEntity = utils.findEntity(id, Patient.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(id, responseEntity.getBody().getId());
    }

    @Test
    void find_patient_notFound() {

        ResponseEntity<String> responseEntity = utils.findEntity(Long.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void findAll_patient_successfully() {

        ResponseEntity<Patient[]> responseEntity = utils.findAll(Patient[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(patients.size(), responseEntity.getBody().length);
    }
}
