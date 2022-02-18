package com.jackob101.hms.integrationstests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.model.IEntity;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"no-security"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseIntegrationTest<T extends IEntity, F extends IEntity> {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    protected TestUtils utils;

    @Setter
    private Class<T> modelClass;

    @Setter
    private Class<T[]> arrayModelClass;

    @Setter
    private F form;

    @Setter
    private Long id;

    public ConfigurationCallbacks<T, F> callbacks = new ConfigurationCallbacks<>();


    protected void configure(String mapping) {
        this.utils = new TestUtils(mapping, testRestTemplate);
        System.out.println("This is test configuration");
    }

    @Test
    public void create_entity_successfully() throws JsonProcessingException {

        if (callbacks.getCreateSuccessfullyBefore() != null)
            callbacks.getCreateSuccessfullyBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.createEntity(form, modelClass);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        if (callbacks.getCreateSuccessfullyAfter() != null)
            callbacks.getCreateSuccessfullyAfter().accept(responseEntity);

    }

    @Test
    public void create_entity_failed() throws JsonProcessingException {

        if (callbacks.getCreateFailedBefore() != null)
            callbacks.getCreateFailedBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.createEntity(form, modelClass);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        if (callbacks.getCreateFailedAfter() != null)
            callbacks.getCreateFailedAfter().accept(responseEntity);
    }

    @Test
    void update_entity_successfully() {

        if (callbacks.getUpdateSuccessfullyBefore() != null)
            callbacks.getUpdateSuccessfullyBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.updateEntity(form, modelClass);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        if (callbacks.getUpdateSuccessfullyAfter() != null)
            callbacks.getUpdateSuccessfullyAfter().accept(responseEntity);

    }

    @Test
    void update_entity_failed() {

        if (callbacks.getUpdateFailedBefore() != null)
            callbacks.getUpdateFailedBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.updateEntity(form, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ResponseEntity<T> entity = utils.findEntity(form.getId(), modelClass);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity.getBody());

        if (callbacks.getUpdateFailedAfter() != null)
            callbacks.getUpdateFailedAfter().accept(entity);

    }

    @Test
    void delete_entity_successfully() {

        if (callbacks.getDeleteSuccessfullyBefore() != null)
            callbacks.getDeleteSuccessfullyBefore().accept(id);

        ResponseEntity<String> responseType = utils.deleteEntity(id, String.class);

        assertEquals(HttpStatus.OK, responseType.getStatusCode());

        ResponseEntity<String> afterDeletionResponse = utils.findEntity(id, String.class);

        assertNotNull(afterDeletionResponse);
        assertEquals(HttpStatus.BAD_REQUEST, afterDeletionResponse.getStatusCode());
        assertNotNull(afterDeletionResponse.getBody());

        if (callbacks.getDeleteSuccessfullyAfter() != null)
            callbacks.getDeleteSuccessfullyAfter().accept(afterDeletionResponse);
    }

    @Test
    void delete_entity_notFound() {

        if (callbacks.getDeleteFailedBefore() != null)
            callbacks.getDeleteFailedBefore().accept(id);

        ResponseEntity<String> responseEntity = utils.deleteEntity((long) Integer.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        if (callbacks.getDeleteFailedAfter() != null)
            callbacks.getDeleteFailedAfter().accept(responseEntity);
    }

    @Test
    void find_entity_successfully() {

        if (callbacks.getFindSuccessfullyBefore() != null)
            callbacks.getFindSuccessfullyBefore().accept(id);

        ResponseEntity<T> responseEntity = utils.findEntity(id, modelClass);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        if (callbacks.getFindSuccessfullyAfter() != null)
            callbacks.getFindSuccessfullyAfter().accept(responseEntity);

    }

    @Test
    void find_entity_failed() {

        if (callbacks.getFindFailedBefore() != null)
            callbacks.getFindFailedBefore().accept(id);

        ResponseEntity<String> responseEntity = utils.findEntity(id, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        if (callbacks.getFindFailedAfter() != null)
            callbacks.getFindFailedAfter().accept(responseEntity);

    }


    @Test
    void findAll_entity_successfully() {

        if (callbacks.getFindAllSuccessfullyBefore() != null)
            callbacks.getFindAllSuccessfullyBefore().accept(id);

        ResponseEntity<T[]> responseEntity = utils.findAll(arrayModelClass);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        if (callbacks.getFindAllSuccessfullyAfter() != null)
            callbacks.getFindAllSuccessfullyAfter().accept(responseEntity);
    }
}


