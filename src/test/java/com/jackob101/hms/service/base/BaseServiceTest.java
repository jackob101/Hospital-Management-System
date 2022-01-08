package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

@ActiveProfiles("no-security")
@SpringBootTest
class BaseServiceTest {

    @Autowired
    PatientService patientService;

    @Autowired
    MessageSource messageSource;

    @Test
    void create_ExceptionMessage() {

        Patient patient = new Patient();

        try {
            patientService.update(patient);
        } catch (HmsException ex) {
            String message = messageSource.getMessage(ex.getCode(), ex.getParams(), Locale.US);
            ex.printStackTrace();
            System.out.println(message);
        }
    }

}