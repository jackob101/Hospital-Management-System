package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.UserDetailsApi;
import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.UserDetailsGenerator;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

public class UserDetailsApiIntegrationTests extends BaseApiIntegrationTest<UserDetails, UserDetailsForm> {


    @Autowired
    UserDetailsRepository userDetailsRepository;


    List<UserDetails> userDetails;

    @Override
    protected String configureRequestMapping() {
        return UserDetailsApi.REQUEST_MAPPING;
    }

    @Override
    protected UserDetailsForm configureForm() {


        UserDetailsForm userDetailsForm = new UserDetailsForm(9999L,
                "123123123",
                "123123123",
                "Tom",
                "Mot",
                "John",
                LocalDate.now(),
                "123123123");
        userDetailsForm.setId(userDetails.get(0).getId());
        return userDetailsForm;
    }

    @Override
    protected Long configureId() {
        return userDetails.get(0).getId();
    }

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<UserDetails, UserDetailsForm>.TestCallbacks> callbacks) {

        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(ITestName.FIND_ENTITY_NOT_FOUND).setBefore(id -> setId(Long.MAX_VALUE));

        callbacks.get(ITestName.UPDATE_ENTITY_FAILED).setBefore(form -> form.setFirstName(""));
    }

    @Override
    protected void createMockData() {
        userDetails = userDetailsRepository.saveAll(new UserDetailsGenerator().generate(5));
    }

    @Override
    protected void clearMockData() {
        userDetailsRepository.deleteAll();
    }
}
