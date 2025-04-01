package com.fronteers.services;

import com.fronteers.brightlife.model.ADHDForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.AdhdFormEntity;
import com.fronteers.repositories.AdhdRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdhdService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final AdhdRepository adhdRepository;

  public Success submitAdhd(ADHDForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    AdhdFormEntity adhdForm = new AdhdFormEntity();
    adhdForm.setPatientId(patient.getPatientId());
    adhdForm.setPatient(patient);
    adhdForm.setProjectCompletionProblem(request.getProjectCompletionProblem());
    adhdForm.setOrganizationRate(request.getOrganizationRate());
    adhdForm.setMemoryRate(request.getMemoryRate());
    adhdForm.setAttitudeToChallenge(request.getAttitudeToChallenge());
    adhdForm.setFidgetRateOnsit(request.getFidgetRateOnsit());
    adhdForm.setActiveToWork(request.getActiveToWork());
    adhdForm.setCarelessMistakes(request.getCarelessMistakes());
    adhdForm.setAttentionToBoringWork(request.getAttentionToBoringWork());
    adhdForm.setConcentrationRate(request.getConcentrationRate());
    adhdForm.setMisplaceRate(request.getMisplaceRate());
    adhdForm.setDistractionRate(request.getDistractionRate());
    adhdForm.setExcuseRate(request.getExcuseRate());
    adhdForm.setRestlessRate(request.getRestlessRate());
    adhdForm.setTroubleRelaxing(request.getTroubleRelaxing());
    adhdForm.setExcessiveTalks(request.getExcessiveTalks());
    adhdForm.setPeopleSentenceCompletion(request.getPeopleSentenceCompletion());
    adhdForm.setPatienceOnQueue(request.getPatienceOnQueue());
    adhdForm.setInterruptOthers(request.getInterruptOthers());
    patient.setAdhdForm(adhdForm);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Adhd Form Submitted");
  }

  public ADHDForm getAdhd(Long id) {
    AdhdFormEntity adhdFormEntity = checkIfAdhdFormExists(id);
    ADHDForm adhdFormDto = new ADHDForm();
    adhdFormDto.setId(adhdFormEntity.getId());
    adhdFormDto.setPatientId(UUID.fromString(adhdFormEntity.getPatientId()));
    adhdFormDto.setProjectCompletionProblem(adhdFormEntity.getProjectCompletionProblem());
    adhdFormDto.setOrganizationRate(adhdFormEntity.getOrganizationRate());
    adhdFormDto.setMemoryRate(adhdFormEntity.getMemoryRate());
    adhdFormDto.setAttitudeToChallenge(adhdFormEntity.getAttitudeToChallenge());
    adhdFormDto.setFidgetRateOnsit(adhdFormEntity.getFidgetRateOnsit());
    adhdFormDto.setActiveToWork(adhdFormEntity.getActiveToWork());
    adhdFormDto.setCarelessMistakes(adhdFormEntity.getCarelessMistakes());
    adhdFormDto.setAttentionToBoringWork(adhdFormEntity.getAttentionToBoringWork());
    adhdFormDto.setConcentrationRate(adhdFormEntity.getConcentrationRate());
    adhdFormDto.setMisplaceRate(adhdFormEntity.getMisplaceRate());
    adhdFormDto.setDistractionRate(adhdFormEntity.getDistractionRate());
    adhdFormDto.setExcuseRate(adhdFormEntity.getExcuseRate());
    adhdFormDto.setRestlessRate(adhdFormEntity.getRestlessRate());
    adhdFormDto.setTroubleRelaxing(adhdFormEntity.getTroubleRelaxing());
    adhdFormDto.setExcessiveTalks(adhdFormEntity.getExcessiveTalks());
    adhdFormDto.setPeopleSentenceCompletion(adhdFormEntity.getPeopleSentenceCompletion());
    adhdFormDto.setPatienceOnQueue(adhdFormEntity.getPatienceOnQueue());
    adhdFormDto.setInterruptOthers(adhdFormEntity.getInterruptOthers());
    return adhdFormDto;
  }

  private AdhdFormEntity checkIfAdhdFormExists(Long id) {
    AdhdFormEntity adhdFormEntity = adhdRepository.findOneById(id);
    if (adhdFormEntity == null){
      throw new NotFoundException(String.format("Adhd form with id %s does not exist", id));
    }
    return adhdFormEntity;
  }
}
