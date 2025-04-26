package com.fronteers.repositories;

import com.fronteers.models.entity.forms.MoodDisorderAssessmentFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodDisorderAssessmentRepository extends
    JpaRepository<MoodDisorderAssessmentFormEntity, Long> {

  Optional<MoodDisorderAssessmentFormEntity> findOneById(Long id);

  MoodDisorderAssessmentFormEntity findOneByPatientId(String patientId);
}
