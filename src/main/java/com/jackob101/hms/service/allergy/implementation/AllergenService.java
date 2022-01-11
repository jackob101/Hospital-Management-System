package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.repository.allergy.AllergenRepository;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class AllergenService extends BaseService<Allergen> implements IAllergenService {

    private final AllergenRepository allergenRepository;

    public AllergenService(Validator validator, AllergenRepository allergenRepository) {
        super(validator, "Allergen");
        this.allergenRepository = allergenRepository;
    }

    @Override
    public Allergen create(Allergen entity) {

        validate(entity, OnCreate.class);

        checkIdAvailability(entity.getId(), allergenRepository);

        return allergenRepository.save(entity);
    }

    @Override
    public Allergen update(Allergen entity) {

        validate(entity, OnUpdate.class);

        boolean isFound = allergenRepository.existsById(entity.getId());

        if (!isFound)
            throw HmsException.params(entity.getId()).code("Allergen with ID: %s couldn't be updated because entity with that ID doesn't exist");

        return allergenRepository.save(entity);
    }

    @Override
    public boolean delete(Long id) {

        checkIdForDeletion(id, allergenRepository);

        allergenRepository.deleteById(id);

        return !allergenRepository.existsById(id);
    }

    @Override
    public Allergen find(Long id) {

        checkIdForSearch(id);

        Optional<Allergen> optionalAllergen = allergenRepository.findById(id);

        return optionalAllergen.orElseThrow(() -> HmsException.params(id).code("Allergen with ID: %s was not found"));
    }

    @Override
    public List<Allergen> findAll() {
        return allergenRepository.findAll();
    }
}
