package com.jackob101.hms.unittests;

import com.jackob101.hms.model.IEntity;
import lombok.Setter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Setter
public class TestConfiguration<T extends IEntity, F extends IEntity> {

    private Consumer<T> before;

    private Consumer<T> after;

    private BiConsumer<T, F> beforeForm;

    public Consumer<T> getBefore() {
        if (before == null)
            return new Consumer<T>() {
                @Override
                public void accept(T t) {

                }
            };
        return before;
    }

    public Consumer<T> getAfter() {
        if (after == null)
            return new Consumer<T>() {
                @Override
                public void accept(T t) {

                }
            };
        return after;
    }

    public BiConsumer<T, F> getBeforeForm() {
        if (beforeForm == null)
            return new BiConsumer<T, F>() {
                @Override
                public void accept(T t, F f) {

                }
            };
        return beforeForm;
    }
}
