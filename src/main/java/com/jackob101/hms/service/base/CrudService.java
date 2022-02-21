package com.jackob101.hms.service.base;

import java.util.List;

public interface CrudService<T> {

    T create(T entity);

    T update(T entity);

    boolean delete(Long id);

    T find(Long id);

    List<T> findAll();
}
