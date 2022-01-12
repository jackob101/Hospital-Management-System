package com.jackob101.hms.utils;

import com.jackob101.hms.exceptions.HmsException;
import org.springframework.validation.BindingResult;

public class ApiUtils {

    public static void checkBindings(BindingResult errors, String entityName) {

        if (errors.hasErrors()) {
            throw HmsException.params(entityName)
                    .fields(errors.getFieldErrors())
                    .code("Validation for %s failed");
        }


    }
}
