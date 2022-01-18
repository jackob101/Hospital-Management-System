package com.jackob101.hms.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnDelete;
import com.jackob101.hms.validation.groups.OnFind;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonIgnoreProperties("hibernateLazyInitializer")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Patient {

    @NotNull(message = "ID cannot be null", groups = {OnUpdate.class, OnDelete.class})
    @Min(value = 0, message = "ID cannot be less than 0", groups = {OnUpdate.class, OnFind.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties("patient")
    @NotNull(message = "User Details cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserDetails userDetails;

    @JsonIgnoreProperties("patient")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient", cascade = CascadeType.DETACH)
    private Set<PatientAllergy> patientAllergy;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    private String religion;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String language;
}
