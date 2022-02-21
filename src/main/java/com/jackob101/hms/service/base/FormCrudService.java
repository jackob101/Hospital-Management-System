package com.jackob101.hms.service.base;

public interface FormCrudService<T, F> extends CrudService<T> {

    T createFromForm(F form);

    T updateFromForm(F form);

    T convert(F form);

}
