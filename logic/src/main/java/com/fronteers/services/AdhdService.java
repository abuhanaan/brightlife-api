package com.fronteers.services;

import com.fronteers.brightlife.model.ADHDForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.AdhdFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
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
    checkForAdhDUniqueness(request.getPatientId().toString());
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
    return PatientDtoMapper.mapAdhdEntityToDto(checkIfAdhdFormExists(id));
  }

  private AdhdFormEntity checkIfAdhdFormExists(Long id) {
    return adhdRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(String.format("Adhd form with id %s does not exist", id)));
  }

  private void checkForAdhDUniqueness(String patientId) {
    AdhdFormEntity adhdFormEntity = adhdRepository.findOneByPatientId(patientId);
    if (adhdFormEntity != null) {
      throw new ConflictException(
          String.format("Adhd form has already been filled for patient %s", patientId));
    }
  }
}
