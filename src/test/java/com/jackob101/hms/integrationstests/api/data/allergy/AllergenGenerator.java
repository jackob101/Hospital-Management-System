package com.jackob101.hms.integrationstests.api.data.allergy;

import com.jackob101.hms.integrationstests.api.data.DataGenerator;
import com.jackob101.hms.integrationstests.api.data.user.PatientGenerator;
import com.jackob101.hms.model.allergy.Allergen;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AllergenGenerator implements DataGenerator<Allergen> {

    private int nameLength = 5;
    private PatientGenerator patientGenerator;

    @Override
    public List<Allergen> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public Allergen generateSingle() {
        return new Allergen(RandomStringUtils.randomAlphabetic(nameLength));
    }

    public AllergenGenerator nameLength(int length) {
        this.nameLength = length;
        return this;
    }
}
