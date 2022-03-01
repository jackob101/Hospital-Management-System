package com.jackob101.hms.converter;

import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("unchecked")
public class DefaultConverter<T, F> implements Converter<F, T> {

    private final Class<T> entityClass;

    public DefaultConverter(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T convert(F source) {
        if (entityClass.isAssignableFrom(source.getClass())) {
            return (T) source;
        }
        return null;
    }
}
