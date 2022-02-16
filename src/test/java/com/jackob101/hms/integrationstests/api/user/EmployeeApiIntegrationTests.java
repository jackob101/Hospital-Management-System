package com.jackob101.hms.integrationstests.api.user;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.EmployeeGenerator;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmployeeApiIntegrationTests extends BaseIntegrationTest {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    EmployeeForm employeeForm;

    List<UserDetails> userDetails;

    List<Employee> employees;


    @BeforeEach
    void init() {
        this.configure("/employee");

        employees = employeeRepository.saveAll(new EmployeeGenerator().generate(5));
        userDetails = employees.stream().map(Employee::getUserDetails).collect(Collectors.toList());
        employeeForm = new EmployeeForm(1L, userDetails.get(0).getId(), null);
    }

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
        userDetailsRepository.deleteAll();
    }

    @Test
    void create_employee_successfully() throws JsonProcessingException {

        employeeForm.setId(null);

        ResponseEntity<Employee> responseEntity = utils.createEntity(employeeForm, Employee.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void create_employee_bindingError() throws JsonProcessingException {

        employeeForm.setUserDetailsId(null);

        ResponseEntity<Employee> responseEntity = utils.createEntity(employeeForm, Employee.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void update_employee_successfully() {

        employeeForm.setId(employees.get(0).getId());

        ResponseEntity<Employee> responseEntity = utils.updateEntity(employeeForm, Employee.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(employeeForm.getId(), responseEntity.getBody().getId());

    }

    @Test
    void update_employee_bindingError() {

        employeeForm.setId(employees.get(0).getId());
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
        assertEquals(employees.get(0).getId(), responseEntity.getBody().getId());
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
