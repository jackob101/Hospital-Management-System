package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.CrudService;
import com.jackob101.hms.validation.ValidationUtils;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.ParameterizedType;
import java.util.EnumMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * The base class for service unit tests. It contains tests for basic CRUD functionality nothing more.
 * If some tests should be excluded they need to be overridden, so they won't execute. This mechanism can also be used to
 * create tests for some operations that don't operate in default way, don't forget to add <em>@Test</em> annotation.
 * <p>
 * Every test that use <em>service#find()</em> or <em>repository#existsiById()</em> use default value of 1L.
 *
 * @param <T> The <strong>domain</strong> model of your entity
 * @param <S> The service that handles this entity and extends <em>CrudService</em>
 */
@ActiveProfiles("no-security")
@SpringBootTest
@Getter
public abstract class BaseServiceUnitTest<T extends IEntity, S extends CrudService<T>> {

    private JpaRepository<T, Long> repository;

    @Spy
    private ValidationUtils validationUtils;

    private Class<T> entityClass;

    private S service;

    private T entity;

    private final EnumMap<TestName, TestCallbacks<T>> callbacks = new EnumMap<>(TestName.class);

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
    protected void configureCallbacks(EnumMap<TestName, TestCallbacks<T>> callbacks) {
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        entity = configureEntity();
        service = configureService();
        repository = configureRepository();
        configureCallbacks(callbacks);
    }


    @Test
    public void create_entity_successfully() {
        TestCallbacks<T> config = callbacks.getOrDefault(TestName.CREATE_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));

        T savedEntity = service.create(entity);

        assertNotNull(savedEntity);

        config.getAfter().accept(savedEntity);
    }

    @Test
    public void create_entity_idTaken() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.CREATE_ID_TAKEN, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(true).when(repository).existsById(anyLong());

        assertThrows(HmsException.class, () -> service.create(entity));

    }

    @Test
    public void create_entity_validationError() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.CREATE_VALIDATION_ERROR, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doThrow(HmsException.class).when(validationUtils).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> service.create(entity));

    }

    @Test
    public void update_entity_successfully() {

        TestCallbacks<T> config = this.callbacks.getOrDefault(TestName.UPDATE_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));
        doReturn(true).when(repository).existsById(anyLong());

        T updated = service.update(entity);

        assertNotNull(updated);

        config.getAfter().accept(updated);
    }

    @Test
    public void update_entity_validationError() {
        TestCallbacks<T> config = callbacks.getOrDefault(TestName.UPDATE_VALIDATION_ERROR, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doThrow(HmsException.class).when(validationUtils).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> service.create(entity));

        config.getAfter().accept(entity);
    }

    @Test
    public void update_entity_idNotFound() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.UPDATE_ID_NOT_FOUND, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(false).when(repository).existsById(anyLong());

        assertThrows(HmsException.class, () -> service.update(entity));

        config.getAfter().accept(entity);
    }

    @Test
    public void find_entity_successfully() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.FIND_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(Optional.of(entity)).when(repository).findById(anyLong());

        T foundEntity = service.find(1L);
        assertEquals(entity.getId(), foundEntity.getId());

        config.getAfter().accept(foundEntity);
    }

    @Test
    public void find_entity_idNull() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.FIND_ID_NULL, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.find(null));

        config.getAfter().accept(entity);
    }

    @Test
    public void find_entity_notFound() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.FIND_ID_NOT_FOUND, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThrows(HmsException.class, () -> service.find(1L));

        config.getAfter().accept(entity);
    }

    @Test
    public void delete_employee_successfully() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.DELETE_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(true, false).when(repository).existsById(anyLong());

        assertTrue(service.delete(entity.getId()));

        config.getAfter().accept(entity);
    }

    @Test
    public void delete_entity_idNull() {
        TestCallbacks<T> config = callbacks.getOrDefault(TestName.DELETE_ID_NULL, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(null));

        config.getAfter().accept(entity);
    }

    @Test
    public void delete_entity_notFound() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.DELETE_ID_NOT_FOUND, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(Long.MAX_VALUE));

        config.getAfter().accept(entity);
    }

    @Test
    public void delete_entity_failed() {
        TestCallbacks<T> config = callbacks.getOrDefault(TestName.DELETE_FAILED, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(true, true).when(repository).existsById(anyLong());

        assertFalse(service.delete(1L));
        config.getAfter().accept(entity);
    }

    @Test
    public void update_entity_null() {

        TestCallbacks<T> config = callbacks.getOrDefault(TestName.UPDATE_ENTITY_NULL, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.update(null));

        config.getAfter().accept(entity);
    }
}