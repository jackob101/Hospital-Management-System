package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;

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
}
