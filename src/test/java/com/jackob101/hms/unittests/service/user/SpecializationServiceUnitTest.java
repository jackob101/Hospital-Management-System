package com.jackob101.hms.unittests.service.user;

import com.jackob101.hms.model.user.Specialization;
import com.jackob101.hms.repository.user.SpecializationRepository;
import com.jackob101.hms.service.user.definition.ISpecializationService;
import com.jackob101.hms.service.user.implementation.SpecializationService;
import com.jackob101.hms.unittests.service.BaseServiceUnitTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.JpaRepository;

@ExtendWith(MockitoExtension.class)
class SpecializationServiceUnitTest extends BaseServiceUnitTest<Specialization, ISpecializationService> {

    @Mock
    SpecializationRepository repository;

    @Override
    protected ISpecializationService configureService() {
        return new SpecializationService(repository, getValidationUtils());
    }

    @Override
    protected Specialization configureEntity() {

        Specialization specialization = new Specialization();

        specialization.setName("Doctor");
        specialization.setId(1L);

        return specialization;
    }

    @Override
    protected JpaRepository<Specialization, Long> configureRepository() {
        return repository;
    }

}