package com.jackob101.hms.api.visit;

import com.jackob101.hms.api.base.BaseController;
import com.jackob101.hms.model.visit.PatientStatus;
import com.jackob101.hms.service.base.CrudService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = PatientStatusApi.REQUEST_MAPPING, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PatientStatusApi extends BaseController<PatientStatus, PatientStatus> {

    public final static String REQUEST_MAPPING = "patient_status";

    public PatientStatusApi(CrudService<PatientStatus> service) {
        super(service, REQUEST_MAPPING, null);
    }
}