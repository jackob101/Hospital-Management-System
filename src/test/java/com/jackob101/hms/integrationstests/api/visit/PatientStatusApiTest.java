package com.jackob101.hms.integrationstests.api.visit;

import com.jackob101.hms.api.visit.PatientStatusApi;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.visit.PatientStatusGenerator;
import com.jackob101.hms.model.visit.PatientStatus;
import com.jackob101.hms.repository.visit.PatientStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;

class PatientStatusApiTest extends BaseApiIntegrationTest<PatientStatus, PatientStatus> {

    @Autowired
    PatientStatusRepository repository;

    List<PatientStatus> patientStatusList;

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<PatientStatus, PatientStatus>.TestCallbacks> callbacks) {
        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(patientStatus -> patientStatus.setId(null));
        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(patientStatus -> patientStatus.setName(null));
        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(patientStatus -> patientStatus.setName(null));
    }

    @Override
    protected String configureRequestMapping() {
        return PatientStatusApi.REQUEST_MAPPING;
    }

    @Override
    protected PatientStatus configureForm() {
        return new PatientStatus(patientStatusList.get(0).getId(), "This is test name");
    }

    @Override
    protected void createMockData() {
        patientStatusList = repository.saveAll(new PatientStatusGenerator().generate(10));
    }

    @Override
    protected void clearMockData() {
        repository.deleteAll();
    }
}