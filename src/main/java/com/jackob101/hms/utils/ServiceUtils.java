package com.jackob101.hms.utils;

import com.jackob101.hms.exceptions.HmsException;
import org.springframework.data.repository.CrudRepository;

public class ServiceUtils<T> {

    private final CrudRepository<T, Long> repository;
    private final String entityName;
    private final Class<T> entityClass;

    public ServiceUtils(CrudRepository<T, Long> repository, Class<T> entityClass) {
        this.repository = repository;
        this.entityName = entityClass.getSimpleName().replaceAll("(?<!^)([A-Z])", " $1");
        this.entityClass = entityClass;
    }

    public String getFormattedName() {
        return this.entityName;
    }

    public void checkIdAvailability(Long id) {

        if (id != null && repository.existsById(id))
            throw HmsException.params(entityName, id).code("%s ID: %s is already taken");

    }

    public void checkIdForDeletion(Long id) {

        if (id == null)
            throw HmsException.params(entityName).code("Could not delete %s because given ID is null");

        boolean isFound = repository.existsById(id);

        if (!isFound)
            throw HmsException.params(entityName, id).code("Could not delete %s because entity with ID %s was not found");
    }

    public void checkIdForSearch(Long id) {
        if (id == null)
            throw HmsException.params(entityName).code("Could not find %s because given ID is null");
    }

    public void checkIdForUpdate(Long id) {
        boolean isFound = repository.existsById(id);

        if (!isFound)
            throw HmsException.params(entityName, id).code("%s with ID: %s couldn't be updated because entity with that ID doesn't exist");
    }
}
