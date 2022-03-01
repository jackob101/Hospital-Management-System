package com.jackob101.hms.TestUtils.data.allergy;

import com.jackob101.hms.TestUtils.data.DataGenerator;
import com.jackob101.hms.TestUtils.data.user.PatientGenerator;
import com.jackob101.hms.model.allergy.PatientAllergy;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PatientAllergyGenerator implements DataGenerator<PatientAllergy> {

    private AllergyTypeGenerator allergyTypeGenerator = new AllergyTypeGenerator();
    private AllergenGenerator allergenGenerator = new AllergenGenerator();
    private PatientGenerator patientGenerator = new PatientGenerator();
    private int reactionLength = 20;
    private int notesLength = 40;

    @Override
    public List<PatientAllergy> generate(int amount) {
        return IntStream.range(0, amount)
                .mapToObj(value -> generateSingle())
                .collect(Collectors.toList());
    }

    @Override
    public PatientAllergy generateSingle() {
        return new PatientAllergy(allergenGenerator.generateSingle(),
                allergyTypeGenerator.generateSingle(),
                patientGenerator.generateSingle(),
                RandomStringUtils.randomAlphabetic(reactionLength),
                RandomStringUtils.randomAlphabetic(notesLength));
    }

    public PatientAllergyGenerator allergyTypeGenerator(AllergyTypeGenerator allergyTypeGenerator) {
        this.allergyTypeGenerator = allergyTypeGenerator;
        return this;
    }

    public PatientAllergyGenerator allergenGenerator(AllergenGenerator allergenGenerator) {
        this.allergenGenerator = allergenGenerator;
        return this;
    }

    public PatientAllergyGenerator patientGenerator(PatientGenerator patientGenerator) {
        this.patientGenerator = patientGenerator;
        return this;
    }

    public PatientAllergyGenerator reactionLength(int reactionLength) {
        this.reactionLength = reactionLength;
        return this;
    }

    public PatientAllergyGenerator notesLength(int notesLength) {
        this.notesLength = notesLength;
        return this;
    }
}
