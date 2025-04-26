package com.fronteers.services;

import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TreatmentConsentTelehealthInPersonTreatmentConsent;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.TreatmentConsentTelehealthInPersonTreatmentConsentEntity;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.TreatmentConsentTelehealthInPersonTreatmentConsentRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TreatmentConsentTelehealthInPersonTreatmentConsentService {

  private final PatientUtils patientUtils;
  private final PatientRepository patientRepository;
  private final TreatmentConsentTelehealthInPersonTreatmentConsentRepository tctInPersonTcRepository;

  public Success submit(TreatmentConsentTelehealthInPersonTreatmentConsent request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForEntityUniqueness(request.getPatientId().toString());
    TreatmentConsentTelehealthInPersonTreatmentConsentEntity entity =
        TreatmentConsentTelehealthInPersonTreatmentConsentEntity.builder()
            .patient(patient)
            .patientId(patient.getPatientId())
            .isMinor(request.getIsMinor())
            .guardianName(request.getGuardianName())
            .guardianSignDate(Date.valueOf(request.getGuardianSignDate()))
            .patientSignDate(Date.valueOf(request.getPatientSignDate()))
            .build();
    patient.setTreatmentConsentTelehealth(entity);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully",
        "TreatmentConsentTelehealthInPersonTreatmentConsent Form Submitted");
  }

  public TreatmentConsentTelehealthInPersonTreatmentConsent fetch(Long id) {
    TreatmentConsentTelehealthInPersonTreatmentConsentEntity entity = checkIfEntityExists(id);
    TreatmentConsentTelehealthInPersonTreatmentConsent dto = new TreatmentConsentTelehealthInPersonTreatmentConsent();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setGuardianName(entity.getGuardianName());
    dto.setGuardianSignDate(entity.getGuardianSignDate().toLocalDate());
    dto.setPatientSignDate(entity.getPatientSignDate().toLocalDate());
    dto.setFile(entity.getTreatmentConsentTelehealthInPersonFile());
    return dto;
  }

  private TreatmentConsentTelehealthInPersonTreatmentConsentEntity checkIfEntityExists(Long id) {
    return tctInPersonTcRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format(
            "TreatmentConsentTelehealthInPersonTreatmentConsent form with id %s does not exist",
            id)));
  }

  private void checkForEntityUniqueness(String patientId) {
    TreatmentConsentTelehealthInPersonTreatmentConsentEntity entity = tctInPersonTcRepository.findOneByPatientId(
        patientId);
    if (entity != null) {
      throw new ConflictException(String.format(
          "TreatmentConsentTelehealthInPersonTreatmentConsent form has already been filled for patient %s",
          patientId));
    }
  }
}
