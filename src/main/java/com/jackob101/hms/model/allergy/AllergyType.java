package com.jackob101.hms.model.allergy;

import javax.persistence.*;
import java.util.Set;

@Entity
public class AllergyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "allergyType", fetch = FetchType.LAZY)
    private Set<PatientAllergy> patientAllergy;
}
