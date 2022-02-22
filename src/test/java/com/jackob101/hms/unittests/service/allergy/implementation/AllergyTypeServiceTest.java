package com.jackob101.hms.unittests.service.allergy.implementation;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.allergy.implementation.AllergyTypeService;
import com.jackob101.hms.unittests.service.TestCallbacks;
import com.jackob101.hms.unittests.service.TestName;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AllergyTypeServiceTest extends BaseServiceTest<AllergyType> {

    @Mock
    AllergyTypeRepository allergyTypeRepository;

    @Override
    protected void configure() {
        AllergyTypeService service = new AllergyTypeService(validationUtils, allergyTypeRepository);

        configure(allergyTypeRepository, AllergyType.class, service);
    }

    @Override
    protected void setUpData() {

        AllergyType allergyType = new AllergyType("Test allergen");
        allergyType.setId(1L);

        this.entity = allergyType;
    }

    @Override
    protected void setUpCallbacks(Map<TestName, TestCallbacks<AllergyType>> configs) {

    }
}