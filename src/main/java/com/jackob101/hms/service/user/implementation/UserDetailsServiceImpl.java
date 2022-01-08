package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl extends BaseService<UserDetails> implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository, Validator validator) {
        super(validator, "User Details");
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails create(UserDetails entity) {

        validate(entity, OnCreate.class);

        if (entity.getId() != null && userDetailsRepository.existsById(entity.getId()))
            throw HmsException.params("User Details", entity.getId()).code("service.create.id_is_taken");

        return userDetailsRepository.save(entity);
    }

    @Override
    public UserDetails update(UserDetails entity) {

        validate(entity, OnUpdate.class);

        boolean isFound = userDetailsRepository.existsById(entity.getId());

        UserDetails userDetails;

        if (isFound)
            userDetails = userDetailsRepository.save(entity);
        else
            throw HmsException.params("User Details", entity.getId()).code("service.update.entity_not_found");

        return userDetails;
    }

    @Override
    public boolean delete(Long id) {

        if (id == null)
            throw HmsException.params("User Details").code("service.delete.id_null");

        boolean isFound = userDetailsRepository.existsById(id);

        if (!isFound)
            throw HmsException.params("User Details", id).code("service.delete.id_not_found");

        userDetailsRepository.deleteById(id);

        return !userDetailsRepository.existsById(id);

    }

    @Override
    public UserDetails find(Long id) {

        if (id == null)
            throw HmsException.code("id.null");

        Optional<UserDetails> byId = userDetailsRepository.findById(id);

        return byId.orElseThrow(() -> HmsException.params("User Details", id).code("service.find.not_found"));
    }

    @Override
    public List<UserDetails> findAll() {

        return userDetailsRepository.findAll();
    }
}
