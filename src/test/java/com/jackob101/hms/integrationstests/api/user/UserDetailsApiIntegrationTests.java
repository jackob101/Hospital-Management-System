package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.TestUtils.data.user.UserDetailsGenerator;
import com.jackob101.hms.api.user.UserDetailsApi;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;

public class UserDetailsApiIntegrationTests extends BaseApiIntegrationTest<UserDetails, UserDetails> {


    @Autowired
    UserDetailsRepository userDetailsRepository;


    List<UserDetails> userDetails;

    @Override
    protected String configureRequestMapping() {
        return UserDetailsApi.REQUEST_MAPPING;
    }

    @Override
    protected UserDetails configureForm() {
        UserDetails form = new UserDetailsGenerator().generateSingle();
        form.setId(userDetails.get(0).getId());
        return form;

    }

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<UserDetails, UserDetails>.TestCallbacks> callbacks) {

        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setUserCredentialsId(""));

        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setUserCredentialsId(""));
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
