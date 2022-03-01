package com.jackob101.hms.unittests.converter.user;

import com.jackob101.hms.TestUtils.data.user.PatientGenerator;
import com.jackob101.hms.converter.user.PatientFormConverter;
import com.jackob101.hms.dto.user.PatientForm;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PatientFormConverterTest {

    @Mock
    IUserDetailsService iUserDetailsService;

    Patient patient;
    PatientForm patientForm;

    PatientFormConverter patientFormConverter;

    @BeforeEach
    void setUp() {
        patient = new PatientGenerator().generateSingle();

        patientForm = new PatientForm(
                1L,
                1L,
                patient.getMaritalStatus(),
                patient.getReligion(),
                patient.getNationality(),
                patient.getGender(),
                patient.getLanguage());

        patientFormConverter = new PatientFormConverter(iUserDetailsService);

    }

    @Test
    void convert_successfully() {
        Mockito.doReturn(patient.getUserDetails()).when(iUserDetailsService).find(Mockito.anyLong());

        Patient converted = patientFormConverter.convert(patientForm);

        assertNotNull(converted);
        assertEquals(patient.getUserDetails().getLastName(), converted.getUserDetails().getLastName());

    }
}