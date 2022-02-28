package com.jackob101.hms.dto.visit;

import java.time.LocalDate;
import java.time.LocalTime;

public class PatientAppointmentForm {

    private Long id;

    private LocalDate startDate;

    private LocalTime startTime;

    private String description;

    private LocalDate endDate;

    private Long statusId;
}
