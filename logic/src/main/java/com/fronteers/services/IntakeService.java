package com.fronteers.services;

import com.fronteers.brightlife.model.AlcoholDrugHistory;
import com.fronteers.brightlife.model.IntakeForm;
import com.fronteers.brightlife.model.IntakeIntroUpdate;
import com.fronteers.brightlife.model.IntakePsychHistoryUpdate;
import com.fronteers.brightlife.model.Medication;
import com.fronteers.brightlife.model.PastMarriagesInfo;
import com.fronteers.brightlife.model.PastProviders;
import com.fronteers.brightlife.model.PastTreatmentInfo;
import com.fronteers.brightlife.model.RelativeWithMentalIllnessOrSuicide;
import com.fronteers.brightlife.model.SubstanceUsage;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.AlcoholDrugHistoryEntity;
import com.fronteers.models.entity.MedicationEntity;
import com.fronteers.models.entity.PastMarriageEntity;
import com.fronteers.models.entity.PastProviderEntity;
import com.fronteers.models.entity.PastTreatmentEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.RelativesWithMentalIllnessOrSuicideEntity;
import com.fronteers.models.entity.SubstanceUsageEntity;
import com.fronteers.models.entity.forms.IntakeFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.AlcoholDrugHistoryRepository;
import com.fronteers.repositories.IntakeRepository;
import com.fronteers.repositories.MedicationRepository;
import com.fronteers.repositories.PastMarriageRepository;
import com.fronteers.repositories.PastProviderRepository;
import com.fronteers.repositories.PastTreatmentRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.RelativeWithMentalIllnessOrSuicideRepository;
import com.fronteers.repositories.SubstanceUsageRepository;
import com.fronteers.utils.CopyBeanUtil;
import com.fronteers.utils.PatientUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class IntakeService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final IntakeRepository intakeRepository;
  private final PastMarriageRepository pastMarriageRepository;
  private final PastProviderRepository pastProviderRepository;
  private final MedicationRepository medicationRepository;
  private final AlcoholDrugHistoryRepository alcoholDrugHistoryRepository;
  private final SubstanceUsageRepository substanceUsageRepository;
  private final PastTreatmentRepository pastTreatmentRepository;
  private final RelativeWithMentalIllnessOrSuicideRepository relWithMentalIllnessOrSuicideRepository;
  private final EmailService emailService;
  @Value("${fe.base.url}")
  private String feBaseUrl;

//  TODO: Do null checks on arrays as oppose to empty checks

  @Transactional
  public Success submitIntake(IntakeForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForIntakeUniqueness(request.getPatientId().toString());
    IntakeFormEntity newIntakeForm = IntakeFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .doYouShareHome(request.getDoYouShareHome())
        .complaints(request.getComplaints())
        .sexPreference(request.getSexPreference())
        .onProbation(request.getOnProbation())
        .inLawsuit(request.getInLawsuit())
        .childrenCount(request.getChildrenCount())
        .marriageCount(request.getMarriageCount())
        .hasAttemptedSuicide(request.getHasAttemptedSuicide())
        .isPsychHospitalized(request.getIsPsychHospitalized())
        .build();
    newIntakeForm.setPastMarriagesInfo(
        (request.getPastMarriagesInfo() != null && !request.getPastMarriagesInfo().isEmpty()) ?
            mapPastMarriagesDtoToEntities(request.getPastMarriagesInfo(), newIntakeForm) : null);
    newIntakeForm.setPastProviders(
        (request.getPastProviders() != null && !request.getPastProviders().isEmpty()) ?
            mapPastProviderDtosToEntities(request.getPastProviders(), newIntakeForm) : null);
    newIntakeForm.setMedications(
        mapMedicationDtosToEntites(request.getPastMedications(), request.getCurrentMedications(),
            newIntakeForm));
    newIntakeForm.setAlcoholDrugHistory(request.getAlcoholDrugHistory() != null ?
        processAlcoholDrugHistory(request.getAlcoholDrugHistory(), newIntakeForm) : null);
    patient.setIntakeForm(newIntakeForm);
    patientRepository.save(patient);
    notifyAdminAndPatient(patient);
    return new Success(true, "Form Submitted Successfully", "Intake Form Submitted");
  }

  @Transactional
  public Success updateIntakeIntro(Long intakeId, IntakeIntroUpdate request) {
    IntakeFormEntity intakeFormEntity = checkIfIntakeFormExists(intakeId);
    intakeFormEntity.setDoYouShareHome(request.getDoYouShareHome());
    intakeFormEntity.setSexPreference(request.getSexPreference());
    intakeFormEntity.setComplaints(request.getComplaints());
    intakeFormEntity.setOnProbation(request.getOnProbation());
    intakeFormEntity.setInLawsuit(request.getInLawsuit());
    intakeFormEntity.setChildrenCount(request.getChildrenCount());
    intakeFormEntity.setMarriageCount(request.getMarriageCount());
    pastMarriageRepository.deleteAll(intakeFormEntity.getPastMarriagesInfo());
    intakeFormEntity.getPastMarriagesInfo().clear();
    intakeFormEntity.setPastMarriagesInfo(
        mapPastMarriagesDtoToEntities(request.getPastMarriagesInfo(), intakeFormEntity));
    intakeRepository.save(intakeFormEntity);
    return new Success(true, "Updated Successfully",
        "Intro Details Of Intake Form Updated Successfully");
  }

  @Transactional
  public Success updateIntakePsychHistory(Long intakeId, IntakePsychHistoryUpdate request) {
    IntakeFormEntity intakeFormEntity = checkIfIntakeFormExists(intakeId);
    intakeFormEntity.setHasAttemptedSuicide(request.getHasAttemptedSuicide());
    intakeFormEntity.setIsPsychHospitalized(request.getIsPsychHospitalized());
    pastProviderRepository.deleteAll(intakeFormEntity.getPastProviders());
    intakeFormEntity.getPastProviders().clear();
    intakeFormEntity.setPastProviders(
        mapPastProviderDtosToEntities(request.getPastProviders(), intakeFormEntity));
    medicationRepository.deleteAll(intakeFormEntity.getMedications());
    intakeFormEntity.getMedications().clear();
    intakeFormEntity.setMedications(mapMedicationDtosToEntites(
        request.getPastMedications(), request.getCurrentMedications(), intakeFormEntity));
    return new Success(true, "Updated Successfully",
        "Psych History Detaios of Intake Form Updated Successfully");
  }

  public Success updateIntakeDrugHistory(Long intakeId, AlcoholDrugHistory request) {
    IntakeFormEntity intakeFormEntity = checkIfIntakeFormExists(intakeId);
    updateAlcoholDrugHistory(intakeFormEntity, request);
    return new Success(true, "Updated Success",
        "Drug Histrory Details Of Intake Form Updated Successfully");
  }


  private void updateMedications(IntakeFormEntity intakeForm, List<Medication> currentMeds,
      List<Medication> pastMeds) {
    if (intakeForm.getMedications() == null) {
      mapMedicationDtosToEntites(pastMeds, currentMeds, intakeForm);
    } else {
      List<MedicationEntity> existingMedEntites = intakeForm.getMedications();
      List<Long> existingMedIds = existingMedEntites.stream().map(MedicationEntity::getId).collect(
          Collectors.toList());
      if (currentMeds != null && !currentMeds.isEmpty()) {
        processMedDtosForUpdate(currentMeds, intakeForm, true, existingMedIds, existingMedEntites);
      }
      if (pastMeds != null && !pastMeds.isEmpty()) {
        processMedDtosForUpdate(pastMeds, intakeForm, false, existingMedIds, existingMedEntites);
      }
    }
  }

  private void processMedDtosForUpdate(List<Medication> medDtos, IntakeFormEntity intakeForm,
      boolean isCurrent, List<Long> existingMedIds, List<MedicationEntity> existingMedEntites) {
    for (Medication medDto : medDtos) {
      if (medDto.getId() != null) {
        if (existingMedIds.contains(medDto.getId())) {
          MedicationEntity existingMedEntity = existingMedEntites.stream().filter(
              existingMed -> Objects.equals(existingMed.getId(), medDto.getId())
          ).findFirst().orElseThrow(
              () -> new NotFoundException("Medication with id " + medDto.getId() + " Not Found"));
          MedicationEntity updatedMedEntity = mapMedDtoToEntity(medDto, isCurrent);
          CopyBeanUtil.copyNonNullProperties(updatedMedEntity, existingMedEntity);
        } else {
          MedicationEntity newMedEntity = mapMedDtoToEntity(medDto, isCurrent);
          existingMedEntites.add(newMedEntity);
        }
      }
    }
  }

  private MedicationEntity mapMedDtoToEntity(Medication medDto, boolean isCurrent) {
    return MedicationEntity.builder()
        .medication(medDto.getMedication())
        .conditionTreated(medDto.getConditionTreated())
        .instruction(medDto.getUsageInstruction())
        .prescription(medDto.getPrescription())
        .isCurrent(isCurrent)
        .build();
  }

  private void updatePastProviders(IntakeFormEntity intakeForm, List<PastProviders> pastProviders) {
    if (pastProviders != null && !pastProviders.isEmpty()) {
      if (intakeForm.getPastProviders() == null) {
        intakeForm.setPastProviders(mapPastProviderDtosToEntities(pastProviders, intakeForm));
      } else {
        List<PastProviderEntity> existingPastProviders = intakeForm.getPastProviders();
        List<Long> existingPastMarriagesIds = existingPastProviders.stream()
            .map(PastProviderEntity::getId).toList();
        for (PastProviders pp : pastProviders) {
          if (pp.getId() != null) {
            if (existingPastMarriagesIds.contains(pp.getId())) {
              PastProviderEntity existingPastProvider = existingPastProviders.stream().filter(
                  existingPp -> Objects.equals(existingPp.getId(), pp.getId())
              ).findFirst().orElseThrow(() -> new NotFoundException(
                  "Past Provider With Id " + pp.getId() + " not found"));
              PastProviderEntity updatedPastProvider = mapPastProviderDtoToEntity(pp);
              CopyBeanUtil.copyNonNullProperties(updatedPastProvider, existingPastProvider);
            } else {
              PastProviderEntity newPastProviderEntity = mapPastProviderDtoToEntity(pp);
              newPastProviderEntity.setIntakeForm(intakeForm);
              existingPastProviders.add(newPastProviderEntity);
            }
          }
        }
      }
    }
  }

  private void updatePastMarriagesInfo(IntakeFormEntity intakeForm,
      List<PastMarriagesInfo> pastMarriagesInfo) {
    if (pastMarriagesInfo == null) {
      throw new BadRequestException("Past Marriages Details Can not be null");
    }
    if (!intakeForm.getPastMarriagesInfo().isEmpty()) {
      pastMarriageRepository.deleteAll(intakeForm.getPastMarriagesInfo());
    }

  }

//  private void updatePastMarriagesInfo(IntakeFormEntity intakeForm, List<PastMarriagesInfo> pastMarriagesInfo) {
//    if (pastMarriagesInfo != null && !pastMarriagesInfo.isEmpty()) {
//      if (intakeForm.getPastMarriagesInfo() == null) {
//        // If there are no existing past marriages, map the new ones and save them
//        List<PastMarriageEntity> newPastMarriages = mapPastMarriagesDtoToEntities(pastMarriagesInfo, intakeForm);
//        intakeForm.setPastMarriagesInfo(newPastMarriages);
//      } else {
//        List<PastMarriageEntity> existingPastMarriages = intakeForm.getPastMarriagesInfo();
//        List<Long> existingPastMarriagesIds = existingPastMarriages.stream().map(PastMarriageEntity::getId).toList();
//        for (PastMarriagesInfo pmInfo : pastMarriagesInfo) {
//          if (pmInfo.getId() != null){
//            if (existingPastMarriagesIds.contains(pmInfo.getId())){
//              PastMarriageEntity existingPastMarriageEntity = existingPastMarriages.stream().filter(
//                existingPm -> Objects.equals(existingPm.getId(), pmInfo.getId())).findFirst().orElseThrow(
//                  () -> new NotFoundException("Past Marriage Info With Id " + pmInfo.getId() + " not found"));
//              PastMarriageEntity updatedPastMarriage = mapPastMarriageDtoToEntity(pmInfo);
//              CopyBeanUtil.copyNonNullProperties(updatedPastMarriage, existingPastMarriageEntity);
//            } else{
//              PastMarriageEntity newPastMarriageEntity = mapPastMarriageDtoToEntity(pmInfo);
//              newPastMarriageEntity.setIntakeForm(intakeForm);
//              existingPastMarriages.add(newPastMarriageEntity);
//            }
//          }
//        }
//      }
//    }
//  }

  private PastMarriageEntity mapPastMarriageDtoToEntity(PastMarriagesInfo pmInfo) {
    return PastMarriageEntity.builder()
        .description(pmInfo.getMarriageDescription())
        .duration(pmInfo.getDuration())
        .divorceReason(pmInfo.getDivorceReason()).build();
  }

  private PastProviderEntity mapPastProviderDtoToEntity(PastProviders pp) {
    return PastProviderEntity.builder()
        .appointmentDate(
            pp.getAppointmentDate() != null ? Date.valueOf(pp.getAppointmentDate()) : null)
        .provider(pp.getProvider())
        .build();
  }

  private void notifyAdminAndPatient(PatientEntity patient) {
    Map<String, Object> variables = Map.of(
        "name", patient.getFirstName() + " " + patient.getLastName(),
        "email", patient.getEmail(),
        "formType", "Intake",
        "appointmentUrl", feBaseUrl + "/appointment/" + patient.getPatientId());
    try {
      emailService.sendEmail("fronteers.dev@gmail.com", "Intake Form Submission Details",
          "admin-form-submission-notification", variables);
      emailService.sendEmail(patient.getEmail(), "Acknowledgement",
          "patient-appointment-template.html", variables);
    } catch (MessagingException e) {
      log.warn(e.getMessage());
      throw new BadRequestException(e.getMessage());
    }
  }

  public IntakeForm getIntake(Long id) {
    return PatientDtoMapper.mapIntakeEntityToDto(checkIfIntakeFormExists(id));
  }

  private AlcoholDrugHistoryEntity processAlcoholDrugHistory(AlcoholDrugHistory alcoholDrugHistory,
      IntakeFormEntity intakeForm) {
    AlcoholDrugHistoryEntity alcoholDrugHistoryEntity = mapAlcoholDrugHistoryBasicInfo(
        alcoholDrugHistory);
    alcoholDrugHistoryEntity.setIntakeForm(intakeForm);
    alcoholDrugHistoryEntity.setSubstanceUsages(
        setSubstanceUsages(alcoholDrugHistory.getSubstanceUsages(), alcoholDrugHistoryEntity));
    alcoholDrugHistoryEntity.setPastTreatments(
        setPastTreatments(alcoholDrugHistory.getPastTreatmentInfo(), alcoholDrugHistoryEntity));
    alcoholDrugHistoryEntity.setRelativesWithMentalIllnessOrSuicide(
        setRelativesWithMentalIllnessOrSuicide(
            alcoholDrugHistory.getRelativesWithMentalIllnessOrSuicide(), alcoholDrugHistoryEntity));
    alcoholDrugHistoryEntity.setIntakeForm(intakeForm);
    return alcoholDrugHistoryRepository.save(alcoholDrugHistoryEntity);
  }

  @Transactional
  private void updateAlcoholDrugHistory(IntakeFormEntity intakeForm,
      AlcoholDrugHistory alcoholDrugHistory) {
    if (alcoholDrugHistory == null) {
      throw new BadRequestException("Request cannot be null");
    }
    AlcoholDrugHistoryEntity existingAdHistory = intakeForm.getAlcoholDrugHistory();
    AlcoholDrugHistoryEntity updatedAdHistory = mapAlcoholDrugHistoryBasicInfo(alcoholDrugHistory);
    CopyBeanUtil.copyNonNullProperties(updatedAdHistory, existingAdHistory);
    existingAdHistory.getSubstanceUsages().clear();
    existingAdHistory.getSubstanceUsages()
        .addAll(setSubstanceUsages(alcoholDrugHistory.getSubstanceUsages(), existingAdHistory));
    existingAdHistory.getPastTreatments().clear();
    existingAdHistory.getPastTreatments()
        .addAll(setPastTreatments(alcoholDrugHistory.getPastTreatmentInfo(), existingAdHistory));
    existingAdHistory.getRelativesWithMentalIllnessOrSuicide().clear();
    existingAdHistory.getRelativesWithMentalIllnessOrSuicide()
        .addAll(setRelativesWithMentalIllnessOrSuicide(
            alcoholDrugHistory.getRelativesWithMentalIllnessOrSuicide(), existingAdHistory));
    alcoholDrugHistoryRepository.save(existingAdHistory);
  }

  private AlcoholDrugHistoryEntity mapAlcoholDrugHistoryBasicInfo(
      AlcoholDrugHistory alcoholDrugHistory) {
    return AlcoholDrugHistoryEntity.builder()
        .usageFrequency(alcoholDrugHistory.getUsageFrequency())
        .brand(alcoholDrugHistory.getBrand())
        .lastUsed(alcoholDrugHistory.getLastUsed())
        .drinkGuiltCheck(alcoholDrugHistory.getDrinkGuiltCheck())
        .weeklyAverageSpending(alcoholDrugHistory.getWeeklyAverageSpending())
        .isPastStepRecoveryParticipant(alcoholDrugHistory.getIsPastStepRecoveryParticipant())
        .isCurrentStepRecoveryParticipant(alcoholDrugHistory.getIsCurrentStepRecoveryParticipant())
        .birthPlace(alcoholDrugHistory.getBirthPlace())
        .growthPlace(alcoholDrugHistory.getGrowthPlace())
        .raisedBy(alcoholDrugHistory.getRaisedBy())
        .siblingsCount(alcoholDrugHistory.getSiblingsCount())
        .childhoodInfo(alcoholDrugHistory.getChildhoodInfo())
        .wasPhysicallyAbused(alcoholDrugHistory.getWasPhysicallyAbused())
        .wasEmotionallyAbused(alcoholDrugHistory.getWasEmotionallyAbused())
        .wasSexuallyAbused(alcoholDrugHistory.getWasSexuallyAbused())
        .hasMedicalDisability(alcoholDrugHistory.getHasMedicalDisability())
        .pastMedicalHistory((alcoholDrugHistory.getPastMedicalHistory() != null
            && !alcoholDrugHistory.getPastMedicalHistory().isEmpty()) ?
            new HashSet<>(alcoholDrugHistory.getPastMedicalHistory()) : new HashSet<>())
        .pastSurgicalHistory((alcoholDrugHistory.getPastSurgicalHistory() != null
            && !alcoholDrugHistory.getPastSurgicalHistory().isEmpty()) ?
            new HashSet<>(alcoholDrugHistory.getPastSurgicalHistory()) : new HashSet<>())
        .allergies((alcoholDrugHistory.getAllergies() != null && !alcoholDrugHistory.getAllergies()
            .isEmpty()) ?
            new HashSet<>(alcoholDrugHistory.getAllergies()) : null)
        .otherUsefulInfo(alcoholDrugHistory.getOtherUsefulInfo())
        .build();
  }

  private List<RelativesWithMentalIllnessOrSuicideEntity> setRelativesWithMentalIllnessOrSuicide(
      List<RelativeWithMentalIllnessOrSuicide> relativesWithMentalIllnessOrSuicide,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    List<RelativesWithMentalIllnessOrSuicideEntity> sickRelativeEntities = new ArrayList<>();
    if (relativesWithMentalIllnessOrSuicide != null
        && !relativesWithMentalIllnessOrSuicide.isEmpty()) {
      for (RelativeWithMentalIllnessOrSuicide sickRelative : relativesWithMentalIllnessOrSuicide) {
        sickRelativeEntities.add(RelativesWithMentalIllnessOrSuicideEntity.builder()
            .relative(sickRelative.getRelative())
            .illness(sickRelative.getIllness())
            .alcoholDrugHistory(alcoholDrugHistoryEntity)
            .build());
      }
    }
    return sickRelativeEntities;
  }

  private List<PastTreatmentEntity> setPastTreatments(List<PastTreatmentInfo> pastTreatmentInfos,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    List<PastTreatmentEntity> pastTreatmentEntities = new ArrayList<>();
    if (!pastTreatmentInfos.isEmpty()) {
      for (PastTreatmentInfo pastTreatmentInfo : pastTreatmentInfos) {
        pastTreatmentEntities.add(PastTreatmentEntity.builder()
            .facility(pastTreatmentInfo.getFacility())
            .date(pastTreatmentInfo.getDate() != null ? Date.valueOf(pastTreatmentInfo.getDate())
                : null)
            .drugTreated(pastTreatmentInfo.getDrugTreated())
            .isTreatmentCompleted(pastTreatmentInfo.getIsTreatmentCompleted())
            .alcoholDrugHistory(alcoholDrugHistoryEntity)
            .build());
      }
    }
    return pastTreatmentEntities;
  }

  private List<SubstanceUsageEntity> setSubstanceUsages(List<SubstanceUsage> substanceUsages,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    List<SubstanceUsageEntity> substanceUsageEntities = new ArrayList<>();
    if (!substanceUsages.isEmpty()) {
      for (SubstanceUsage substanceUsage : substanceUsages) {
        substanceUsageEntities.add(SubstanceUsageEntity.builder()
            .name(substanceUsage.getSubstanceName())
            .ageAtFirstUse(substanceUsage.getAgeAtFirstUse())
            .qtyUse(substanceUsage.getQtyUse())
            .frequentUsage(substanceUsage.getUsageFrequency())
            .lastUsed(substanceUsage.getLastUsed())
            .alcoholDrugHistory(alcoholDrugHistoryEntity)
            .build());
      }
    }
    return substanceUsageEntities;
  }

  private List<MedicationEntity> mapMedicationDtosToEntites(List<Medication> pastMedications,
      List<Medication> currentMedications, IntakeFormEntity intakeForm) {
    List<MedicationEntity> medicationEntities = new ArrayList<>();
    if (pastMedications != null && !pastMedications.isEmpty()) {
      processMedications(medicationEntities, pastMedications, false, intakeForm);
    }
    if (currentMedications != null && !currentMedications.isEmpty()) {
      processMedications(medicationEntities, currentMedications, true, intakeForm);
    }
    if (medicationEntities.isEmpty()) {
      return medicationEntities;
    }
    return medicationRepository.saveAll(medicationEntities);
  }

  private void processMedications(List<MedicationEntity> medicationEntities,
      List<Medication> medicationDtos, Boolean isCurrent, IntakeFormEntity intakeForm) {
    for (Medication medication : medicationDtos) {
      medicationEntities.add(MedicationEntity.builder()
          .medication(medication.getMedication())
          .conditionTreated(medication.getConditionTreated())
          .instruction(medication.getUsageInstruction())
          .prescription(medication.getPrescription())
          .isCurrent(isCurrent)
          .intakeForm(intakeForm)
          .build());
    }
  }

  private List<PastProviderEntity> mapPastProviderDtosToEntities(List<PastProviders> pastProviders,
      IntakeFormEntity intakeForm) {
    List<PastProviderEntity> pastProviderEntities = new ArrayList<>();
    for (PastProviders pastProvider : pastProviders) {
      pastProviderEntities.add(PastProviderEntity.builder()
          .provider(pastProvider.getProvider())
          .appointmentDate(pastProvider.getAppointmentDate() != null ? Date.valueOf(
              pastProvider.getAppointmentDate()) : null)
          .intakeForm(intakeForm)
          .build());
    }
    return pastProviderRepository.saveAll(pastProviderEntities);
  }

  private List<PastMarriageEntity> mapPastMarriagesDtoToEntities(
      List<PastMarriagesInfo> pastMarriagesInfo, IntakeFormEntity intakeForm) {
    List<PastMarriageEntity> pastMarriageEntities = new ArrayList<>();
    for (PastMarriagesInfo pastMarriage : pastMarriagesInfo) {
      pastMarriageEntities.add(PastMarriageEntity.builder()
          .description(pastMarriage.getMarriageDescription())
          .duration(pastMarriage.getDuration())
          .divorceReason(pastMarriage.getDivorceReason())
          .intakeForm(intakeForm)
          .build());
    }
    return pastMarriageRepository.saveAll(pastMarriageEntities);
  }

  private IntakeFormEntity checkIfIntakeFormExists(Long id) {
    return intakeRepository.findOneById(id)
        .orElseThrow(() -> new NotFoundException(
            String.format("Intake form with id %s does not exist", id)));
  }

  private void checkForIntakeUniqueness(String patientId) {
    IntakeFormEntity intakeFormEntity = intakeRepository.findOneByPatientId(patientId);
    if (intakeFormEntity != null) {
      throw new ConflictException(
          String.format("Intake form has already been filled for patient %s", patientId));
    }
  }
}
