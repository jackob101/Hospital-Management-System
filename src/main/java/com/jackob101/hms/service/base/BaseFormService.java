package com.jackob101.hms.service.base;

import com.jackob101.hms.model.IEntity;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseFormService<T extends IEntity, F> extends BaseService<T> implements FormCrudService<T, F> {

    public BaseFormService(ValidationUtils validator, Class<T> entityClass, JpaRepository<T, Long> repository) {
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
