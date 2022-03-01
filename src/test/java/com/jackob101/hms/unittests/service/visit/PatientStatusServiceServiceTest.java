package com.jackob101.hms.unittests.service.visit;

import com.jackob101.hms.repository.visit.PatientStatusRepository;
import com.jackob101.hms.service.visit.implementation.PatientStatusServiceService;
import com.jackob101.hms.unittests.service.BaseServiceUnitTest;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

class PatientStatusServiceServiceTest extends BaseServiceUnitTest<com.jackob101.hms.model.visit.PatientStatus, PatientStatusServiceService> {

    @Mock
    PatientStatusRepository patientStatusRepository;

    @Override
    protected PatientStatusServiceService configureService() {
        return new PatientStatusServiceService(getValidationUtils(), patientStatusRepository);
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