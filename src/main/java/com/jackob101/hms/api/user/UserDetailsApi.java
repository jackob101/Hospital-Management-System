package com.jackob101.hms.api.user;

import com.jackob101.hms.api.BaseController;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(UserDetailsApi.REQUEST_MAPPING)
@RestController
public class UserDetailsApi extends BaseController<UserDetails, UserDetails> {

    public final static String REQUEST_MAPPING = "user_details";

    public UserDetailsApi(IUserDetailsService userDetailsService) {
        super(userDetailsService, REQUEST_MAPPING, null);
    }

}
