package com.jackob101.hms.unittests.service.allergy.implementation;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import com.jackob101.hms.service.allergy.implementation.AllergenService;
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
class AllergenServiceTest extends BaseServiceTest<Allergen, Allergen> {

    @Mock
    AllergenRepository allergenRepository;

    @Override
    protected void configure() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        AllergenService service = new AllergenService(validator, allergenRepository);

        configure(allergenRepository, Allergen.class, service);

    }

    @Override
    protected void setUpData() {
        Allergen allergen = new Allergen("Test allergen");
        allergen.setId(1L);
        this.entity = allergen;
    }

    @Override
    protected void setUpCallbacks(Map<BaseTests, TestConfiguration<Allergen, Allergen>> configs) {

    }
}