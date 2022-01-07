package com.jackob101.hms.model.user;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "specialization")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    public Specialization(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
