package com.jackob101.hms.api.user;

import com.jackob101.hms.api.base.BaseFormController;
import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(UserDetailsApi.REQUEST_MAPPING)
@RestController
public class UserDetailsApi extends BaseFormController<UserDetails, UserDetailsForm> {

    public final static String REQUEST_MAPPING = "user_details";

    public UserDetailsApi(IUserDetailsService userDetailsService) {
        super(userDetailsService, "User Details", REQUEST_MAPPING);
    }

}
