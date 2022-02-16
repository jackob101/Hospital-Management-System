package com.jackob101.hms.integrationstests.api.allergy;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.allergy.AllergenGenerator;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AllergenApiIntegrationTest extends BaseIntegrationTest {

    @Autowired
    AllergenRepository allergenRepository;

    List<Allergen> allergenList;

    @BeforeEach
    void setUp() {

        configure("/allergen");

        List<Allergen> allergenList = new AllergenGenerator().generate(10);

        allergenList = allergenRepository.saveAll(allergenList);

    }

    @Test
    void create_allergen_successfully() throws JsonProcessingException {
        Allergen allergen = new Allergen("Some allergen");

        ResponseEntity<Allergen> responseEntity = utils.createEntity(allergen, Allergen.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(allergen.getName(), responseEntity.getBody().getName());
    }

    @ParameterizedTest
    @CsvSource(value = {"NULL", "''"}, nullValues = "NULL")
    void create_allergen_bindingError(String name) throws JsonProcessingException {

        Allergen allergen = new Allergen(name);

        ResponseEntity<String> responseEntity = utils.createEntity(allergen, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }
}
