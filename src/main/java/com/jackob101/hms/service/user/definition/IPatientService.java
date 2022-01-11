package com.jackob101.hms.service.user.definition;

import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.base.CrudService;

public interface IPatientService extends CrudService<Patient, Long> {

    Patient create(Patient patient, Long userDetailsId);
}
