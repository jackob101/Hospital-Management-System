package com.jackob101.hms.api.user;

import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.exceptions.ExceptionCode;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.service.user.definition.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(PatientApi.REQUEST_MAPPING)
@RestController
public class PatientApi {

    public static final String REQUEST_MAPPING = "patient";

    private final PatientService patientService;
    private final ModelMapper modelMapper;

    public PatientApi(PatientService patientService) {
        this.patientService = patientService;
        this.modelMapper = new ModelMapper();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createPatient(@RequestBody @Valid PatientDTO patientDTO, BindingResult bindingResult) throws URISyntaxException {

        log.info("Creating new patient.");

        checkBinding(bindingResult);

        Patient patient = modelMapper.map(patientDTO, Patient.class);

        Patient saved = patientService.create(patient, patientDTO.getUserDetailsId());

        if (saved == null)
            throw new HmsException("Error during creation of new patient",
                    ExceptionCode.PATIENT_CREATION_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        log.info("Patient with id: " + saved.getId() + " created successfully");

        return ResponseEntity.created(new URI("/" + REQUEST_MAPPING + "/" + saved.getId()))
                .body(saved);


    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePatient(@RequestBody @Valid PatientDTO patientDTO, BindingResult bindingResult){

        log.info("Updating patient");

        if(patientDTO.getId() == null)
            bindingResult.addError(new ObjectError("patientDto","When updating patient id cannot be null"));

        checkBinding(bindingResult);

        Patient patient = modelMapper.map(patientDTO, Patient.class);

        Patient updated = patientService.update(patient);

        if(updated == null){
            log.error("Patient update failed");

            throw new HmsException("Patient update failed",
                    ExceptionCode.PATIENT_UPDATE_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Patient with id: " + updated.getId() + " updated successfully.");

        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getPatient(@PathVariable("id") Long id){

        Patient patient = patientService.find(id);

        if(patient == null)
            log.error("Patient with id: " + id + " was not found.");

        return ResponseEntity.ok(patient);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getPatients(){
        List<Patient> all = patientService.findAll();

        return ResponseEntity.ok(all);
    }


    private void checkBinding(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {

            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(" |-| "));

            log.error("Error during binding data to model.");
            throw new HmsException(errorMessage, ExceptionCode.USER_DETAILS_BINDING_ERROR, HttpStatus.BAD_REQUEST);
        }

    }

}
