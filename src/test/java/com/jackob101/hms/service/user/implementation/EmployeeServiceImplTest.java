package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    UserDetailsService userDetailsService;

    EmployeeServiceImpl employeeService;

    UserDetails userDetails;

    Employee employee;

    Specialization specialization;

    @BeforeEach
    void setUp() {
        openMocks(this);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        employeeService = new EmployeeServiceImpl(validator, employeeRepository, userDetailsService);

        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("Mot")
                .build();

        specialization = new Specialization(1L, "Doctor");

        employee = Employee.builder()
                .id(1L)
                .userDetails(userDetails)
                .specializations(Set.of(specialization))
                .build();

    }

    @Test
    void create_employeeNull_throwException() {

        assertThrows(HmsException.class, () -> employeeService.create(null));

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
    void createFromForm_employeeFormNull_throwException() {

        assertThrows(HmsException.class, () -> employeeService.createFromForm(null));
    }

    @Test
    void create_employeeUserDetailsNull_throwException() {

        employee.setUserDetails(null);

        assertThrows(HmsException.class, () -> employeeService.create(employee));
    }

    @Test
    void create_employeeAlreadyExists_throwException() {

        doReturn(true).when(employeeRepository).existsById(anyLong());

        assertThrows(HmsException.class,() -> employeeService.create(employee));
    }

    @Test
    void create_employee_successfully() {

        doAnswer(returnsFirstArg()).when(employeeRepository).save(any(Employee.class));

        Employee saved = employeeService.create(this.employee);

        assertEquals(this.employee.getId(), saved.getId());
    }

    @Test
    void update_employeeIdNull_throwException() {
        employee.setId(null);

        assertThrows(HmsException.class,() -> employeeService.update(employee));
    }

    @Test
    void update_employeeNull_throwException() {

        assertThrows(HmsException.class,() -> employeeService.update(null));

    }

    @Test
    void update_employeeUserDetailsNull_throwException() {

        employee.setUserDetails(null);

        assertThrows(HmsException.class, () -> employeeService.update(employee));
    }

    @Test
    void update_employeeNotFound_throwException() {

        doReturn(false).when(employeeRepository).existsById(anyLong());

        assertThrows(HmsException.class, () -> employeeService.update(employee));
    }

    @Test
    void update_employee_successfully() {

        doAnswer(returnsFirstArg()).when(employeeRepository).save(any(Employee.class));
        doReturn(true).when(employeeRepository).existsById(anyLong());

        Employee updated = employeeService.update(employee);

        assertEquals(employee.getId(), updated.getId());
        assertEquals(employee.getSpecializations(), updated.getSpecializations());
    }

    @Test
    void update_employeeIdLessThanZero_throwException() {

        employee.setId(-10L);

        assertThrows(HmsException.class,() -> employeeService.update(employee));
    }

    @Test
    void delete_employeeNull_throwException() {

        assertThrows(HmsException.class,() -> employeeService.delete(null));

    }

    @Test
    void delete_employeeIdNull_throwException() {

        employee.setId(null);

        assertThrows(HmsException.class,() -> employeeService.delete(employee.getId()));
    }

    @Test
    void delete_employee_failed() {

        doReturn(true, true).when(employeeRepository).existsById(anyLong());

        assertFalse(employeeService.delete(employee.getId()));
    }

    @Test
    void delete_employee_successfully() {

        doReturn(true, false).when(employeeRepository).existsById(anyLong());

        assertTrue(employeeService.delete(employee.getId()));
    }

    @Test
    void delete_employeeId_notFound() {

        assertThrows(HmsException.class,() -> employeeService.delete(employee.getId()));
    }

    @Test
    void find_idNull_throwException() {

        assertThrows(HmsException.class, () -> employeeService.find(null));
    }

    @Test
    void find_successfully() {

        doReturn(Optional.of(employee)).when(employeeRepository).findById(anyLong());

        assertEquals(employee.getId(),employeeService.find(1L).getId());
    }

    @Test
    void find_notFound() {

        doReturn(Optional.empty()).when(employeeRepository).findById(anyLong());

        assertThrows(HmsException.class,() -> employeeService.find(1L));
    }

}