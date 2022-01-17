package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.integrationstests.api.TestUtils;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.integrationstests.api.data.TestDataGenerator;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
public class SpecializationApiIntegrationTest {

    @Autowired
    SpecializationRepository specializationRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    TestUtils utils;

    List<Specialization> specializationList;

    Specialization specialization;

    @BeforeEach
    void setUp() {

        utils = new TestUtils("/specialization", testRestTemplate);

        specialization = new Specialization(1L, "Doctor");

        specializationList = TestDataGenerator.generateAndSaveSpecializations(specializationRepository);

    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @ParameterizedTest
    @CsvSource(value = {"1001, Doctor", "NULL, Doctor"}, nullValues = "NULL")
    void create_specialization_successfully(Long id, String name) throws JsonProcessingException {

        Specialization parameter = new Specialization(id, name);

        ResponseEntity<Specialization> responseEntity = utils.createEntity(parameter, Specialization.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(name, responseEntity.getBody().getName());

    }

    @ParameterizedTest
    @CsvSource(value = {"''", "NULL"}, nullValues = "NULL")
    void create_specialization_bindingError(String name) throws JsonProcessingException {

        Specialization parameter = new Specialization(name);
        ResponseEntity<String> responseEntity = utils.createEntity(parameter, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertFalse(responseEntity.getBody().isBlank());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void update_specialization_successfully() {

        Long id = specializationList.get(0).getId();

        specialization.setId(id);

        ResponseEntity<Specialization> responseEntity = utils.updateEntity(specialization, Specialization.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(specialization.getName(), responseEntity.getBody().getName());
    }

    @ParameterizedTest
    @CsvSource(value = {"NULL,NULL", "99999,''", "9999, Nurse"}, nullValues = "NULL")
    void update_specialization_bindingError(Long id, String name) {

        specialization.setId(id);
        specialization.setName(name);

        ResponseEntity<String> responseEntity = utils.updateEntity(specialization, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.hasBody());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    @Test
    void delete_specialization_successfully() {

        Long id = specialization.getId();
        ResponseEntity<String> responseEntity = utils.deleteEntity(id, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @ParameterizedTest
    @CsvSource(value = {"NULL", "99999"}, nullValues = "NULL")
    void delete_specialization_failed(Long id) {

        ResponseEntity<String> responseEntity = utils.deleteEntity(id, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void find_specialization_successfully() {

        Long id = specializationList.get(0).getId();

        ResponseEntity<Specialization> responseEntity = utils.findEntity(id, Specialization.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(specializationList.get(0).getName(), responseEntity.getBody().getName());
    }

    @ParameterizedTest
    @CsvSource(value = {"NULL", "99999"}, nullValues = "NULL")
    void find_specialization_notFound(Long id) {

        ResponseEntity<String> responseEntity = utils.findEntity(id, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.hasBody());
    }

    @Test
    void findAll_specialization_successfully() {

        ResponseEntity<Specialization[]> responseEntity = utils.findAll(Specialization[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(specializationList.size(), responseEntity.getBody().length);
    }
}
