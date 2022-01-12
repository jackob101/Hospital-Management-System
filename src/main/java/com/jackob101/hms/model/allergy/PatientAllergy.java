package com.jackob101.hms.model.allergy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties("hibernateLazyInitializer")
@Getter
@Setter
@Entity
public class PatientAllergy {

    @NotNull(message = "ID cannot be null", groups = OnUpdate.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Allergen cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Allergen allergen;

    @NotNull(message = "Allergy Type cannot be null", groups = {OnCreate.class, OnUpdate.class})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AllergyType allergyType;

    @JsonIgnoreProperties("patientAllergy")
    @OneToOne(mappedBy = "patientAllergy", fetch = FetchType.LAZY, optional = true)
    private Patient patient;

    private String reaction;
}