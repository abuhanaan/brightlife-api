package com.fronteers.services;

import com.fronteers.brightlife.model.Referral;
import com.fronteers.brightlife.model.ScreeningForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.ReferralEntity;
import com.fronteers.models.entity.forms.ScreeningFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.ReferralRepository;
import com.fronteers.repositories.ScreeningRepository;
import com.fronteers.utils.PatientUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ScreeningService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final ScreeningRepository screeningRepository;
  private final ReferralRepository referralRepository;

  public Success submitScreening(ScreeningForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForScreeningUniqueness(patient.getPatientId());
    ScreeningFormEntity screeningFormEntity = ScreeningFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .mhBhPhone(request.getMhBhPhone())
        .helpNeeds(request.getHelpNeeds())
        .inCrisis(request.getInCrisis())
        .currentlyOnPsychMed(request.getCurrentlyOnPsychMed())
        .stableOnMed(request.getStableOnMed())
        .isPsychiatristConsult(request.getIsPsychiatristConsult())
        .isTherapistConsult(request.getIsTherapistConsult())
        .anyMentalHealthTreatment(request.getAnyMentalHealthTreatment())
        .suicideAttemptHistory(request.getSuicideAttemptHistory())
        .harmToSelfOrOthers(request.getHarmToSelfOrOthers())
        .intent(request.getIntent())
        .healthSymptoms(request.getHealthSymptoms())
        .healthSymptomsFrequency(request.getHealthSymptomsFrequency())
        .build();
    setRefferal(screeningFormEntity, request.getReferral());
    patient.setScreeningForm(screeningFormEntity);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully",
        "Screening Form Submitted");
  }

  public ScreeningForm getScreening(Long id) {
    ScreeningFormEntity entity = checkIfScreeningExists(id);
    ScreeningForm dto = new ScreeningForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setMhBhPhone(entity.getMhBhPhone());
    dto.setHelpNeeds(entity.getHelpNeeds());
    dto.setInCrisis(entity.getInCrisis());
    dto.setCurrentlyOnPsychMed(entity.getCurrentlyOnPsychMed());
    dto.setStableOnMed(entity.getStableOnMed());
    dto.setIsPsychiatristConsult(entity.getIsPsychiatristConsult());
    dto.setIsTherapistConsult(entity.getIsTherapistConsult());
    dto.setAnyMentalHealthTreatment(entity.getAnyMentalHealthTreatment());
    dto.setSuicideAttemptHistory(entity.getSuicideAttemptHistory());
    dto.setHarmToSelfOrOthers(entity.getHarmToSelfOrOthers());
    setReferralToDto(dto, entity.getReferral());
    return dto;
  }

  private void setReferralToDto(ScreeningForm dto, ReferralEntity referralEntity) {
    Referral referralDto = new Referral();
    referralDto.setId(referralEntity.getId());
    referralEntity.setSource(referralEntity.getSource());
    referralDto.setTherapist(referralEntity.getTherapist());
    referralDto.setFirstName(referralEntity.getFirstName());
    referralDto.setMiddleName(referralEntity.getMiddleName());
    referralDto.setLastName(referralEntity.getLastName());
    referralDto.setPhone(referralEntity.getPhone());
    referralDto.setAddress(PatientDtoMapper.mapAddressEntityToAddressDto(referralEntity.getAddress()));
    dto.setReferral(referralDto);
  }

  private void setRefferal(ScreeningFormEntity screeningFormEntity, Referral referral) {
    ReferralEntity referralEntity = ReferralEntity.builder()
        .screeningForm(screeningFormEntity)
        .source(referral.getSource())
        .therapist(referral.getTherapist())
        .firstName(referral.getFirstName())
        .middleName(referral.getMiddleName())
        .lastName(referral.getLastName())
        .phone(referral.getPhone())
        .address(patientUtils.mapAddressProperties(referral.getAddress()))
        .build();
    screeningFormEntity.setReferral(referralRepository.save(referralEntity));
  }

  private void checkForScreeningUniqueness(String patientId) {
    ScreeningFormEntity entity = screeningRepository.findOneByPatientId(patientId);
    if (entity != null){
      throw new ConflictException(String.format("Screening form has already been filled for patient %s", patientId));
    }
  }

  private ScreeningFormEntity checkIfScreeningExists(Long id){
    return screeningRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format("Screening form with id %s does not exist", id)));
  }
}
