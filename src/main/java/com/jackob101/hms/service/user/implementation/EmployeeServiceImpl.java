package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

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
