package com.jackob101.hms.model.visit;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
public class PatientAppointment implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Start date cannot be null", groups = {OnCreate.class, OnUpdate.class})
    private LocalDate startDate;

    private LocalTime startTime;

    private String description;

    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PatientStatus patientStatus;

}
