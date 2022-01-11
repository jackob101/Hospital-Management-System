package com.jackob101.hms.service.user.definition;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.base.CrudService;

public interface IEmployeeService extends CrudService<Employee, Long> {

    Employee createFromForm(EmployeeForm employeeForm);

    Employee updateFromForm(EmployeeForm employeeForm);
}
