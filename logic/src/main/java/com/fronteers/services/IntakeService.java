package com.fronteers.services;

import com.fronteers.brightlife.model.AlcoholDrugHistory;
import com.fronteers.brightlife.model.IntakeForm;
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
import com.fronteers.utils.PatientUtils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    newIntakeForm.setPastMarriagesInfo(!request.getPastMarriagesInfo().isEmpty() ?
        mapPastMarriagesDtoToEntities(request.getPastMarriagesInfo(), newIntakeForm) : null);
    newIntakeForm.setPastProviders(!request.getPastProviders().isEmpty() ?
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
    AlcoholDrugHistoryEntity alcoholDrugHistoryEntity = AlcoholDrugHistoryEntity.builder()
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
        .pastMedicalHistory(!alcoholDrugHistory.getPastMedicalHistory().isEmpty() ?
            new HashSet<>(alcoholDrugHistory.getPastMedicalHistory()) : new HashSet<>())
        .pastSurgicalHistory(!alcoholDrugHistory.getPastSurgicalHistory().isEmpty() ?
            new HashSet<>(alcoholDrugHistory.getPastSurgicalHistory()) : new HashSet<>())
        .allergies(!alcoholDrugHistory.getAllergies().isEmpty() ?
            new HashSet<>(alcoholDrugHistory.getAllergies()) : null)
        .otherUsefulInfo(alcoholDrugHistory.getOtherUsefulInfo())
        .intakeForm(intakeForm)
        .build();
    setSubstanceUsages(alcoholDrugHistory.getSubstanceUsages(), alcoholDrugHistoryEntity);
    setPastTreatments(alcoholDrugHistory.getPastTreatmentInfo(), alcoholDrugHistoryEntity);
    setRelativesWithMentalIllnessOrSuicide(
        alcoholDrugHistory.getRelativesWithMentalIllnessOrSuicide(), alcoholDrugHistoryEntity);
    alcoholDrugHistoryEntity.setIntakeForm(intakeForm);
    return alcoholDrugHistoryRepository.save(alcoholDrugHistoryEntity);
  }

  private void setRelativesWithMentalIllnessOrSuicide(
      List<RelativeWithMentalIllnessOrSuicide> relativesWithMentalIllnessOrSuicide,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    if (!relativesWithMentalIllnessOrSuicide.isEmpty()) {
      List<RelativesWithMentalIllnessOrSuicideEntity> sickRelativeEntities = new ArrayList<>();
      for (RelativeWithMentalIllnessOrSuicide sickRelative : relativesWithMentalIllnessOrSuicide) {
        sickRelativeEntities.add(RelativesWithMentalIllnessOrSuicideEntity.builder()
            .relative(sickRelative.getRelative())
            .illness(sickRelative.getIllness())
            .alcoholDrugHistory(alcoholDrugHistoryEntity)
            .build());
      }
      alcoholDrugHistoryEntity.setRelativesWithMentalIllnessOrSuicide(
          relWithMentalIllnessOrSuicideRepository.saveAll(sickRelativeEntities));
    }
  }

  private void setPastTreatments(List<PastTreatmentInfo> pastTreatmentInfos,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    if (!pastTreatmentInfos.isEmpty()) {
      List<PastTreatmentEntity> pastTreatmentEntities = new ArrayList<>();
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
      alcoholDrugHistoryEntity.setPastTreatments(
          pastTreatmentRepository.saveAll(pastTreatmentEntities));
    }
  }

  private void setSubstanceUsages(List<SubstanceUsage> substanceUsages,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    if (!substanceUsages.isEmpty()) {
      List<SubstanceUsageEntity> substanceUsageEntities = new ArrayList<>();
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
      alcoholDrugHistoryEntity.setSubstanceUsages(
          substanceUsageRepository.saveAll(substanceUsageEntities));
    }
  }

  private List<MedicationEntity> mapMedicationDtosToEntites(List<Medication> pastMedications,
      List<Medication> currentMedications, IntakeFormEntity intakeForm) {
    List<MedicationEntity> medicationEntities = new ArrayList<>();
    if (!pastMedications.isEmpty()) {
      processMedications(medicationEntities, pastMedications, false, intakeForm);
    }
    if (!currentMedications.isEmpty()) {
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
          .appointmentDate(Date.valueOf(pastProvider.getAppointmentDate()))
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
