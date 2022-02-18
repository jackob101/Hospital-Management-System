package com.jackob101.hms.api.allergy;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RequestMapping(value = "allergy_types", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AllergyTypeApi {

    private final IAllergyTypeService allergyTypeService;

    public AllergyTypeApi(IAllergyTypeService allergyTypeService) {
        this.allergyTypeService = allergyTypeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAllergyType(@RequestBody AllergyType allergyType) throws URISyntaxException {


        log.info("Creating Allergy Type");
        AllergyType saved = allergyTypeService.create(allergyType);

        log.info("New Allergy Type with id: " + saved.getId() + " created");

        return ResponseEntity
                .created(new URI("/allergy_type/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateAllergyType(@RequestBody AllergyType allergyType) {

        log.info("Updating Allergy Type with id: " + allergyType.getId());

        AllergyType updated = allergyTypeService.update(allergyType);

        log.info("Allergy Type with id: " + allergyType.getId() + " updated");

        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getAllergyType(@PathVariable Long id) {

        log.info("Searching for Allergy type with id: " + id);
        AllergyType allergyType = allergyTypeService.find(id);

        log.info("Allergy type with id: " + id + " found");

        return ResponseEntity.ok(allergyType);
    }

    @GetMapping
    public ResponseEntity<Object> getAllAllergyTypes() {

        log.info("Fetching all allergy types");
        List<AllergyType> all = allergyTypeService.findAll();

        log.info("Allergy types fetched");

        return ResponseEntity.ok(all);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteAllergyTypes(@PathParam("id") Long id) {

        log.info("Deleting allergy type with id: " + id);
        allergyTypeService.delete(id);

        log.info("Allergy type removed");

        return ResponseEntity.ok().build();
    }

}
