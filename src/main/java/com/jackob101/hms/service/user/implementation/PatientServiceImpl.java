package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Patient;
import com.jackob101.hms.repository.user.PatientRepository;
import com.jackob101.hms.service.user.definition.PatientService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient create(Patient entity) {
        if (entity == null)
            throw new RuntimeException("Patient cannot be null");

        if(entity.getUserDetails() == null)
            throw new RuntimeException("Patient user details cannot be null");

        return patientRepository.save(entity);
    }

    @Override
    public Patient update(Patient patient) {

        if(patient == null)
            throw new RuntimeException("Patient cannot be null");

        if(patient.getUserDetails() == null)
            throw new RuntimeException("Patient user details cannot be null");

        if(patient.getId() == null)
            throw new RuntimeException("Patient id cannot be null");

        if(patient.getId() < 0)
            throw new RuntimeException("Patient id cannot be less than 0");

        Patient updated = create(patient);

        if(updated == null)
            throw new RuntimeException("Patient update failed");

        return updated;
    }

    @Override
    public boolean delete(Patient patient) {
        if(patient == null)
            throw new RuntimeException("Patient cannot be null");

        if(patient.getId() == null)
            throw new RuntimeException("Patient id cannot be null");

        boolean isFound = patientRepository.existsById(patient.getId());

        if(!isFound)
            throw new RuntimeException("Patient not found");

        patientRepository.delete(patient);

        return !patientRepository.existsById(patient.getId());
    }

    @Override
    public Patient find(Long id) {

        if (id == null)
            throw new RuntimeException("Id cannot be null");

        if(id < 0)
            throw new RuntimeException("Id cannot be less than 0");

        Optional<Patient> optionalPatient = patientRepository.findById(id);

        return optionalPatient.orElseThrow(() -> new RuntimeException("Patient not found"));
    }
}
