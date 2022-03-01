package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.TestUtils.data.user.SpecializationGenerator;
import com.jackob101.hms.api.user.SpecializationAPI;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;

public class SpecializationApiIntegrationTest extends BaseApiIntegrationTest<Specialization, Specialization> {

    @Autowired
    SpecializationRepository specializationRepository;

    List<Specialization> specializationList;

    @Override
    protected String configureRequestMapping() {
        return SpecializationAPI.REQUEST_MAPPING;
    }

    @Override
    protected Specialization configureForm() {
        return new Specialization(specializationList.get(0).getId(), "Doctor");
    }

    @Override
    protected void createMockData() {
        specializationList = specializationRepository.saveAll(new SpecializationGenerator().generate(10));
    }

    @Override
    protected void clearMockData() {
        specializationRepository.deleteAll();
    }

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<Specialization, Specialization>.TestCallbacks> callbacks) {

        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setName(""));

        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setName(""));

    }
}
