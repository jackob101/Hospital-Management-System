package com.jackob101.hms.api.allergy;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequestMapping(value = "patient_allergy", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PatientAllergyApi {

    private final IPatientAllergyService patientAllergyService;

    public PatientAllergyApi(IPatientAllergyService patientAllergyService) {
        this.patientAllergyService = patientAllergyService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPatientAllergy(@RequestBody PatientAllergyForm patientAllergyForm) throws URISyntaxException {

        PatientAllergy saved = patientAllergyService.createFromForm(patientAllergyForm);

        return ResponseEntity.created(new URI("/patient_allergy/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePatientAllergy(@RequestBody PatientAllergyForm patientAllergyForm) {

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
}
