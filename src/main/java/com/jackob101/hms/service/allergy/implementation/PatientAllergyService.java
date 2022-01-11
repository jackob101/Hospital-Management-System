package com.jackob101.hms.service.allergy.implementation;

import com.jackob101.hms.dto.allergy.PatientAllergyForm;
import com.jackob101.hms.exceptions.HmsException;
import com.jackob101.hms.model.allergy.Allergen;
import com.jackob101.hms.model.allergy.AllergyType;
import com.jackob101.hms.model.allergy.PatientAllergy;
import com.jackob101.hms.repository.allergy.PatientAllergyRepository;
import com.jackob101.hms.service.allergy.definition.IAllergenService;
import com.jackob101.hms.service.allergy.definition.IAllergyTypeService;
import com.jackob101.hms.service.allergy.definition.IPatientAllergyService;
import com.jackob101.hms.service.base.BaseService;
import com.jackob101.hms.validation.groups.OnCreate;
import com.jackob101.hms.validation.groups.OnUpdate;
import org.modelmapper.ModelMapper;

import javax.validation.Validator;
import java.util.List;
import java.util.Optional;

public class PatientAllergyService extends BaseService<PatientAllergy> implements IPatientAllergyService {

    private final PatientAllergyRepository patientAllergyRepository;
    private final IAllergenService allergenService;
    private final IAllergyTypeService allergyTypeService;

    public PatientAllergyService(Validator validator, PatientAllergyRepository patientAllergyRepository, IAllergenService allergenService, IAllergyTypeService allergyTypeService) {
        super(validator, "Patient Allergy");
        this.patientAllergyRepository = patientAllergyRepository;
        this.allergenService = allergenService;
        this.allergyTypeService = allergyTypeService;
    }

    @Override
    public PatientAllergy create(PatientAllergy entity) {
        validate(entity, OnCreate.class);

        checkIdAvailability(entity.getId(), patientAllergyRepository);

        return patientAllergyRepository.save(entity);
    }

    @Override
    public PatientAllergy createFromForm(PatientAllergyForm patientAllergyForm) {
        PatientAllergy patientAllergy = convertToModel(patientAllergyForm);
        return create(patientAllergy);
    }

    @Override
    public PatientAllergy update(PatientAllergy entity) {
        validate(entity, OnUpdate.class);

        checkIdForUpdate(entity.getId(), patientAllergyRepository);

        return patientAllergyRepository.save(entity);
    }

    @Override
    public PatientAllergy updateFromForm(PatientAllergyForm patientAllergyForm) {

        PatientAllergy patientAllergy = convertToModel(patientAllergyForm);

        return update(patientAllergy);
    }

    @Override
    public boolean delete(Long aLong) {
        checkIdForDeletion(aLong, patientAllergyRepository);

        patientAllergyRepository.deleteById(aLong);

        return !patientAllergyRepository.existsById(aLong);
    }

    @Override
    public PatientAllergy find(Long aLong) {
        checkIdForSearch(aLong);

        Optional<PatientAllergy> optionalPatientAllergy = patientAllergyRepository.findById(aLong);

        return optionalPatientAllergy.orElseThrow(() -> HmsException.params(aLong).code("Patient Allergy with ID %s was not found"));
    }

    @Override
    public List<PatientAllergy> findAll() {
        return patientAllergyRepository.findAll();
    }

    private PatientAllergy convertToModel(PatientAllergyForm form) {

        ModelMapper modelMapper = new ModelMapper();
        PatientAllergy mapped = modelMapper.map(form, PatientAllergy.class);

        AllergyType allergyType = allergyTypeService.find(form.getAllergyTypeId());
        Allergen allergen = allergenService.find(form.getAllergenId());

        mapped.setAllergen(allergen);
        mapped.setAllergyType(allergyType);

        return mapped;
    }

}
