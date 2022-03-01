package com.jackob101.hms.unittests.service.user;

import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.UserDetailsService;
import com.jackob101.hms.unittests.service.base.BaseFormServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplUnitTest extends BaseFormServiceUnitTest<UserDetails, UserDetailsForm, IUserDetailsService> {

    @Mock
    UserDetailsRepository repository;

    private UserDetails userDetails;

    @Override
    protected IUserDetailsService configureService() {
        return new UserDetailsService(repository, getValidationUtils());
    }

    @Override
    protected UserDetails configureEntity() {
        userDetails = UserDetails.builder()
                .id(1L)
                .userCredentialsId("Some credentials")
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();
        return userDetails;
    }

    @Override
    protected JpaRepository<UserDetails, Long> configureRepository() {
        return repository;
    }

    @Override
    protected UserDetailsForm configureForm() {
        return new UserDetailsForm(1L,
                userDetails.getUserCredentialsId(),
                userDetails.getPesel(),
                userDetails.getFirstName(),
                userDetails.getSecondName(),
                userDetails.getLastName(),
                userDetails.getDateOfBirth(),
                userDetails.getPhoneNumber());
    }

}