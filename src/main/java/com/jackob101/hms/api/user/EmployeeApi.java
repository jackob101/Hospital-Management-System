package com.jackob101.hms.api.user;

import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequestMapping(EmployeeApi.REQUEST_MAPPING)
@RestController()
public class EmployeeApi {

    public static final String REQUEST_MAPPING = "employee";
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;

    public EmployeeApi(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.modelMapper = new ModelMapper();
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createEmployee(@RequestBody Employee employee) throws URISyntaxException {

        log.info("Creating new employee");

        Employee saved = employeeService.create(employee);

        return ResponseEntity.created(new URI("/" + REQUEST_MAPPING + "/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getEmployee(@PathVariable("id") Long id) {

        log.info("Fetching employee with id: " + id);

        Employee employee = employeeService.find(id);

        log.info("Employee with id: " + id + " was fetched");

        return ResponseEntity.ok(employee);

    }
}
