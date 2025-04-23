package com.fronteers.services;

import com.fronteers.brightlife.model.AlcoholDrugHistory;
import com.fronteers.brightlife.model.DrinkGuiltCheck;
import com.fronteers.brightlife.model.IntakeForm;
import com.fronteers.brightlife.model.Medication;
import com.fronteers.brightlife.model.Medication.CategoryEnum;
import com.fronteers.brightlife.model.PastMarriagesInfo;
import com.fronteers.brightlife.model.PastProviders;
import com.fronteers.brightlife.model.PastTreatmentInfo;
import com.fronteers.brightlife.model.RelativeWithMentalIllnessOrSuicide;
import com.fronteers.brightlife.model.SubstanceUsage;
import com.fronteers.brightlife.model.Success;
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
import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


//  TODO: Do null checks on arrays as oppose to empty checks

  @Transactional
  public Success submitIntake(IntakeForm request){
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
          mapPastMarriagesDtoToEntities(request.getPastMarriagesInfo(), newIntakeForm): null);
      newIntakeForm.setPastProviders(!request.getPastProviders().isEmpty() ?
          mapPastProviderDtosToEntities(request.getPastProviders(), newIntakeForm) : null);
    newIntakeForm.setMedications(mapMedicationDtosToEntites(request.getPastMedications(), request.getCurrentMedications(), newIntakeForm));
    newIntakeForm.setAlcoholDrugHistory(request.getAlcoholDrugHistory() !=null ?
        processAlcoholDrugHistory(request.getAlcoholDrugHistory(), newIntakeForm) : null);
    patient.setIntakeForm(newIntakeForm);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Intake Form Submitted");
  }

  public IntakeForm getIntake(Long id){
    IntakeFormEntity intakeFormEntity = intakeRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format("Intake form with id %s does not exist", id)));
    return mapIntakeEntityToDto(intakeFormEntity);
  }

  private IntakeForm mapIntakeEntityToDto(IntakeFormEntity intakeFormEntity) {
    IntakeForm dto = new IntakeForm();
    dto.setId(intakeFormEntity.getId());
    dto.setPatientId(UUID.fromString(intakeFormEntity.getPatientId()));
    dto.setDoYouShareHome(intakeFormEntity.getDoYouShareHome());
    dto.setComplaints(intakeFormEntity.getComplaints());
    dto.setSexPreference(intakeFormEntity.getSexPreference());
    dto.setOnProbation(intakeFormEntity.getOnProbation());
    dto.setInLawsuit(intakeFormEntity.getInLawsuit());
    dto.setChildrenCount(intakeFormEntity.getChildrenCount());
    dto.setMarriageCount(intakeFormEntity.getMarriageCount());
    dto.setPastMarriagesInfo(!intakeFormEntity.getPastMarriagesInfo().isEmpty() ?
        intakeFormEntity.getPastMarriagesInfo().stream().map(pastMarriageEntity -> {
      PastMarriagesInfo infoDto = new PastMarriagesInfo();
      infoDto.setId(pastMarriageEntity.getId());
      infoDto.setDuration(pastMarriageEntity.getDuration());
      infoDto.setDivorceReason(pastMarriageEntity.getDivorceReason());
      infoDto.setMarriageDescription(pastMarriageEntity.getDescription());
      return infoDto;
    }).toList() : null);
    dto.setPastProviders(intakeFormEntity.getPastProviders().stream().map(pastProviderEntity -> {
      PastProviders providerDto = new PastProviders();
      providerDto.setId(pastProviderEntity.getId());
      providerDto.setProvider(pastProviderEntity.getProvider());
      providerDto.setAppointmentDate(pastProviderEntity.getAppointmentDate() != null ?
          pastProviderEntity.getAppointmentDate().toLocalDate() : null);
      return providerDto;
    }).toList());
    processDtoMedications(dto, intakeFormEntity.getMedications());
    dto.setHasAttemptedSuicide(intakeFormEntity.getHasAttemptedSuicide());
    dto.setIsPsychHospitalized(intakeFormEntity.getIsPsychHospitalized());
    setAlcoholDrugHistory(intakeFormEntity.getAlcoholDrugHistory(), dto);
    return dto;
  }

  private void setAlcoholDrugHistory(AlcoholDrugHistoryEntity adhEntity, IntakeForm intakeFormDto) {
    AlcoholDrugHistory alcoholDrugHistoryDto = new AlcoholDrugHistory();
    alcoholDrugHistoryDto.setId(adhEntity.getId());
    alcoholDrugHistoryDto.setUsageFrequency(adhEntity.getUsageFrequency());
    alcoholDrugHistoryDto.setBrand(adhEntity.getBrand());
    alcoholDrugHistoryDto.setLastUsed(adhEntity.getLastUsed());
    mapDrinkGuiltCheck(adhEntity,alcoholDrugHistoryDto);
    alcoholDrugHistoryDto.setSubstanceUsages(adhEntity.getSubstanceUsages().stream().map(substanceUsageEntity -> {
      SubstanceUsage substanceUsage = new SubstanceUsage();
      substanceUsage.setId(substanceUsageEntity.getId());
      substanceUsage.setSubstanceName(substanceUsageEntity.getName());
      substanceUsage.setAgeAtFirstUse(substanceUsageEntity.getAgeAtFirstUse());
      substanceUsage.setQtyUse(substanceUsageEntity.getQtyUse());
      substanceUsage.setUsageFrequency(substanceUsageEntity.getFrequentUsage());
      substanceUsage.setLastUsed(substanceUsageEntity.getLastUsed());
      return substanceUsage;
    }).toList());
    alcoholDrugHistoryDto.setWeeklyAverageSpending(adhEntity.getWeeklyAverageSpending());
    alcoholDrugHistoryDto.setPastTreatmentInfo(adhEntity.getPastTreatments().stream().map(pastTreatmentEntity -> {
      PastTreatmentInfo pastTreatment = new PastTreatmentInfo();
      pastTreatment.setId(pastTreatmentEntity.getId());
      pastTreatment.setDate(pastTreatmentEntity.getDate().toLocalDate());
      pastTreatment.setDrugTreated(pastTreatmentEntity.getDrugTreated());
      pastTreatment.setIsTreatmentCompleted(pastTreatmentEntity.getIsTreatmentCompleted());
      pastTreatment.setFacility(pastTreatmentEntity.getFacility());
      return pastTreatment;
    }).toList());
    alcoholDrugHistoryDto.setIsPastStepRecoveryParticipant(adhEntity.getIsPastStepRecoveryParticipant());
    alcoholDrugHistoryDto.setIsCurrentStepRecoveryParticipant(adhEntity.getIsCurrentStepRecoveryParticipant());
    alcoholDrugHistoryDto.setBirthPlace(adhEntity.getBirthPlace());
    alcoholDrugHistoryDto.growthPlace(adhEntity.getGrowthPlace());
    alcoholDrugHistoryDto.setRaisedBy(adhEntity.getRaisedBy());
    alcoholDrugHistoryDto.setSiblingsCount(adhEntity.getSiblingsCount());
    alcoholDrugHistoryDto.setChildhoodInfo(adhEntity.getChildhoodInfo());
    alcoholDrugHistoryDto.setWasPhysicallyAbused(adhEntity.getWasPhysicallyAbused());
    alcoholDrugHistoryDto.setWasEmotionallyAbused(adhEntity.getWasEmotionallyAbused());
    alcoholDrugHistoryDto.setWasSexuallyAbused(adhEntity.getWasSexuallyAbused());
    alcoholDrugHistoryDto.setHasMedicalDisability(adhEntity.getHasMedicalDisability());
    alcoholDrugHistoryDto.setPastMedicalHistory(adhEntity.getPastMedicalHistory() != null ?
        adhEntity.getPastMedicalHistory().stream().toList() : null);
    alcoholDrugHistoryDto.setPastSurgicalHistory(adhEntity.getPastSurgicalHistory() != null ?
        adhEntity.getPastSurgicalHistory().stream().toList() : null);
    alcoholDrugHistoryDto.setAllergies(adhEntity.getAllergies() != null ?
        adhEntity.getAllergies().stream().toList() : null);
    alcoholDrugHistoryDto.setRelativesWithMentalIllnessOrSuicide(
        adhEntity.getRelativesWithMentalIllnessOrSuicide() != null ?
            adhEntity.getRelativesWithMentalIllnessOrSuicide().stream().map(sickRelEntity -> {
          RelativeWithMentalIllnessOrSuicide sickRelDto = new RelativeWithMentalIllnessOrSuicide();
          sickRelDto.setId(sickRelEntity.getId());
          sickRelDto.setRelative(sickRelEntity.getRelative());
          sickRelDto.setIllness(sickRelDto.getIllness());
          return sickRelDto;
        }).toList() : null);
    alcoholDrugHistoryDto.setOtherUsefulInfo(adhEntity.getOtherUsefulInfo());
    intakeFormDto.setAlcoholDrugHistory(alcoholDrugHistoryDto);
  }

  private void mapDrinkGuiltCheck(AlcoholDrugHistoryEntity adhEntity, AlcoholDrugHistory alcoholDrugHistoryDto) {
    DrinkGuiltCheck drinkGuiltCheck = new DrinkGuiltCheck();
    drinkGuiltCheck.setFeelGuilt(adhEntity.getFeelGuilt());
    drinkGuiltCheck.setUpWithDrink(adhEntity.getUpWithDrink());
    drinkGuiltCheck.setAngeredByCritics(adhEntity.getAngeredByCritics());
    drinkGuiltCheck.setHaveCutBack(adhEntity.getHaveCutBack());
    alcoholDrugHistoryDto.setDrinkGuiltCheck(drinkGuiltCheck);
  }

  private void processDtoMedications(IntakeForm dto, List<MedicationEntity> medications) {
    List<Medication> currentMedicationDtos = new ArrayList<>();
    List<Medication> pastMedicationDtos = new ArrayList<>();
    for (MedicationEntity medicationEntity: medications){
      Medication medicationDto = new Medication();
      setMedicationDtoProps(medicationDto, medicationEntity);
      if (medicationEntity.getIsCurrent()){
        currentMedicationDtos.add(medicationDto);
      } else {
        pastMedicationDtos.add(medicationDto);
      }
    }
    dto.setCurrentMedications(currentMedicationDtos);
    dto.setPastMedications(pastMedicationDtos);
  }

  private void setMedicationDtoProps(Medication medicationDto, MedicationEntity medicationEntity) {
    medicationDto.setMedication(medicationEntity.getMedication());
    medicationDto.setId(medicationEntity.getId());
    medicationDto.setCategory(medicationEntity.getIsCurrent() ? CategoryEnum.CURRENT : CategoryEnum.PAST);
    medicationDto.setPrescription(medicationEntity.getPrescription());
    medicationDto.setConditionTreated(medicationEntity.getConditionTreated());
    medicationDto.setUsageInstruction(medicationEntity.getInstruction());
  }

  private AlcoholDrugHistoryEntity processAlcoholDrugHistory(AlcoholDrugHistory alcoholDrugHistory, IntakeFormEntity intakeForm) {
    AlcoholDrugHistoryEntity alcoholDrugHistoryEntity = AlcoholDrugHistoryEntity.builder()
        .usageFrequency(alcoholDrugHistory.getUsageFrequency())
        .brand(alcoholDrugHistory.getBrand())
        .lastUsed(alcoholDrugHistory.getLastUsed())
        .haveCutBack(alcoholDrugHistory.getDrinkGuiltCheck().getHaveCutBack())
        .angeredByCritics(alcoholDrugHistory.getDrinkGuiltCheck().getAngeredByCritics())
        .feelGuilt(alcoholDrugHistory.getDrinkGuiltCheck().getFeelGuilt())
        .upWithDrink(alcoholDrugHistory.getDrinkGuiltCheck().getUpWithDrink())
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
            new HashSet<>(alcoholDrugHistory.getPastMedicalHistory()): new HashSet<>())
        .pastSurgicalHistory(!alcoholDrugHistory.getPastSurgicalHistory().isEmpty() ?
            new HashSet<>(alcoholDrugHistory.getPastSurgicalHistory()) : new HashSet<>())
        .allergies(!alcoholDrugHistory.getAllergies().isEmpty() ?
            new HashSet<>(alcoholDrugHistory.getAllergies()) : null)
        .otherUsefulInfo(alcoholDrugHistory.getOtherUsefulInfo())
        .intakeForm(intakeForm)
        .build();
    setSubstanceUsages(alcoholDrugHistory.getSubstanceUsages(), alcoholDrugHistoryEntity);
    setPastTreatments(alcoholDrugHistory.getPastTreatmentInfo(), alcoholDrugHistoryEntity);
    setRelativesWithMentalIllnessOrSuicide(alcoholDrugHistory.getRelativesWithMentalIllnessOrSuicide(), alcoholDrugHistoryEntity);
    alcoholDrugHistoryEntity.setIntakeForm(intakeForm);
    return alcoholDrugHistoryRepository.save(alcoholDrugHistoryEntity);
  }

  private void setRelativesWithMentalIllnessOrSuicide(
      List<RelativeWithMentalIllnessOrSuicide> relativesWithMentalIllnessOrSuicide,
      AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    if (!relativesWithMentalIllnessOrSuicide.isEmpty()){
      List<RelativesWithMentalIllnessOrSuicideEntity> sickRelativeEntities = new ArrayList<>();
      for (RelativeWithMentalIllnessOrSuicide sickRelative: relativesWithMentalIllnessOrSuicide){
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

  private void setPastTreatments(List<PastTreatmentInfo> pastTreatmentInfos, AlcoholDrugHistoryEntity alcoholDrugHistoryEntity) {
    if (!pastTreatmentInfos.isEmpty()){
      List<PastTreatmentEntity> pastTreatmentEntities = new ArrayList<>();
      for (PastTreatmentInfo pastTreatmentInfo: pastTreatmentInfos){
        pastTreatmentEntities.add(PastTreatmentEntity.builder()
                .facility(pastTreatmentInfo.getFacility())
                .date(pastTreatmentInfo.getDate() != null ? Date.valueOf(pastTreatmentInfo.getDate()): null)
                .drugTreated(pastTreatmentInfo.getDrugTreated())
                .isTreatmentCompleted(pastTreatmentInfo.getIsTreatmentCompleted())
                .alcoholDrugHistory(alcoholDrugHistoryEntity)
            .build());
      }
      alcoholDrugHistoryEntity.setPastTreatments(pastTreatmentRepository.saveAll(pastTreatmentEntities));
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
    if (medicationEntities.isEmpty()){
      return medicationEntities;
    }
    return medicationRepository.saveAll(medicationEntities);
  }

  private void processMedications(List<MedicationEntity> medicationEntities,
      List<Medication> medicationDtos, Boolean isCurrent, IntakeFormEntity intakeForm){
    for (Medication medication: medicationDtos){
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

  private List<PastProviderEntity> mapPastProviderDtosToEntities(List<PastProviders> pastProviders, IntakeFormEntity intakeForm) {
    List<PastProviderEntity> pastProviderEntities = new ArrayList<>();
    for (PastProviders pastProvider: pastProviders){
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
    for (PastMarriagesInfo pastMarriage: pastMarriagesInfo){
      pastMarriageEntities.add(PastMarriageEntity.builder()
          .description(pastMarriage.getMarriageDescription())
              .duration(pastMarriage.getDuration())
              .divorceReason(pastMarriage.getDivorceReason())
              .intakeForm(intakeForm)
          .build());
    }
  return pastMarriageRepository.saveAll(pastMarriageEntities);
  }

  private void checkForIntakeUniqueness(String patientId) {
    IntakeFormEntity intakeFormEntity = intakeRepository.findOneByPatientId(patientId);
    if (intakeFormEntity != null){
      throw new ConflictException(String.format("Intake form has already been filled for patient %s", patientId));
    }
  }
}
