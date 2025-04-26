package com.fronteers.services;

import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TerminationPolicyForm;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.TerminationPolicyFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.TerminationPolicyRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TerminationPolicyService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final TerminationPolicyRepository terminationPolicyRepository;

  public Success submit(TerminationPolicyForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForTpUniqueness(request.getPatientId().toString());
    TerminationPolicyFormEntity entity = TerminationPolicyFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .witnessName(request.getWitnessName())
        .witnessSignDate(request.getWitnessSignDate() != null ?
            Timestamp.from(request.getWitnessSignDate().toInstant()) : null)
        .patientSignDate(request.getPatientSignDate() != null ?
            Timestamp.from(request.getPatientSignDate().toInstant()) : null)
        .build();
    patient.setTerminationPolicyForm(entity);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "TerminationPolicy Form Submitted");
  }

  public TerminationPolicyForm getTp(Long id) {
    return PatientDtoMapper.mapTerminationPolicyEntityToDto(checkIfTpExists(id));
  }

  private void checkForTpUniqueness(String patientId) {
    TerminationPolicyFormEntity entity = terminationPolicyRepository.findOneByPatientId(patientId);
    if (entity != null) {
      throw new ConflictException(
          String.format("Termination Policy form has already been filled for patient %s",
              patientId));
    }
  }

  private TerminationPolicyFormEntity checkIfTpExists(Long id) {
    return terminationPolicyRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(
            String.format("Termination Policy form with id %s does not exist", id)));
  }

}
