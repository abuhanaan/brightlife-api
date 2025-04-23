package com.fronteers.repositories;

import com.fronteers.models.entity.forms.MedicationConsentFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationConsentRepository extends JpaRepository<MedicationConsentFormEntity, Long> {

  Optional<MedicationConsentFormEntity> findOneById(Long id);

  MedicationConsentFormEntity findOneByPatientId(String patientId);
}
