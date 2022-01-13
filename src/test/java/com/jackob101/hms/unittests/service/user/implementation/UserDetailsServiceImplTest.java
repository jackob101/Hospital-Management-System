package com.jackob101.hms.unittests.service.user.implementation;

import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import com.jackob101.hms.service.user.implementation.UserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserDetailsRepository userDetailsRepository;

    UserDetailsService userService;

    UserDetails userDetails;

    @BeforeEach
    void setUp() {

        openMocks(this);

        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        userService = new UserDetailsService(userDetailsRepository, validator);

    }

    @Test
    void save_user_details_successfully() {
        //given

        //when
        doAnswer(returnsFirstArg()).when(userDetailsRepository).save(any(UserDetails.class));

        //then
        assertThrows(RuntimeException.class, () -> userService.create(null));

        UserDetails saved = userService.create(userDetails);

        assertNotNull(saved);
        assertEquals(userDetails.getFirstName(), saved.getFirstName());
    }

    @Test
    void save_user_details_when_id_already_exists() {

        doReturn(true).when(userDetailsRepository).existsById(anyLong());

        assertThrows(HmsException.class, () -> userService.create(userDetails));
    }

    @Test
    void save_user_details_when_no_id() {

        userDetails.setId(null);
        doAnswer(returnsFirstArg()).when(userDetailsRepository).save(any(UserDetails.class));

        assertDoesNotThrow(() -> userService.create(userDetails));
        assertEquals(userDetails.getFirstName(), userService.create(userDetails).getFirstName());
    }

    @Test
    void update_user_details_when_user_details_is_null() {

        assertThrows(RuntimeException.class, () -> userService.update(null));

    }

    @Test
    void update_user_details_when_user_details_not_found() {

        doReturn(false).when(userDetailsRepository).existsById(anyLong());

        assertThrows(RuntimeException.class, () -> userService.update(userDetails));

    }

    @Test
    void update_user_details_successfully() {

        doAnswer(returnsFirstArg()).when(userDetailsRepository).save(any(UserDetails.class));
        doReturn(true).when(userDetailsRepository).existsById(anyLong());

        UserDetails updated = userService.update(userDetails);

        assertEquals(userDetails.getFirstName(), updated.getFirstName());
        assertEquals(userDetails.getId(), updated.getId());

    }

    @Test
    void delete_user_details_when_user_details_is_null() {

        assertThrows(RuntimeException.class, () -> userService.delete(null));
    }

    @Test
    void delete_user_details_when_user_details_not_found() {

        doReturn(false).when(userDetailsRepository).existsById(userDetails.getId());

        assertThrows(RuntimeException.class, () -> userService.delete(userDetails.getId()));

    }

    @Test
    void delete_user_details_successfully() {

        doReturn(true, false).when(userDetailsRepository).existsById(userDetails.getId());

        assertTrue(userService.delete(userDetails.getId()));

    }

    @Test
    void find_user_details_when_id_is_null() {

        assertThrows(RuntimeException.class, () -> userService.find(null));

    }

    @Test
    void find_user_details_when_id_is_less_than_zero() {

        assertThrows(RuntimeException.class, () -> userService.find(-10L));

    }

    @Test
    void find_user_details_when_user_details_not_found() {

        doReturn(Optional.empty()).when(userDetailsRepository).findById(anyLong());

        assertThrows(RuntimeException.class, () -> userService.find(1L));

    }

    @Test
    void find_user_details_successfully() {

        doReturn(Optional.of(userDetails)).when(userDetailsRepository).findById(anyLong());

        assertEquals(userDetails, userService.find(1L));
    }
}