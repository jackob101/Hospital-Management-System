package com.jackob101.hms.integrationstests.api.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.integrationstests.api.TestUtils;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.integrationstests.api.data.TestDataGenerator;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("no-security")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeApiIntegrationTests {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    EmployeeForm employeeForm;

    List<UserDetails> userDetails;

    List<Employee> employees;

    TestUtils utils;

    @BeforeEach
    void init() {

        employeeForm = TestDataGenerator.generateEmployeeForm(1L);

        userDetails = TestDataGenerator.generateAndSaveUserDetails(userDetailsRepository);
        employees = TestDataGenerator.generateAndSaveEmployee(employeeRepository, userDetails);

        utils = new TestUtils("/employee",
                testRestTemplate);
    }


    @Test
    void create_employee_successfully() throws JsonProcessingException {

        employeeForm.setId(null);

        ResponseEntity<Employee> responseEntity = utils.createEntity(employeeForm, Employee.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(101L, responseEntity.getBody().getId());
    }

    @Test
    void create_employee_bindingError() throws JsonProcessingException {

        employeeForm.setUserDetailsId(null);

        ResponseEntity<Employee> responseEntity = utils.createEntity(employeeForm, Employee.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void update_employee_successfully() {

        employeeForm.setId(1L);

        ResponseEntity<Employee> responseEntity = utils.updateEntity(employeeForm, Employee.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(employeeForm.getId(), responseEntity.getBody().getId());

    }

    @Test
    void update_employee_bindingError() {

        employeeForm.setId(1L);
        employeeForm.setUserDetailsId(null);

        ResponseEntity<String> responseEntity = utils.updateEntity(employeeForm, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ResponseEntity<Employee> entity = utils.findEntity(employeeForm.getId(), Employee.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().getUserDetails());

    }

    @Test
    void delete_employee_successfully() {

        Long id = employees.get(0).getId();

        ResponseEntity<String> responseType = utils.deleteEntity(id, String.class);

        assertEquals(HttpStatus.OK, responseType.getStatusCode());

        ResponseEntity<String> afterDeletionResponse = utils.findEntity(id, String.class);

        assertNotNull(afterDeletionResponse);
        assertEquals(HttpStatus.BAD_REQUEST, afterDeletionResponse.getStatusCode());
        assertNotNull(afterDeletionResponse.getBody());

    }

    @Test
    void delete_employee_notFound() {

        ResponseEntity<String> responseEntity = utils.deleteEntity((long) Integer.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @Test
    void find_employee_successfully() {

        Long id = employees.get(0).getId();
        ResponseEntity<Employee> responseEntity = utils.findEntity(id, Employee.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getId());
    }

    @Test
    void find_employee_notFound() {

        ResponseEntity<String> responseEntity = utils.findEntity(Long.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void findAll_employee_successfully() {

        ResponseEntity<Employee[]> responseEntity = utils.findAll(Employee[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(employees.size(), responseEntity.getBody().length);
    }
}
