package com.jackob101.hms.unittests.service.visit;

import com.jackob101.hms.integrationstests.api.data.visit.PatientAppointmentGenerator;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import com.jackob101.hms.service.visit.definition.IPatientAppointmentService;
import com.jackob101.hms.service.visit.implementation.PatientAppointmentService;
import com.jackob101.hms.unittests.service.base.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
public class PatientAppointmentServiceUnitTest extends BaseServiceUnitTest<PatientAppointment, IPatientAppointmentService> {

    @Mock
    PatientAppointmentRepository repository;

    @Override
    protected IPatientAppointmentService configureService() {
        return new PatientAppointmentService(getValidationUtils(), repository);
    }

    @Override
    protected PatientAppointment configureEntity() {

        PatientAppointmentGenerator generator = new PatientAppointmentGenerator();
        PatientAppointment patientAppointment = generator.generateSingle();
        patientAppointment.setId(1L);

        return patientAppointment;
    }

    @Override
    protected JpaRepository<PatientAppointment, Long> configureRepository() {
        return repository;
    }
}
