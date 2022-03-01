package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.BaseService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.validation.ValidationUtils;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService extends BaseService<UserDetails> implements IUserDetailsService {

    public UserDetailsService(UserDetailsRepository userDetailsRepository, ValidationUtils validator) {
        super(validator, UserDetails.class, userDetailsRepository);
    }

}
