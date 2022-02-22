package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.dto.user.UserDetailsForm;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import com.jackob101.hms.service.user.implementation.UserDetailsService;
import com.jackob101.hms.unittests.service.base.BaseFormServiceTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest extends BaseFormServiceTest<UserDetails, UserDetailsForm, IUserDetailsService> {

    @Mock
    UserDetailsRepository repository;

    @Override
    protected void configure() {
        UserDetailsService service = new UserDetailsService(repository, getValidationUtils());
        configure(repository, service);
    }

    @Override
    protected void setUpData() {
        UserDetails userDetails = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();

        UserDetailsForm userDetailsForm = new UserDetailsForm(1L,
                userDetails.getUserCredentialsId(),
                userDetails.getPesel(),
                userDetails.getFirstName(),
                userDetails.getSecondName(),
                userDetails.getLastName(),
                userDetails.getDateOfBirth(),
                userDetails.getPhoneNumber());


        setData(userDetails, userDetailsForm);
    }
}