package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.utils.ServiceUtils;
import com.jackob101.hms.validation.ValidationUtils;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;


public abstract class BaseService<T extends IEntity> implements CrudService<T> {

    protected final JpaRepository<T, Long> repository;

    private final ValidationUtils<T> validation;

    @Getter
    protected final ServiceUtils<T> utils;

    public BaseService(Validator validator, Class<T> entityClass, JpaRepository<T, Long> repository) {
        this.utils = new ServiceUtils<>(repository, entityClass);
        this.validation = new ValidationUtils<T>(utils, validator);
        this.repository = repository;
    }


    @Override
    public T create(T entity) {
        validation.validate(entity, OnCreate.class);

        utils.checkIdAvailability(entity.getId());

        return repository.save(entity);
    }

    @Override
    public T update(T entity) {

        validation.validate(entity, OnUpdate.class);

        utils.checkIdForUpdate(entity.getId());

        return repository.save(entity);
    }

    @Override
    public boolean delete(Long id) {

        utils.checkIdForDeletion(id);

        repository.deleteById(id);

        return !repository.existsById(id);
    }

    @Override
    public T find(Long id) {
        utils.checkIdForSearch(id);

        Optional<T> byId = repository.findById(id);

        return byId.orElseThrow(() -> HmsException.params(utils.getFormattedName(), id).code("Couldn't find %s with given ID %s"));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
}
