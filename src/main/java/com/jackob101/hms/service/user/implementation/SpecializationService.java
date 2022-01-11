package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class SpecializationService extends BaseService<Specialization> implements ISpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository, Validator validator) {
        super(validator, "Specialization");
        this.specializationRepository = specializationRepository;
    }

    @Override
    public Specialization create(Specialization entity) {
        validate(entity, OnCreate.class);

        if (entity.getId() != null && specializationRepository.existsById(entity.getId()))
            throw HmsException.params(entity.getId()).code("Specialization with ID %s already exists");

        return specializationRepository.save(entity);
    }

    @Override
    public Specialization update(Specialization entity) {

        validate(entity, OnUpdate.class);

        boolean isFound = specializationRepository.existsById(entity.getId());

        if (!isFound)
            throw HmsException.params(entity.getId()).code("Couldn't update Specialization with ID %s because entity with this ID was not found");

        return specializationRepository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Specialization find(Long aLong) {

        if (aLong == null)
            throw HmsException.code("Couldn't find Specialization because given ID is null");

        Optional<Specialization> optionalSpecialization = specializationRepository.findById(aLong);
        return optionalSpecialization.orElseThrow(() -> HmsException.params(aLong).code("Couldn't find Specialization with ID %s"));

    }

    @Override
    public List<Specialization> findAll() {
        return specializationRepository.findAll();
    }
}
