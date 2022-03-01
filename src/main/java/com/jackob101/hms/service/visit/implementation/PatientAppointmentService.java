package com.jackob101.hms.service.visit.implementation;

import com.jackob101.hms.dto.visit.PatientAppointmentForm;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.model.visit.PatientStatus;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import com.jackob101.hms.service.base.BaseFormService;
import com.jackob101.hms.service.visit.definition.IPatientAppointmentService;
import com.jackob101.hms.service.visit.definition.IPatientStatusService;
import com.jackob101.hms.validation.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PatientAppointmentService extends BaseFormService<PatientAppointment, PatientAppointmentForm> implements IPatientAppointmentService {

    private final IPatientStatusService patientStatusService;

    public PatientAppointmentService(ValidationUtils validationUtils, PatientAppointmentRepository repository, IPatientStatusService patientStatusService) {
        super(validationUtils, PatientAppointment.class, repository);
        this.patientStatusService = patientStatusService;
    }

    @Override
    public PatientAppointment convert(PatientAppointmentForm form) {

        PatientStatus patientStatus = patientStatusService.find(form.getStatusId());
        ModelMapper mapper = new ModelMapper();
        PatientAppointment mapped = mapper.map(form, PatientAppointment.class);
        mapped.setPatientStatus(patientStatus);

        return mapped;

    }
}
