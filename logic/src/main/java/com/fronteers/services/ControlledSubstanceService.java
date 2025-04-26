package com.fronteers.services;

import com.fronteers.brightlife.model.ControlledSubstanceForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.ControlledSubstanceFormEntity;
import com.fronteers.repositories.ControlledSubstanceRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ControlledSubstanceService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final ControlledSubstanceRepository controlledSubstanceRepository;

  public Success submitControlled(ControlledSubstanceForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForControlledSubstanceUniqueness(request.getPatientId().toString());
    ControlledSubstanceFormEntity controlledSubstanceForm = new ControlledSubstanceFormEntity();
    controlledSubstanceForm.setPatient(patient);
    controlledSubstanceForm.setPatientId(patient.getPatientId());
    controlledSubstanceForm.setIsMinor(request.getIsMinor());
    controlledSubstanceForm.setPatientSignDate(
        request.getPatientSignDate() != null ? Timestamp.from(
            request.getPatientSignDate().toInstant()) : null);
    controlledSubstanceForm.setGuardianName(request.getGuardianName());
    controlledSubstanceForm.setPatientGuardianRelationship(
        request.getPatientGuardianRelationship());
    controlledSubstanceForm.setGuardianSignDate(
        request.getGuardianSignDate() != null ? Timestamp.from(
            request.getGuardianSignDate().toInstant()) : null);
    controlledSubstanceForm.setControlledSubstanceFile(request.getFile());
    patient.setControlledSubstanceForm(controlledSubstanceForm);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Controlled Substance Form Submitted");
  }

  public ControlledSubstanceForm getControlledSubstance(Long id) {
    ControlledSubstanceFormEntity controlledSubstanceFormEntity = checkIfControlledSubstanceFormExists(
        id);
    ControlledSubstanceForm controlledSubstanceDto = new ControlledSubstanceForm();
    controlledSubstanceDto.setId(id);
    controlledSubstanceDto.setPatientId(
        UUID.fromString(controlledSubstanceFormEntity.getPatientId()));
    controlledSubstanceDto.setIsMinor(controlledSubstanceFormEntity.getIsMinor());
    controlledSubstanceDto.setPatientSignDate(
        controlledSubstanceFormEntity.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC));
    controlledSubstanceDto.setGuardianName(controlledSubstanceFormEntity.getGuardianName());
    controlledSubstanceDto.setPatientGuardianRelationship(
        controlledSubstanceFormEntity.getPatientGuardianRelationship());
    controlledSubstanceDto.setGuardianSignDate(
        controlledSubstanceFormEntity.getGuardianSignDate().toInstant().atOffset(ZoneOffset.UTC));
    controlledSubstanceDto.setFile(controlledSubstanceFormEntity.getControlledSubstanceFile());
    return controlledSubstanceDto;
  }

  private ControlledSubstanceFormEntity checkIfControlledSubstanceFormExists(Long id) {
    return controlledSubstanceRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(
            String.format("Controlled Substance form with id %s does not exist", id)));
  }

  private void checkForControlledSubstanceUniqueness(String patientId) {
    ControlledSubstanceFormEntity controlledSubstanceFormEntity = controlledSubstanceRepository.findOneByPatientId(
        patientId);
    if (controlledSubstanceFormEntity != null) {
      throw new ConflictException(
          String.format("Controlled Substance form has already been filled for patient %s",
              patientId));
    }
  }
}
