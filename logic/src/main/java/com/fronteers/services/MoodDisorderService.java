package com.fronteers.services;

import com.fronteers.brightlife.model.MoodDisorderAssessmentForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.MoodDisorderAssessmentFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.MoodDisorderAssessmentRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MoodDisorderService {

  private final PatientUtils patientUtils;
  private final PatientRepository patientRepository;
  private final MoodDisorderAssessmentRepository moodDisorderAssessmentRepository;

  public Success submitMoodDisorderAssessment(MoodDisorderAssessmentForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForMoodDisorderUniqueness(request.getPatientId().toString());
    MoodDisorderAssessmentFormEntity mdafEntity = MoodDisorderAssessmentFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .hyperFeeling(request.getHyperFeeling())
        .isIrritable(request.getIsIrritable())
        .isOverConfident(request.getIsOverConfident())
        .lessSleep(request.getLessSleep())
        .talkMore(request.getTalkMore())
        .pacedThoughts(request.getPacedThoughts())
        .easyDistraction(request.getEasyDistraction())
        .overEnergetic(request.getOverEnergetic())
        .overActive(request.getOverActive())
        .overSocial(request.getOverSocial())
        .sexaholic(request.getSexaholic())
        .overFoolish(request.getOverFoolish())
        .overSpending(request.getOverSpending())
        .sameTimeOccurrence(request.getSameTimeOccurrence())
        .influenceOnLife(request.getInfluenceOnLife())
        .isRelativeWithBipolar(request.getIsRelativeWithBipolar())
        .isBipolarDiagnosed(request.getIsBipolarDiagnosed())
        .build();
    patient.setMoodDisorderAssessmentForm(mdafEntity);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully",
        "Mood Disorder Assessment Form Submitted");
  }

  public MoodDisorderAssessmentForm getMoodDisorder(Long id) {
    return PatientDtoMapper.mapMoodDisorderEntityToDto(checkIfMoodAssesmentExists(id));
  }

  private MoodDisorderAssessmentFormEntity checkIfMoodAssesmentExists(Long id) {
    return moodDisorderAssessmentRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format("Mood Disorder Assessment form with id %s does not exist", id)
    ));
  }

  private void checkForMoodDisorderUniqueness(String patientId) {
    MoodDisorderAssessmentFormEntity mdafEntity = moodDisorderAssessmentRepository.findOneByPatientId(
        patientId);
    if (mdafEntity != null) {
      throw new ConflictException(
          String.format("Mood Disorder Assessment form has already been filled for patient %s",
              patientId));
    }
  }
}
