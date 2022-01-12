package com.jackob101.hms.dto.allergy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientAllergyForm {

    private Long id;
    private Long patient;
    private Long allergenId;
    private Long allergyTypeId;
    private String reaction;
}
