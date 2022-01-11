package com.jackob101.hms.service.user.definition;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.base.CrudService;

import java.util.List;

public interface IUserDetailsService extends CrudService<UserDetails, Long> {

    List<UserDetails> findAll();
}
