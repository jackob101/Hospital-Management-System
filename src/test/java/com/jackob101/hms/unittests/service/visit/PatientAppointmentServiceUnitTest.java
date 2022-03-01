package com.jackob101.hms.unittests.service.visit;

import com.jackob101.hms.dto.visit.PatientAppointmentForm;
import com.jackob101.hms.integrationstests.api.data.visit.PatientAppointmentGenerator;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import com.jackob101.hms.service.visit.definition.IPatientAppointmentService;
import com.jackob101.hms.service.visit.definition.IPatientStatusService;
import com.jackob101.hms.service.visit.implementation.PatientAppointmentService;
import com.jackob101.hms.unittests.service.base.BaseFormServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
public class PatientAppointmentServiceUnitTest extends BaseFormServiceUnitTest<PatientAppointment, PatientAppointmentForm, IPatientAppointmentService> {

    @Mock
    PatientAppointmentRepository repository;

    @Mock
    IPatientStatusService patientStatusService;

    @Override
    protected IPatientAppointmentService configureService() {
        return new PatientAppointmentService(getValidationUtils(), repository, patientStatusService);
    }

    @Override
    protected PatientAppointment configureEntity() {

        PatientAppointmentGenerator generator = new PatientAppointmentGenerator();
        PatientAppointment patientAppointment = generator.generateSingle();
        patientAppointment.setId(1L);
        patientAppointment.getPatientStatus().setId(1L);

        return patientAppointment;
    }

    @Override
    protected JpaRepository<PatientAppointment, Long> configureRepository() {
        return repository;
    }

    @Override
    protected PatientAppointmentForm configureForm() {

        PatientAppointment entity = getEntity();

        return new PatientAppointmentForm(
                entity.getId(),
                entity.getStartDate(),
                entity.getStartTime(),
                entity.getDescription(),
                entity.getEndDate(),
                entity.getPatientStatus().getId());
    }

    @Override
    protected void mockConvert() {

        Mockito.doReturn(getEntity().getPatientStatus()).when(patientStatusService).find(Mockito.anyLong());

    }
}
