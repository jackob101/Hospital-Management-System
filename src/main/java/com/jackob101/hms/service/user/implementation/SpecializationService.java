package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import org.springframework.stereotype.Service;

import javax.validation.Validator;

@Service
public class SpecializationService extends BaseService<Specialization> implements ISpecializationService {

    public SpecializationService(SpecializationRepository specializationRepository, Validator validator) {
        super(validator, Specialization.class, specializationRepository);
    }
}
