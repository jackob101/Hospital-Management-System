package com.jackob101.hms.configs;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jackob101.hms.deserializers.user.EmployeeDeserializer;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializersConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public Module employeeDeserializer() {

        SimpleModule simpleModule = new SimpleModule();

        EmployeeDeserializer employeeDeserializer = new EmployeeDeserializer();
        employeeDeserializer.setUserDetailsService(userDetailsService);

        simpleModule.addDeserializer(Employee.class, employeeDeserializer);

        return simpleModule;

    }
}
