package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.FormCrudService;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * This class extends <em>BaseServiceUnitTest</em> and tests operations on <strong>forms</strong>.
 *
 * @param <T> The <strong>domain</strong> model of your entity
 * @param <F> The <strong>form</strong> model of your entity
 * @param <S> The service that operates on <strong>form</strong> and <strong>domain</strong> models
 * @see com.jackob101.hms.unittests.service.base.BaseServiceUnitTest
 */
@Getter
public abstract class BaseFormServiceUnitTest<T extends IEntity, F extends IEntity, S extends FormCrudService<T, F>> extends BaseServiceUnitTest<T, S> {

    protected F form;

    @Setter
    private EnumMap<TestName, TestFormCallbacks<T, F>> formCallbacks = new EnumMap<>(TestName.class);

    protected abstract F configureForm();

    /**
     * Util function that help to set up callbacks for form tests. You will need these callbacks to for example configure conversion from form to domain model
     * by mocking results of others services/repositories.
     *
     * @param formCallbacks The callbacks that this method receive when its overridden. Use them to configure test behaviour.
     * @see com.jackob101.hms.unittests.service.base.BaseServiceUnitTest#configureCallbacks(EnumMap)
     */
    protected void configureFormCallbacks(EnumMap<TestName, TestFormCallbacks<T, F>> formCallbacks) {
    }

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        this.form = configureForm();
        configureFormCallbacks(formCallbacks);
    }

    @Test
    void createFromForm_entity_successfully() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.CREATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doAnswer(returnsFirstArg()).when(configureRepository()).save(any(getEntityClass()));

        T saved = configureService().createFromForm(form);
        assertNotNull(saved);

        callbacks.getAfter().accept(saved);
    }

    @Test
    void createFromForm_entity_validationError() {
        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.CREATE_VALIDATION_ERROR, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doThrow(HmsException.class).when(getValidationUtils()).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> configureService().createFromForm(form));

        callbacks.getAfter().accept(null);
    }

    @Test
    void updateFromForm_entity_successfully() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.UPDATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doAnswer(returnsFirstArg()).when(configureRepository()).save(any(getEntityClass()));
        doReturn(true).when(configureRepository()).existsById(anyLong());

        T updated = configureService().updateFromForm(form);

        assertNotNull(updated);


        callbacks.getAfter().accept(updated);
    }

    @Test
    void updateFromForm_entity_validationError() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.UPDATE_FROM_FORM_VALIDATION_ERROR, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doThrow(HmsException.class).when(getValidationUtils()).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> configureService().createFromForm(form));

        callbacks.getAfter().accept(null);
    }
}
