package com.jackob101.hms.unittests.service.allergy.implementation;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.allergy.implementation.AllergyTypeService;
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
class AllergyTypeServiceTest extends BaseServiceTest<AllergyType, AllergyType> {

    @Mock
    AllergyTypeRepository allergyTypeRepository;

    @Override
    protected void configure() {

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        AllergyTypeService service = new AllergyTypeService(validator, allergyTypeRepository);

        configure(allergyTypeRepository, AllergyType.class, service);
    }

    @Override
    protected void setUpData() {

        AllergyType allergyType = new AllergyType("Test allergen");
        allergyType.setId(1L);

        this.entity = allergyType;
    }

    @Override
    protected void setUpCallbacks(Map<BaseTests, TestConfiguration<AllergyType, AllergyType>> configs) {

    }
}