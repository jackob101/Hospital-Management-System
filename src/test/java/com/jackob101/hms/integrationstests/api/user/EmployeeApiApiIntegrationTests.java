package com.jackob101.hms.integrationstests.api.user;


import com.jackob101.hms.api.user.EmployeeApi;
import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.EmployeeGenerator;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmployeeApiApiIntegrationTests extends BaseApiIntegrationTest<Employee, EmployeeForm> {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;


    List<UserDetails> userDetails;

    List<Employee> employees;

    @Override
    protected String configureRequestMapping() {
        return EmployeeApi.REQUEST_MAPPING;
    }

    @Override
    protected EmployeeForm configureForm() {
        return new EmployeeForm(9999L, userDetails.get(0).getId(), null);
    }

    @Override
    protected Long configureId() {
        return employees.get(0).getId();
    }

    @Override
    protected void createMockData() {
        employees = employeeRepository.saveAll(new EmployeeGenerator().generate(5));
        userDetails = employees.stream().map(Employee::getUserDetails).collect(Collectors.toList());
    }

    @Override
    protected void clearMockData() {
        employeeRepository.deleteAll();
        userDetailsRepository.deleteAll();
    }

    @Override
    protected void configureCallbacks(EnumMap<TestName, BaseApiIntegrationTest<Employee, EmployeeForm>.TestCallbacks> callbacks) {

        callbacks.get(TestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(model -> model.setId(null));

        callbacks.get(TestName.CREATE_ENTITY_FAILED).setBefore(form -> form.setUserDetailsId(null));

        callbacks.get(TestName.UPDATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(employees.get(0).getId()));

        callbacks.get(TestName.UPDATE_ENTITY_SUCCESSFULLY).setAfter(response -> {
            ResponseEntity<Employee> responseEntity = (ResponseEntity<Employee>) response;
            assertEquals(getForm().getId(), responseEntity.getBody().getId());
        });

        callbacks.get(TestName.UPDATE_ENTITY_FAILED).setBefore(form -> {
            form.setId(employees.get(0).getId());
            form.setUserDetailsId(null);
        });
        callbacks.get(TestName.UPDATE_ENTITY_FAILED).setAfter(response -> {
            ResponseEntity<Employee> responseEntity = (ResponseEntity<Employee>) response;

            assertNotNull(responseEntity.getBody().getUserDetails());
        });

        callbacks.get(TestName.FIND_ENTITY_SUCCESSFULLY).setAfter(response -> {
            ResponseEntity<Employee> responseEntity = (ResponseEntity<Employee>) response;
            assertEquals(employees.get(0).getId(), responseEntity.getBody().getId());
        });

        callbacks.get(TestName.FIND_ENTITY_NOT_FOUND).setBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.get(TestName.FIND_ALL_SUCCESSFULLY).setAfter(response -> {
            ResponseEntity<Object[]> responseEntity = (ResponseEntity<Object[]>) response;
            assertEquals(employees.size(), responseEntity.getBody().length);
        });
    }
}
