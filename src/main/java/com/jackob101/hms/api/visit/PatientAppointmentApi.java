package com.jackob101.hms.api.visit;


import com.jackob101.hms.api.base.BaseFormController;
import com.jackob101.hms.dto.visit.PatientAppointmentForm;
import com.jackob101.hms.model.visit.PatientAppointment;
import com.jackob101.hms.service.visit.definition.IPatientAppointmentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = PatientAppointmentApi.REQUEST_MAPPING, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PatientAppointmentApi extends BaseFormController<PatientAppointment, PatientAppointmentForm> {

    public final static String REQUEST_MAPPING = "patient_appointments";

    public PatientAppointmentApi(IPatientAppointmentService service) {
        super(service, "Patient Appointment", REQUEST_MAPPING);
    }
}
