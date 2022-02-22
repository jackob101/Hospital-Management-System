package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.EmployeeService;
import com.jackob101.hms.unittests.service.TestCallbacks;
import com.jackob101.hms.unittests.service.TestName;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest extends BaseServiceTest<Employee, EmployeeForm> {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    IUserDetailsService userDetailsService;

    @Mock
    ISpecializationService specializationService;


    EmployeeService employeeService;

    UserDetails userDetails;
    Specialization specialization;

    @Override
    protected void configure() {
        EmployeeService service = new EmployeeService(this.validationUtils, employeeRepository, userDetailsService, specializationService);
        employeeService = service;
        configure(employeeRepository, Employee.class, service);
    }

    @Override
    protected void setUpData() {

        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("Mot")
                .build();

        this.specialization = new Specialization(1L, "Doctor");

        this.entity = Employee.builder()
                .id(1L)
                .userDetails(userDetails)
                .specializations(Set.of(specialization))
                .build();

    }

    @Override
    protected void setUpCallbacks(Map<TestName, TestCallbacks<Employee, EmployeeForm>> configs) {

        TestCallbacks<Employee, EmployeeForm> updateSuccessfully = new TestCallbacks<>();
        updateSuccessfully.setAfter(employee1 -> {
            assertEquals(entity.getId(), employee1.getId());
            assertEquals(entity.getSpecializations(), employee1.getSpecializations());
        });
        configs.put(TestName.UPDATE_SUCCESSFULLY, updateSuccessfully);
    }


    @Test
    void createFromForm_employee_successfully() {

        doReturn(userDetails).when(userDetailsService).find(anyLong());
        doAnswer(returnsFirstArg()).when(employeeRepository).save(any(Employee.class));

        EmployeeForm employeeForm = new EmployeeForm();
        employeeForm.setId(1L);
        employeeForm.setUserDetailsId(1L);

        assertNotNull(employeeService.createFromForm(employeeForm));
    }

    @Test
    void create_employeeUserDetailsNull_throwException() {

        this.entity.setUserDetails(null);

        assertThrows(HmsException.class, () -> employeeService.create(this.entity));
    }

    @Test
    void update_employeeUserDetailsNull_throwException() {

        this.entity.setUserDetails(null);

        assertThrows(HmsException.class, () -> employeeService.update(entity));
    }

    @Test
    void update_employeeIdLessThanZero_throwException() {

        entity.setId(-10L);

        assertThrows(HmsException.class, () -> employeeService.update(entity));
    }

    @Test
    void updateFromForm_employeeForm_successfully() {

        EmployeeForm employeeForm = new EmployeeForm();

        employeeForm.setId(entity.getId());
        employeeForm.setUserDetailsId(entity.getUserDetails().getId());
        employeeForm.setSpecializations(Set.of(1L));

        doReturn(userDetails).when(userDetailsService).find(anyLong());
        doReturn(true).when(employeeRepository).existsById(anyLong());
        doAnswer(returnsFirstArg()).when(employeeRepository).save(any(Employee.class));

        Employee updated = employeeService.updateFromForm(employeeForm);

        assertEquals(entity.getId(), updated.getId());
        assertEquals(entity.getUserDetails().getId(), updated.getUserDetails().getId());
//        assertEquals(employee.getSpecializations().size(), updated.getSpecializations().size());

    }
}