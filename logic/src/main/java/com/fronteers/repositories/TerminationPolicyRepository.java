package com.fronteers.repositories;

import com.fronteers.models.entity.forms.TerminationPolicyFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerminationPolicyRepository extends JpaRepository<TerminationPolicyFormEntity, Long> {

  Optional<TerminationPolicyFormEntity> findOneById(Long id);
  TerminationPolicyFormEntity findOneByPatientId(String patientId);
}
