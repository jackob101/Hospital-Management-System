package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class AllergyTypeService extends BaseService<AllergyType> implements IAllergyTypeService {


    public AllergyTypeService(ValidationUtils validator, AllergyTypeRepository allergyTypeRepository) {
        super(validator, AllergyType.class, allergyTypeRepository);
    }


}
