package com.fronteers.services;

import com.fronteers.brightlife.model.AnxietyDisorderForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.AdhdFormEntity;
import com.fronteers.models.entity.forms.AnxietyDisorderFormEntity;
import com.fronteers.repositories.AnxietyDisorderRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnxietyDisorderService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final AnxietyDisorderRepository anxietyDisorderRepository;

  public Success submitAnxietyDisorder(AnxietyDisorderForm request) {
    PatientEntity patientEntity = patientUtils.checkIfPatientExists(
        request.getPatientId().toString());
    checkForAnxietyDisorderUniqueness(request.getPatientId().toString());
    AnxietyDisorderFormEntity anxietyDisorderFormEntity = new AnxietyDisorderFormEntity();
    anxietyDisorderFormEntity.setPatient(patientEntity);
    anxietyDisorderFormEntity.setPatientId(patientEntity.getPatientId());
    anxietyDisorderFormEntity.setNervousRate(request.getNervousRate());
    anxietyDisorderFormEntity.setControlOverWorry(request.getControlOverWorry());
    anxietyDisorderFormEntity.setExcessiveWorry(request.getExcessiveWorry());
    anxietyDisorderFormEntity.setRelaxTrouble(request.getRelaxTrouble());
    anxietyDisorderFormEntity.setRestlessness(request.getRestlessness());
    anxietyDisorderFormEntity.setAnnoyanceRate(request.getAnnoyanceRate());
    anxietyDisorderFormEntity.setFrightRate(request.getFrightRate());
    anxietyDisorderFormEntity.setLifeInfluenceSummaryFile(request.getLifeInfluenceSummary());
    patientEntity.setAnxietyDisorderForm(anxietyDisorderFormEntity);
    patientRepository.save(patientEntity);
    return new Success(true, "Form Submitted Successfully", "Anxiety Disorder Form Submitted");
  }

  public AnxietyDisorderForm getAnxietyDisorder(Long id){
    AnxietyDisorderFormEntity anxietyDisorderFormEntity = checkIfAnxietyDisorderFormExists(id);
    AnxietyDisorderForm anxietyDisorderDto = new AnxietyDisorderForm();
    anxietyDisorderDto.setId(id);
    anxietyDisorderDto.setPatientId(UUID.fromString(anxietyDisorderFormEntity.getPatientId()));
    anxietyDisorderDto.setNervousRate(anxietyDisorderFormEntity.getNervousRate());
    anxietyDisorderDto.setControlOverWorry(anxietyDisorderFormEntity.getControlOverWorry());
    anxietyDisorderDto.setExcessiveWorry(anxietyDisorderFormEntity.getExcessiveWorry());
    anxietyDisorderDto.setRelaxTrouble(anxietyDisorderFormEntity.getRelaxTrouble());
    anxietyDisorderDto.setRestlessness(anxietyDisorderFormEntity.getRestlessness());
    anxietyDisorderDto.setAnnoyanceRate(anxietyDisorderFormEntity.getAnnoyanceRate());
    anxietyDisorderDto.setFrightRate(anxietyDisorderFormEntity.getFrightRate());
    anxietyDisorderDto.setLifeInfluenceSummary(anxietyDisorderDto.getLifeInfluenceSummary());
    return anxietyDisorderDto;
  }

  private AnxietyDisorderFormEntity checkIfAnxietyDisorderFormExists(Long id) {
    return anxietyDisorderRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(String.format("Anxiety Disorder form with id %s does not exist", id)));
  }

  private void checkForAnxietyDisorderUniqueness(String patientId){
    AnxietyDisorderFormEntity anxietyDisorderFormEntity = anxietyDisorderRepository.findOneByPatientId(patientId);
    if (anxietyDisorderFormEntity != null) {
      throw new ConflictException(String.format("Anxiety Disorder form has already been filled for patient %s", patientId));
    }
  }
}
