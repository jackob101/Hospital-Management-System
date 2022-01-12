package com.jackob101.hms.api.user;

import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.IPatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RequestMapping(PatientApi.REQUEST_MAPPING)
@RestController
public class PatientApi {

    public static final String REQUEST_MAPPING = "patient";

    private final IPatientService patientService;
    private final ModelMapper modelMapper;

    public PatientApi(IPatientService patientService) {
        this.patientService = patientService;
        this.modelMapper = new ModelMapper();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPatient(@RequestBody PatientDTO patientDTO) throws URISyntaxException {

        log.info("Creating new patient.");

        Patient saved = patientService.createFromForm(patientDTO);

        log.info("Patient with id: " + saved.getId() + " created successfully");

        return ResponseEntity
                .created(new URI("/" + REQUEST_MAPPING + "/" + saved.getId()))
                .body(saved);


    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePatient(@RequestBody PatientDTO patientDTO) {

        log.info("Updating patient");

        Patient updated = patientService.updateFromForm(patientDTO);

        log.info("Patient with id: " + updated.getId() + " updated successfully.");

        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPatient(@PathVariable("id") Long id){

        Patient patient = patientService.find(id);

        return ResponseEntity.ok(patient);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getPatients(){
        List<Patient> all = patientService.findAll();

        return ResponseEntity.ok(all);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletePatient(@PathVariable("id") Long id){

        patientService.delete(id);

        return ResponseEntity.ok("Deleted");
    }
}
