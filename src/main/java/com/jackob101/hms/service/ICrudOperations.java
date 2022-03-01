package com.jackob101.hms.service;

import java.util.List;

public interface ICrudOperations<T> {

    T create(T entity);

    T update(T entity);

    boolean delete(Long id);

    T find(Long id);

    List<T> findAll();
}
