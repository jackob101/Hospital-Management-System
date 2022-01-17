package com.jackob101.hms.api.allergy;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import com.jackob101.hms.utils.ApiUtils;
import com.jackob101.hms.validation.groups.OnCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequestMapping(value = "patient_allergy", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RestController
public class PatientAllergyApi {

    private final IPatientAllergyService patientAllergyService;

    public PatientAllergyApi(IPatientAllergyService patientAllergyService) {
        this.patientAllergyService = patientAllergyService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPatientAllergy(@RequestBody @Validated(OnCreate.class) PatientAllergyForm patientAllergyForm, BindingResult bindingResult) throws URISyntaxException {

        ApiUtils.checkBindings(bindingResult, "Patient Allergy Form");

        PatientAllergy saved = patientAllergyService.createFromForm(patientAllergyForm);

        return ResponseEntity.created(new URI("/patient_allergy/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePatientAllergy(@RequestBody @Validated PatientAllergyForm patientAllergyForm, BindingResult bindingResult) {

        ApiUtils.checkBindings(bindingResult, "Patient Allergy Form");

        PatientAllergy updated = patientAllergyService.updateFromForm(patientAllergyForm);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPatientAllergy(@PathVariable("id") Long id) {

        PatientAllergy patientAllergy = patientAllergyService.find(id);

        return ResponseEntity.ok(patientAllergy);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllPatientAllergy() {
        List<PatientAllergy> all = patientAllergyService.findAll();
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePatientAllergy(@PathVariable("id") Long id) {

        log.info("Deleting Patient Allergy with ID: " + id);

        boolean delete = patientAllergyService.delete(id);

        log.info("Patient Allergy with ID: " + id + " was deleted successfully");

        return ResponseEntity.ok(delete);
    }
}
