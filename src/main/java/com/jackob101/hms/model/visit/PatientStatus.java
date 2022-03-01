package com.jackob101.hms.model.visit;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PatientStatus implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = OnUpdate.class)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    private String name;
}
