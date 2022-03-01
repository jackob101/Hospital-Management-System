package com.jackob101.hms.TestUtils.data.user;

import com.jackob101.hms.TestUtils.data.DataGenerator;
import com.jackob101.hms.model.user.UserDetails;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserDetailsGenerator implements DataGenerator<UserDetails> {

    private int firstNameLength = 10;
    private int secondNameLength = 10;
    private int lastNameLength = 10;

    @Override
    public List<UserDetails> generate(int amount) {

        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());

    }

    @Override
    public UserDetails generateSingle() {
        return UserDetails.builder()
                .firstName(RandomStringUtils.randomAlphabetic(firstNameLength))
                .secondName(RandomStringUtils.randomAlphabetic(secondNameLength))
                .lastName(RandomStringUtils.randomAlphabetic(lastNameLength))
                .userCredentialsId(RandomStringUtils.randomAlphabetic(10))
                .dateOfBirth(LocalDate.now())
                .pesel(RandomStringUtils.random(10, false, true))
                .phoneNumber(RandomStringUtils.random(10, false, true))
                .build();
    }

    public UserDetailsGenerator firstNameLength(int firstNameLength) {
        this.firstNameLength = firstNameLength;
        return this;
    }

    public UserDetailsGenerator secondNameLength(int secondNameLength) {
        this.secondNameLength = secondNameLength;
        return this;
    }

    public UserDetailsGenerator lastNameLength(int lastNameLength) {
        this.lastNameLength = lastNameLength;
        return this;
    }

}
