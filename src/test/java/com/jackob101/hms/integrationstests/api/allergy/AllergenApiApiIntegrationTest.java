package com.jackob101.hms.integrationstests.api.allergy;


import com.jackob101.hms.api.allergy.AllergenApi;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.allergy.AllergenGenerator;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AllergenApiApiIntegrationTest extends BaseApiIntegrationTest<Allergen, Allergen> {

    @Autowired
    AllergenRepository allergenRepository;

    List<Allergen> allergenList;

    @BeforeEach
    void setUp() {


        allergenList = allergenRepository.saveAll(new AllergenGenerator().generate(10));
        Allergen form = new Allergen("Test allergen");
        form.setId(allergenList.get(0).getId());

        setId(allergenList.get(0).getId());
        configure(AllergenApi.REQUEST_MAPPING);
        setForm(form);
        setArrayModelClass(Allergen[].class);
        setModelClass(Allergen.class);

        callbacks.setCreateSuccessfullyBefore(model -> model.setId(null));

        callbacks.setFindFailedBefore(aLong -> setId(Long.MAX_VALUE));

        callbacks.setUpdateFailedBefore(model -> model.setName(""));
    }

    @AfterEach
    void tearDown() {
        allergenRepository.deleteAll();
    }
}
