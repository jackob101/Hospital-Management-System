package com.jackob101.hms.service.base;

public interface FormCrudService<T, F, ID> extends CrudService<T, ID> {

    T createFromForm(F form);

    T updateFromForm(F form);

}
