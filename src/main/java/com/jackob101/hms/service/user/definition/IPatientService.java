package com.jackob101.hms.service.user.definition;

import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.base.CrudService;

public interface IPatientService extends CrudService<Patient, Long> {

    Patient create(Patient patient, Long userDetailsId);

    Patient createFromForm(PatientDTO patientDTO);

    Patient updateFromForm(PatientDTO patientDTO);
}
