package com.jackob101.hms.service.visit.implementation;

import com.jackob101.hms.repository.visit.PatientStatusRepository;
import com.jackob101.hms.unittests.service.base.BaseServiceUnitTest;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

class PatientStatusServiceServiceTest extends BaseServiceUnitTest<com.jackob101.hms.model.visit.PatientStatus, PatientStatusService> {

    @Mock
    PatientStatusRepository patientStatusRepository;

    @Override
    protected PatientStatusService configureService() {
        return new PatientStatusService(getValidationUtils(), patientStatusRepository);
    }

    @Override
    protected com.jackob101.hms.model.visit.PatientStatus configureEntity() {

        return new com.jackob101.hms.model.visit.PatientStatus(1L, "This is test status");
    }

    @Override
    protected JpaRepository<com.jackob101.hms.model.visit.PatientStatus, Long> configureRepository() {
        return patientStatusRepository;
    }
}