package com.jackob101.hms.api.allergy;

import com.jackob101.hms.api.BaseController;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = AllergyTypeApi.REQUEST_MAPPING, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AllergyTypeApi extends BaseController<AllergyType, AllergyType> {

    public final static String REQUEST_MAPPING = "allergy_types";

    public AllergyTypeApi(IAllergyTypeService allergyTypeService) {
        super(allergyTypeService, REQUEST_MAPPING, null);
    }

}
