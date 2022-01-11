package com.jackob101.hms.api.allergy;

import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequestMapping(value = "allergy_type", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AllergyTypeApi {

    private final IAllergyTypeService allergyTypeService;

    public AllergyTypeApi(IAllergyTypeService allergyTypeService) {
        this.allergyTypeService = allergyTypeService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAllergyType(@RequestBody AllergyType allergyType) throws URISyntaxException {

        AllergyType saved = allergyTypeService.create(allergyType);

        return ResponseEntity
                .created(new URI("/allergy_type/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateAllergyType(@RequestBody AllergyType allergyType) {

        AllergyType updated = allergyTypeService.update(allergyType);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getAllergyType(@PathVariable Long id) {

        AllergyType allergyType = allergyTypeService.find(id);

        return ResponseEntity.ok(allergyType);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllAllergyTypes() {

        List<AllergyType> all = allergyTypeService.findAll();

        return ResponseEntity.ok(all);
    }


}
