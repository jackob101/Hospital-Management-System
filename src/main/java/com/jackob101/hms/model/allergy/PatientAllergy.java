package com.jackob101.hms.model.allergy;

import javax.persistence.*;

@Entity
public class PatientAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Allergen allergen;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AllergyType allergyType;

    private String reaction;
}
