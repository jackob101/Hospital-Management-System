package com.jackob101.hms.unittests.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackob101.hms.api.user.EmployeeApi;
import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.IEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("no-security")
@WebMvcTest(EmployeeApi.class)
class EmployeeApiTest {

    @MockBean
    IEmployeeService employeeService;

    @Autowired
    MockMvc mockMvc;

    Employee employee;

    EmployeeForm employeeForm;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {


        employeeForm = new EmployeeForm();
        employeeForm.setId(1L);
        employeeForm.setUserDetailsId(1L);
        employeeForm.setSpecializations(Set.of(1L));

        ModelMapper modelMapper = new ModelMapper();

        employee = modelMapper.map(employeeForm, Employee.class);

        objectMapper = new ObjectMapper();

    }

    @Test
    void create_employee_successfully() throws Exception {


        doReturn(employee).when(employeeService).createFromForm(any(EmployeeForm.class));

        String content = objectMapper.writeValueAsString(employeeForm);


        mockMvc.perform(post("/employee").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(employeeForm.getId()));


    }

    @Test
    void create_employee_bindingError() throws Exception {

        employeeForm.setUserDetailsId(null);
        String content = objectMapper.writeValueAsString(employeeForm);

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_employee_successfully() throws Exception {

        doReturn(employee).when(employeeService).find(anyLong());


        mockMvc.perform(get("/employee/" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeForm.getId()));
    }

    @Test
    void getAll_employee_successfully() throws Exception {

        doReturn(List.of(employee)).when(employeeService).findAll();


        mockMvc.perform(get("/employee/all"))
                .andExpect(status().isOk());
    }

    @Test
    void update_employee_successfully() throws Exception {


        doReturn(employee).when(employeeService).updateFromForm(any(EmployeeForm.class));

        String content = objectMapper.writeValueAsString(employeeForm);

        mockMvc.perform(put("/employee").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(employeeForm.getId()));

    }


    @Test
    void update_employee_bindingError() throws Exception {

        employeeForm.setUserDetailsId(null);
        String content = objectMapper.writeValueAsString(employeeForm);

        mockMvc.perform(put("/employee")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest());
    }
}
