package com.jackob101.hms.unittests.service;

import com.jackob101.hms.model.IEntity;
import lombok.Setter;

import java.util.function.BiConsumer;

public class TestFormCallbacks<T extends IEntity, F extends IEntity> extends TestCallbacks<T> {

    @Setter
    private BiConsumer<T, F> beforeForm;

    public BiConsumer<T, F> getBeforeForm() {
        if (beforeForm == null)
            return (t, f) -> {

            };
        return beforeForm;
    }
}
