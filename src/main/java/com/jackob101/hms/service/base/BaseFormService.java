package com.jackob101.hms.service.base;

import com.jackob101.hms.model.IEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.Validator;

public abstract class BaseFormService<T extends IEntity, F> extends BaseService<T> implements FormCrudService<T, F> {

    public BaseFormService(Validator validator, Class<T> entityClass, JpaRepository<T, Long> repository) {
        super(validator, entityClass, repository);
    }

    @Override
    public T createFromForm(F form) {
        return create(convert(form));
    }

    @Override
    public T updateFromForm(F form) {
        return update(convert(form));
    }

}
