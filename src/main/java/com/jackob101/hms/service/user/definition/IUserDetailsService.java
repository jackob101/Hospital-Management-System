package com.jackob101.hms.service.user.definition;

import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.base.FormCrudService;

import java.util.List;

public interface IUserDetailsService extends FormCrudService<UserDetails, UserDetailsForm> {

    List<UserDetails> findAll();
}
