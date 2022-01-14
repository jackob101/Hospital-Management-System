package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.integrationstests.api.data.TestDataGenerator;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("no-security")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDetailsApiIntegrationTests {

    String baseUrl = "/userdetails";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    UserDetailsDTO userDetailsDTO;

    ObjectMapper objectMapper;

    List<UserDetails> userDetails;

    @BeforeEach
    void setUp() {

        userDetailsDTO = TestDataGenerator.generateUserDetailsForm();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetails = TestDataGenerator.generateAndSaveUserDetails(userDetailsRepository);
    }

    @Test
    void create_userDetails_successfully() throws Exception {


        userDetailsDTO.setId(null);
        String content = objectMapper.writeValueAsString(userDetailsDTO);

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(baseUrl, content, String.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    void create_userDetailsIdNull_successfully() throws Exception {


        String content = objectMapper.writeValueAsString(userDetailsDTO);

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.postForEntity(baseUrl, content, UserDetails.class);

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

        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(baseUrl, content, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void getAll_userDetails_successfully() {

        ResponseEntity<UserDetails[]> responseEntity = testRestTemplate.getForEntity(baseUrl + "/all", UserDetails[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void getWithId_userDetails_successfully() {

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.getForEntity(baseUrl + "/1", UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals(userDetails.get(0).getFirstName(), responseEntity.getBody().getFirstName());
    }

    @Test
    void getWithId_userDetails_notFound() {

        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(baseUrl + "/1231231", String.class);

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

        testRestTemplate.put(baseUrl, objectMapper.writeValueAsString(userDetailsDTO));

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.getForEntity(baseUrl + "/1", UserDetails.class);

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

        testRestTemplate.put(baseUrl, objectMapper.writeValueAsString(userDetailsDTO));

        ResponseEntity<UserDetails> responseEntity = testRestTemplate.getForEntity(baseUrl + "/1", UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userDetailsSaved.getFirstName(), responseEntity.getBody().getFirstName());
        assertEquals(userDetailsSaved.getSecondName(), responseEntity.getBody().getSecondName());

    }
}
