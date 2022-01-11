package com.jackob101.hms.model.allergy;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Allergen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "allergen")
    private Set<PatientAllergy> patientAllergy;
}
