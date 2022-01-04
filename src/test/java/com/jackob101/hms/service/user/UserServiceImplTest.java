package com.jackob101.hms.service.user;

import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserDetailsRepository userDetailsRepository;

    @InjectMocks
    UserServiceImpl userService;

    UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = UserDetails.builder()
                .id(1L)
                .firstName("John")
                .secondName("Tom")
                .lastName("Noice")
                .dateOfBirth(LocalDate.now())
                .phoneNumber("123_456_789")
                .pesel("123456789")
                .build();

    }

    @Test
    void save() {
        //given

        //when
        doAnswer(returnsFirstArg()).when(userDetailsRepository).save(any(UserDetails.class));

        //then
        assertThrows(RuntimeException.class,() -> userService.save(null));

        UserDetails saved = userService.save(userDetails);

        assertNotNull(saved);
        assertEquals(userDetails.getFirstName(),saved.getFirstName());
    }

    @Test
    void update_userDetails_is_null() {

        assertThrows(RuntimeException.class,() -> userService.update(null));

    }

    @Test
    void update_userDetails_not_found() {

        doReturn(false).when(userDetailsRepository).existsById(anyLong());

        assertThrows(RuntimeException.class,() -> userService.update(userDetails));

    }

    @Test
    void update_userDetails_updated() {

        doAnswer(returnsFirstArg()).when(userDetailsRepository).save(any(UserDetails.class));
        doReturn(true).when(userDetailsRepository).existsById(anyLong());

        UserDetails updated = userService.update(userDetails);

        assertEquals(userDetails.getFirstName(),updated.getFirstName());
        assertEquals(userDetails.getId(),updated.getId());

    }

    @Test
    void delete_userDetails_is_null() {

        assertThrows(RuntimeException.class,() -> userService.delete(null));
    }

    @Test
    void delete_userDetails_not_found() {

        doReturn(false).when(userDetailsRepository).existsById(userDetails.getId());

        assertThrows(RuntimeException.class,() -> userService.delete(userDetails));

    }

    @Test
    void delete_success() {

        doReturn(true, false).when(userDetailsRepository).existsById(userDetails.getId());

        assertTrue(userService.delete(userDetails));

    }

    @Test
    void find_id_null() {

        assertThrows(RuntimeException.class,() -> userService.find(null));

    }

    @Test
    void find_id_less_than_zero() {

        assertThrows(RuntimeException.class,() -> userService.find(-10L));

    }

    @Test
    void find_userDetails_not_found() {

        doReturn(Optional.empty()).when(userDetailsRepository).findById(anyLong());

        assertThrows(RuntimeException.class,() -> userService.find(1L));

    }

    @Test
    void find_userDetails_found() {

        doReturn(Optional.of(userDetails)).when(userDetailsRepository).findById(anyLong());

        assertEquals(userDetails, userService.find(1L));
    }
}