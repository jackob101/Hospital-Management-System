package com.jackob101.hms.model.allergy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@JsonIgnoreProperties("hibernateLazyInitializer")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class AllergyType {

    @NotNull(message = "Id cannot be null", groups = {OnUpdate.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name must not be blank", groups = {OnUpdate.class, OnCreate.class})
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "allergyType", fetch = FetchType.LAZY)
    private Set<PatientAllergy> patientAllergy;

    public AllergyType(String name) {
        this.name = name;
    }
}
