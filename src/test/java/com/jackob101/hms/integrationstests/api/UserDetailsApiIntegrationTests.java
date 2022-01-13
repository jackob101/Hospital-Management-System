package com.jackob101.hms.integrationstests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("no-security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TestWebSecurityConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDetailsApiIntegrationTests {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    UserDetailsDTO userDetailsDTO;

    ObjectMapper objectMapper;

    List<UserDetails> userDetails;

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
        userDetailsDTO = new UserDetailsDTO(9999L,
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


        userDetailsRepository.saveAll(userDetails);
    }

    @Test
    void create_userDetails_successfully() throws Exception {


        userDetailsDTO.setId(null);
        String content = objectMapper.writeValueAsString(userDetailsDTO);

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/userdetails", content, String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    void create_userDetailsIdNull_successfully() throws Exception {


        String content = objectMapper.writeValueAsString(userDetailsDTO);

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.postForEntity("/userdetails", content, UserDetails.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
    }

    @Test
    void create_userDetailsDTOBindingError_failed() throws Exception {

        userDetailsDTO.setFirstName(null);
        userDetailsDTO.setLastName(null);

        String content = objectMapper.writeValueAsString(userDetailsDTO);

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity("/userdetails", content, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void getAll_userDetails_successfully() {

        ResponseEntity<UserDetails[]> responseEntity = testRestTemplate.getForEntity("/userdetails/all", UserDetails[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void getWithId_userDetails_successfully() {

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.getForEntity("/userdetails/1", UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals(userDetails.get(0).getFirstName(), responseEntity.getBody().getFirstName());
    }

    @Test
    void getWithId_userDetails_notFound() {

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity("/userdetails/123123", String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void update_userDetails_successfully() throws JsonProcessingException {
        UserDetails userDetailsSaved = this.userDetails.get(0);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(1L,
                userDetailsSaved.getUserCredentialsId(),
                userDetailsSaved.getPesel(),
                "Tom",
                "Mot",
                userDetailsSaved.getLastName(),
                userDetailsSaved.getDateOfBirth(),
                userDetailsSaved.getPhoneNumber());

        testRestTemplate.put("/userdetails", objectMapper.writeValueAsString(userDetailsDTO));

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.getForEntity("/userdetails/1", UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userDetailsSaved.getFirstName(), responseEntity.getBody().getFirstName());
        assertEquals(userDetailsSaved.getSecondName(), responseEntity.getBody().getSecondName());

    }


    @Test
    void update_userDetails_bindingErrors() throws JsonProcessingException {
        UserDetails userDetailsSaved = this.userDetails.get(0);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(1L,
                userDetailsSaved.getUserCredentialsId(),
                userDetailsSaved.getPesel(),
                "",
                "",
                userDetailsSaved.getLastName(),
                userDetailsSaved.getDateOfBirth(),
                userDetailsSaved.getPhoneNumber());

        testRestTemplate.put("/userdetails", objectMapper.writeValueAsString(userDetailsDTO));

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.getForEntity("/userdetails/1", UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userDetailsSaved.getFirstName(), responseEntity.getBody().getFirstName());
        assertEquals(userDetailsSaved.getSecondName(), responseEntity.getBody().getSecondName());

    }
}
