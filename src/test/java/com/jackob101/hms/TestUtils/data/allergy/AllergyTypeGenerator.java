package com.jackob101.hms.TestUtils.data.allergy;

import com.jackob101.hms.TestUtils.data.DataGenerator;
import com.jackob101.hms.model.allergy.AllergyType;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AllergyTypeGenerator implements DataGenerator<AllergyType> {

    private int nameLength = 5;

    @Override
    public List<AllergyType> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public AllergyType generateSingle() {
        return new AllergyType(RandomStringUtils.randomAlphabetic(nameLength));
    }

    public AllergyTypeGenerator nameLength(int nameLength) {
        this.nameLength = nameLength;
        return this;
    }
}
