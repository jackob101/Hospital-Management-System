package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.integrationstests.api.data.TestDataGenerator;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("no-security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PatientApiIntegrationTests {

    String requestMapping = "/patient";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    PatientRepository patientRepository;

    ObjectMapper objectMapper;

    List<UserDetails> userDetails;

    List<Patient> patients;

    PatientDTO patientDTO;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetails = TestDataGenerator.generateAndSaveUserDetails(userDetailsRepository);
        patients = TestDataGenerator.generateAndSavePatient(patientRepository, userDetails);

        patientDTO = TestDataGenerator.generatePatientForm(userDetails.get(0).getId());
    }

    @Test
    void create_patient_successfully() throws JsonProcessingException {

        String content = objectMapper.writeValueAsString(patientDTO);

        ResponseEntity<Patient> responseEntity = testRestTemplate.postForEntity("/patient", content, Patient.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(101L, responseEntity.getBody().getId());
    }

    @Test
    void create_patient_bindingError() throws JsonProcessingException {

        patientDTO.setUserDetailsId(null);

        String content = objectMapper.writeValueAsString(patientDTO);

        ResponseEntity<Patient> response = testRestTemplate.postForEntity(requestMapping, content, Patient.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void update_patient_successfully() throws JsonProcessingException {

        patientDTO.setId(1L);

        String content = objectMapper.writeValueAsString(patientDTO);

        testRestTemplate.put(requestMapping, content);

        ResponseEntity<Patient> response = testRestTemplate.getForEntity(requestMapping + "/" + patientDTO.getId(), Patient.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(patientDTO.getId(), response.getBody().getId());
        assertEquals(patientDTO.getLanguage(), response.getBody().getLanguage());

    }

    @Test
    void update_patient_bindingError() throws JsonProcessingException {

        patientDTO.setId(1L);
        patientDTO.setUserDetailsId(null);

        String content = objectMapper.writeValueAsString(patientDTO);

        testRestTemplate.put(requestMapping, content);

        ResponseEntity<Patient> response = testRestTemplate.getForEntity(requestMapping + "/" + patientDTO.getId(), Patient.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getUserDetails());

    }

    @Test
    void delete_patient_successfully() {

        ResponseEntity<Patient> patientResponse = testRestTemplate.getForEntity(requestMapping + "/1", Patient.class);

        assertNotNull(patientResponse);
        assertEquals(HttpStatus.OK, patientResponse.getStatusCode());
        assertNotNull(patientResponse.getBody());

        testRestTemplate.delete(requestMapping + "/" + patientResponse.getBody().getId());

        ResponseEntity<String> afterDeletionResponse = testRestTemplate.getForEntity(requestMapping + "/1", String.class);

        assertNotNull(afterDeletionResponse);
        assertEquals(HttpStatus.BAD_REQUEST, afterDeletionResponse.getStatusCode());
        assertNotNull(afterDeletionResponse.getBody());
    }

    @Test
    void delete_patient_notFound() {

        ResponseEntity<String> deleteResponse = testRestTemplate.exchange(requestMapping + "/99999",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class);

        assertNotNull(deleteResponse);
        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());
        assertNotNull(deleteResponse.getBody());
    }

    @Test
    void find_patient_successfully() {

        ResponseEntity<Patient> responseEntity = testRestTemplate.getForEntity(requestMapping + "/1", Patient.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getId());
    }

    @Test
    void find_patient_notFound() {

        ResponseEntity<Patient> responseEntity = testRestTemplate.getForEntity(requestMapping + "/99999", Patient.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void findAll_patient_successfully() {

        ResponseEntity<Patient[]> responseEntity = testRestTemplate.getForEntity(requestMapping + "/all", Patient[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(patients.size(), responseEntity.getBody().length);
    }
}
