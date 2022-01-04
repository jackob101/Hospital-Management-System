package com.jackob101.hms.service.user;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.base.CrudService;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends CrudService<UserDetails, Long> {

}
