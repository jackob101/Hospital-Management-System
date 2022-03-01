package com.jackob101.hms.integrationstests.api.user;


import com.jackob101.hms.TestUtils.data.user.EmployeeGenerator;
import com.jackob101.hms.api.user.EmployeeApi;
import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EmployeeApiIntegrationTests extends BaseApiIntegrationTest<Employee, EmployeeForm> {

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
        return new EmployeeForm(employees.get(0).getId(), userDetails.get(0).getId(), null);
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
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<Employee, EmployeeForm>.TestCallbacks> callbacks) {

        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(model -> model.setId(null));

        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setUserDetailsId(null));

        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setUserDetailsId(null));

        callbacks.get(ITestName.UPDATE_ENTITY_SUCCESSFULLY).setAfter(response -> {
            if (response.getBody() instanceof Employee) {
                Employee employee = (Employee) response.getBody();
                assertEquals(getForm().getId(), employee.getId());
            }
        });

        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setAfter(response -> {
            if (response.getBody() instanceof Employee) {
                Employee employee = (Employee) response.getBody();
                assertNotNull(employee.getUserDetails());
            }
        });

        callbacks.get(ITestName.FIND_ENTITY_SUCCESSFULLY).setAfter(response -> {
            if (response.getBody() instanceof Employee) {
                Employee employee = (Employee) response.getBody();
                assertEquals(employees.get(0).getId(), employee.getId());
            }
        });

        callbacks.get(ITestName.FIND_ALL_SUCCESSFULLY).setAfter(response -> {
            if (response.getBody() instanceof Object[]) {
                Object[] objects = (Object[]) response.getBody();
                assertEquals(employees.size(), objects.length);
            }
        });
    }
}
