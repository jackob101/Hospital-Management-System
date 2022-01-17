package com.jackob101.hms.api.user;

import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.utils.ApiUtils;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RequestMapping("specialization")
@RestController
public class SpecializationAPI {

    private final ISpecializationService specializationService;

    public SpecializationAPI(ISpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getSpecialization(@PathVariable("id") Long id) {

        log.info("Fetching specialization with ID " + id);

        Specialization specialization = specializationService.find(id);

        log.info("Specialization with ID " + id + " fetched successfully");

        return ResponseEntity.ok(specialization);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllSpecializations() {

        log.info("Fetching all specializations");

        List<Specialization> all = specializationService.findAll();

        log.info("Fetched all specializations");

        return ResponseEntity.ok(all);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createSpecialization(@Validated(OnCreate.class) @RequestBody Specialization specialization, BindingResult bindingResult) {

        log.info("Creating new Specialization");

        ApiUtils.checkBindings(bindingResult, "Specialization");

        Specialization saved = specializationService.create(specialization);

        log.info("New Specialization created");

        return ResponseEntity
                .created(URI.create("/specialization/" + saved.getId()))
                .body(saved);

    }

    @PutMapping()
    public ResponseEntity<Object> updateSpecialization(@Validated(OnUpdate.class) @RequestBody Specialization specialization, BindingResult bindingResult) {

        log.info("Updating specialization with ID " + specialization.getId());

        ApiUtils.checkBindings(bindingResult, "Specialization");

        Specialization update = specializationService.update(specialization);

        log.info("Specialization with ID " + specialization.getId() + " updated successfully");

        return ResponseEntity.ok(update);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteSpecialization(@PathVariable Long id) {

        log.info("Deleting Specialization with ID: " + id);

        boolean delete = specializationService.delete(id);

        log.info("Specialization with ID: " + id + " removed successfully");

        return ResponseEntity.ok(delete);

    }

}
