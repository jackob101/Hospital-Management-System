package com.jackob101.hms.service.base;

public interface CrudService<T, ID> {

    T create(T entity);

    T update(T entity);

    boolean delete(T entity);

    T find(ID id);
}
