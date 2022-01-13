package com.jackob101.hms.integrationstests.api;

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
}
