package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.SpecializationAPI;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.SpecializationGenerator;
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
        return new Specialization(1L, "Doctor");
    }

    @Override
    protected Long configureId() {
        return specializationList.get(0).getId();
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
    protected void configureCallbacks(EnumMap<TestName, BaseApiIntegrationTest<Specialization, Specialization>.TestCallbacks> callbacks) {

        callbacks.get(TestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(TestName.UPDATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(specializationList.get(0).getId()));

        callbacks.get(TestName.FIND_ENTITY_NOT_FOUND).setBefore(form -> setId(Long.MAX_VALUE));

        callbacks.get(TestName.UPDATE_ENTITY_FAILED).setBefore(form -> {
            form.setName("");
            form.setId(specializationList.get(0).getId());
        });

        callbacks.get(TestName.CREATE_ENTITY_FAILED).setBefore(form -> form.setName(""));

    }
}
