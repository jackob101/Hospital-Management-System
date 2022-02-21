package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.implementation.UserDetailsService;
import com.jackob101.hms.unittests.TestConfiguration;
import com.jackob101.hms.unittests.service.BaseTests;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest extends BaseServiceTest<UserDetails, UserDetailsForm> {

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Override
    protected void configure() {

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        UserDetailsService service = new UserDetailsService(userDetailsRepository, validator);
        configure(userDetailsRepository, UserDetails.class, service);
    }

    @Override
    protected void setUpData() {
        this.entity = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();
    }

    @Override
    protected void setUpCallbacks(Map<BaseTests, TestConfiguration<UserDetails, UserDetailsForm>> configs) {
    }
}