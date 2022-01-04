package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    public UserDetailsServiceImpl(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public UserDetails save(UserDetails entity) {

        if (entity == null)
            throw new RuntimeException("User Details cannot be null");

        return userDetailsRepository.save(entity);
    }

    @Override
    public UserDetails update(UserDetails entity) {

        if (entity == null)
            throw new RuntimeException("User Details cannot be null");

        boolean isFound = userDetailsRepository.existsById(entity.getId());

        UserDetails userDetails;

        if (isFound)
            userDetails = save(entity);
        else
            throw new RuntimeException("User Details does not exist");

        return userDetails;
    }

    @Override
    public boolean delete(UserDetails entity) {

        if (entity == null)
            throw new RuntimeException("User Details cannot be null");

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

        if (id < 0)
            throw new RuntimeException("Id cannot be a negative number");

        Optional<UserDetails> byId = userDetailsRepository.findById(id);

        return byId.orElseThrow(() -> new RuntimeException("User not found"));
    }

}
