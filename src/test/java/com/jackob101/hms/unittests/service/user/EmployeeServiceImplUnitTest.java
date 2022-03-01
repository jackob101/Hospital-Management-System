package com.jackob101.hms.unittests.service.user;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import com.jackob101.hms.service.user.implementation.EmployeeService;
import com.jackob101.hms.unittests.service.BaseServiceUnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.EnumMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplUnitTest extends BaseServiceUnitTest<Employee, IEmployeeService> {

    @Mock
    EmployeeRepository repository;

    UserDetails userDetails;

    Specialization specialization;

    @Override
    protected IEmployeeService configureService() {
        return new EmployeeService(getValidationUtils(), repository);
    }

    @Override
    protected Employee configureEntity() {
        this.userDetails = UserDetails.builder()
                .id(1L)
                .firstName("Tom")
                .lastName("Mot")
                .build();

        this.specialization = new Specialization(1L, "Doctor");

        return Employee.builder()
                .id(1L)
                .userDetails(userDetails)
                .specializations(Set.of(specialization))
                .build();
    }

    @Override
    protected JpaRepository<Employee, Long> configureRepository() {
        return repository;
    }


    @Override
    protected void configureCallbacks(EnumMap<BaseTestNames, TestCallbacks> callbacks) {

        callbacks.get(BaseTestNames.UPDATE_SUCCESSFULLY).setAfter(employee -> {
            assertEquals(getEntity().getId(), employee.getId());
            assertEquals(getEntity().getSpecializations(), employee.getSpecializations());
        });
    }
    @Test
    void create_employeeUserDetailsNull_throwException() {

        this.getEntity().setUserDetails(null);

        assertThrows(HmsException.class, () -> getService().create(getEntity()));
    }

    @Test
    void update_employeeUserDetailsNull_throwException() {

        this.getEntity().setUserDetails(null);

        assertThrows(HmsException.class, () -> getService().update(getEntity()));
    }

    @Test
    void update_employeeIdLessThanZero_throwException() {

        configureEntity().setId(-10L);

        assertThrows(HmsException.class, () -> getService().update(getEntity()));
    }

}