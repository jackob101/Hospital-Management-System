package com.jackob101.hms.dto.user;

import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PatientDTO {

    @Min(0)
    private Long id;

    @NotNull(message = "{patient.user_details_id.null}")
    @Min(value = 0, message = "{patient.user_details_id.less_than_zero")
    private Long userDetailsId;

    private MaritalStatus maritalStatus;

    @Size(max = 100, message = "{patient.religion.length_over_max}")
    private String religion;

    @Size(max = 100, message = "{patient.nationality.length_over_max}")
    private String nationality;

    private Gender gender;

    @Size(max = 100, message = "{patient.language.length_over_max}")
    private String language;


}
