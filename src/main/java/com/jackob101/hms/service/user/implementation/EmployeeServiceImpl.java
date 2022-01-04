package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public Employee save(Employee entity) {
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
}
