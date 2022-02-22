package com.jackob101.hms.unittests.service;

import com.jackob101.hms.model.IEntity;
import lombok.Setter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Setter
public class TestCallbacks<T extends IEntity, F extends IEntity> {

    private Consumer<T> before;

    private Consumer<T> after;

    private BiConsumer<T, F> beforeForm;

    public Consumer<T> getBefore() {
        if (before == null)
            return t -> {
            };
        return before;
    }

    public Consumer<T> getAfter() {
        if (after == null)
            return t -> {
            };
        return after;
    }

    public BiConsumer<T, F> getBeforeForm() {
        if (beforeForm == null)
            return (t, f) -> {
            };
        return beforeForm;
    }
}
