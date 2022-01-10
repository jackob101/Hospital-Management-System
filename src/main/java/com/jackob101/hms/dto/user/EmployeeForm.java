package com.jackob101.hms.dto.user;

import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class EmployeeForm {

    @NotNull(message = "ID cannot be null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "User Details ID cannot be null", groups = {OnUpdate.class, OnCreate.class})
    private Long userDetailsId;

    private Set<Long> specializations = new HashSet<>();
}
