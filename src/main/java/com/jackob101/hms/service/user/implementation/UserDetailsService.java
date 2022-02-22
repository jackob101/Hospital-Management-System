package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.base.BaseFormService;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.validation.ValidationUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService extends BaseFormService<UserDetails, UserDetailsForm> implements IUserDetailsService {

    public UserDetailsService(UserDetailsRepository userDetailsRepository, ValidationUtils validator) {
        super(validator, UserDetails.class, userDetailsRepository);
    }

    public UserDetails convert(UserDetailsForm userDetailsForm) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(userDetailsForm, UserDetails.class);
    }

}
