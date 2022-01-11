package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService extends BaseService<Employee> implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IUserDetailsService userDetailsService;
    private final ISpecializationService specializationService;


    public EmployeeService(Validator validator, EmployeeRepository employeeRepository, IUserDetailsService userDetailsService, ISpecializationService specializationService) {
        super(validator, "Employee");
        this.employeeRepository = employeeRepository;
        this.userDetailsService = userDetailsService;
        this.specializationService = specializationService;
    }

    @Override
    public Employee create(Employee entity) {
        validate(entity, OnCreate.class);

        if (entity.getId() != null && employeeRepository.existsById(entity.getId()))
            throw HmsException.params(entity.getId()).code("Employee ID: %s is already taken");

        return employeeRepository.save(entity);
    }

    @Override
    public Employee createFromForm(EmployeeForm employeeForm) {

        Employee employee = convertToModel(employeeForm);

        //TODO specializations

        return create(employee);
    }

    @Override
    public Employee update(Employee entity) {

        validate(entity, OnUpdate.class);

        if (!employeeRepository.existsById(entity.getId()))
            throw HmsException.params(entity.getId()).code("Employee update failed because entity with ID %s does not exists");

        return employeeRepository.save(entity);
    }

    @Override
    public Employee updateFromForm(EmployeeForm employeeForm) {

        Employee employee = convertToModel(employeeForm);

        return update(employee);
    }


    @Override
    public boolean delete(Long id) {

        if (id == null)
            throw HmsException.code("Could not delete Employee because given ID is null");

        boolean isFound = employeeRepository.existsById(id);

        if (!isFound)
            throw HmsException.params(id).code("Could not delete Employee because entity with ID %s was not found");

        employeeRepository.deleteById(id);

        return !employeeRepository.existsById(id);
    }

    @Override
    public Employee find(Long id) {

        if (id == null)
            throw HmsException.code("Could not find Employee because given ID is null");

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        return optionalEmployee.orElseThrow(() -> HmsException.params(id).code("Couldn't find Employee with ID %s"));
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    private Employee convertToModel(EmployeeForm employeeForm) {

        ModelMapper modelMapper = new ModelMapper();

        Employee employee = modelMapper.map(employeeForm, Employee.class);

        UserDetails userDetails = userDetailsService.find(employeeForm.getUserDetailsId());

        employee.setUserDetails(userDetails);

        Set<Specialization> specializations = employeeForm.getSpecializations().stream()
                .map(specializationService::find)
                .collect(Collectors.toSet());

        employee.setSpecializations(specializations);

        return employee;
    }
}
