package com.jackob101.hms.unittests.service.allergy;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.allergy.implementation.AllergenService;
import com.jackob101.hms.unittests.service.base.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
class AllergenServiceUnitTest extends BaseServiceUnitTest<Allergen, IAllergenService> {

    @Mock
    AllergenRepository repository;

    @Override
    protected IAllergenService configureService() {
        return new AllergenService(getValidationUtils(), repository);
    }

    @Override
    protected Allergen configureEntity() {
        Allergen allergen = new Allergen("Test allergen");
        allergen.setId(1L);
        return allergen;
    }

    @Override
    protected JpaRepository<Allergen, Long> configureRepository() {
        return repository;
    }
}