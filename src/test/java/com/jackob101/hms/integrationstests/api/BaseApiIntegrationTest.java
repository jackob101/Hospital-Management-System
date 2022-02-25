package com.jackob101.hms.integrationstests.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jackob101.hms.integrationstests.api.config.TestRestTemplateConfig;
import com.jackob101.hms.integrationstests.api.config.TestWebSecurityConfig;
import com.jackob101.hms.model.IEntity;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"no-security"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseApiIntegrationTest<T extends IEntity, F extends IEntity> {

    protected enum TestName {
        CREATE_ENTITY_SUCCESSFULLY,
        CREATE_ENTITY_VALIDATION_ERROR,
        CREATE_ENTITY_FAILED,
        UPDATE_ENTITY_SUCCESSFULLY,
        UPDATE_ENTITY_VALIDATION_ERROR,
        UPDATE_ENTITY_FAILED,
        FIND_ENTITY_SUCCESSFULLY,
        FIND_ENTITY_NOT_FOUND,
        FIND_ENTITY_ID_NULL,
        FIND_ALL_SUCCESSFULLY,
        DELETE_ENTITY_SUCCESSFULLY,
        DELETE_ENTITY_NOT_FOUND,
        DELETE_ENTITY_ID_NULL;
    }

    @Setter
    public class TestCallbacks {
        private Consumer<F> before;
        private Consumer<Object> after;

        public Consumer<F> getBefore() {
            if (before == null)
                return f -> {
                };
            return before;
        }

        public Consumer<Object> getAfter() {
            if (after == null)
                return f -> {
                };
            return after;
        }
    }

    @Getter
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Getter
    private TestUtils utils;

    @Getter
    private Class<T> modelClass;

    @Getter
    private F form;

    @Setter
    private Long id;

    private final EnumMap<TestName, TestCallbacks> callbacks = new EnumMap<>(TestName.class);

    protected void configureCallbacks(EnumMap<TestName, TestCallbacks> callbacks) {
    }

    protected abstract String configureRequestMapping();

    protected abstract F configureForm();

    protected abstract Long configureId();

    protected abstract void createMockData();

    protected abstract void clearMockData();

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {

        Arrays.stream(TestName.values()).forEach(testName -> callbacks.put(testName, new TestCallbacks()));

        this.modelClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        createMockData();
        this.utils = new TestUtils(configureRequestMapping(), testRestTemplate);
        this.form = configureForm();
        this.id = configureId();
        configureCallbacks(callbacks);
    }

    @AfterEach
    void tearDown() {
        clearMockData();
    }

    @Test
    public void create_entity_successfully() throws JsonProcessingException {

        TestCallbacks callback = getCallback(TestName.CREATE_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.createEntity(form, modelClass);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);

    }

    @Test
    public void create_entity_failed() throws JsonProcessingException {

        TestCallbacks callback = getCallback(TestName.CREATE_ENTITY_FAILED);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.createEntity(form, modelClass);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        callback.getAfter().accept(responseEntity);
    }

    @Test
    void update_entity_successfully() {

        TestCallbacks callback = getCallback(TestName.UPDATE_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.updateEntity(form, modelClass);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());


        callback.getAfter().accept(responseEntity);
    }

    @Test
    void update_entity_failed() {

        TestCallbacks callback = getCallback(TestName.UPDATE_ENTITY_FAILED);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.updateEntity(form, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        ResponseEntity<T> entity = utils.findEntity(form.getId(), modelClass);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity.getBody());

        callback.getAfter().accept(entity);

    }

    @Test
    void delete_entity_successfully() {

        TestCallbacks callback = getCallback(TestName.DELETE_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseType = utils.deleteEntity(id, String.class);

        assertEquals(HttpStatus.OK, responseType.getStatusCode());

        ResponseEntity<String> afterDeletionResponse = utils.findEntity(id, String.class);

        assertNotNull(afterDeletionResponse);
        assertEquals(HttpStatus.BAD_REQUEST, afterDeletionResponse.getStatusCode());
        assertNotNull(afterDeletionResponse.getBody());

        callback.getAfter().accept(afterDeletionResponse);
    }

    @Test
    void delete_entity_notFound() {

        TestCallbacks callback = getCallback(TestName.DELETE_ENTITY_NOT_FOUND);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.deleteEntity((long) Integer.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    @Test
    void find_entity_successfully() {

        TestCallbacks callback = getCallback(TestName.FIND_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.findEntity(id, modelClass);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    @Test
    void find_entity_failed() {

        TestCallbacks callback = getCallback(TestName.FIND_ENTITY_NOT_FOUND);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.findEntity(id, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);

    }


    @Test
    void findAll_entity_successfully() {

        TestCallbacks callback = getCallback(TestName.FIND_ALL_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<Object[]> responseEntity = utils.findAll(Object[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    private TestCallbacks getCallback(TestName testName) {
        return callbacks.getOrDefault(testName, new TestCallbacks());
    }
}


