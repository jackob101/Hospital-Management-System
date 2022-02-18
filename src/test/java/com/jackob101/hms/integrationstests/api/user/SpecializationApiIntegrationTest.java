package com.jackob101.hms.integrationstests.api.user;

import com.jackob101.hms.api.user.SpecializationAPI;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.user.SpecializationGenerator;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SpecializationApiIntegrationTest extends BaseIntegrationTest<Specialization, Specialization> {

    @Autowired
    SpecializationRepository specializationRepository;

    List<Specialization> specializationList;

    Specialization specialization;

    @BeforeEach
    void setUp() {

        configure(SpecializationAPI.REQUEST_MAPPING);

        specialization = new Specialization(1L, "Doctor");

        specializationList = specializationRepository.saveAll(new SpecializationGenerator().generate(10));

        setId(specializationList.get(0).getId());
        setForm(specialization);
        setModelClass(Specialization.class);
        setArrayModelClass(Specialization[].class);

        callbacks.setCreateSuccessfullyBefore(model -> model.setId(null));

        callbacks.setUpdateSuccessfullyBefore(model -> model.setId(specializationList.get(0).getId()));

        callbacks.setFindFailedBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.setUpdateFailedBefore(model -> model.setId(specializationList.get(0).getId()));

        callbacks.setCreateFailedBefore(model -> model.setName(""));

        callbacks.setUpdateFailedBefore(model -> {
            model.setName("");
            model.setId(specializationList.get(0).getId());
        });

    }

    @AfterEach
    void tearDown() {
        specializationRepository.deleteAll();
    }
}
