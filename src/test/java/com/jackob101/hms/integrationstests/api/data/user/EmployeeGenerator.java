package com.jackob101.hms.integrationstests.api.data.user;

import com.jackob101.hms.integrationstests.api.data.DataGenerator;
import com.jackob101.hms.model.user.Employee;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeeGenerator implements DataGenerator<Employee> {

    private UserDetailsGenerator userDetailsGen = new UserDetailsGenerator();

    @Override
    public List<Employee> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public Employee generateSingle() {
        return Employee.builder()
                .userDetails(userDetailsGen.generateSingle())
                .build();
    }

    public EmployeeGenerator userDetailsGen(UserDetailsGenerator userDetailsGenerator) {
        this.userDetailsGen = userDetailsGenerator;
        return this;
    }

}
