package com.jackob101.hms.integrationstests.api.allergy;


import com.jackob101.hms.integrationstests.api.TestUtils;
import com.jackob101.hms.integrationstests.api.data.allergy.PatientAllergyGenerator;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.List;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
//@ExtendWith(SpringExtension.class)
//@ActiveProfiles("no-security")
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PatientAllergyApiIntegrationTest {

    @Autowired
    PatientAllergyRepository patientAllergyRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    List<PatientAllergy> patientAllergyList;

    TestUtils utils;

    @BeforeEach
    void setUp() {
        patientAllergyList = patientAllergyRepository.saveAll(new PatientAllergyGenerator().generate(10));

        utils = new TestUtils("/patient_allergy", testRestTemplate);
    }

    //    @Test
    void create_() {
    }
}
