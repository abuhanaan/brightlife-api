package com.fronteers.repositories;

import com.fronteers.models.entity.forms.ControlledSubstanceFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlledSubstanceRepository extends JpaRepository<ControlledSubstanceFormEntity, Long> {

  Optional<ControlledSubstanceFormEntity> findOneById(Long id);
  ControlledSubstanceFormEntity findOneByPatientId(String patientId);
}
