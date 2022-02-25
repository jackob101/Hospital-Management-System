package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.service.base.FormCrudService;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * This class extends <em>BaseServiceUnitTest</em> and tests operations on <strong>forms</strong>. <br>
 * <p>
 * If entity have some advanced conversion mechanism from <strong>form</strong> to <strong>domain model</strong> don't forget to mock
 * other services implementations otherwise the tests will fail.
 *
 * @param <T> The <strong>domain</strong> model of your entity
 * @param <F> The <strong>form</strong> model of your entity
 * @param <S> The service that operates on <strong>form</strong> and <strong>domain</strong> models
 * @see com.jackob101.hms.unittests.service.base.BaseServiceUnitTest
 */
@Getter
public abstract class BaseFormServiceUnitTest<T extends IEntity, F extends IEntity, S extends FormCrudService<T, F>> extends BaseServiceUnitTest<T, S> {

    public enum FormTestNames {
        CREATE_FROM_FORM_SUCCESSFULLY,
        CREATE_FROM_FORM_VALIDATION_ERROR,
        UPDATE_FROM_FORM_SUCCESSFULLY,
        UPDATE_FROM_FORM_VALIDATION_ERROR
    }

    @Setter
    public class TestFormCallbacks {

        private BiConsumer<T, F> beforeForm;

        private Consumer<T> after;

        public BiConsumer<T, F> getBeforeForm() {
            if (beforeForm == null) return (t, f) -> {
            };
            return beforeForm;
        }

        public Consumer<T> getAfter() {
            if (after == null) return (t) -> {
            };
            return after;
        }
    }

    protected F form;

    @Setter
    private EnumMap<FormTestNames, TestFormCallbacks> formCallbacks = new EnumMap<>(FormTestNames.class);

    protected abstract F configureForm();

    /**
     * Util function that help to set up callbacks for form tests. You will need these callbacks to for example configure conversion from form to domain model
     * by mocking results of others services/repositories.
     *
     * @param formCallbacks The callbacks that this method receive when its overridden. Use them to configure test behaviour.
     * @see com.jackob101.hms.unittests.service.base.BaseServiceUnitTest#configureCallbacks(EnumMap)
     */
    protected void configureFormCallbacks(EnumMap<FormTestNames, TestFormCallbacks> formCallbacks) {
    }

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        Arrays.stream(FormTestNames.values()).forEach(name -> formCallbacks.put(name, new TestFormCallbacks()));

        this.form = configureForm();
        configureFormCallbacks(formCallbacks);
    }

    /**
     * After Callback accepts - saved entity
     */
    @Test
    void createFromForm_entity_successfully() {

        TestFormCallbacks callbacks = formCallbacks.getOrDefault(FormTestNames.CREATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doAnswer(returnsFirstArg()).when(configureRepository()).save(any(getEntityClass()));

        T saved = configureService().createFromForm(form);
        assertNotNull(saved);

        callbacks.getAfter().accept(saved);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    void createFromForm_entity_validationError() {
        TestFormCallbacks callbacks = formCallbacks.getOrDefault(FormTestNames.CREATE_FROM_FORM_VALIDATION_ERROR, new TestFormCallbacks());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doThrow(HmsException.class).when(getValidationUtils()).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> configureService().createFromForm(form));

        callbacks.getAfter().accept(null);
    }

    /**
     * After Callback accepts - updated entity
     */
    @Test
    void updateFromForm_entity_successfully() {

        TestFormCallbacks callbacks = formCallbacks.getOrDefault(FormTestNames.UPDATE_FROM_FORM_SUCCESSFULLY, new TestFormCallbacks());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doAnswer(returnsFirstArg()).when(configureRepository()).save(any(getEntityClass()));
        doReturn(true).when(configureRepository()).existsById(anyLong());

        T updated = configureService().updateFromForm(form);

        assertNotNull(updated);


        callbacks.getAfter().accept(updated);
    }

    /**
     * After Callback accepts - no value
     */
    @Test
    void updateFromForm_entity_validationError() {

        TestFormCallbacks callbacks = formCallbacks.getOrDefault(FormTestNames.UPDATE_FROM_FORM_VALIDATION_ERROR, new TestFormCallbacks());
        callbacks.getBeforeForm().accept(configureEntity(), form);

        doThrow(HmsException.class).when(getValidationUtils()).validate(any(Object.class), any(String.class), any(Class.class));

        assertThrows(HmsException.class, () -> configureService().createFromForm(form));

        callbacks.getAfter().accept(null);
    }
}
