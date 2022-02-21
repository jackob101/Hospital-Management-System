package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.CrudService;
import com.jackob101.hms.unittests.TestConfiguration;
import com.jackob101.hms.unittests.service.BaseTests;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
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
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ActiveProfiles("no-security")
@SpringBootTest
public abstract class BaseServiceTest<T extends IEntity, F extends IEntity> {

    private JpaRepository<T, Long> repository;

    @Getter
    @Setter
    private Class<T> entityClass;

    @Getter
    @Setter
    private CrudService<T> service;

    protected T entity;

    private Map<BaseTests, TestConfiguration<T, F>> configs;

    protected void configure(JpaRepository<T, Long> repository, Class<T> entityClass, CrudService<T> service) {
        this.service = service;
        this.repository = repository;
        this.entityClass = entityClass;
    }

    protected abstract void configure();

    protected abstract void setUpData();

    protected abstract void setUpCallbacks(Map<BaseTests, TestConfiguration<T, F>> configs);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configs = new EnumMap<>(BaseTests.class);
        configure();
        setUpData();
        setUpCallbacks(configs);
    }

    @Test
    void create_entity_successfully() {
        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.CREATE_SUCCESSFULLY, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));

        T savedEntity = service.create(entity);

        assertEquals(entity.getId(), savedEntity.getId());

        config.getAfter().accept(savedEntity);
    }

    @Test
    void create_entity_idTaken() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.CREATE_ID_TAKEN, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doReturn(true).when(repository).existsById(anyLong());

        assertThrows(HmsException.class, () -> service.create(entity));

    }

    //    @Test
    void create_entity_validationError() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.CREATE_VALIDATION_ERROR, new TestConfiguration<>());
        config.getBefore().accept(entity);
    }

    @Test
    void update_entity_successfully() {

        TestConfiguration<T, F> config = this.configs.getOrDefault(BaseTests.UPDATE_SUCCESSFULLY, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doAnswer(returnsFirstArg()).when(repository).save(any(entityClass));
        doReturn(true).when(repository).existsById(anyLong());

        T updated = service.update(entity);

        assertNotNull(updated);

        config.getAfter().accept(updated);
    }

    //    @Test
    void update_entity_validationError() {
        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.UPDATE_VALIDATION_ERROR, new TestConfiguration<>());
        config.getBefore().accept(entity);

        config.getAfter().accept(entity);
    }

    @Test
    void update_entity_idNotFound() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.UPDATE_ID_NOT_FOUND, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doReturn(false).when(repository).existsById(anyLong());

        assertThrows(HmsException.class, () -> service.update(entity));

        config.getAfter().accept(entity);
    }

    @Test
    void find_entity_successfully() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.FIND_SUCCESSFULLY, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doReturn(Optional.of(entity)).when(repository).findById(anyLong());

        T foundEntity = service.find(1L);
        assertEquals(entity.getId(), foundEntity.getId());

        config.getAfter().accept(foundEntity);
    }

    @Test
    void find_entity_idNull() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.FIND_ID_NULL, new TestConfiguration<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.find(null));

        config.getAfter().accept(entity);
    }

    @Test
    void find_entity_notFound() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.FIND_ID_NOT_FOUND, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doReturn(Optional.empty()).when(repository).findById(anyLong());

        assertThrows(HmsException.class, () -> service.find(1L));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_employee_successfully() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.DELETE_SUCCESSFULLY, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doReturn(true, false).when(repository).existsById(anyLong());

        assertTrue(service.delete(entity.getId()));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_entity_idNull() {
        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.DELETE_ID_NULL, new TestConfiguration<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(null));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_entity_notFound() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.DELETE_ID_NOT_FOUND, new TestConfiguration<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.delete(Long.MAX_VALUE));

        config.getAfter().accept(entity);
    }

    @Test
    void delete_entity_failed() {
        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.DELETE_FAILED, new TestConfiguration<>());
        config.getBefore().accept(entity);

        doReturn(true, true).when(repository).existsById(anyLong());

        assertFalse(service.delete(1L));
        config.getAfter().accept(entity);
    }

    @Test
    void update_entity_null() {

        TestConfiguration<T, F> config = configs.getOrDefault(BaseTests.UPDATE_ENTITY_NULL, new TestConfiguration<>());
        config.getBefore().accept(entity);

        assertThrows(HmsException.class, () -> service.update(null));

        config.getAfter().accept(entity);
    }
}