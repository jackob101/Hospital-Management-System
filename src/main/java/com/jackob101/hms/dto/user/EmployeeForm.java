package com.jackob101.hms.dto.user;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmployeeForm implements IEntity {

    @NotNull(message = "ID cannot be null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "User Details ID cannot be null")
    private Long userDetailsId;

    private Set<Long> specializations = new HashSet<>();
}
