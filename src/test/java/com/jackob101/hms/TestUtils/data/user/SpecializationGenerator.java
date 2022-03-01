package com.jackob101.hms.TestUtils.data.user;

import com.jackob101.hms.TestUtils.data.DataGenerator;
import com.jackob101.hms.model.user.Specialization;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpecializationGenerator implements DataGenerator<Specialization> {

    private int nameLength = 10;

    @Override
    public List<Specialization> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public Specialization generateSingle() {
        return new Specialization(RandomStringUtils.randomAlphabetic(nameLength));
    }

    public SpecializationGenerator nameLength(int nameLength) {
        this.nameLength = nameLength;
        return this;
    }
}
