package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.CrudService;
import com.jackob101.hms.validation.ValidationUtils;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Optional;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * The base class for service unit tests. It contains tests for basic CRUD functionality nothing more.<br><br>
 * If some tests should be excluded they need to be overridden, so they won't execute. This mechanism can also be used to
 * create tests for some operations that don't operate in default way, don't forget to add <em>@Test</em> annotation.<br><br>
 * <p>
 * Every test that use <em>service#find()</em> or <em>repository#existsiById()</em> use default value of entity id.<br>
 * The set <strong>Entity</strong> should have no validation violating data and the <strong>ID</strong> shouldn't be null.<br><br>
 * The validationUtils are mocked so there is no need to add callbacks to create incorrect data.
 *
 * @param <T> The <strong>domain</strong> model of your entity
 * @param <S> The service that handles this entity and extends <em>CrudService</em>
 */
@ActiveProfiles("no-security")
@SpringBootTest
@Getter
public abstract class BaseServiceUnitTest<T extends IEntity, S extends CrudService<T>> {

    public enum BaseTestNames {
        CREATE_SUCCESSFULLY,
        CREATE_VALIDATION_ERROR,
        CREATE_ID_TAKEN,
        UPDATE_SUCCESSFULLY,
        UPDATE_VALIDATION_ERROR,
        UPDATE_ID_NOT_FOUND,
        UPDATE_ENTITY_NULL,
        FIND_SUCCESSFULLY,
        FIND_ID_NULL,
        FIND_ID_NOT_FOUND,
        DELETE_SUCCESSFULLY,
        DELETE_ID_NULL,
        DELETE_ID_NOT_FOUND,
        DELETE_FAILED,
    }

    @Setter
    public class TestCallbacks {

        private Consumer<T> before;

        private Consumer<T> after;


        public Consumer<T> getBefore() {
            if (before == null) return t -> {
            };
            return before;
        }

        public Consumer<T> getAfter() {
            if (after == null) return t -> {
            };
            return after;
        }
    }

    private JpaRepository<T, Long> repository;

    @Spy
    private ValidationUtils validationUtils;

    private Class<T> entityClass;

    private S service;

    private T entity;

    private final EnumMap<BaseTestNames, TestCallbacks> callbacks = new EnumMap<>(BaseTestNames.class);

    /**
     * Here you can set which service should be tested.
     */
    protected abstract S configureService();

    /**
     * Set up entity on which the service will operate
     */
    protected abstract T configureEntity();

    /**
     * Set up <strong>Mock repository</strong> for tests.
     */
    protected abstract JpaRepository<T, Long> configureRepository();

    /**
     * This allows to configure callbacks behaviour. Can be used to add some necessary mocks to test or change the data.
     * Each test function have two callbacks. One before and one after the test.
     *
     * @param callbacks The callbacks that you can configure
     */
    protected void configureCallbacks(EnumMap<BaseTestNames, TestCallbacks> callbacks) {
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {

        MockitoAnnotations.openMocks(this);
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Arrays.stream(BaseTestNames.values()).forEach(name -> callbacks.put(name, new TestCallbacks()));

        entity = configureEntity();
        service = configureService();
        repository = configureRepository();
        configureCallbacks(callbacks);
    }


    /**
     * After Callback accepts - saved entity
     */
    @Test
    public void create_entity_successfully() {
        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.CREATE_SUCCESSFULLY, new TestCallbacks());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));

        T savedEntity = service.create(entity);

        assertNotNull(savedEntity);

        config.getAfter().accept(savedEntity);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void create_entity_idTaken() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.CREATE_ID_TAKEN, new TestCallbacks());
        config.getBefore().accept(entity);

        doReturn(true).when(repository).existsById(entity.getId());

        assertThrows(HmsException.class, () -> service.create(entity));

        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void create_entity_validationError() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.CREATE_VALIDATION_ERROR, new TestCallbacks());
        config.getBefore().accept(entity);

        doThrow(HmsException.class).when(validationUtils).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> service.create(entity));

        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - updated entity
     */
    @Test
    public void update_entity_successfully() {

        TestCallbacks config = this.callbacks.getOrDefault(BaseTestNames.UPDATE_SUCCESSFULLY, new TestCallbacks());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));
        doReturn(true).when(repository).existsById(entity.getId());

        T updated = service.update(entity);

        assertNotNull(updated);

        config.getAfter().accept(updated);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void update_entity_validationError() {
        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.UPDATE_VALIDATION_ERROR, new TestCallbacks());
        config.getBefore().accept(entity);

        doThrow(HmsException.class).when(validationUtils).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> service.create(entity));
        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void update_entity_idNotFound() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.UPDATE_ID_NOT_FOUND, new TestCallbacks());
        config.getBefore().accept(entity);

        doReturn(false).when(repository).existsById(entity.getId());

        assertThrows(HmsException.class, () -> service.update(entity));
        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - found entity
     */
    @Test
    public void find_entity_successfully() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.FIND_SUCCESSFULLY, new TestCallbacks());
        config.getBefore().accept(entity);

        doReturn(Optional.of(entity)).when(repository).findById(entity.getId());

        T foundEntity = service.find(entity.getId());
        assertEquals(entity.getId(), foundEntity.getId());

        config.getAfter().accept(foundEntity);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void find_entity_idNull() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.FIND_ID_NULL, new TestCallbacks());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.find(null));
        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void find_entity_notFound() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.FIND_ID_NOT_FOUND, new TestCallbacks());
        config.getBefore().accept(entity);

        doReturn(Optional.empty()).when(repository).findById(entity.getId());

        assertThrows(HmsException.class, () -> service.find(entity.getId()));
        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void delete_employee_successfully() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.DELETE_SUCCESSFULLY, new TestCallbacks());
        config.getBefore().accept(entity);

        doReturn(true, false).when(repository).existsById(entity.getId());

        assertTrue(service.delete(entity.getId()));
        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void delete_entity_idNull() {
        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.DELETE_ID_NULL, new TestCallbacks());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(null));
        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void delete_entity_notFound() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.DELETE_ID_NOT_FOUND, new TestCallbacks());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(Long.MAX_VALUE));

        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void delete_entity_failed() {
        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.DELETE_FAILED, new TestCallbacks());
        config.getBefore().accept(entity);

        doReturn(true, true).when(repository).existsById(entity.getId());

        assertFalse(service.delete(entity.getId()));

        config.getAfter().accept(null);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    public void update_entity_null() {

        TestCallbacks config = callbacks.getOrDefault(BaseTestNames.UPDATE_ENTITY_NULL, new TestCallbacks());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.update(null));

        config.getAfter().accept(null);
    }
}