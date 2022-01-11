package com.jackob101.hms.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.IPatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

@ActiveProfiles("no-security")
@SpringBootTest
class BaseServiceTest {

    @Autowired
    IPatientService patientService;

    @Autowired
    MessageSource messageSource;

    @Test
    void create_ExceptionMessage() {

        Patient patient = new Patient();

        try {
            patientService.update(patient);
        } catch (HmsException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            System.out.println(Arrays.toString(ex.getFields()));
        }
    }

}