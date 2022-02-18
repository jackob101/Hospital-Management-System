package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.UserDetailsApi;
import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.UserDetailsGenerator;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class UserDetailsApiIntegrationTests extends BaseIntegrationTest<UserDetails, UserDetailsForm> {


    @Autowired
    UserDetailsRepository userDetailsRepository;

    UserDetailsForm userDetailsForm;

    List<UserDetails> userDetails;

    @BeforeEach
    void setUp() {
        configure(UserDetailsApi.REQUEST_MAPPING);

        userDetails = userDetailsRepository.saveAll(new UserDetailsGenerator().generate(5));

        userDetailsForm = new UserDetailsForm(9999L,
                "123123123",
                "123123123",
                "Tom",
                "Mot",
                "John",
                LocalDate.now(),
                "123123123");
        userDetailsForm.setId(userDetails.get(0).getId());

        setId(userDetails.get(0).getId());
        setForm(userDetailsForm);
        setModelClass(UserDetails.class);
        setArrayModelClass(UserDetails[].class);

        callbacks.setCreateSuccessfullyBefore(form -> form.setId(null));

        callbacks.setFindFailedBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.setUpdateFailedBefore(form -> form.setFirstName(""));
    }

    @AfterEach
    void tearDown() {
        userDetailsRepository.deleteAll();
    }

//    @Test
//    void create_userDetails_successfully() throws Exception {
//
//
//        userDetailsForm.setId(null);
//
//        ResponseEntity<UserDetails> responseEntity = utils.createEntity(userDetailsForm, UserDetails.class);
//
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertTrue(responseEntity.hasBody());
//    }
//
//    @Test
//    void create_userDetailsIdNull_successfully() throws Exception {
//
//        ResponseEntity<UserDetails> responseEntity = utils.createEntity(userDetailsForm, UserDetails.class);
//
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertTrue(responseEntity.hasBody());
//        assertNotNull(responseEntity.getBody());
//        assertNotNull(responseEntity.getBody().getId());
//    }
//
//    @Test
//    void create_userDetailsDTOBindingError_failed() throws Exception {
//
//        userDetailsForm.setFirstName(null);
//        userDetailsForm.setLastName(null);
//
//        ResponseEntity<String> responseEntity = utils.createEntity(userDetailsForm, String.class);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }
//
//
//    @Test
//    void getAll_userDetails_successfully() {
//
//        ResponseEntity<UserDetails[]> responseEntity = utils.findAll(UserDetails[].class);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    }
//
//
//    @Test
//    void getWithId_userDetails_successfully() {
//
//        Long id = userDetails.get(0).getId();
//        ResponseEntity<UserDetails> responseEntity = utils.findEntity(id, UserDetails.class);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//        assertEquals(id, responseEntity.getBody().getId());
//        assertEquals(userDetails.get(0).getFirstName(), responseEntity.getBody().getFirstName());
//    }
//
//    @Test
//    void getWithId_userDetails_notFound() {
//
//        ResponseEntity<String> responseEntity = utils.findEntity(Long.MAX_VALUE, String.class);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//    }
//
//    @Test
//    void update_userDetails_successfully() {
//        UserDetails userDetailsSaved = this.userDetails.get(0);
//
//        UserDetailsForm userDetailsForm = new UserDetailsForm(1L,
//                userDetailsSaved.getUserCredentialsId(),
//                userDetailsSaved.getPesel(),
//                "Tom",
//                "Mot",
//                userDetailsSaved.getLastName(),
//                userDetailsSaved.getDateOfBirth(),
//                userDetailsSaved.getPhoneNumber());
//
//        ResponseEntity<UserDetails> responseEntity = utils.updateEntity(userDetailsForm, UserDetails.class);
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//        assertEquals(userDetailsForm.getFirstName(), responseEntity.getBody().getFirstName());
//        assertEquals(userDetailsForm.getSecondName(), responseEntity.getBody().getSecondName());
//
//    }
//
//
//    @Test
//    void update_userDetails_bindingErrors() {
//        UserDetails userDetailsSaved = this.userDetails.get(0);
//
//        UserDetailsForm userDetailsForm = new UserDetailsForm(1L,
//                userDetailsSaved.getUserCredentialsId(),
//                userDetailsSaved.getPesel(),
//                "",
//                "",
//                userDetailsSaved.getLastName(),
//                userDetailsSaved.getDateOfBirth(),
//                userDetailsSaved.getPhoneNumber());
//
//        ResponseEntity<String> responseEntity = utils.updateEntity(userDetailsForm, String.class);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//
//    }
}
