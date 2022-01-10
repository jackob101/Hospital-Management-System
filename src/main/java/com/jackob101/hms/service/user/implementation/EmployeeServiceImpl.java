package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.EmployeeService;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl extends BaseService<Employee> implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserDetailsService userDetailsService;


    public EmployeeServiceImpl(Validator validator, EmployeeRepository employeeRepository, UserDetailsService userDetailsService) {
        super(validator, "Employee");
        this.employeeRepository = employeeRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Employee create(Employee entity) {
        validate(entity, OnCreate.class);

        if (entity.getId() != null && employeeRepository.existsById(entity.getId()))
            throw HmsException.params("Employee", entity.getId()).code("service.create.id_is_taken");

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
            throw HmsException.params("Employee", entity.getId()).code("service.update.entity_not_found");

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
            throw HmsException.params("Employee").code("service.delete.id_null");

        boolean isFound = employeeRepository.existsById(id);

        if (!isFound)
            throw HmsException.params("Employee", id).code("service.delete.id_not_found");

        employeeRepository.deleteById(id);

        return !employeeRepository.existsById(id);
    }

    @Override
    public Employee find(Long id) {

        if (id == null)
            throw HmsException.params("Employee").code("service.find.id_null");

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        return optionalEmployee.orElseThrow(() -> HmsException.params(id).code("employee_service.find.not_found"));
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

        return employee;
    }
}
