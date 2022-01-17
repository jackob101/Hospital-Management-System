package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.integrationstests.api.TestUtils;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserDetailsApiIntegrationTests {


    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    UserDetailsDTO userDetailsDTO;

    ObjectMapper objectMapper;

    List<UserDetails> userDetails;

    TestUtils utils;

    @BeforeEach
    void setUp() {

        userDetailsDTO = TestDataGenerator.generateUserDetailsForm();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetails = TestDataGenerator.generateAndSaveUserDetails(userDetailsRepository);

        utils = new TestUtils("/userdetails", testRestTemplate);
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void create_userDetails_successfully() throws Exception {


        userDetailsDTO.setId(null);

        ResponseEntity<UserDetails> responseEntity = utils.createEntity(userDetailsDTO, UserDetails.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void create_userDetailsIdNull_successfully() throws Exception {

        ResponseEntity<UserDetails> responseEntity = utils.createEntity(userDetailsDTO, UserDetails.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
    }

    @Test
    void create_userDetailsDTOBindingError_failed() throws Exception {

        userDetailsDTO.setFirstName(null);
        userDetailsDTO.setLastName(null);

        ResponseEntity<String> responseEntity = utils.createEntity(userDetailsDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void getAll_userDetails_successfully() {

        ResponseEntity<UserDetails[]> responseEntity = utils.findAll(UserDetails[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void getWithId_userDetails_successfully() {

        Long id = userDetails.get(0).getId();
        ResponseEntity<UserDetails> responseEntity = utils.findEntity(id, UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals(userDetails.get(0).getFirstName(), responseEntity.getBody().getFirstName());
    }

    @Test
    void getWithId_userDetails_notFound() {

        ResponseEntity<String> responseEntity = utils.findEntity(Long.MAX_VALUE, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void update_userDetails_successfully() {
        UserDetails userDetailsSaved = this.userDetails.get(0);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(1L,
                userDetailsSaved.getUserCredentialsId(),
                userDetailsSaved.getPesel(),
                "Tom",
                "Mot",
                userDetailsSaved.getLastName(),
                userDetailsSaved.getDateOfBirth(),
                userDetailsSaved.getPhoneNumber());

        ResponseEntity<UserDetails> responseEntity = utils.updateEntity(userDetailsDTO, UserDetails.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userDetailsDTO.getFirstName(), responseEntity.getBody().getFirstName());
        assertEquals(userDetailsDTO.getSecondName(), responseEntity.getBody().getSecondName());

    }


    @Test
    void update_userDetails_bindingErrors() {
        UserDetails userDetailsSaved = this.userDetails.get(0);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO(1L,
                userDetailsSaved.getUserCredentialsId(),
                userDetailsSaved.getPesel(),
                "",
                "",
                userDetailsSaved.getLastName(),
                userDetailsSaved.getDateOfBirth(),
                userDetailsSaved.getPhoneNumber());

        ResponseEntity<String> responseEntity = utils.updateEntity(userDetailsDTO, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }
}
