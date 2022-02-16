package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.integrationstests.api.TestUtils;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.integrationstests.api.data.TestDataGenerator;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("no-security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PatientApiIntegrationTests {


    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PatientRepository patientRepository;

    ObjectMapper objectMapper;

    List<UserDetails> userDetails;

    List<Patient> patients;

    PatientDTO patientDTO;

    TestUtils utils;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetails = TestDataGenerator.generateUserDetailsList();
        patients = TestDataGenerator.generateAndSavePatient(patientRepository, userDetails);

        patientDTO = TestDataGenerator.generatePatientForm(userDetails.get(0).getId());

        utils = new TestUtils("/patient", testRestTemplate);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void update_patient_successfully() {

        patientDTO.setId(1L);

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

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
