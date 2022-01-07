package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl extends BaseService<UserDetails> implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository, Validator validator) {
        super(validator, "user_details.null", "user_details.");
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails create(UserDetails entity) {

        validate(entity, OnCreate.class);

        if (entity.getId() != null && userDetailsRepository.existsById(entity.getId()))
            throw new HmsException("user_details.already_exists", HttpStatus.BAD_REQUEST);

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
            throw new HmsException("user_details.not_found", HttpStatus.BAD_REQUEST);

        return userDetails;
    }

    @Override
    public boolean delete(Long id) {

        if (id == null)
            throw new HmsException("service.delete.id_null", "User Details");

        boolean isFound = userDetailsRepository.existsById(id);

        if (!isFound)
            throw new HmsException("service.delete.id_not_found", "User Details", id);

        userDetailsRepository.deleteById(id);

        return !userDetailsRepository.existsById(id);

    }

    @Override
    public UserDetails find(Long id) {

        if (id == null)
            throw new HmsException("id.null", HttpStatus.BAD_REQUEST);

        Optional<UserDetails> byId = userDetailsRepository.findById(id);

        return byId.orElseThrow(() -> new HmsException("user_details.not_found"));
    }

    @Override
    public List<UserDetails> findAll() {

        return userDetailsRepository.findAll();
    }
}
