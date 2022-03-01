package com.jackob101.hms.TestUtils.data.user;

import com.jackob101.hms.TestUtils.data.DataGenerator;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PatientGenerator implements DataGenerator<Patient> {

    private int languageLength = 10;
    private int nationalityLength = 10;
    private int religionLength = 10;
    private UserDetailsGenerator userDetailsGen = new UserDetailsGenerator();

    @Override
    public List<Patient> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public Patient generateSingle() {
        return Patient.builder()
                .maritalStatus(MaritalStatus.SINGLE)
                .language(RandomStringUtils.randomAlphabetic(languageLength))
                .nationality(RandomStringUtils.randomAlphabetic(nationalityLength))
                .religion(RandomStringUtils.randomAlphabetic(religionLength))
                .userDetails(userDetailsGen.generateSingle())
                .build();
    }

    public PatientGenerator languageLength(int languageLength) {
        this.languageLength = languageLength;
        return this;
    }

    public PatientGenerator nationalityLength(int nationalityLength) {
        this.nationalityLength = nationalityLength;
        return this;
    }

    public PatientGenerator religionLength(int religionLength) {
        this.religionLength = religionLength;
        return this;
    }

    public PatientGenerator userDetailsGen(UserDetailsGenerator userDetailsGenerator) {
        this.userDetailsGen = userDetailsGenerator;
        return this;
    }

}
