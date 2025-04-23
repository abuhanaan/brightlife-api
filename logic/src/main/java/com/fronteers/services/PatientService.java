package com.fronteers.services;

import com.fronteers.brightlife.model.IdGenerationRequest;
import com.fronteers.brightlife.model.IdGenerationResponse;
import com.fronteers.brightlife.model.PatientIdValidationResponse;
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
import com.fronteers.utils.PatientUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientEntityMapper patientEntityMapper;
  private final PatientRegistrationFormRepository patientRegistrationFormRepository;
  private final PatientUtils patientUtils;

  public Success submitRegistrationForm(PatientRegistrationForm request) {
    PatientEntity existingPatient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    String patientId = existingPatient.getPatientId();
    PatientRegistrationFormEntity patientRegistrationFormEntity = patientEntityMapper.mapRegFormToRegFormEntity(
        request, existingPatient);
    existingPatient.setPatientRegistrationForm(patientRegistrationFormEntity);
    patientRepository.save(existingPatient);
    //    TODO: send email
    return new Success(true, "Patient Registered Successfully",
        String.format("PatientId: %s", patientId));
  }

  private String generatePatientId() {
    return UUID.randomUUID().toString();
  }

  public PatientRegistrationForm getRegistrationDetails(String patientId) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = checkIfPatientExist(patientId);
    return PatientDtoMapper.mapPatientRegFormEntityToPatientRegFormDTO(
        patientRegistrationFormEntity);
  }

  public PatientRegistrationFormEntity checkIfPatientExist(String patientId) {
    return patientRegistrationFormRepository.findOneByPatientId(patientId).orElseThrow(() ->
        new NotFoundException(String.format("Patient with id %s does not exist", patientId)));
  }

  public PatientIdValidationResponse validatePatientId(String patientId) {
    PatientRegistrationFormEntity patientRegFormEntity = checkIfPatientExist(patientId);
    PatientIdValidationResponse response = new PatientIdValidationResponse();
    response.setPatientId(patientRegFormEntity.getPatientId());
    response.setFirstName(patientRegFormEntity.getFirstName());
    response.setLastName(patientRegFormEntity.getLastName());
    response.setMiddleName(patientRegFormEntity.getMiddleName());
    response.setGender(patientRegFormEntity.getGender());
    response.setDob(patientRegFormEntity.getDob() != null ?
        patientRegFormEntity.getDob().toLocalDate(): null);
    response.setPhone(patientRegFormEntity.getCellPhone());
    response.setEmail(patientRegFormEntity.getEmail());
    response.setAddress(patientRegFormEntity.getAddress() != null ?
        PatientDtoMapper.mapAddressEntityToAddressDto(patientRegFormEntity.getAddress()) : null);
    return response;
  }

  public IdGenerationResponse generateId(IdGenerationRequest request) {
    confirmPatientUniqueness(request.getEmail());
    PatientEntity newPatientEntity = new PatientEntity();
    String patientId = generatePatientId();
    newPatientEntity.setPatientId(patientId);
    newPatientEntity.setFullName(request.getFirstName() + " " + request.getLastName());
    newPatientEntity.setEmail(request.getEmail());
    patientRepository.save(newPatientEntity);
    IdGenerationResponse response = new IdGenerationResponse();
    response.setPatientId(patientId);
    response.setMessage("Patient Id generated successfully");
    response.setStatus(true);
    return response;
  }

  private void confirmPatientUniqueness(String email) {
    PatientEntity patientEntity = patientRepository.findOneByEmail(email);
    if (patientEntity != null){
      throw new ConflictException(String.format("Patient with email %s already exist", email));
    }
  }
}
