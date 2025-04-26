package com.fronteers.repositories;

import com.fronteers.models.entity.forms.DepressionAssessmentFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepressionAssessmentRepository extends
    JpaRepository<DepressionAssessmentFormEntity, Long> {

  Optional<DepressionAssessmentFormEntity> findOneById(Long id);

  DepressionAssessmentFormEntity findOneByPatientId(String patientId);
}
