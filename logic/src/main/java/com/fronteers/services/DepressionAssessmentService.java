package com.fronteers.services;

import com.fronteers.brightlife.model.DepressionAssessmentForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.DepressionAssessmentFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.DepressionAssessmentRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DepressionAssessmentService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final DepressionAssessmentRepository depressionAssessmentRepository;

  public Success submitDepressionAssessment(DepressionAssessmentForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForDepressionAssessmentUniqueness(request.getPatientId().toString());
    DepressionAssessmentFormEntity depAssessmentFormEntity = new DepressionAssessmentFormEntity();
    depAssessmentFormEntity.setPatient(patient);
    depAssessmentFormEntity.setPatientId(patient.getPatientId());
    depAssessmentFormEntity.setPleasureInterest(request.getPleasureInterest());
    depAssessmentFormEntity.setDepressionRate(request.getDepressionRate());
    depAssessmentFormEntity.setSleepRate(request.getSleepRate());
    depAssessmentFormEntity.setSleepRate(request.getSleepRate());
    depAssessmentFormEntity.setFailureRate(request.getFailureRate());
    depAssessmentFormEntity.setAppetiteRate(request.getAppetiteRate());
    depAssessmentFormEntity.setFailureRate(request.getFailureRate());
    depAssessmentFormEntity.setConcentrationRate(request.getConcentrationRate());
    depAssessmentFormEntity.setRestlessnessRate(request.getRestlessnessRate());
    depAssessmentFormEntity.setSuicideThought(request.getSuicideThought());
    patient.setDepressionAssessmentForm(depAssessmentFormEntity);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Depression Assessment Form Submitted");
  }

  public DepressionAssessmentForm getDepAssessment(Long id) {
    return PatientDtoMapper.mapDepressionAssessmentEntityToDto(checkIfDepressionAssessmentFormExists(id));
  }

  private DepressionAssessmentFormEntity checkIfDepressionAssessmentFormExists(Long id) {
    return depressionAssessmentRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(
            String.format("Depression Assessment form with id %s does not exist", id)));
  }

  private void checkForDepressionAssessmentUniqueness(String patientId) {
    DepressionAssessmentFormEntity depAssessmentEntity = depressionAssessmentRepository.findOneByPatientId(
        patientId);
    if (depAssessmentEntity != null) {
      throw new ConflictException(
          String.format("Depression Assessment form has already been filled for patient %s",
              patientId));
    }

  }
}
