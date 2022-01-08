package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class BaseService<T> {

    private final Validator validator;
    private final String entityName;

    public BaseService(Validator validator, String entityName) {
        this.validator = validator;
        this.entityName = entityName;
    }

    protected void validate(T entity, Class<?>... groups) {

        if (entity == null)
            throw HmsException.params(entityName).code("service.entity_is_null");

        Set<ConstraintViolation<T>> validationResult = validator.validate(entity, groups);

        if (!validationResult.isEmpty()) {
            String errorMessage = validationResult.stream()
                    .map(ConstraintViolation::getMessage)
                    .map(s -> "-> " + s + "\n")
                    .collect(Collectors.joining());

            throw HmsException.internalError().params(entityName, errorMessage).code("service.validation.failed");
        }


    }
}
