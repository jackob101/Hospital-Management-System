package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.CrudService;
import com.jackob101.hms.unittests.service.TestCallbacks;
import com.jackob101.hms.unittests.service.TestName;
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

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ActiveProfiles("no-security")
@SpringBootTest
public abstract class BaseServiceTest<T extends IEntity, F extends IEntity> {

    private JpaRepository<T, Long> repository;

    @Spy
    protected ValidationUtils validationUtils;

    @Getter
    @Setter
    private Class<T> entityClass;

    @Getter
    @Setter
    private CrudService<T> service;

    protected T entity;

    private Map<TestName, TestCallbacks<T, F>> configs;

    protected void configure(JpaRepository<T, Long> repository, Class<T> entityClass, CrudService<T> service) {
        this.service = service;
        this.repository = repository;
        this.entityClass = entityClass;
    }

    protected abstract void configure();

    protected void setUpData() {
    }

    ;

    protected void setUpCallbacks(Map<TestName, TestCallbacks<T, F>> configs) {
    }

    ;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configs = new EnumMap<>(TestName.class);
        configure();
        setUpData();
        setUpCallbacks(configs);
    }

    @Test
    void create_entity_successfully() {
        TestCallbacks<T, F> config = configs.getOrDefault(TestName.CREATE_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));

        T savedEntity = service.create(entity);

        assertEquals(entity.getId(), savedEntity.getId());

        config.getAfter().accept(savedEntity);
    }

    @Test
    void create_entity_idTaken() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.CREATE_ID_TAKEN, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(true).when(repository).existsById(anyLong());

        assertThrows(HmsException.class, () -> service.create(entity));

    }

    @Test
    void create_entity_validationError() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.CREATE_VALIDATION_ERROR, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doThrow(HmsException.class).when(validationUtils).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> service.create(entity));

    }

    @Test
    void update_entity_successfully() {

        TestCallbacks<T, F> config = this.configs.getOrDefault(TestName.UPDATE_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));
        doReturn(true).when(repository).existsById(anyLong());

        T updated = service.update(entity);

        assertNotNull(updated);

        config.getAfter().accept(updated);
    }

    @Test
    void update_entity_validationError() {
        TestCallbacks<T, F> config = configs.getOrDefault(TestName.UPDATE_VALIDATION_ERROR, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doThrow(HmsException.class).when(validationUtils).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> service.create(entity));

        config.getAfter().accept(entity);
    }

    @Test
    void update_entity_idNotFound() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.UPDATE_ID_NOT_FOUND, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(false).when(repository).existsById(anyLong());

        assertThrows(HmsException.class, () -> service.update(entity));

        config.getAfter().accept(entity);
    }

    @Test
    void find_entity_successfully() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.FIND_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(Optional.of(entity)).when(repository).findById(anyLong());

        T foundEntity = service.find(1L);
        assertEquals(entity.getId(), foundEntity.getId());

        config.getAfter().accept(foundEntity);
    }

    @Test
    void find_entity_idNull() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.FIND_ID_NULL, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.find(null));

        config.getAfter().accept(entity);
    }

    @Test
    void find_entity_notFound() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.FIND_ID_NOT_FOUND, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThrows(HmsException.class, () -> service.find(1L));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_employee_successfully() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.DELETE_SUCCESSFULLY, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(true, false).when(repository).existsById(anyLong());

        assertTrue(service.delete(entity.getId()));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_entity_idNull() {
        TestCallbacks<T, F> config = configs.getOrDefault(TestName.DELETE_ID_NULL, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(null));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_entity_notFound() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.DELETE_ID_NOT_FOUND, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(Long.MAX_VALUE));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_entity_failed() {
        TestCallbacks<T, F> config = configs.getOrDefault(TestName.DELETE_FAILED, new TestCallbacks<>());
        config.getBefore().accept(entity);

        doReturn(true, true).when(repository).existsById(anyLong());

        assertFalse(service.delete(1L));
        config.getAfter().accept(entity);
    }

    @Test
    void update_entity_null() {

        TestCallbacks<T, F> config = configs.getOrDefault(TestName.UPDATE_ENTITY_NULL, new TestCallbacks<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.update(null));

        config.getAfter().accept(entity);
    }
}