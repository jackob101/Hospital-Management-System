package com.jackob101.hms.integrationstests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.api.user.SpecializationAPI;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.SpecializationGenerator;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpecializationApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    SpecializationRepository specializationRepository;

    List<Specialization> specializationList;

    Specialization specialization;

    @BeforeEach
    void setUp() {

        configure(SpecializationAPI.REQUEST_MAPPING);

        specialization = new Specialization(1L, "Doctor");

        specializationList = specializationRepository.saveAll(new SpecializationGenerator().generate(10));

    }

    @AfterEach
    void tearDown() {
        specializationRepository.deleteAll();
    }

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

    @Test
    void delete_specialization_successfully() {

        Long id = specializationList.get(0).getId();
        ResponseEntity<String> responseEntity = utils.deleteEntity(id, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

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
