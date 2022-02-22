package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.UserDetailsService;
import com.jackob101.hms.unittests.service.base.BaseServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest extends BaseServiceTest<UserDetails, IUserDetailsService> {

    @Mock
    UserDetailsRepository repository;

    @Override
    protected void configure() {
        UserDetailsService service = new UserDetailsService(repository, validationUtils);
        configure(repository, UserDetails.class, service);
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
}