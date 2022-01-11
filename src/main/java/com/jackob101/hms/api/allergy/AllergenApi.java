package com.jackob101.hms.api.allergy;

import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequestMapping(value = "allergen", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AllergenApi {

    private final IAllergenService allergenService;

    public AllergenApi(IAllergenService allergenService) {
        this.allergenService = allergenService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAllergen(@RequestBody Allergen allergen) throws URISyntaxException {

        Allergen saved = allergenService.create(allergen);

        return ResponseEntity
                .created(new URI("/allergen/" + saved.getId()))
                .body(saved);

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateAllergen(@RequestBody Allergen allergen) {

        Allergen updated = allergenService.update(allergen);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getAllergen(@PathVariable("id") Long id) {

        Allergen allergen = allergenService.find(id);

        return ResponseEntity.ok(allergen);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllergens() {
        List<Allergen> all = allergenService.findAll();

        return ResponseEntity.ok(all);
    }
}
