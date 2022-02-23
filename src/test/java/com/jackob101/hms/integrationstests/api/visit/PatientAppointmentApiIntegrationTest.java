package com.jackob101.hms.integrationstests.api.visit;

import com.jackob101.hms.api.visit.PatientAppointmentApi;
import com.jackob101.hms.integrationstests.api.BaseIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.visit.PatientAppointmentGenerator;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PatientAppointmentApiIntegrationTest extends BaseIntegrationTest<PatientAppointment, PatientAppointment> {

    @Autowired
    PatientAppointmentRepository repository;

    @BeforeEach
    void setUp() {
        this.configure(PatientAppointmentApi.REQUEST_MAPPING);

        PatientAppointment patientAppointment = new PatientAppointment();
        patientAppointment.setId(1L);
        patientAppointment.setDescription("This is appointment description");
        patientAppointment.setStartDate(LocalDate.now().plusDays(1L));
        patientAppointment.setEndDate(LocalDate.now().plusDays(2L));
        patientAppointment.setStartTime(LocalTime.now());

        PatientAppointmentGenerator generator = new PatientAppointmentGenerator();
        List<PatientAppointment> saved = repository.saveAll(generator.generate(10));

        patientAppointment.setId(saved.get(0).getId());

        setId(saved.get(0).getId());
        setArrayModelClass(PatientAppointment[].class);
        setModelClass(PatientAppointment.class);
        setForm(patientAppointment);

        callbacks.setCreateFailedBefore(patientAppointment1 -> patientAppointment1.setStartTime(null));
        callbacks.setFindFailedBefore(aLong -> setId(null));
        callbacks.setCreateSuccessfullyBefore(patientAppointment1 -> patientAppointment1.setId(null));
        callbacks.setUpdateFailedBefore(patientAppointment1 -> patientAppointment1.setStartDate(null));
    }
}
