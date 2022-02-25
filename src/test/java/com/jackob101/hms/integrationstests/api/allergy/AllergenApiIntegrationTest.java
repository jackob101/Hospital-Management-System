package com.jackob101.hms.integrationstests.api.allergy;


import com.jackob101.hms.api.allergy.AllergenApi;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.allergy.AllergenGenerator;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;

public class AllergenApiIntegrationTest extends BaseApiIntegrationTest<Allergen, Allergen> {

    @Autowired
    AllergenRepository allergenRepository;

    List<Allergen> allergenList;

    @Override
    protected String configureRequestMapping() {
        return AllergenApi.REQUEST_MAPPING;
    }

    @Override
    protected Allergen configureForm() {
        Allergen form = new Allergen("Test allergen");
        form.setId(allergenList.get(0).getId());
        return form;
    }

    @Override
    protected Long configureId() {
        return allergenList.get(0).getId();
    }

    @Override
    protected void createMockData() {
        allergenList = allergenRepository.saveAll(new AllergenGenerator().generate(10));
    }

    @Override
    protected void clearMockData() {
        allergenRepository.deleteAll();
    }

    @Override
    protected void configureCallbacks(EnumMap<TestName, BaseApiIntegrationTest<Allergen, Allergen>.TestCallbacks> callbacks) {

        callbacks.get(TestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(TestName.FIND_ENTITY_NOT_FOUND).setBefore(form -> setId(Long.MAX_VALUE));

        callbacks.get(TestName.UPDATE_ENTITY_FAILED).setBefore(form -> form.setName(""));
    }

}
