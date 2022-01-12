package com.jackob101.hms.service.allergy.definition;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.service.base.CrudService;

public interface IPatientAllergyService extends CrudService<PatientAllergy, Long> {

    PatientAllergy createFromForm(PatientAllergyForm patientAllergyForm);

    PatientAllergy updateFromForm(PatientAllergyForm patientAllergyForm);
}