package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;
import org.springframework.data.repository.CrudRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;


public abstract class BaseService<T> {

    private final Validator validator;
    private final String entityName;

    public BaseService(Validator validator, String entityName) {
        this.validator = validator;
        this.entityName = entityName;
    }

    protected void validate(T entity, Class<?>... groups) {

        if (entity == null)
            throw HmsException.params(entityName).code("Entity %s is null");

        Set<ConstraintViolation<T>> validationResult = validator.validate(entity, groups);

        if (!validationResult.isEmpty()) {

            String[] fields = validationResult.stream()
                    .map(ConstraintViolation::getMessage)
                    .toArray(String[]::new);

            throw HmsException.internalError().fields(fields).params(entityName).code("Validation for entity %s failed");
        }


    }

    protected void checkIdAvailability(Long id, CrudRepository<T, Long> repository) {

        if (id != null && repository.existsById(id))
            throw HmsException.params(entityName, id).code("%s ID: %s is already taken");

    }

    protected void checkIdForDeletion(Long id, CrudRepository<T, Long> repository) {

        if (id == null)
            throw HmsException.params(entityName).code("Could not delete %s because given ID is null");

        boolean isFound = repository.existsById(id);

        if (!isFound)
            throw HmsException.params(entityName, id).code("Could not delete %s because entity with ID %s was not found");
    }

    protected void checkIdForSearch(Long id) {
        if (id == null)
            throw HmsException.params(entityName).code("Could not find %s because given ID is null");
    }

    protected void checkIdForUpdate(Long id, CrudRepository<T, Long> repository) {
        boolean isFound = repository.existsById(id);

        if (!isFound)
            throw HmsException.params(entityName, id).code("%s with ID: %s couldn't be updated because entity with that ID doesn't exist");
    }
}
