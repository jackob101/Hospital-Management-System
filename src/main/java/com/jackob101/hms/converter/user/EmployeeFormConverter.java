package com.jackob101.hms.converter.user;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmployeeFormConverter implements Converter<EmployeeForm, Employee> {

    private final IUserDetailsService userDetailsService;
    private final ISpecializationService specializationService;

    public EmployeeFormConverter(IUserDetailsService userDetailsService, ISpecializationService specializationService) {
        this.userDetailsService = userDetailsService;
        this.specializationService = specializationService;
    }

    @Override
    public Employee convert(EmployeeForm source) {

        ModelMapper modelMapper = new ModelMapper();

        Employee employee = modelMapper.map(source, Employee.class);

        UserDetails userDetails = userDetailsService.find(source.getUserDetailsId());

        employee.setUserDetails(userDetails);

        Set<Specialization> specializations = new HashSet<>();

        if (source.getSpecializations() != null) {
            specializations = source.getSpecializations().stream()
                    .map(specializationService::find)
                    .collect(Collectors.toSet());

        }
        employee.setSpecializations(specializations);

        return employee;
    }
}
