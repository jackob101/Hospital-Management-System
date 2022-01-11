package com.jackob101.hms.repository.allergy;

import com.jackob101.hms.model.allergy.Allergen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, Long> {
}
