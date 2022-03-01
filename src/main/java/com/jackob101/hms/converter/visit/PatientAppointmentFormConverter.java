package com.jackob101.hms.converter.visit;

import com.jackob101.hms.dto.visit.PatientAppointmentForm;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.model.visit.PatientStatus;
import com.jackob101.hms.service.visit.definition.IPatientStatusService;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;

public class PatientAppointmentFormConverter implements Converter<PatientAppointmentForm, PatientAppointment> {

    private final IPatientStatusService patientStatusService;

    public PatientAppointmentFormConverter(IPatientStatusService patientStatusService) {
        this.patientStatusService = patientStatusService;
    }

    @Override
    public PatientAppointment convert(PatientAppointmentForm source) {

        PatientStatus patientStatus = patientStatusService.find(source.getStatusId());
        ModelMapper mapper = new ModelMapper();
        PatientAppointment mapped = mapper.map(source, PatientAppointment.class);
        mapped.setPatientStatus(patientStatus);

        return mapped;
    }

}
