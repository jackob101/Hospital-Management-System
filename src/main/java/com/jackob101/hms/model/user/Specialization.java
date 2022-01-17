package com.jackob101.hms.model.user;

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
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "specialization")
public class Specialization {

    @NotNull(groups = OnUpdate.class, message = "ID cannot be null")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank", groups = {OnUpdate.class, OnCreate.class})
    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(mappedBy = "specializations", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    public Specialization(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Specialization(String name) {
        this.name = name;
    }
}
