package com.jackob101.hms.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EmployeeForm {

    private Long id;
    private Long userDetailsId;
    private Set<Long> specializations = new HashSet<>();
}
