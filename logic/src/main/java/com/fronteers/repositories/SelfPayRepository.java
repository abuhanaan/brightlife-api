package com.fronteers.repositories;

import com.fronteers.models.entity.forms.SelfPayFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfPayRepository extends JpaRepository<SelfPayFormEntity, Long> {

  Optional<SelfPayFormEntity> findOneById(Long id);
  SelfPayFormEntity findOneByPatientId(String patientId);
}
