package com.jackob101.hms.unittests.service.base;

import com.jackob101.hms.model.IEntity;
import lombok.Setter;

import java.util.function.Consumer;

@Setter
public class TestCallbacks<T extends IEntity> {

    private Consumer<T> before;

    private Consumer<T> after;


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
}
