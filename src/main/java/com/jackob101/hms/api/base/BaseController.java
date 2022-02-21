package com.jackob101.hms.api.base;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.CrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
public abstract class BaseController<T extends IEntity> {

    protected final CrudService<T> service;
    protected final String entityName;
    protected final String baseMapping;

    public BaseController(CrudService<T> service, String entityName, String baseMapping) {
        this.service = service;
        this.entityName = entityName;

        if (baseMapping.startsWith("/"))
            this.baseMapping = baseMapping;
        else
            this.baseMapping = "/" + baseMapping;
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
