package com.fronteers.services;

import com.fronteers.brightlife.model.PatientInformationConsentAndFinancialPolicyForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.PatientInformationConsentAndFinancialPolicyFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.PatientInfoConsentAndFinPolicyRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PatientInfoConsentAndFinPolicyService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final PatientInfoConsentAndFinPolicyRepository patientInfoConsentAndFinPolicyRepository;

  public Success submitPatientInfoAndConsentFinPolicy(
      PatientInformationConsentAndFinancialPolicyForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForPatientInfoAndFinancialPolicyUniqueness(patient.getPatientId());
    PatientInformationConsentAndFinancialPolicyFormEntity form =
        PatientInformationConsentAndFinancialPolicyFormEntity.builder()
            .patient(patient)
            .patientId(patient.getPatientId())
            .date(request.getDate() != null ? Timestamp.from(request.getDate().toInstant()) : null)
            .patientInfoFinFile(request.getFile())
            .build();
    patient.setPatientInformationConsentAndFinancialPolicyForm(form);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully",
        "Patient Information Consent And Financial Policy Form Submitted");
  }

  public PatientInformationConsentAndFinancialPolicyForm getNoticeOfPrivacy(Long id) {
    return PatientDtoMapper.mapPatientInfoConsentAndFinPolicyFormEntityToDto(
        checkIfPatientInfoConsentExists(id));
  }

  private PatientInformationConsentAndFinancialPolicyFormEntity checkIfPatientInfoConsentExists(
      Long id) {
    return patientInfoConsentAndFinPolicyRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(String.format(
            "Patient Information Consent And Financial Policy form with id %s does not exist",
            id)));
  }

  private void checkForPatientInfoAndFinancialPolicyUniqueness(String patientId) {
    PatientInformationConsentAndFinancialPolicyFormEntity form =
        patientInfoConsentAndFinPolicyRepository.findOneByPatientId(patientId);
    if (form != null) {
      throw new ConflictException(String.format(
          "Patient Information Consent And Financial Policy form has already been filled for patient %s",
          patientId));
    }
  }
}
