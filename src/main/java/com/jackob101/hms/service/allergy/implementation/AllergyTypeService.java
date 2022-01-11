package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.repository.allergy.AllergyTypeRepository;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class AllergyTypeService extends BaseService<AllergyType> implements IAllergyTypeService {

    private final AllergyTypeRepository allergyTypeRepository;

    public AllergyTypeService(Validator validator, AllergyTypeRepository allergyTypeRepository) {
        super(validator, "Allergy Type");
        this.allergyTypeRepository = allergyTypeRepository;
    }

    @Override
    public AllergyType create(AllergyType entity) {
        validate(entity, OnCreate.class);

        checkIdAvailability(entity.getId(), allergyTypeRepository);

        return allergyTypeRepository.save(entity);
    }

    @Override
    public AllergyType update(AllergyType entity) {
        validate(entity, OnUpdate.class);

        checkIdForUpdate(entity.getId(), allergyTypeRepository);

        return allergyTypeRepository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        checkIdForDeletion(id, allergyTypeRepository);

        allergyTypeRepository.deleteById(id);

        return !allergyTypeRepository.existsById(id);
    }

    @Override
    public AllergyType find(Long id) {

        checkIdForSearch(id);

        Optional<AllergyType> optionalAllergyType = allergyTypeRepository.findById(id);

        return optionalAllergyType.orElseThrow(() -> HmsException.params(id).code("Allergy Type with ID %s was not found"));

    }

    @Override
    public List<AllergyType> findAll() {
        return allergyTypeRepository.findAll();
    }
}
