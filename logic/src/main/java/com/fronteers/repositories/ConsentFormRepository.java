package com.fronteers.repositories;

import com.fronteers.brightlife.model.ConsentTypeEnum;
import com.fronteers.models.entity.forms.ConsentFormEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsentFormRepository extends JpaRepository<ConsentFormEntity, Long> {

  Optional<ConsentFormEntity> findOneByPatientIdAndConsentType(String patientId, ConsentTypeEnum consentType);


}
