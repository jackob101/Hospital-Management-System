package com.jackob101.hms.api.user;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.EmployeeService;
import com.jackob101.hms.utils.ApiUtils;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequestMapping(EmployeeApi.REQUEST_MAPPING)
@RestController()
public class EmployeeApi {

    public static final String REQUEST_MAPPING = "employee";
    private final EmployeeService employeeService;

    public EmployeeApi(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createEmployee(@RequestBody @Validated(OnCreate.class) EmployeeForm employeeForm, BindingResult errors) throws URISyntaxException {

        log.info("Creating new employee");

        ApiUtils.checkBindings(errors, "Employee Form");

        Employee saved = employeeService.createFromForm(employeeForm);

        return ResponseEntity
                .created(new URI("/" + REQUEST_MAPPING + "/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getEmployee(@PathVariable("id") Long id) {

        log.info("Fetching employee with id: " + id);

        Employee employee = employeeService.find(id);

        log.info("Employee with id: " + id + " was fetched");

        return ResponseEntity.ok(employee);

    }

    @PutMapping
    public ResponseEntity<Object> updateEmployee(@RequestBody @Validated(OnUpdate.class) EmployeeForm employeeForm, BindingResult errors) {

        log.info("Updating employee with id: " + employeeForm.getId());

        ApiUtils.checkBindings(errors, "Employee Form");

        Employee employee = employeeService.updateFromForm(employeeForm);

        return ResponseEntity.ok(employee);
    }
}
