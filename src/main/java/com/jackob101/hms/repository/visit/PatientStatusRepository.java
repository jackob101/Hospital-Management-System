package com.jackob101.hms.repository.visit;

import com.jackob101.hms.model.visit.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientStatusRepository extends JpaRepository<PatientStatus, Long> {
}
