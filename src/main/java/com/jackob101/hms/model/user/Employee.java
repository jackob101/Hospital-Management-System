package com.jackob101.hms.model.user;

import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnDelete;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "employee")
public class Employee {

    @Min(value = 0, message = "{employee.id.less_than_zero}", groups = OnUpdate.class)
    @NotNull(message = "{employee.id.null}", groups = {OnUpdate.class, OnDelete.class})
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{employee.user_details.null}")
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
