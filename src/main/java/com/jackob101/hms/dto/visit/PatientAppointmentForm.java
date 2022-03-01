package com.jackob101.hms.dto.visit;

import com.jackob101.hms.model.IEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientAppointmentForm implements IEntity {

    private Long id;

    private LocalDate startDate;

    private LocalTime startTime;

    private String description;

    private LocalDate endDate;

    private Long statusId;
}
