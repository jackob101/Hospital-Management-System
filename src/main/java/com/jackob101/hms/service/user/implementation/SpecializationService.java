package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class SpecializationService extends BaseService<Specialization> implements ISpecializationService {

    public SpecializationService(SpecializationRepository specializationRepository, ValidationUtils validator) {
        super(validator, Specialization.class, specializationRepository);
    }
}
