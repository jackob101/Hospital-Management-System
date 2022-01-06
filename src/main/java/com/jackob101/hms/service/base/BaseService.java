package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.ExceptionCode;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Patient;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class BaseService<T> {

    private final Validator validator;
    private final String nullMessage;
    private final String validationMessage;
    private final ExceptionCode exceptionCode;

    public BaseService(Validator validator, String nullMessage, String validationMessage, ExceptionCode exceptionCode) {
        this.validator = validator;
        this.nullMessage = nullMessage;
        this.validationMessage = validationMessage;
        this.exceptionCode = exceptionCode;
    }

    protected void validate(T entity, Class<?> ... groups){

        if (entity == null)
            throw new HmsException("Patient cannot be null",ExceptionCode.PATIENT_VALIDATION_ERROR,HttpStatus.BAD_REQUEST);

        Set<ConstraintViolation<T>> validate = validator.validate(entity, groups);

        if (!validate.isEmpty()){
            String errorMessage = validate.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(HmsException.MESSAGE_DELIMITER));

            throw new HmsException(errorMessage, ExceptionCode.PATIENT_VALIDATION_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
