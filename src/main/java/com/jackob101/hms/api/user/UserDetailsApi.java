package com.jackob101.hms.api.user;

import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.service.user.definition.IUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RequestMapping(UserDetailsApi.REQUEST_MAPPING)
@RestController
public class UserDetailsApi {


    public final static String REQUEST_MAPPING = "userdetails";
    private final IUserDetailsService userDetailsService;
    private final ModelMapper modelMapper;

    public UserDetailsApi(IUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllUserDetails() {

        log.info("Fetching all User Details");

        List<UserDetails> all = userDetailsService.findAll();

        log.info("Fetched all User Details");

        return ResponseEntity.ok(all);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getUserDetails(@PathVariable("id") Long id) {

        log.info("Searching for User Details with id: " + id);

        UserDetails userDetails = userDetailsService.find(id);

        log.info("User Details with id: " + id + " was found");

        return ResponseEntity.ok(userDetails);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createUserDetails(@RequestBody UserDetailsDTO userDetailsDTO) {

        log.info("Creating new user.");

        UserDetails userDetails = modelMapper.map(userDetailsDTO, UserDetails.class);

        UserDetails saved = userDetailsService.create(userDetails);

        log.info("User with id: " + saved.getId() + " created successfully.");

        return ResponseEntity
                .created(URI.create("/" + REQUEST_MAPPING + "/" + saved.getId()))
                .body(saved);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@RequestBody UserDetailsDTO userDetailsDTO) throws URISyntaxException {

        log.info("Updating user details with id: " + userDetailsDTO.getId());

        UserDetails userDetails = modelMapper.map(userDetailsDTO, UserDetails.class);

        UserDetails updated = userDetailsService.update(userDetails);

        log.info("User details with id: " + updated.getId() + " were updated successfully");

        return ResponseEntity
                .ok(updated);
    }


}
