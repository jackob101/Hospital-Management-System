package com.jackob101.hms.integrationstests.api.visit;

import com.jackob101.hms.api.visit.PatientAppointmentApi;
import com.jackob101.hms.dto.visit.PatientAppointmentForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.visit.PatientAppointmentGenerator;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;

public class PatientAppointmentApiIntegrationTest extends BaseApiIntegrationTest<PatientAppointment, PatientAppointmentForm> {

    @Autowired
    PatientAppointmentRepository repository;

    List<PatientAppointment> patientAppointments;

    @Override
    protected String configureRequestMapping() {
        return PatientAppointmentApi.REQUEST_MAPPING;
    }

    @Override
    protected PatientAppointmentForm configureForm() {
        PatientAppointmentForm patientAppointment = new PatientAppointmentForm();
        patientAppointment.setId(patientAppointments.get(0).getId());
        patientAppointment.setDescription("This is appointment description");
        patientAppointment.setStartDate(LocalDate.now().plusDays(1L));
        patientAppointment.setEndDate(LocalDate.now().plusDays(2L));
        patientAppointment.setStartTime(LocalTime.now());
        patientAppointment.setStatusId(patientAppointments.get(0).getPatientStatus().getId());
        return patientAppointment;
    }

    @Override
    protected void createMockData() {
        PatientAppointmentGenerator generator = new PatientAppointmentGenerator();
        patientAppointments = repository.saveAll(generator.generate(10));
    }

    @Override
    protected void clearMockData() {
        repository.deleteAll();
    }

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<PatientAppointment, PatientAppointmentForm>.TestCallbacks> callbacks) {
        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setStartDate(null));
        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));
        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setStartDate(null));
    }
}
