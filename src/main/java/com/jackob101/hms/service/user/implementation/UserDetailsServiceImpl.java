package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.exceptions.ExceptionCode;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnDelete;
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
        super(validator, "User details cannot be null", "User details validation failed", ExceptionCode.USER_DETAILS_VALIDATION_FAILED);
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails create(UserDetails entity) {

        validate(entity, OnCreate.class);

        if (userDetailsRepository.existsById(entity.getId()))
            throw new HmsException(String.format("User with id: %d was not found", entity.getId()),
                    ExceptionCode.USER_DETAILS_ALREADY_EXISTS,
                    HttpStatus.BAD_REQUEST);

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
            throw new RuntimeException("User Details does not exist");

        return userDetails;
    }

    @Override
    public boolean delete(UserDetails entity) {

        validate(entity, OnDelete.class);

        boolean isFound = userDetailsRepository.existsById(entity.getId());

        if (!isFound)
            throw new RuntimeException("User Details not found");

        userDetailsRepository.delete(entity);
        return !userDetailsRepository.existsById(entity.getId());

    }

    @Override
    public UserDetails find(Long id) {

        if (id == null)
            throw new RuntimeException("Id cannot be null");

        Optional<UserDetails> byId = userDetailsRepository.findById(id);

        return byId.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDetails> findAll() {

        return userDetailsRepository.findAll();
    }
}
