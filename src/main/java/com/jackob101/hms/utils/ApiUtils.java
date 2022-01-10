package com.jackob101.hms.utils;

import com.jackob101.hms.exceptions.HmsException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

public class ApiUtils {

    public static void checkBindings(BindingResult errors, String entityName) {

        if (errors.hasErrors()) {

            String[] objects = errors.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toArray(String[]::new);

            throw HmsException.params(entityName)
                    .fields(objects)
                    .code("controller.validation.failed");
        }


    }
}
