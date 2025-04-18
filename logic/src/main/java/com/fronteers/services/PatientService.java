package com.fronteers.services;

import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.models.mappers.PatientEntityMapper;
import com.fronteers.repositories.PatientRegistrationFormRepository;
import com.fronteers.repositories.PatientRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientEntityMapper patientEntityMapper;
  private final PatientRegistrationFormRepository patientRegistrationFormRepository;

  public Success submitRegistrationForm(PatientRegistrationForm request) {
    PatientEntity existingPatient = patientRepository.findOneByEmail(
        request.getPersonalInfo().getEmail());
    if (existingPatient != null) {
      throw new ConflictException(String.format("Patient with email: %s already exist",
          request.getPersonalInfo().getEmail()));
    }
    String patientId = generateUUID();
    PatientEntity newPatient = PatientEntity.builder()
        .patientId(patientId)
        .fullName(String.format("%s %s", request.getPersonalInfo().getLastName(),
            request.getPersonalInfo().getFirstName()))
        .email(request.getPersonalInfo().getEmail())
        .build();
    PatientRegistrationFormEntity patientRegistrationFormEntity = patientEntityMapper.mapRegFormToRegFormEntity(
        request,
        newPatient);
    newPatient.setPatientRegistrationForm(patientRegistrationFormEntity);
    patientRepository.save(newPatient);
    //    TODO: send email
    return new Success(true, "Patient Registered Successfully",
        String.format("PatientId: %s", patientId));
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }

  public PatientRegistrationForm getRegistrationDetails(String patientId) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = checkIfPatientExist(patientId);
    return PatientDtoMapper.mapPatientRegFormEntityToPatientRegFormDTO(
        patientRegistrationFormEntity);
  }

  public PatientRegistrationFormEntity checkIfPatientExist(String patientId) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = patientRegistrationFormRepository.findOneByPatientId(
        patientId);
    if (patientRegistrationFormEntity == null) {
      throw new NotFoundException(String.format("Patient with id %s does not exist", patientId));
    }
    return patientRegistrationFormEntity;
  }
}
