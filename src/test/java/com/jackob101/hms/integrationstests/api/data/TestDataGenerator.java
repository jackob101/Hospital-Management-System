package com.jackob101.hms.integrationstests.api.data;

import com.jackob101.hms.dto.user.EmployeeForm;
import com.jackob101.hms.dto.user.PatientDTO;
import com.jackob101.hms.dto.user.UserDetailsDTO;
import com.jackob101.hms.model.user.Employee;
import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.model.user.UserDetails;
import com.jackob101.hms.model.user.enums.Gender;
import com.jackob101.hms.model.user.enums.MaritalStatus;
import com.jackob101.hms.repository.user.EmployeeRepository;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.repository.user.UserDetailsRepository;
import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestDataGenerator {

    public static List<UserDetails> generateUserDetails() {

        return new Random()
                .ints()
                .limit(10)
                .mapToObj(value -> UserDetails.builder()
                        .firstName(RandomStringUtils.randomAlphabetic(10))
                        .secondName(RandomStringUtils.randomAlphabetic(10))
                        .lastName(RandomStringUtils.randomAlphabetic(10))
                        .userCredentialsId(RandomStringUtils.randomAlphabetic(10))
                        .employee(null)
                        .patient(null)
                        .dateOfBirth(LocalDate.now())
                        .pesel(RandomStringUtils.random(10, false, true))
                        .phoneNumber(RandomStringUtils.random(10, false, true))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<UserDetails> generateAndSaveUserDetails(UserDetailsRepository userDetailsRepository) {
        return userDetailsRepository.saveAll(generateUserDetails());
    }


    public static List<Patient> generatePatent(List<UserDetails> userDetails) {

        return userDetails.stream()
                .map(entry -> Patient.builder()
                        .maritalStatus(MaritalStatus.SINGLE)
                        .language(RandomStringUtils.randomAlphabetic(10))
                        .nationality(RandomStringUtils.randomAlphabetic(10))
                        .religion(RandomStringUtils.randomAlphabetic(10))
                        .userDetails(entry)
                        .build())
                .collect(Collectors.toList());

    }

    public static List<Patient> generateAndSavePatient(PatientRepository patientRepository, List<UserDetails> userDetails) {

        return patientRepository.saveAll(generatePatent(userDetails));
    }

    public static List<Employee> generateEmployee(List<UserDetails> userDetails) {

        return userDetails.stream()
                .map(entry -> Employee.builder()
                        .userDetails(entry)
                        .build())
                .collect(Collectors.toList());

    }

    public static List<Employee> generateAndSaveEmployee(EmployeeRepository employeeRepository, List<UserDetails> userDetails) {

        return employeeRepository.saveAll(generateEmployee(userDetails));
    }

    public static UserDetailsDTO generateUserDetailsForm() {

        return new UserDetailsDTO(9999L,
                "123123123",
                "123123123",
                "Tom",
                "Mot",
                "John",
                LocalDate.now(),
                "123123123");
    }

    public static PatientDTO generatePatientForm() {

        return PatientDTO.builder()
                .language("English")
                .maritalStatus(MaritalStatus.SINGLE)
                .religion(RandomStringUtils.randomAlphabetic(10))
                .gender(Gender.MALE)
                .build();
    }

    public static PatientDTO generatePatientForm(Long userDetailsId) {

        PatientDTO patientDTO = generatePatientForm();
        patientDTO.setUserDetailsId(userDetailsId);
        return patientDTO;
    }

    public static EmployeeForm generateEmployeeForm() {

        return EmployeeForm.builder()
                .id(1L)
                .build();
    }

    public static EmployeeForm generateEmployeeForm(Long userDetailsId) {
        EmployeeForm employeeForm = generateEmployeeForm();
        employeeForm.setUserDetailsId(userDetailsId);
        return employeeForm;
    }

    public static List<Specialization> generateSpecializations() {

        return IntStream.range(0, 10)
                .mapToObj(value -> new Specialization(RandomStringUtils.randomAlphabetic(10)))
                .collect(Collectors.toList());

    }

    public static List<Specialization> generateAndSaveSpecializations(SpecializationRepository repository) {
        return repository.saveAll(generateSpecializations());
    }


}
