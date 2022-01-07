package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.EmployeeService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl extends BaseService<Employee> implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(Validator validator, EmployeeRepository employeeRepository) {
        super(validator, "employee.null", "employee.validation.failed");
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee entity) {
        validate(entity, OnCreate.class);

        if (entity.getId() != null && employeeRepository.existsById(entity.getId()))
            throw new HmsException("employee.already_exists");

        return employeeRepository.save(entity);
    }

    @Override
    public Employee update(Employee entity) {

        validate(entity, OnUpdate.class);

        if (!employeeRepository.existsById(entity.getId()))
            throw new HmsException("employee.not_found", entity.getId());

        return employeeRepository.save(entity);
    }

    @Override
    public boolean delete(Long id) {

        if(id == null)
            throw new HmsException("service.delete.id_null", "Employee");

        boolean isFound = employeeRepository.existsById(id);

        if (!isFound)
            throw new HmsException("service.delete.id_not_found", "Employee", id);

        employeeRepository.deleteById(id);

        return !employeeRepository.existsById(id);
    }

    @Override
    public Employee find(Long id) {

        if (id == null)
            throw new HmsException("employee_service.find.id_cannot_be_null");

        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        return optionalEmployee.orElseThrow(() -> new HmsException("employee_service.find.not_found", id));
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
