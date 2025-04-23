package com.fronteers.repositories;

import com.fronteers.models.entity.forms.IntakeFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntakeRepository extends JpaRepository<IntakeFormEntity, Long> {

  Optional<IntakeFormEntity> findOneById(Long id);

  IntakeFormEntity findOneByPatientId(String patientId);
}
