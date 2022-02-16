package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.UserDetailsGenerator;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsApiIntegrationTests extends BaseIntegrationTest {


    @Autowired
    UserDetailsRepository userDetailsRepository;

    UserDetailsDTO userDetailsDTO;

    List<UserDetails> userDetails;

    @BeforeEach
    void setUp() {
        configure("/userdetails");

        userDetailsDTO = new UserDetailsDTO(9999L,
                "123123123",
                "123123123",
                "Tom",
                "Mot",
                "John",
                LocalDate.now(),
                "123123123");

        userDetails = userDetailsRepository.saveAll(new UserDetailsGenerator().generate(5));

    }

    @AfterEach
    void tearDown() {
        userDetailsRepository.deleteAll();
    }

    @Test
    void create_userDetails_successfully() throws Exception {


        userDetailsDTO.setId(null);

        ResponseEntity<UserDetails> responseEntity = utils.createEntity(userDetailsDTO, UserDetails.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertTrue(responseEntity.hasBody());
    }

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
        assertEquals(id, responseEntity.getBody().getId());
        assertEquals(userDetails.get(0).getFirstName(), responseEntity.getBody().getFirstName());
    }

    @Test
    void getWithId_userDetails_notFound() {

        ResponseEntity<String> responseEntity = utils.findEntity(Long.MAX_VALUE, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

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
