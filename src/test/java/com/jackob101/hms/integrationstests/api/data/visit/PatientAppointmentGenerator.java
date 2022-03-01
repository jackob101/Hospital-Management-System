package com.jackob101.hms.integrationstests.api.data.visit;

import com.jackob101.hms.integrationstests.api.data.DataGenerator;
import com.jackob101.hms.model.visit.PatientAppointment;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PatientAppointmentGenerator implements DataGenerator<PatientAppointment> {

    private int descriptionLength = 50;
    private PatientStatusGenerator patientStatusGenerator = new PatientStatusGenerator();

    @Override
    public List<PatientAppointment> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public PatientAppointment generateSingle() {
        PatientAppointment patientAppointment = new PatientAppointment();
        patientAppointment.setDescription(RandomStringUtils.randomAlphabetic(descriptionLength));
        patientAppointment.setStartDate(LocalDate.now().plusDays((long) (Math.random() * 100 + 1)));
        patientAppointment.setEndDate(LocalDate.now().plusDays((long) (Math.random() * 100 + 100)));
        patientAppointment.setStartTime(LocalTime.now());
        patientAppointment.setPatientStatus(patientStatusGenerator.generateSingle());

        return patientAppointment;
    }

    public PatientAppointmentGenerator setDescriptionLength(int descriptionLength) {
        this.descriptionLength = descriptionLength;
        return this;
    }
}
