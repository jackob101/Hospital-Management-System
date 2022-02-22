package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.FormCrudService;
import com.jackob101.hms.unittests.service.TestFormCallbacks;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Getter
public abstract class BaseFormServiceTest<T extends IEntity, F extends IEntity, S extends FormCrudService<T, F>> extends BaseServiceTest<T, S> {

    protected F form;

    @Setter
    private EnumMap<TestName, TestFormCallbacks<T, F>> formCallbacks = new EnumMap<>(TestName.class);

    protected void setData(T entity, F form) {
        setData(entity);
        this.form = form;
    }


    @Test
    void createFromForm_entity_successfully() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.CREATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(getEntity(), form);

        doAnswer(returnsFirstArg()).when(getRepository()).save(any(getEntityClass()));

        T saved = getService().createFromForm(form);
        assertNotNull(saved);

        callbacks.getAfter().accept(saved);
    }

    @Test
    void createFromForm_entity_validationError() {
        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.CREATE_VALIDATION_ERROR, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(getEntity(), form);

        doThrow(HmsException.class).when(getValidationUtils()).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> getService().createFromForm(form));

        callbacks.getAfter().accept(null);
    }

    @Test
    void updateFromForm_entity_successfully() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.UPDATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(getEntity(), form);

        doAnswer(returnsFirstArg()).when(getRepository()).save(any(getEntityClass()));
        doReturn(true).when(getRepository()).existsById(anyLong());

        T updated = getService().updateFromForm(form);

        assertNotNull(updated);


        callbacks.getAfter().accept(updated);
    }

    @Test
    void updateFromForm_entity_validationError() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.UPDATE_FROM_FORM_VALIDATION_ERROR, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(getEntity(), form);

        doThrow(HmsException.class).when(getValidationUtils()).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> getService().createFromForm(form));

        callbacks.getAfter().accept(null);
    }
}
