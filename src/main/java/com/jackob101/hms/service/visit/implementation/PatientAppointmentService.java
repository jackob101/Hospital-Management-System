package com.jackob101.hms.service.visit.implementation;

import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.repository.visit.PatientAppointmentRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.visit.definition.IPatientAppointmentService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientAppointmentService extends BaseService<PatientAppointment> implements IPatientAppointmentService {


    public PatientAppointmentService(ValidationUtils validationUtils, PatientAppointmentRepository repository) {
        super(validationUtils, PatientAppointment.class, repository);
    }
}
