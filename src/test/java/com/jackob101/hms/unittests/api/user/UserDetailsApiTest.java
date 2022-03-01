package com.jackob101.hms.unittests.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jackob101.hms.api.user.UserDetailsApi;
import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles("no-security")
//@WebMvcTest(UserDetailsApi.class)
class UserDetailsApiTest {

    String requestMapping = "/" + UserDetailsApi.REQUEST_MAPPING;

    @MockBean
    IUserDetailsService userDetailsService;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper;

    UserDetailsForm userDetailsForm;

    UserDetails userDetails;

    //    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        userDetailsForm = new UserDetailsForm(1L,
                "asd",
                "123123",
                "Tom",
                "John",
                "Nhoj",
                LocalDate.now(),
                "123123123");

        userDetails = new ModelMapper().map(userDetailsForm, UserDetails.class);
    }

    //    @Test
    void create_user_details_successfully() throws Exception {

        doReturn(userDetails).when(userDetailsService).createFromForm(Mockito.any(UserDetailsForm.class));

        String content = objectMapper.writeValueAsString(userDetailsForm);

        mockMvc.perform(post(requestMapping)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.firstName").value(userDetailsForm.getFirstName()));

    }


    //    @Test
    void get_user_details_by_id() throws Exception {

        doReturn(userDetails).when(userDetailsService).find(anyLong());

        mockMvc.perform(get(requestMapping + "/" + 1))
                .andDo(print())
                .andExpect(jsonPath("$.id").value(userDetailsForm.getId()))
                .andExpect(jsonPath("$.firstName").value(userDetailsForm.getFirstName()));
    }


    //    @Test
    void update_user_details_successfully() throws Exception {

        doReturn(userDetails).when(userDetailsService).updateFromForm(any(UserDetailsForm.class));

        String content = objectMapper.writeValueAsString(userDetailsForm);

        mockMvc.perform(put(requestMapping)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(jsonPath("$.id").value(userDetailsForm.getId()));
    }
}