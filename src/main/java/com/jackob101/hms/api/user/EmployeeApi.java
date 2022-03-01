package com.jackob101.hms.api.user;

import com.jackob101.hms.api.BaseController;
import com.jackob101.hms.converter.user.EmployeeFormConverter;
import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(EmployeeApi.REQUEST_MAPPING)
@RestController()
public class EmployeeApi extends BaseController<Employee, EmployeeForm> {

    public static final String REQUEST_MAPPING = "employees";

    public EmployeeApi(IEmployeeService service, EmployeeFormConverter converter) {
        super(service, REQUEST_MAPPING, converter);
    }

}
