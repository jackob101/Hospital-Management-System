package com.jackob101.hms.model.allergy;

import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Allergen {

    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank or null", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "allergen")
    private Set<PatientAllergy> patientAllergy;

    public Allergen(String name) {
        this.name = name;
    }
}
