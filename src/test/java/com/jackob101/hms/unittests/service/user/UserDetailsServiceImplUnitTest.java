package com.jackob101.hms.unittests.service.user;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.UserDetailsService;
import com.jackob101.hms.unittests.service.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplUnitTest extends BaseServiceUnitTest<UserDetails, IUserDetailsService> {

    @Mock
    UserDetailsRepository repository;

    @Override
    protected IUserDetailsService configureService() {
        return new UserDetailsService(repository, getValidationUtils());
    }

    @Override
    protected UserDetails configureEntity() {
        return UserDetails.builder()
                .id(1L)
                .userCredentialsId("Some credentials")
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();
    }

    @Override
    protected JpaRepository<UserDetails, Long> configureRepository() {
        return repository;
    }

}