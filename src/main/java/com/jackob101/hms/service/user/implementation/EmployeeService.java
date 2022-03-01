package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends BaseService<Employee> implements IEmployeeService {

    public EmployeeService(ValidationUtils validator, EmployeeRepository employeeRepository) {
        super(validator, Employee.class, employeeRepository);
    }
}
