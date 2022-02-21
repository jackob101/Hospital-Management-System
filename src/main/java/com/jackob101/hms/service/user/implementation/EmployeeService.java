package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.base.BaseFormService;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService extends BaseFormService<Employee, EmployeeForm> implements IEmployeeService {

    private final IUserDetailsService userDetailsService;
    private final ISpecializationService specializationService;

    public EmployeeService(Validator validator, EmployeeRepository employeeRepository, IUserDetailsService userDetailsService, ISpecializationService specializationService) {
        super(validator, Employee.class, employeeRepository);
        this.userDetailsService = userDetailsService;
        this.specializationService = specializationService;
    }

    @Override
    public Employee convert(EmployeeForm employeeForm) {

        ModelMapper modelMapper = new ModelMapper();

        Employee employee = modelMapper.map(employeeForm, Employee.class);

        UserDetails userDetails = userDetailsService.find(employeeForm.getUserDetailsId());

        employee.setUserDetails(userDetails);

        Set<Specialization> specializations = new HashSet<>();

        if (employeeForm.getSpecializations() != null) {
            specializations = employeeForm.getSpecializations().stream()
                    .map(specializationService::find)
                    .collect(Collectors.toSet());

        }
        employee.setSpecializations(specializations);

        return employee;
    }
}
