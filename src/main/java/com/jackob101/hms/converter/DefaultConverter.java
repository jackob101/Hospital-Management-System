package com.jackob101.hms.converter;

import org.springframework.core.convert.converter.Converter;

public class DefaultConverter<T> implements Converter<T, T> {
    @Override
    public T convert(T source) {
        return source;
    }
}
