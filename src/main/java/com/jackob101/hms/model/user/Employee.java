package com.jackob101.hms.model.user;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private UserDetails userDetails;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "employee_specialization",
            joinColumns = {
                    @JoinColumn(name = "employee_id",
                            referencedColumnName = "id",
                            nullable = false,
                            updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "specialization_id",
                            referencedColumnName = "id",
                            nullable = false,
                            updatable = false)
            })
    private Set<Specialization> specializations;
}
