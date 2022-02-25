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
import org.springframework.http.HttpHeaders;
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

/**
 * This is base class for testing API CRUD operations.
 * During configuration please pass correct model, without any validation errors ( when these errors will be required, they can be set in callback).
 * Entity <em>ID</em> should point to id of already saved entity. The entity <em>ID</em> field will be used to search for other entities.<br>
 * To set up mock data use <em>configureMockData()</em>. <br>
 * The configure methods are executed in order:
 * <ul>
 *     <li>createMockData()</li>
 *     <li>configureForm()</li>
 *     <li>configureCallbacks()</li>
 * </ul>
 * <p>
 * After test the <em>clearMockData()</em> executes.<br>
 * <p>
 * Necessary callbacks to configure if instruction above are followed are:
 * <ul>
 *     <li>CREATE_ENTITY_SUCCESSFULLY#before() - set ID to null or other id that is not taken</li>
 *     <li>CREATE_ENTITY_VALIDATION_ERROR#before() - add some validation violations to form</li>
 *     <li>UPDATE_ENTITY_VALIDATION_ERROR#before() - add some validation violations to form</li>
 * </ul>
 *
 * @param <T> The <strong>domain</strong> model on which application operates.
 * @param <F> The <strong>form</strong> of your <strong>domain</strong> model. If your model doesn't have form model, simply pass <string>domain</string> model second time
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestWebSecurityConfig.class, TestRestTemplateConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"no-security"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class BaseApiIntegrationTest<T extends IEntity, F extends IEntity> {

    protected enum ITestName {
        CREATE_ENTITY_SUCCESSFULLY,
        CREATE_ENTITY_VALIDATION_ERROR,
        UPDATE_ENTITY_SUCCESSFULLY,
        UPDATE_ENTITY_VALIDATION_ERROR,
        FIND_ENTITY_SUCCESSFULLY,
        FIND_ENTITY_NOT_FOUND,
        FIND_ENTITY_ID_NULL,
        FIND_ALL_SUCCESSFULLY,
        DELETE_ENTITY_SUCCESSFULLY,
        DELETE_ENTITY_NOT_FOUND,
        DELETE_ENTITY_ID_NULL
    }

    @Setter
    protected class TestCallbacks {
        private Consumer<F> before;
        private Consumer<ResponseEntity<?>> after;

        public Consumer<F> getBefore() {
            if (before == null)
                return f -> {
                };
            return before;
        }

        public Consumer<ResponseEntity<?>> getAfter() {
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

    private final EnumMap<ITestName, TestCallbacks> callbacks = new EnumMap<>(ITestName.class);

    /**
     * This function receive callback which can be changed. The map is already populated with all <em>TestNames</em>. <br><br>
     * Example: <br>
     * <code>
     * callbacks.get(ITestName.CREATE_ENTITY_SUCCESSFULLY).setBefore(form -> form.setId(null));
     * </code>
     *
     * @param callbacks Map of all callbacks
     */
    protected void configureCallbacks(EnumMap<ITestName, TestCallbacks> callbacks) {
    }

    protected abstract String configureRequestMapping();

    protected abstract F configureForm();

    protected abstract void createMockData();

    protected abstract void clearMockData();

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {

        Arrays.stream(ITestName.values()).forEach(testName -> callbacks.put(testName, new TestCallbacks()));

        this.modelClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        createMockData();
        this.utils = new TestUtils(configureRequestMapping(), testRestTemplate);
        this.form = configureForm();
        configureCallbacks(callbacks);
    }

    @AfterEach
    void tearDown() {
        clearMockData();
    }

    /**
     * The form ID must be either null or point to ID that is not taken yet.
     * <br><br>
     * Callback receive created entity
     *
     * @throws JsonProcessingException Can be thrown during parsing form to json
     */
    @Test
    public void create_entity_successfully() throws JsonProcessingException {

        TestCallbacks callback = getCallback(ITestName.CREATE_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.createEntity(form, modelClass);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getHeaders().get(HttpHeaders.LOCATION));

        callback.getAfter().accept(responseEntity);

    }

    /**
     * In order for this test to pass, some validation error need to be set up in callback.
     * If object have no validation rules simply override this test.<br><br>
     * <p>
     * Callback receive validation error message.
     *
     * @throws JsonProcessingException Can be thrown during parsing form to json
     */
    @Test
    public void create_entity_validationError() throws JsonProcessingException {

        TestCallbacks callback = getCallback(ITestName.CREATE_ENTITY_VALIDATION_ERROR);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.createEntity(form, modelClass);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    /**
     * Form <em>ID</em> must point to saved entity.<br>
     * No validation errors.<br>
     * <p>
     * Callback receive updated entity.
     */
    @Test
    public void update_entity_successfully() {

        TestCallbacks callback = getCallback(ITestName.UPDATE_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.updateEntity(form, modelClass);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    /**
     * Form must contain validation error. If form doesn't have validation constraints, override this test.
     * ID must point to saved entity
     * <br><br>
     * <p>
     * Callback receive not updated old entity
     */
    @Test
    public void update_entity_validationError() {

        TestCallbacks callback = getCallback(ITestName.UPDATE_ENTITY_VALIDATION_ERROR);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.updateEntity(form, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        ResponseEntity<T> entity = utils.findEntity(form.getId(), modelClass);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity.getBody());

        callback.getAfter().accept(entity);

    }

    /**
     * This test deletes entity and then checks if entity removed entity still exists in database.
     * Entity <em>ID</em> should point to existing ID.<br><br>
     * <p>
     * Callback receives response from database check.
     */
    @Test
    public void delete_entity_successfully() {

        TestCallbacks callback = getCallback(ITestName.DELETE_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseType = utils.deleteEntity(form.getId(), String.class);

        assertEquals(HttpStatus.OK, responseType.getStatusCode());

        ResponseEntity<String> afterDeletionResponse = utils.findEntity(form.getId(), String.class);

        assertNotNull(afterDeletionResponse);
        assertEquals(HttpStatus.BAD_REQUEST, afterDeletionResponse.getStatusCode());
        assertNotNull(afterDeletionResponse.getBody());

        callback.getAfter().accept(afterDeletionResponse);
    }

    /**
     * This test try to delete entity with ID <em>Integer.MAX_VALUE</em> ( entity with this id shouldn't exist).<br><br>
     * <p>
     * Callback receive error message from deletion.
     */
    @Test
    public void delete_entity_notFound() {

        TestCallbacks callback = getCallback(ITestName.DELETE_ENTITY_NOT_FOUND);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.deleteEntity((long) Integer.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    /**
     * Try to delete entity with null ID <br><br>
     * <p>
     * Callback receive error message
     */
    @Test
    void delete_entity_idNull() {

        TestCallbacks callback = getCallback(ITestName.DELETE_ENTITY_ID_NULL);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.deleteEntity(null, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    /**
     * Searches for entity.<br>
     * Entity <em>ID</em> should point to existing ID.<br><br>
     * <p>
     * Callback receive found entity.
     */
    @Test
    public void find_entity_successfully() {

        TestCallbacks callback = getCallback(ITestName.FIND_ENTITY_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<T> responseEntity = utils.findEntity(form.getId(), modelClass);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }


    /**
     * Searches for ID with value LONG.MAX_VALUE ( entity with this id shouldn't exist).<br><br>
     * <p>
     * Callback receive error message.
     */
    @Test
    public void find_entity_notFound() {

        TestCallbacks callback = getCallback(ITestName.FIND_ENTITY_NOT_FOUND);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.findEntity(Long.MAX_VALUE, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);

    }

    /**
     * This test is trying to find entity with null ID<br><br>
     * Callback receive error message
     */
    @Test
    void find_idNull() {

        TestCallbacks callback = getCallback(ITestName.FIND_ENTITY_ID_NULL);
        callback.getBefore().accept(form);

        ResponseEntity<String> responseEntity = utils.findEntity(null, String.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    /**
     * Fetches all entities<br><br>
     * <p>
     * Callback receive array with all entities
     */
    @Test
    public void findAll_entity_successfully() {

        TestCallbacks callback = getCallback(ITestName.FIND_ALL_SUCCESSFULLY);
        callback.getBefore().accept(form);

        ResponseEntity<Object[]> responseEntity = utils.findAll(Object[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        callback.getAfter().accept(responseEntity);
    }

    private TestCallbacks getCallback(ITestName ITestName) {
        return callbacks.getOrDefault(ITestName, new TestCallbacks());
    }
}


