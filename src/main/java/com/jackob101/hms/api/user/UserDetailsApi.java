package com.jackob101.hms.api.user;

import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.exceptions.ExceptionCode;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.UserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(UserDetailsApi.REQUEST_MAPPING)
@RestController
public class UserDetailsApi {


    public final static String REQUEST_MAPPING = "userdetails";
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    public UserDetailsApi(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllUserDetails() {

        List<UserDetails> all = userDetailsService.findAll();

        return ResponseEntity.ok(all);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUserDetails(@PathVariable("id") Long id) {

        UserDetails userDetails = userDetailsService.find(id);

        if (userDetails == null)
            log.error("User with id: " + id + " was not found");
        return ResponseEntity.ok(userDetails);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUserDetails(@RequestBody @Valid UserDetailsDTO userDetailsDTO, BindingResult bindingResult) {

        log.info("Creating new user.");

        checkBinding(bindingResult, HttpStatus.BAD_REQUEST);

        UserDetails userDetails = modelMapper.map(userDetailsDTO, UserDetails.class);

        UserDetails saved = userDetailsService.create(userDetails);

        if (saved == null)
            throw new HmsException("Error during creating User Details",
                    ExceptionCode.USER_DETAILS_CREATION_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        log.info("User with id: " + saved.getId() + "created successfully.");

        return ResponseEntity.created(URI.create("/" + REQUEST_MAPPING + "/" + saved.getId()))
                .body(saved);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@RequestBody @Valid UserDetailsDTO userDetailsDTO, BindingResult bindingResult) throws URISyntaxException {

        if (userDetailsDTO.getId() == null)
            bindingResult.addError(new ObjectError("userdetails", "Id cannot be null"));

        checkBinding(bindingResult, HttpStatus.BAD_REQUEST);

        log.info("Updating user details with id: " + userDetailsDTO.getId());

        UserDetails userDetails = modelMapper.map(userDetailsDTO, UserDetails.class);

        UserDetails updated = userDetailsService.update(userDetails);

        if (updated == null)
            throw new HmsException("User details could not be update",
                    ExceptionCode.USER_DETAILS_UPDATE_FAILED,
                    HttpStatus.INTERNAL_SERVER_ERROR);

        log.info("User details with id: " + updated.getId() + " were updated successfully");

        return ResponseEntity.created(new URI("/" + REQUEST_MAPPING + "/" + updated.getId()))
                .body(updated);


    }

    private void checkBinding(BindingResult bindingResult, HttpStatus httpStatus) {

        if (bindingResult.hasErrors()) {

            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(" |-| "));

            log.error("Error during binding data to model.");
            throw new HmsException(errorMessage, ExceptionCode.USER_DETAILS_BINDING_ERROR, httpStatus);
        }

    }


}
