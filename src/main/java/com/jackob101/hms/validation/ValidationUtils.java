package com.jackob101.hms.validation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.utils.ServiceUtils;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validator;
import java.util.HashMap;

public class ValidationUtils<T> {

    private final ServiceUtils<T> utils;
    private final SmartValidator validator;

    public ValidationUtils(ServiceUtils<T> utils, Validator validator) {
        this.utils = utils;
        this.validator = new SpringValidatorAdapter(validator);
    }

    public void validate(T entity, Object... groups) {

        if (entity == null)
            throw HmsException.params(utils.getFormattedName()).code("Entity %s is null");

        MapBindingResult errors = new MapBindingResult(new HashMap<>(), utils.getFormattedName());

        validator.validate(entity, errors, groups);


        if (errors.hasErrors()) {

            throw HmsException.badRequest()
                    .fields(errors.getFieldErrors())
                    .params(utils.getFormattedName())
                    .code("Validation for entity %s failed");
        }


    }
}
