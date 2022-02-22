package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.EmployeeService;
import com.jackob101.hms.unittests.service.TestCallbacks;
import com.jackob101.hms.unittests.service.TestFormCallbacks;
import com.jackob101.hms.unittests.service.base.BaseFormServiceTest;
import com.jackob101.hms.unittests.service.base.TestName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.EnumMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest extends BaseFormServiceTest<Employee, EmployeeForm, IEmployeeService> {

    @Mock
    IUserDetailsService userDetailsService;

    @Mock
    ISpecializationService specializationService;

    @Mock
    EmployeeRepository repository;

    UserDetails userDetails;
    Specialization specialization;

    @Override
    protected void configure() {

        EmployeeService service = new EmployeeService(getValidationUtils(), repository, userDetailsService, specializationService);
        configure(repository, service);

        configureFormCallbacks();
        configureBaseCallbacks();

    }

    private void configureFormCallbacks() {

        EnumMap<TestName, TestFormCallbacks<Employee, EmployeeForm>> formCallbacks = getFormCallbacks();
        TestFormCallbacks<Employee, EmployeeForm> createForm = new TestFormCallbacks<>();
        createForm.setBeforeForm((employee, employeeForm) -> {
            doReturn(userDetails).when(userDetailsService).find(anyLong());
        });
        formCallbacks.put(TestName.CREATE_FROM_FORM_SUCCESSFULLY, createForm);

        TestFormCallbacks<Employee, EmployeeForm> updateForm = new TestFormCallbacks<>();
        updateForm.setBeforeForm(createForm.getBeforeForm());
        formCallbacks.put(TestName.UPDATE_FROM_FORM_SUCCESSFULLY, updateForm);

        setFormCallbacks(formCallbacks);
    }

    private void configureBaseCallbacks() {

        EnumMap<TestName, TestCallbacks<Employee>> callbacks = getCallbacks();
        TestCallbacks<Employee> updateSuccessfully = new TestCallbacks<>();
        updateSuccessfully.setAfter(employee1 -> {
            assertEquals(getEntity().getId(), employee1.getId());
            assertEquals(getEntity().getSpecializations(), employee1.getSpecializations());
        });
        callbacks.put(TestName.UPDATE_SUCCESSFULLY, updateSuccessfully);

        setCallbacks(callbacks);
    }

    @Override
    protected void setUpData() {

        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("Mot")
                .build();

        this.specialization = new Specialization(1L, "Doctor");

        Employee entity = Employee.builder()
                .id(1L)
                .userDetails(userDetails)
                .specializations(Set.of(specialization))
                .build();

        EmployeeForm employeeForm = new EmployeeForm();
        employeeForm.setUserDetailsId(userDetails.getId());
        employeeForm.setId(1L);
        employeeForm.setSpecializations(Set.of(1L));

        setData(entity, employeeForm);

    }

    @Test
    void create_employeeUserDetailsNull_throwException() {

        this.getEntity().setUserDetails(null);

        assertThrows(HmsException.class, () -> getService().create(this.getEntity()));
    }

    @Test
    void update_employeeUserDetailsNull_throwException() {

        this.getEntity().setUserDetails(null);

        assertThrows(HmsException.class, () -> getService().update(getEntity()));
    }

    @Test
    void update_employeeIdLessThanZero_throwException() {

        getEntity().setId(-10L);

        assertThrows(HmsException.class, () -> getService().update(getEntity()));
    }

    @Test
    void updateFromForm_employeeForm_successfully() {

        EmployeeForm employeeForm = new EmployeeForm();

        employeeForm.setId(getEntity().getId());
        employeeForm.setUserDetailsId(getEntity().getUserDetails().getId());
        employeeForm.setSpecializations(Set.of(1L));

        doReturn(userDetails).when(userDetailsService).find(anyLong());
        doReturn(true).when(repository).existsById(anyLong());
        doAnswer(returnsFirstArg()).when(repository).save(any(Employee.class));

        Employee updated = getService().updateFromForm(employeeForm);

        assertEquals(getEntity().getId(), updated.getId());
        assertEquals(getEntity().getUserDetails().getId(), updated.getUserDetails().getId());
//        assertEquals(employee.getSpecializations().size(), updated.getSpecializations().size());

    }
}