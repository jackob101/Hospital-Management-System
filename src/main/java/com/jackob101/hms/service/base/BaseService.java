package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class BaseService<T> {

    private final Validator validator;
    private final String nullEntityCode;
    private final String failedValidationCode;

    public BaseService(Validator validator, String nullEntityCode, String failedValidationCode) {
        this.validator = validator;
        this.nullEntityCode = nullEntityCode;
        this.failedValidationCode = failedValidationCode;
    }

    protected void validate(T entity, Class<?> ... groups){

        if (entity == null)
            throw new HmsException(nullEntityCode);

        Set<ConstraintViolation<T>> validate = validator.validate(entity, groups);

        if (!validate.isEmpty()){
            String errorMessage = validate.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(HmsException.MESSAGE_DELIMITER));

            throw new HmsException(failedValidationCode, HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, validate.size());
        }


    }
}
