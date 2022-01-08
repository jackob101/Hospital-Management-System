package com.jackob101.hms.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("no-security")
@WebMvcTest(UserDetailsApi.class)
class UserDetailsApiTest {

    String requestMapping = "/userdetails";

    @MockBean
    UserDetailsService userDetailsService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper;

    UserDetailsDTO userDetailsDTO;

    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetailsDTO = new UserDetailsDTO(1L,
                "asd",
                "123123",
                "Tom",
                "John",
                "Nhoj",
                LocalDate.now(),
                "123123123");

        userDetails = new ModelMapper().map(userDetailsDTO,UserDetails.class);
    }

    @Test
    void create_user_details_successfully() throws Exception {

        doAnswer(returnsFirstArg()).when(userDetailsService).create(Mockito.any(UserDetails.class));

        String content = objectMapper.writeValueAsString(userDetailsDTO);

        mockMvc.perform(post(requestMapping)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.firstName").value(userDetailsDTO.getFirstName()));

    }


    @Test
    void get_user_details_by_id() throws Exception {

        doReturn(userDetails).when(userDetailsService).find(anyLong());

        mockMvc.perform(get(requestMapping+ "/"+1))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(userDetailsDTO.getId()))
                .andExpect(jsonPath("$.firstName").value(userDetailsDTO.getFirstName()));
    }


    @Test
    void update_user_details_successfully() throws Exception {

        doAnswer(returnsFirstArg()).when(userDetailsService).update(any(UserDetails.class));

        String content = objectMapper.writeValueAsString(userDetailsDTO);

        mockMvc.perform(put(requestMapping)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content))
                .andExpect(jsonPath("$.id").value(userDetailsDTO.getId()));
    }
}