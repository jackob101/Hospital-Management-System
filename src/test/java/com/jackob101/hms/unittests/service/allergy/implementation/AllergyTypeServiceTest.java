package com.jackob101.hms.unittests.service.allergy.implementation;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.allergy.implementation.AllergyTypeService;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AllergyTypeServiceTest extends BaseServiceTest<AllergyType, IAllergyTypeService> {

    @Mock
    AllergyTypeRepository repository;

    @Override
    protected void configure() {
        AllergyTypeService service = new AllergyTypeService(getValidationUtils(), repository);

        configure(repository, service);
    }

    @Override
    protected void setUpData() {

        AllergyType allergyType = new AllergyType("Test allergen");
        allergyType.setId(1L);

        setData(allergyType);
    }
}