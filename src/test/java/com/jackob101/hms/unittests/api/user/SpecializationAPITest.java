package com.jackob101.hms.unittests.api.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jackob101.hms.api.user.SpecializationAPI;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("no-security")
@WebMvcTest(SpecializationAPI.class)
class SpecializationAPITest {

    String MAPPING = "/specialization";

    @MockBean
    ISpecializationService specializationService;

    @Autowired
    Validator validator;

    @Autowired
    MockMvc mockMvc;

    Specialization specialization;

    ObjectMapper mapper;

    String specializationJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        specialization = new Specialization();
        specialization.setId(1L);
        specialization.setName("Doctor");

        mapper = new ObjectMapper();
        specializationJson = mapper.writeValueAsString(specialization);
        doReturn(specialization).when(specializationService).create(any(Specialization.class));
        doReturn(specialization).when(specializationService).update(any(Specialization.class));
        doReturn(specialization).when(specializationService).find(anyLong());
    }

    @Test
    void create_specialization_success() throws Exception {

        doReturn(specialization).when(specializationService).create(any(Specialization.class));


        mockMvc.perform(
                        post(MAPPING)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(specializationJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(specialization.getId()))
                .andExpect(jsonPath("$.name").value(specialization.getName()));
    }

//    @test
////    void create_specializationblankname_failed() throws exception {
////
////        specialization.setname("");
////
////        mockmvc.perform(
////                        post(mapping)
////                                .contenttype(mediatype.application_json_value)
////                                .content(specializationjson))
////                .andexpect(status().isbadrequest());
//    }

    @Test
    void update_specialization_successfully() throws Exception {

        mockMvc.perform(
                        put(MAPPING)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(specializationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(specialization.getId()))
                .andExpect(jsonPath("$.name").value(specialization.getName()));

    }

//    @Test
//    void update_specializationBlankName_failed() throws Exception {
//
//        specialization.setName("");
//
//        mockMvc.perform(
//                        put(MAPPING)
//                                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                                .content(specializationJson))
//                .andExpect(status().isBadRequest());
//    }
}