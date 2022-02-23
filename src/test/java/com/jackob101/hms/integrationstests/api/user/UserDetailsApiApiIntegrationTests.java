package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.UserDetailsApi;
import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.UserDetailsGenerator;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

public class UserDetailsApiApiIntegrationTests extends BaseApiIntegrationTest<UserDetails, UserDetailsForm> {


    @Autowired
    UserDetailsRepository userDetailsRepository;

    UserDetailsForm userDetailsForm;

    List<UserDetails> userDetails;

    @BeforeEach
    void setUp() {
        configure(UserDetailsApi.REQUEST_MAPPING);

        userDetails = userDetailsRepository.saveAll(new UserDetailsGenerator().generate(5));

        userDetailsForm = new UserDetailsForm(9999L,
                "123123123",
                "123123123",
                "Tom",
                "Mot",
                "John",
                LocalDate.now(),
                "123123123");
        userDetailsForm.setId(userDetails.get(0).getId());

        setId(userDetails.get(0).getId());
        setForm(userDetailsForm);
        setModelClass(UserDetails.class);
        setArrayModelClass(UserDetails[].class);

        callbacks.setCreateSuccessfullyBefore(form -> form.setId(null));

        callbacks.setFindFailedBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.setUpdateFailedBefore(form -> form.setFirstName(""));
    }

    @AfterEach
    void tearDown() {
        userDetailsRepository.deleteAll();
    }
}
