package com.fronteers.repositories;

import com.fronteers.models.entity.forms.InitialEvaluationFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitialEvaluationRepository extends JpaRepository<InitialEvaluationFormEntity, Long> {

  Optional<InitialEvaluationFormEntity> findOneById(Long id);

  InitialEvaluationFormEntity findOneByPatientId(String patientId);
}
