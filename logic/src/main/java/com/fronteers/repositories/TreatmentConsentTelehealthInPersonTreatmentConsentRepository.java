package com.fronteers.repositories;

import com.fronteers.models.entity.forms.TreatmentConsentTelehealthInPersonTreatmentConsentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentConsentTelehealthInPersonTreatmentConsentRepository extends JpaRepository<TreatmentConsentTelehealthInPersonTreatmentConsentEntity, Long> {

  TreatmentConsentTelehealthInPersonTreatmentConsentEntity findOneByPatientId(String patientId);
  Optional<TreatmentConsentTelehealthInPersonTreatmentConsentEntity> findOneById(Long id);
}
