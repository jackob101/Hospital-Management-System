package com.jackob101.hms.service.visit.implementation;

import com.jackob101.hms.repository.visit.PatientStatusRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.visit.definition.IPatientStatusService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class PatientStatusServiceService extends BaseService<com.jackob101.hms.model.visit.PatientStatus> implements IPatientStatusService {

    public PatientStatusServiceService(ValidationUtils validationUtils, PatientStatusRepository repository) {
        super(validationUtils, com.jackob101.hms.model.visit.PatientStatus.class, repository);
    }
}
