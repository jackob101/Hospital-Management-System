package com.jackob101.hms.api.base;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.FormCrudService;
import com.jackob101.hms.utils.ApiUtils;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public abstract class BaseFormController<T extends IEntity, F extends IEntity> extends BaseController<T> {

    protected final FormCrudService<T, F> formService;

    public BaseFormController(FormCrudService<T, F> service, String entityName, String baseMapping) {
        super(service, entityName, baseMapping);
        this.formService = service;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createEntity(@RequestBody @Validated(OnCreate.class) F form, BindingResult bindingResult) throws URISyntaxException {

        log.info("Creating new " + entityName);

        ApiUtils.checkBindings(bindingResult, entityName);

        T saved = formService.createFromForm(form);

        log.info("Created new " + entityName + " with id: " + saved.getId());

        return ResponseEntity.created(new URI(baseMapping + "/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateEntity(@RequestBody @Validated(OnUpdate.class) F entity, BindingResult bindingResult) {
        log.info("Updating " + entityName + " with id: " + entity.getId());

        ApiUtils.checkBindings(bindingResult, entityName);

        T saved = formService.updateFromForm(entity);

        log.info(entityName + " with id: " + entity.getId() + " updated successfully");

        return ResponseEntity.ok(saved);
    }
}
