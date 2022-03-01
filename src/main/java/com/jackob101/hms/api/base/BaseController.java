package com.jackob101.hms.api.base;

import com.jackob101.hms.converter.DefaultConverter;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.ICrudOperations;
import com.jackob101.hms.utils.ApiUtils;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class BaseController<T extends IEntity, F extends IEntity> {

    protected final ICrudOperations<T> service;
    protected final String entityName;
    protected final String baseMapping;
    private final Converter<F, T> converter;

    @SuppressWarnings("unchecked")
    public BaseController(ICrudOperations<T> service, String baseMapping, @Nullable Converter<F, T> converter) {
        this.service = service;
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        this.entityName = entityClass.getSimpleName().replaceAll("(?<!^)([A-Z])", " $1");
        this.converter = Objects.requireNonNullElseGet(converter, () -> new DefaultConverter<>(entityClass));

        if (baseMapping.startsWith("/"))
            this.baseMapping = baseMapping;
        else
            this.baseMapping = "/" + baseMapping;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateEntity(@RequestBody @Validated(OnUpdate.class) F entity) {

        log.info("Updating " + entityName + " with id: " + entity.getId());

        T saved = service.update(converter.convert(entity));

        log.info(entityName + " with id: " + entity.getId() + " updated successfully");

        return ResponseEntity.ok(saved);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createEntity(@RequestBody @Validated(OnCreate.class) F form, BindingResult bindingResult) throws URISyntaxException {
        log.info("Creating new " + entityName);

        ApiUtils.checkBindings(bindingResult, entityName);

        T saved = service.create(converter.convert(form));

        log.info("Created new " + entityName + " with id: " + saved.getId());

        return ResponseEntity.created(new URI(baseMapping + "/" + saved.getId()))
                .body(saved);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getEntity(@PathVariable("id") Long id) {

        log.info("Fetching " + entityName + " with id: " + id);
        T entity = service.find(id);

        log.info(entityName + " with id: " + id + " fetched");

        return ResponseEntity.ok(entity);
    }


    @GetMapping
    public ResponseEntity<Object> getAllEntities() {

        log.info("Fetching all " + entityName);

        List<T> all = service.findAll();

        log.info("Fetched all " + entityName);

        return ResponseEntity.ok(all);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteEntity(@PathVariable("id") Long id) {

        log.info("Deleting " + entityName + " with id: " + id);

        service.delete(id);

        log.info(entityName + " with id: " + id + " removed successfully");

        return ResponseEntity.ok().build();
    }
}
