package com.fronteers.repositories;

import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRegistrationFormRepository extends
    JpaRepository<PatientRegistrationFormEntity, Long> {

  Optional<PatientRegistrationFormEntity> findOneByPatientId(String patientId);
  PatientRegistrationFormEntity findByPatientId(String patientId);
  PatientRegistrationFormEntity findOneByEmail(String email);
}
