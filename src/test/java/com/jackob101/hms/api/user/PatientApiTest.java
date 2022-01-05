package com.jackob101.hms.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.service.user.definition.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("no-security")
@WebMvcTest(PatientApi.class)
class PatientApiTest {

    String requestMapping = "/" + PatientApi.REQUEST_MAPPING;

    @MockBean
    PatientService patientService;

    @Autowired
    MockMvc mockMvc;

    ModelMapper modelMapper = new ModelMapper();

    PatientDTO patientDTO;
    Patient patient;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO(1L,
                1L,
                MaritalStatus.SINGLE,
                "none",
                "Poland",
                Gender.MALE,
                "Polish");

        patient = new ModelMapper().map(patientDTO, Patient.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void create_patient_when_bindings_errors() throws Exception {

        patientDTO.setUserDetailsId(null);

        mockMvc.perform(post(requestMapping)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void create_patient_successfully() throws Exception {

        doReturn(patient).when(patientService).create(any(Patient.class), anyLong());

        mockMvc.perform(post(requestMapping)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(jsonPath("$.id" ).value(patientDTO.getId()));

    }
}