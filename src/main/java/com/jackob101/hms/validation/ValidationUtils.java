package com.jackob101.hms.validation;

import com.jackob101.hms.exceptions.HmsException;
import org.springframework.stereotype.Component;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashMap;

@Component
public class ValidationUtils {

    private final SmartValidator validator;

    public ValidationUtils() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.validator = new SpringValidatorAdapter(validator);
    }

    public ValidationUtils(Validator validator) {
        Validator validator1 = Validation.buildDefaultValidatorFactory().getValidator();
        this.validator = new SpringValidatorAdapter(validator);
    }

    public void validate(Object entity, String entityName, Object... groups) {

        if (entity == null)
            throw HmsException.params(entityName).code("Entity %s is null");

        MapBindingResult errors = new MapBindingResult(new HashMap<>(), entityName);

        validator.validate(entity, errors, groups);


        if (errors.hasErrors()) {

            throw HmsException.badRequest()
                    .fields(errors.getFieldErrors())
                    .params(entityName)
                    .code("Validation for entity %s failed");
        }


    }
}
