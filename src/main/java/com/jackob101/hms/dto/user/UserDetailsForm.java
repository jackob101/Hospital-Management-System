package com.jackob101.hms.dto.user;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetailsForm implements IEntity {

    @Min(value = 0, groups = OnUpdate.class)
    private Long id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String userCredentialsId;

    @Size(max = 200)
    @NotBlank
    private String pesel;

    @Size(max = 200)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    private String firstName;

    @Size(max = 200)
    @NotBlank
    private String secondName;

    @Size(max = 200)
    @NotBlank
    private String lastName;

    @NotNull
    private LocalDate dateOfBirth;

    @Size(max = 200)
    @NotBlank
    private String phoneNumber;
}
