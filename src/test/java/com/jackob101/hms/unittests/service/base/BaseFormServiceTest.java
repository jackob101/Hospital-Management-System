package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.FormCrudService;
import com.jackob101.hms.unittests.service.TestFormCallbacks;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public abstract class BaseFormServiceTest<T extends IEntity, F extends IEntity, S extends FormCrudService<T, F>> extends BaseServiceTest<T, S> {

    protected F form;

    @Setter
    private EnumMap<TestName, TestFormCallbacks<T, F>> formCallbacks = new EnumMap<>(TestName.class);

    protected void setData(T entity, F form) {
        setData(entity);
        this.form = form;
    }

//    public void putFormCallback(TestName testName, )

    @Test
    void createFromForm_entity_successfully() {

        TestFormCallbacks<T, F> callbacks = formCallbacks.getOrDefault(TestName.CREATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks<>());
        callbacks.getBeforeForm().accept(entity, form);

        doAnswer(returnsFirstArg()).when(repository).save(any(getEntityClass()));

        T saved = service.createFromForm(form);
        assertNotNull(saved);

        callbacks.getAfter().accept(saved);
    }

}
