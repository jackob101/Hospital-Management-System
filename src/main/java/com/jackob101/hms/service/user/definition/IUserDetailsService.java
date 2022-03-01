package com.jackob101.hms.service.user.definition;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.ICrudOperations;

import java.util.List;

public interface IUserDetailsService extends ICrudOperations<UserDetails> {

    List<UserDetails> findAll();
}
