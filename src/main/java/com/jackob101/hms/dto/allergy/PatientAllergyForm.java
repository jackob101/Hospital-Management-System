package com.jackob101.hms.dto.allergy;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PatientAllergyForm implements IEntity {

    @NotNull(message = "id: cannot be null", groups = {OnUpdate.class})
    private Long id;

    @NotNull(message = "patientId: cannot be null", groups = {OnUpdate.class, OnCreate.class})
    private Long patient;

    @NotNull(message = "allergenId: cannot be null", groups = {OnUpdate.class, OnCreate.class})
    private Long allergenId;

    @NotNull(message = "allergyTypeId: cannot be null", groups = {OnUpdate.class, OnCreate.class})
    private Long allergyTypeId;

    private String reaction;
}
