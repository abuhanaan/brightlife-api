package com.fronteers.repositories;

import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRegistrationFormRepository extends
    JpaRepository<PatientRegistrationFormEntity, Long> {

  Optional<PatientRegistrationFormEntity> findOneByPatientId(String patientId);

  PatientRegistrationFormEntity findOneByEmail(String email);
}
