package com.jackob101.hms.TestUtils.data.visit;


import com.jackob101.hms.TestUtils.data.DataGenerator;
import com.jackob101.hms.model.visit.PatientStatus;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PatientStatusGenerator implements DataGenerator<PatientStatus> {
    @Override
    public List<PatientStatus> generate(int amount) {
        return IntStream.range(0, amount).mapToObj(value -> generateSingle()).collect(Collectors.toList());
    }

    @Override
    public PatientStatus generateSingle() {
        PatientStatus patientStatus = new PatientStatus();
        patientStatus.setName(RandomStringUtils.randomAlphabetic(10));
        return patientStatus;
    }
}
