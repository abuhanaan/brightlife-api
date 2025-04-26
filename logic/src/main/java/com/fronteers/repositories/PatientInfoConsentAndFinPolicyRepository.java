package com.fronteers.repositories;

import com.fronteers.models.entity.forms.PatientInformationConsentAndFinancialPolicyFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientInfoConsentAndFinPolicyRepository extends
    JpaRepository<PatientInformationConsentAndFinancialPolicyFormEntity, Long> {

  Optional<PatientInformationConsentAndFinancialPolicyFormEntity> findOneById(Long id);

  PatientInformationConsentAndFinancialPolicyFormEntity findOneByPatientId(String patientId);
}
