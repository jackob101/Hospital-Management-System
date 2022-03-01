package com.jackob101.hms.model.allergy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
@Getter
@Setter
@Entity
public class PatientAllergy implements IEntity {

    @NotNull(message = "ID cannot be null", groups = OnUpdate.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Allergen cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    private Allergen allergen;

    @NotNull(message = "Allergy Type cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    private AllergyType allergyType;

    @NotNull(message = "Patient cannot be null")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private Patient patient;

    private String reaction;

    private String notes;

    public PatientAllergy(Allergen allergen, AllergyType allergyType, Patient patient, String reaction, String notes) {
        this.allergen = allergen;
        this.allergyType = allergyType;
        this.patient = patient;
        this.reaction = reaction;
        this.notes = notes;
    }
}
