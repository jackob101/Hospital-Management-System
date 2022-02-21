package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.base.BaseService;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class AllergenService extends BaseService<Allergen> implements IAllergenService {


    public AllergenService(Validator validator, AllergenRepository allergenRepository) {
        super(validator, Allergen.class, allergenRepository);
    }
}
