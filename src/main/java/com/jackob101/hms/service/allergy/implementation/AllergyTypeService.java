package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.base.BaseService;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class AllergyTypeService extends BaseService<AllergyType> implements IAllergyTypeService {


    public AllergyTypeService(Validator validator, AllergyTypeRepository allergyTypeRepository) {
        super(validator, AllergyType.class, allergyTypeRepository);
    }


}
