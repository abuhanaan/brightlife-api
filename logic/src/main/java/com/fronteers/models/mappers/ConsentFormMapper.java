package com.fronteers.models.mappers;

import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.fronteers.brightlife.model.ConsentForm;
import com.fronteers.models.entity.forms.ConsentFormEntity;

public class ConsentFormMapper {

  public static ConsentForm mapConsentEntityToDto(ConsentFormEntity consentForm) {
    return new ConsentForm()
        .id(consentForm.getId())
        .consentType(consentForm.getConsentType())
        .patientId(consentForm.getPatient() != null ? UUID.fromString(consentForm.getPatient()
            .getPatientId()) : null)
        .patientSignDate(consentForm.getPatientSignDate() != null ? consentForm.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC) : null)
        .file(consentForm.getFile());
  }

  public static List<ConsentForm> mapConsentEntitiesToDtos(List<ConsentFormEntity> consentForms) {
    if (consentForms == null || consentForms.isEmpty()){
      return Collections.emptyList();
    }
    return consentForms.stream().map(ConsentFormMapper::mapConsentEntityToDto).toList();
  }
}
