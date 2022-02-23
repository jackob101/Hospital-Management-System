package com.jackob101.hms.unittests.service.allergy;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.allergy.implementation.AllergyTypeService;
import com.jackob101.hms.unittests.service.base.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
class AllergyTypeServiceUnitTest extends BaseServiceUnitTest<AllergyType, IAllergyTypeService> {

    @Mock
    AllergyTypeRepository repository;

    @Override
    protected IAllergyTypeService configureService() {
        return new AllergyTypeService(getValidationUtils(), repository);
    }

    @Override
    protected AllergyType configureEntity() {
        AllergyType allergyType = new AllergyType("Test allergen");
        allergyType.setId(1L);
        return allergyType;
    }

    @Override
    protected JpaRepository<AllergyType, Long> configureRepository() {
        return repository;
    }
}