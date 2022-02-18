package com.jackob101.hms.api.allergy;

import com.jackob101.hms.api.base.BaseModelController;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = AllergenApi.REQUEST_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AllergenApi extends BaseModelController<Allergen> {

    public final static String REQUEST_MAPPING = "allergens";

    public AllergenApi(IAllergenService allergenService) {
        super(allergenService, "Allergen", AllergenApi.REQUEST_MAPPING);
    }
}
