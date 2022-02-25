package com.jackob101.hms.integrationstests.api.allergy;


import com.jackob101.hms.api.allergy.PatientAllergyApi;
import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.integrationstests.api.BaseApiIntegrationTest;
import com.jackob101.hms.integrationstests.api.data.allergy.PatientAllergyGenerator;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumMap;
import java.util.List;

public class PatientAllergyApiIntegrationTest extends BaseApiIntegrationTest<PatientAllergy, PatientAllergyForm> {

    @Autowired
    PatientAllergyRepository patientAllergyRepository;

    List<PatientAllergy> patientAllergyList;

    @Override
    protected void configureCallbacks(EnumMap<ITestName, BaseApiIntegrationTest<PatientAllergy, PatientAllergyForm>.TestCallbacks> callbacks) {

        callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));

        callbacks.get(ITestName.CREATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setPatient(null));

        callbacks.get(ITestName.UPDATE_ENTITY_VALIDATION_ERROR).setBefore(form -> form.setPatient(null));
    }

    @Override
    protected String configureRequestMapping() {
        return PatientAllergyApi.REQUEST_MAPPING;
    }

    @Override
    protected PatientAllergyForm configureForm() {
        return PatientAllergyForm.builder()
                .id(patientAllergyList.get(0).getId())
                .patient(patientAllergyList.get(0).getPatient().getId())
                .allergenId(patientAllergyList.get(0).getAllergen().getId())
                .allergyTypeId(patientAllergyList.get(0).getAllergyType().getId())
                .reaction("Patient survived")
                .build();
    }

    @Override
    protected void createMockData() {
        patientAllergyList = patientAllergyRepository.saveAll(new PatientAllergyGenerator().generate(10));
    }

    @Override
    protected void clearMockData() {
        patientAllergyRepository.deleteAll();
    }


}
