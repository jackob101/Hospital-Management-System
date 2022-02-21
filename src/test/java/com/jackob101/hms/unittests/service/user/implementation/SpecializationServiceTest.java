package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.service.user.implementation.SpecializationService;
import com.jackob101.hms.unittests.TestConfiguration;
import com.jackob101.hms.unittests.service.BaseTests;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceTest extends BaseServiceTest<Specialization, Specialization> {


    @Mock
    SpecializationRepository specializationRepository;

    @Override
    protected void configure() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        SpecializationService specializationService = new SpecializationService(specializationRepository, validator);
        configure(specializationRepository, Specialization.class, specializationService);
    }

    @Override
    protected void setUpData() {
        Specialization specialization = new Specialization();

        specialization.setName("Doctor");
        specialization.setId(1L);

        this.entity = specialization;
    }

    @Override
    protected void setUpCallbacks(Map<BaseTests, TestConfiguration<Specialization, Specialization>> configs) {

    }

}