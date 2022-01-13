package com.jackob101.hms.integrationstests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@ActiveProfiles("no-security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PatientApiIntegrationTests {


    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    PatientRepository patientRepository;

    UserDetailsDTO userDetailsDTO;

    ObjectMapper objectMapper;

    List<UserDetails> userDetails;

    List<Patient> patients;

    PatientDTO patientDTO;

    @PostConstruct
    public void initialize() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port);
        this.testRestTemplate = new TestRestTemplate(restTemplateBuilder);
        testRestTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                }));
    }

    @BeforeEach
    void setUp() {
        userDetailsDTO = new UserDetailsDTO(1L,
                "123123123",
                "123123123",
                "Tom",
                "Mot",
                "John",
                LocalDate.now(),
                "123123123");


        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetails = new Random()
                .ints()
                .limit(100)
                .mapToObj(value -> UserDetails.builder()
                        .firstName(RandomStringUtils.randomAlphabetic(10))
                        .secondName(RandomStringUtils.randomAlphabetic(10))
                        .lastName(RandomStringUtils.randomAlphabetic(10))
                        .employee(null)
                        .patient(null)
                        .dateOfBirth(LocalDate.now())
                        .pesel(RandomStringUtils.random(10, false, true))
                        .phoneNumber(RandomStringUtils.random(10, false, true))
                        .build())
                .collect(Collectors.toList());

        patients = userDetails.stream()
                .map(entity -> Patient.builder()
                        .userDetails(entity)
                        .gender(Gender.MALE)
                        .nationality("Poland")
                        .language("Polish")
                        .maritalStatus(MaritalStatus.SINGLE)
                        .build())
                .collect(Collectors.toList());


        userDetailsRepository.saveAll(userDetails);
        patientRepository.saveAll(patients);

        patientDTO = PatientDTO.builder()
                .userDetailsId(userDetails.get(0).getId())
                .language("English")
                .maritalStatus(MaritalStatus.SINGLE)
                .build();
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
