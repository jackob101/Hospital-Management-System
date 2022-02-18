package com.jackob101.hms.model.allergy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties("hibernateLazyInitializer")
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Allergen implements IEntity {

    @NotNull(message = "Id cannot be null", groups = OnUpdate.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank or null", groups = {OnCreate.class, OnUpdate.class})
    @Column(unique = true, nullable = false)
    private String name;

    public Allergen(String name) {
        this.name = name;
    }
}
