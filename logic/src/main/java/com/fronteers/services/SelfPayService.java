package com.fronteers.services;

import com.fronteers.brightlife.model.SelfPayForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.SelfPayFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.SelfPayRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SelfPayService {

  private final PatientUtils patientUtils;
  private final PatientRepository patientRepository;
  private final SelfPayRepository selfPayRepository;

  public Success submitSelfPay(SelfPayForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForSelfPayUniqueness(request.getPatientId().toString());
    SelfPayFormEntity entity = SelfPayFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .date(request.getDate() != null ?
            Timestamp.from(request.getDate().toInstant()) : null)
        .selfPayFile(request.getFile())
        .build();
    patient.setSelfPayForm(entity);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Self Pay Form Submitted");
  }

  public SelfPayForm getSelfPay(Long id) {
    return PatientDtoMapper.mapSelfPayEntityToDto(checkIfSelfPayExists(id));
  }

  private SelfPayFormEntity checkIfSelfPayExists(Long id) {
    return selfPayRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format("SelfPay form with id %s does not exist", id)));
  }

  private void checkForSelfPayUniqueness(String patientId) {
    SelfPayFormEntity entity = selfPayRepository.findOneByPatientId(patientId);
    if (entity != null) {
      throw new ConflictException(
          String.format("Selfpay form has already been filled for patient with id %s", patientId));
    }
  }
}
