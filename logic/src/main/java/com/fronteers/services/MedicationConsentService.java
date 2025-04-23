package com.fronteers.services;

import com.fronteers.brightlife.model.MedicationConsentForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.MedicationConsentFormEntity;
import com.fronteers.repositories.MedicationConsentRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MedicationConsentService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final MedicationConsentRepository medicationConsentRepository;

  public MedicationConsentForm getMedConsent(Long id) {
    MedicationConsentFormEntity medConsentEntity = checkIfMedConsentFormExists(id);
    MedicationConsentForm dto = new MedicationConsentForm();
    dto.setId(medConsentEntity.getId());
    dto.setPatientId(UUID.fromString(medConsentEntity.getPatientId()));
    dto.setIsMinor(medConsentEntity.getIsMinor());
    dto.setPatientSignDate(medConsentEntity.getPatientSignDate() != null ?
        medConsentEntity.getPatientSignDate().toLocalDate() : null);
    dto.setGuardianName(medConsentEntity.getGuardianName());
    dto.setPatientGuardianRelationship(medConsentEntity.getPatientGuardianRelationship());
    dto.setGuardianSignDate(medConsentEntity.getGuardianSignDate() != null ?
        medConsentEntity.getGuardianSignDate().toLocalDate() : null);
    dto.setFile(medConsentEntity.getMedicationConsentFile());
    return dto;
  }

  public Success medConsent(MedicationConsentForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForAdhDUniqueness(request.getPatientId().toString());
    MedicationConsentFormEntity medConsentEntity = new MedicationConsentFormEntity();
    medConsentEntity.setPatient(patient);
    medConsentEntity.setPatientId(patient.getPatientId());
    medConsentEntity.setIsMinor(request.getIsMinor());
    medConsentEntity.setPatientSignDate(Date.valueOf(request.getPatientSignDate()));
    medConsentEntity.setGuardianName(request.getGuardianName());
    medConsentEntity.setPatientGuardianRelationship(request.getPatientGuardianRelationship());
    medConsentEntity.setGuardianSignDate(Date.valueOf(request.getGuardianSignDate()));
    medConsentEntity.setMedicationConsentFile(request.getFile());
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Medication Consent Form Submitted");
  }

  private void checkForAdhDUniqueness(String patientId) {
    MedicationConsentFormEntity medConsentEntity = medicationConsentRepository.findOneByPatientId(
        patientId);
    if (medConsentEntity != null){
      throw new ConflictException(String.format(
          "Medication Consent form has already been filled for patient %s", patientId));
    }
  }

  private MedicationConsentFormEntity checkIfMedConsentFormExists(Long id) {
    return medicationConsentRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format("Medication Consent Form with id %s does not exist", id)));
  }
}
