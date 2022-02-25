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

public class UserDetailsApiApiIntegrationTests extends BaseApiIntegrationTest<UserDetails, UserDetailsForm> {


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
    protected void configureCallbacks(EnumMap<TestName, BaseApiIntegrationTest<UserDetails, UserDetailsForm>.TestCallbacks> callbacks) {

        TestCallbacks createSuccessfully = new TestCallbacks();
        createSuccessfully.setBefore(form -> form.setId(null));
        callbacks.put(TestName.CREATE_ENTITY_SUCCESSFULLY, createSuccessfully);

        TestCallbacks findFailed = new TestCallbacks();
        findFailed.setBefore(id -> setId(Long.MAX_VALUE));
        callbacks.put(TestName.FIND_ENTITY_NOT_FOUND, findFailed);

        TestCallbacks updateFailed = new TestCallbacks();
        updateFailed.setBefore(form -> form.setFirstName(""));
        callbacks.put(TestName.UPDATE_ENTITY_FAILED, updateFailed);
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
