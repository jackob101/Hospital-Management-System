package com.jackob101.hms.integrationstests.api.allergy;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.integrationstests.api.TestUtils;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.integrationstests.api.data.TestDataGenerator;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("no-security")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AllergenApiIntegrationTest {


    @Autowired
    AllergenRepository allergenRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    TestUtils utils;

    List<Allergen> allergenList;

    @BeforeEach
    void setUp() {

        utils = new TestUtils("/allergen", testRestTemplate);

        List<Allergen> allergenList = TestDataGenerator.generateAllergenList(10);

        allergenList = allergenRepository.saveAll(allergenList);


    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
