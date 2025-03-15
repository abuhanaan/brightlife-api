package com.fronteers.repositories;

import com.fronteers.models.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, String> {

  PatientEntity findOneByPatientId(String patientId);

  PatientEntity findOneByEmail(String email);

}
