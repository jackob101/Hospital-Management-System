package com.jackob101.hms.unittests.service.allergy.implementation;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.allergy.implementation.AllergenService;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AllergenServiceTest extends BaseServiceTest<Allergen, IAllergenService> {

    @Mock
    AllergenRepository repository;

    @Override
    protected void configure() {
        AllergenService service = new AllergenService(validationUtils, repository);

        configure(repository, Allergen.class, service);

    }

    @Override
    protected void setUpData() {
        Allergen allergen = new Allergen("Test allergen");
        allergen.setId(1L);
        this.entity = allergen;
    }
}