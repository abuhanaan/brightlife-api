package com.fronteers.repositories;

import com.fronteers.models.entity.PatientEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Long>,
    QuerydslPredicateExecutor<PatientEntity> {

  Optional<PatientEntity> findOneByPatientId(String patientId);

  PatientEntity findOneByEmail(String email);

  List<PatientEntity> findTop5ByOrderByCreatedAtDesc();

  boolean existsByPatientId(String patientId);

  boolean existsByEmail(String email);
}
