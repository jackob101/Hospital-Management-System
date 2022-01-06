package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.EmployeeService;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;

@Service
public class EmployeeServiceImpl extends BaseService<Employee> implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(Validator validator, EmployeeRepository employeeRepository) {
        super(validator, "employee.null", "employee.validation.failed");
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee create(Employee entity) {
        return null;
    }

    @Override
    public Employee update(Employee entity) {
        return null;
    }

    @Override
    public boolean delete(Employee entity) {
        return false;
    }

    @Override
    public Employee find(Long aLong) {
        return null;
    }

    @Override
    public List<Employee> findAll() {
        return null;
    }
}
