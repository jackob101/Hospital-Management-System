package com.jackob101.hms.service.user.implementation;

import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationService implements ISpecializationService {

    @Override
    public Specialization create(Specialization entity) {
        return null;
    }

    @Override
    public Specialization update(Specialization entity) {
        return null;
    }

    @Override
    public boolean delete(Long entity) {
        return false;
    }

    @Override
    public Specialization find(Long aLong) {
        return null;
    }

    @Override
    public List<Specialization> findAll() {
        return null;
    }
}
