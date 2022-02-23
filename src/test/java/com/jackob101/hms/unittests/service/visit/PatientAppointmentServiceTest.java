package com.jackob101.hms.unittests.service.visit;

import com.jackob101.hms.integrationstests.api.data.visit.PatientAppointmentGenerator;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import com.jackob101.hms.service.visit.implementation.PatientAppointmentService;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PatientAppointmentServiceTest extends BaseServiceTest<PatientAppointment, PatientAppointmentService> {

    @Mock
    PatientAppointmentRepository repository;

    @Override
    protected void configure() {

        PatientAppointmentService service = new PatientAppointmentService(getValidationUtils(), repository);

        configure(repository, service);

        PatientAppointmentGenerator generator = new PatientAppointmentGenerator();
        PatientAppointment patientAppointment = generator.generateSingle();
        patientAppointment.setId(1L);

        setData(patientAppointment);

    }
}
