package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.ADHDForm;
import com.fronteers.brightlife.model.Address;
import com.fronteers.brightlife.model.AlcoholDrugHistory;
import com.fronteers.brightlife.model.AnxietyDisorderForm;
import com.fronteers.brightlife.model.BasicPatientInfo;
import com.fronteers.brightlife.model.ControlledSubstanceForm;
import com.fronteers.brightlife.model.DepressionAssessmentForm;
import com.fronteers.brightlife.model.EmergencyContact;
import com.fronteers.brightlife.model.Guarantor;
import com.fronteers.brightlife.model.InitialEvaluationForm;
import com.fronteers.brightlife.model.Insurance;
import com.fronteers.brightlife.model.InsuranceProvider;
import com.fronteers.brightlife.model.IntakeForm;
import com.fronteers.brightlife.model.Medication;
import com.fronteers.brightlife.model.Medication.CategoryEnum;
import com.fronteers.brightlife.model.MedicationConsentForm;
import com.fronteers.brightlife.model.MoodDisorderAssessmentForm;
import com.fronteers.brightlife.model.NoticeOfPrivacyPracticesForm;
import com.fronteers.brightlife.model.ParentGuardian;
import com.fronteers.brightlife.model.Party;
import com.fronteers.brightlife.model.PastMarriagesInfo;
import com.fronteers.brightlife.model.PastProviders;
import com.fronteers.brightlife.model.PastTreatmentInfo;
import com.fronteers.brightlife.model.PatientInformationConsentAndFinancialPolicyForm;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PaymentStructure;
import com.fronteers.brightlife.model.PersonalInfo;
import com.fronteers.brightlife.model.Pharmacy;
import com.fronteers.brightlife.model.PolicyHolder;
import com.fronteers.brightlife.model.PrimaryCarePhysician;
import com.fronteers.brightlife.model.Referral;
import com.fronteers.brightlife.model.RelativeWithMentalIllnessOrSuicide;
import com.fronteers.brightlife.model.ReleaseReceiveForm;
import com.fronteers.brightlife.model.ScreeningForm;
import com.fronteers.brightlife.model.SelfPayForm;
import com.fronteers.brightlife.model.SubstanceUsage;
import com.fronteers.brightlife.model.TerminationPolicyForm;
import com.fronteers.brightlife.model.TreatmentConsentTelehealthInPersonTreatmentConsent;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.AlcoholDrugHistoryEntity;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.MedicationEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.PharmacyEntity;
import com.fronteers.models.entity.PrimaryCarePhysicianEntity;
import com.fronteers.models.entity.ReferralEntity;
import com.fronteers.models.entity.forms.AdhdFormEntity;
import com.fronteers.models.entity.forms.AnxietyDisorderFormEntity;
import com.fronteers.models.entity.forms.ControlledSubstanceFormEntity;
import com.fronteers.models.entity.forms.DepressionAssessmentFormEntity;
import com.fronteers.models.entity.forms.InitialEvaluationFormEntity;
import com.fronteers.models.entity.forms.IntakeFormEntity;
import com.fronteers.models.entity.forms.MedicationConsentFormEntity;
import com.fronteers.models.entity.forms.MoodDisorderAssessmentFormEntity;
import com.fronteers.models.entity.forms.NoticeOfPrivacyPracticesFormEntity;
import com.fronteers.models.entity.forms.PatientInformationConsentAndFinancialPolicyFormEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.models.entity.forms.ReleaseReceiveFormEntity;
import com.fronteers.models.entity.forms.ScreeningFormEntity;
import com.fronteers.models.entity.forms.SelfPayFormEntity;
import com.fronteers.models.entity.forms.TerminationPolicyFormEntity;
import com.fronteers.models.entity.forms.TreatmentConsentTelehealthInPersonTreatmentConsentEntity;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientDtoMapper {

  public static List<BasicPatientInfo> mapPatientListToBasicInfoDtos(
      List<PatientEntity> patientEntities) {
    return patientEntities.stream().map(patientEntity -> {
      BasicPatientInfo dto = new BasicPatientInfo();
      dto.setId(patientEntity.getId());
      dto.setPatientId(UUID.fromString(patientEntity.getPatientId()));
      dto.setEmail(patientEntity.getEmail());
      dto.setFirstName(patientEntity.getFirstName());
      dto.setLastName(patientEntity.getLastName());
      PatientRegistrationFormEntity regForm = patientEntity.getPatientRegistrationForm();
      if (regForm != null) {
        dto.setDob(regForm.getDob() != null ? regForm.getDob().toLocalDate() : null);
        dto.setGender(regForm.getGender());
        dto.setPhone(regForm.getCellPhone());
        return dto;
      }
      return dto;
    }).toList();
  }

  public static PatientRegistrationForm mapPatientRegFormEntityToPatientRegFormDTO(
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    PatientRegistrationForm patientRegistrationForm = new PatientRegistrationForm();
    patientRegistrationForm.setId(patientRegistrationFormEntity.getId());
    patientRegistrationForm.setPatientId(
        UUID.fromString(patientRegistrationFormEntity.getPatientId()));
    patientRegistrationForm.setPersonalInfo(mapPersonalInfoDtoProps(patientRegistrationFormEntity));
    patientRegistrationForm.setGuarantor(
        mapGuarantorEntityToGuarantorDto(patientRegistrationFormEntity.getGuarantor()));
    patientRegistrationForm.setParentGuardian(
        mapParentGuardianDtoProps(patientRegistrationFormEntity.getParentGuardian()));
    patientRegistrationForm.setEmergency(
        mapEmergencyDtoProps(patientRegistrationFormEntity.getEmergencyContact()));
    patientRegistrationForm.setPaymentStructure(
        mapPaymentStructureDtoProps(patientRegistrationFormEntity));
    patientRegistrationForm.setDate(patientRegistrationFormEntity.getDate() != null ?
        patientRegistrationFormEntity.getDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    patientRegistrationForm.setPatientRegForm(
        patientRegistrationFormEntity.getPatientRegFormFile());
    return patientRegistrationForm;
  }

  private static PaymentStructure mapPaymentStructureDtoProps(
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    PaymentStructure paymentStructureDTO = new PaymentStructure();
    paymentStructureDTO.setPaymentMode(patientRegistrationFormEntity.getPaymentMode());
//    if (patientRegistrationFormEntity.getPaymentMode().equals(PaymentModeEnum.INSURANCE_CARD)) {
//      paymentStructureDTO.setInsurances(
//          mapInsuranceEntitiesToDTO(patientRegistrationFormEntity.getInsurances()));
//    }
    paymentStructureDTO.setInsurances(
        mapInsuranceEntitiesToDTO(patientRegistrationFormEntity.getInsurances()));
    return paymentStructureDTO;
  }

  private static List<Insurance> mapInsuranceEntitiesToDTO(
      List<InsuranceEntity> insuranceEntities) {
    List<Insurance> insurances = new ArrayList<>();
    for (InsuranceEntity insuranceEntity : insuranceEntities) {
      Insurance insurance = new Insurance();
      insurance.setId(insuranceEntity.getId());
      insurance.setInsuranceProvider(mapInsuranceProviderDTO(insuranceEntity));
      insurance.setPrimary(insuranceEntity.getPrimary());
      insurance.setPolicyHolder(mapInsurancePolicyHolderDTO(insuranceEntity));
      insurances.add(insurance);
    }
    return insurances;
  }

  private static PolicyHolder mapInsurancePolicyHolderDTO(InsuranceEntity insuranceEntity) {
    PolicyHolder policyHolder = new PolicyHolder();
    policyHolder.setDob(insuranceEntity.getDob() != null ?
        insuranceEntity.getDob().toLocalDate() : null);
    policyHolder.setFirstName(insuranceEntity.getFirstName());
    policyHolder.setLastName(insuranceEntity.getLastName());
    policyHolder.setPhone(insuranceEntity.getPhone());
    policyHolder.setRelationship(insuranceEntity.getRelationship());
    policyHolder.setMiddleName(insuranceEntity.getMiddleName());
    return policyHolder;
  }

  private static InsuranceProvider mapInsuranceProviderDTO(InsuranceEntity insuranceEntity) {
    InsuranceProvider insuranceProvider = new InsuranceProvider();
    insuranceProvider.setAddress(mapAddressEntityToAddressDto(insuranceEntity.getAddress()));
    insuranceProvider.setCoPay(insuranceEntity.getCoPay());
    insuranceProvider.setName(insuranceEntity.getProviderName());
    insuranceProvider.setPhone(insuranceEntity.getPhone());
    insuranceProvider.setAuthorizationId(insuranceEntity.getAuthorizationId());
    insuranceProvider.setCoverageEndDate(insuranceEntity.getCoverageEndDate() != null ?
        insuranceEntity.getCoverageEndDate().toLocalDate() : null);
    insuranceProvider.setCoverageStartDate(insuranceEntity.getCoverageStartDate() != null ?
        insuranceEntity.getCoverageStartDate().toLocalDate() : null);
    insuranceProvider.setGroupNumber(insuranceEntity.getGroupNumber());
    insuranceProvider.setPolicyId(insuranceEntity.getPolicyId());
    return insuranceProvider;
  }

  private static EmergencyContact mapEmergencyDtoProps(
      EmergencyContactEntity emergencyContactEntity) {
    EmergencyContact emergencyContactDTO = new EmergencyContact();
    emergencyContactDTO.setId(emergencyContactEntity.getId());
    emergencyContactDTO.setFirstName(emergencyContactEntity.getFirstName());
    emergencyContactDTO.setLastName(emergencyContactEntity.getLastName());
    emergencyContactDTO.setEmail(emergencyContactEntity.getEmail());
    emergencyContactDTO.setCellPhone(emergencyContactEntity.getCellPhone());
    emergencyContactDTO.setHomePhone(emergencyContactEntity.getHomePhone());
    emergencyContactDTO.setRelationship(emergencyContactEntity.getRelationship());
    emergencyContactDTO.setAddress(
        mapAddressEntityToAddressDto(emergencyContactEntity.getAddress()));
    return emergencyContactDTO;
  }

  private static ParentGuardian mapParentGuardianDtoProps(
      ParentGuardianEntity parentGuardianEntity) {
    ParentGuardian parentGuardianDTO = new ParentGuardian();
    parentGuardianDTO.setId(parentGuardianEntity.getId());
    parentGuardianDTO.setFirstName(parentGuardianEntity.getFirstName());
    parentGuardianDTO.setLastName(parentGuardianEntity.getLastName());
    parentGuardianDTO.setGender(parentGuardianEntity.getGender());
    parentGuardianDTO.setMaritalStatus(parentGuardianEntity.getMaritalStatus());
    parentGuardianDTO.setPhone(parentGuardianEntity.getPhone());
    parentGuardianDTO.setEmail(parentGuardianEntity.getEmail());
    parentGuardianDTO.setFamilyRole(parentGuardianEntity.getFamilyRole());
    parentGuardianDTO.setEmploymentStatus(parentGuardianEntity.getEmploymentStatus());
    parentGuardianDTO.setEmployer(parentGuardianEntity.getEmployer());
    parentGuardianDTO.setOccupation(parentGuardianEntity.getOccupation());
    parentGuardianDTO.setAddress(mapAddressEntityToAddressDto(parentGuardianEntity.getAddress()));
    return parentGuardianDTO;
  }

  private static Guarantor mapGuarantorEntityToGuarantorDto(GuarantorEntity guarantorEntity) {
    Guarantor guarantorDTO = new Guarantor();
    guarantorDTO.setId(guarantorEntity.getId());
    guarantorDTO.setFirstName(guarantorEntity.getFirstName());
    guarantorDTO.setLastName(guarantorEntity.getLastName());
    guarantorDTO.setDob(
        guarantorEntity.getDob() != null ? guarantorEntity.getDob().toLocalDate() : null);
    guarantorDTO.setRelationship(guarantorEntity.getRelationship());
    guarantorDTO.setAddress(mapAddressEntityToAddressDto(guarantorEntity.getAddress()));
    guarantorDTO.setPhone(guarantorEntity.getPhone());
    guarantorDTO.setEmail(guarantorEntity.getEmail());
    guarantorDTO.setStateIssuedId(guarantorEntity.getStateIssuedIdFile());
    guarantorDTO.setInsuranceCard(guarantorEntity.getInsuranceCardFile());
    return guarantorDTO;
  }

  private static PersonalInfo mapPersonalInfoDtoProps(
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setFirstName(patientRegistrationFormEntity.getFirstName());
    personalInfo.setLastName(patientRegistrationFormEntity.getLastName());
    personalInfo.setMiddleName(patientRegistrationFormEntity.getMiddleName());
    personalInfo.setGender(patientRegistrationFormEntity.getGender());
    personalInfo.setDob(patientRegistrationFormEntity.getDob() != null ?
        patientRegistrationFormEntity.getDob().toLocalDate() : null);
    personalInfo.maritalStatus(patientRegistrationFormEntity.getMaritalStatus());
    personalInfo.setSocialSecurityNumber(patientRegistrationFormEntity.getSocialSecurityNumber());
    personalInfo.setHomePhone(patientRegistrationFormEntity.getHomePhone());
    personalInfo.setCellPhone(patientRegistrationFormEntity.getCellPhone());
    personalInfo.setWorkPhone(patientRegistrationFormEntity.getWorkPhone());
    personalInfo.setPreferredPhone(patientRegistrationFormEntity.getPreferredPhone());
    personalInfo.setAppointmentReminderMode(
        patientRegistrationFormEntity.getAppointmentReminderMode());
    personalInfo.setEmail(patientRegistrationFormEntity.getEmail());
    personalInfo.setSendMsgToHomePhone(patientRegistrationFormEntity.getSendMsgToHomePhone());
    personalInfo.setSendMsgToRelative(patientRegistrationFormEntity.getSendMsgToRelative());
    personalInfo.setSendMsgToWork(patientRegistrationFormEntity.getSendMsgToWork());
    personalInfo.setSendMsgToCellPhone(patientRegistrationFormEntity.getSendMsgToCellPhone());
    personalInfo.setAddress(
        mapAddressEntityToAddressDto(patientRegistrationFormEntity.getAddress()));
    personalInfo.setHighestEduLevel(patientRegistrationFormEntity.getHighestEduLevel());
    personalInfo.setEmploymentStatus(patientRegistrationFormEntity.getEmploymentStatus());
    personalInfo.setEmployer(patientRegistrationFormEntity.getEmployer());
    personalInfo.setOccupation(patientRegistrationFormEntity.getOccupation());
    personalInfo.setReligion(patientRegistrationFormEntity.getReligion());
    personalInfo.setEthnicity(patientRegistrationFormEntity.getEthnicity());
    personalInfo.setRace(patientRegistrationFormEntity.getRace());
    personalInfo.setPreferredLanguage(patientRegistrationFormEntity.getPreferredLanguage());
    return personalInfo;
  }

  public static Address mapAddressEntityToAddressDto(AddressEntity addressEntity) {
    Address addressDTO = new Address();
    addressDTO.setId(addressEntity.getId());
    addressDTO.setCity(addressEntity.getCity());
    addressDTO.streetName(addressEntity.getStreetName());
    addressDTO.setState(addressEntity.getState());
    addressDTO.setZipCode(addressEntity.getZipCode());
    return addressDTO;
  }

  public static TreatmentConsentTelehealthInPersonTreatmentConsent mapTreatmentConsentTelehealthEntityToDto(
      TreatmentConsentTelehealthInPersonTreatmentConsentEntity entity) {
    TreatmentConsentTelehealthInPersonTreatmentConsent dto = new TreatmentConsentTelehealthInPersonTreatmentConsent();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setGuardianName(entity.getGuardianName());
    dto.setGuardianSignDate(entity.getGuardianSignDate() != null ?
        entity.getGuardianSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setPatientSignDate(entity.getPatientSignDate() != null ?
        entity.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getTreatmentConsentTelehealthInPersonFile());
    return dto;
  }

  public static ReleaseReceiveForm mapReleaseReceiveEntityToDto(ReleaseReceiveFormEntity entity) {
    ReleaseReceiveForm dto = new ReleaseReceiveForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setIsMinor(entity.getIsMinor());
    dto.setReceiveHealthInfo(entity.getReceiveHealthInfo());
    dto.setReceiveHealthInfo(entity.getReceiveHealthInfo());
    dto.setExchangeHealthInfo(entity.getExchangeHealthInfo());
    dto.setParty(entity.getParties() != null ? entity.getParties().stream().map(partyEntity -> {
      Party partyDto = new Party();
      partyDto.setId(partyEntity.getId());
      partyDto.setIsReceive(partyEntity.getIsReceive());
      partyDto.setName(partyEntity.getName());
      partyDto.setPhoneNumber(partyEntity.getPhoneNumber());
      partyDto.setFax(partyEntity.getFax());
      partyDto.setAddress(PatientDtoMapper.mapAddressEntityToAddressDto(partyEntity.getAddress()));
      return partyDto;
    }).toList() : null);
    dto.setDisclosurePurpose(entity.getDisclosurePurposes() != null ?
        entity.getDisclosurePurposes().stream().toList() : null);
    dto.setInfoTypeToRelease(entity.getInfoTypeToRelease() != null ?
        entity.getInfoTypeToRelease().stream().toList() : null);
    dto.setGuardianName(entity.getGuardianName());
    dto.setReleaseReceive(entity.getReleaseReceiveFile());
    dto.setPatientSignDate(entity.getPatientSignDate() != null ?
        entity.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setGuardianSignDate(entity.getGuardianSignDate() != null ?
        entity.getGuardianSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getReleaseReceiveFile());
    return dto;
  }

  public static SelfPayForm mapSelfPayEntityToDto(SelfPayFormEntity entity) {
    SelfPayForm dto = new SelfPayForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setDate(
        entity.getDate() != null ? entity.getDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getSelfPayFile());
    return dto;
  }

  public static TerminationPolicyForm mapTerminationPolicyEntityToDto(
      TerminationPolicyFormEntity entity) {
    TerminationPolicyForm dto = new TerminationPolicyForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setWitnessName(entity.getWitnessName());
    dto.setWitnessSignDate(entity.getWitnessSignDate() != null ?
        entity.getWitnessSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setPatientSignDate(entity.getPatientSignDate() != null ?
        entity.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getTerminationPolicyFile());
    return dto;
  }

  public static MedicationConsentForm mapMedConsentEntityToDto(MedicationConsentFormEntity entity) {
    MedicationConsentForm dto = new MedicationConsentForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setIsMinor(entity.getIsMinor());
    dto.setPatientSignDate(entity.getPatientSignDate() != null ?
        entity.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setGuardianName(entity.getGuardianName());
    dto.setPatientGuardianRelationship(entity.getPatientGuardianRelationship());
    dto.setGuardianSignDate(entity.getGuardianSignDate() != null ?
        entity.getGuardianSignDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getMedicationConsentFile());
    return dto;
  }

  public static PatientInformationConsentAndFinancialPolicyForm
  mapPatientInfoConsentAndFinPolicyFormEntityToDto(
      PatientInformationConsentAndFinancialPolicyFormEntity entity) {
    PatientInformationConsentAndFinancialPolicyForm dto = new PatientInformationConsentAndFinancialPolicyForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setDate(
        entity.getDate() != null ? entity.getDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getPatientInfoFinFile());
    return dto;
  }

  public static NoticeOfPrivacyPracticesForm mapNoticeOfPrivacyEntityToDto(
      NoticeOfPrivacyPracticesFormEntity entity) {
    NoticeOfPrivacyPracticesForm dto = new NoticeOfPrivacyPracticesForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setDate(
        entity.getDate() != null ? entity.getDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setNoticeEffectDate(entity.getNoticeEffectDate().toLocalDate());
    dto.setFile(entity.getNoticeOfPrivacyPractices());
    return dto;
  }

  public static InitialEvaluationForm mapInitialEvaluationEntityToDto(
      InitialEvaluationFormEntity initialEvaluationEntity) {
    InitialEvaluationForm initialEvaluationFormDto = new InitialEvaluationForm();
    initialEvaluationFormDto.setId(initialEvaluationEntity.getId());
    initialEvaluationFormDto.setPatientId(UUID.fromString(initialEvaluationEntity.getPatientId()));
    initialEvaluationFormDto.setDate(initialEvaluationEntity.getDate() != null ?
        initialEvaluationEntity.getDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    initialEvaluationFormDto.setFile(initialEvaluationEntity.getInitialEvaluationFile());
    initialEvaluationFormDto.setPharmacy(
        mapPharmacyEntityToDto(initialEvaluationEntity.getPharmacy()));
    initialEvaluationFormDto.setPrimaryCarePhysician(
        mapPrimaryCarePhysician(initialEvaluationEntity.getPrimaryCarePhysician()));
    return initialEvaluationFormDto;
  }

  public static ADHDForm mapAdhdEntityToDto(AdhdFormEntity adhdFormEntity) {
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

  public static AnxietyDisorderForm mapAnxietyDisorderEntityToDto(
      AnxietyDisorderFormEntity entity) {
    AnxietyDisorderForm anxietyDisorderDto = new AnxietyDisorderForm();
    anxietyDisorderDto.setId(entity.getId());
    anxietyDisorderDto.setPatientId(UUID.fromString(entity.getPatientId()));
    anxietyDisorderDto.setNervousRate(entity.getNervousRate());
    anxietyDisorderDto.setControlOverWorry(entity.getControlOverWorry());
    anxietyDisorderDto.setExcessiveWorry(entity.getExcessiveWorry());
    anxietyDisorderDto.setRelaxTrouble(entity.getRelaxTrouble());
    anxietyDisorderDto.setRestlessness(entity.getRestlessness());
    anxietyDisorderDto.setAnnoyanceRate(entity.getAnnoyanceRate());
    anxietyDisorderDto.setFrightRate(entity.getFrightRate());
    anxietyDisorderDto.setLifeInfluenceSummary(anxietyDisorderDto.getLifeInfluenceSummary());
    return anxietyDisorderDto;
  }

  public static ControlledSubstanceForm mapControlledSubstanceEntityToDto(
      ControlledSubstanceFormEntity controlledSubstanceFormEntity) {
    ControlledSubstanceForm controlledSubstanceDto = new ControlledSubstanceForm();
    controlledSubstanceDto.setId(controlledSubstanceFormEntity.getId());
    controlledSubstanceDto.setPatientId(
        UUID.fromString(controlledSubstanceFormEntity.getPatientId()));
    controlledSubstanceDto.setIsMinor(controlledSubstanceFormEntity.getIsMinor());
    controlledSubstanceDto.setPatientSignDate(
        controlledSubstanceFormEntity.getPatientSignDate() != null ?
            controlledSubstanceFormEntity.getPatientSignDate().toInstant().atOffset(ZoneOffset.UTC)
            : null);
    controlledSubstanceDto.setGuardianName(controlledSubstanceFormEntity.getGuardianName());
    controlledSubstanceDto.setPatientGuardianRelationship(
        controlledSubstanceFormEntity.getPatientGuardianRelationship());
    controlledSubstanceDto.setGuardianSignDate(
        controlledSubstanceFormEntity.getGuardianSignDate() != null ?
            controlledSubstanceFormEntity.getGuardianSignDate().toInstant().atOffset(ZoneOffset.UTC)
            : null);
    controlledSubstanceDto.setFile(controlledSubstanceFormEntity.getControlledSubstanceFile());
    return controlledSubstanceDto;
  }

  public static DepressionAssessmentForm mapDepressionAssessmentEntityToDto(
      DepressionAssessmentFormEntity depAssessmentFormEntity) {
    DepressionAssessmentForm depAssessmentDto = new DepressionAssessmentForm();
    depAssessmentDto.setId(depAssessmentFormEntity.getId());
    depAssessmentDto.setPatientId(UUID.fromString(depAssessmentFormEntity.getPatientId()));
    depAssessmentDto.setPleasureInterest(depAssessmentFormEntity.getPleasureInterest());
    depAssessmentDto.setDepressionRate(depAssessmentFormEntity.getDepressionRate());
    depAssessmentDto.setSleepRate(depAssessmentFormEntity.getSleepRate());
    depAssessmentDto.setFatigueRate(depAssessmentFormEntity.getFatigueRate());
    depAssessmentDto.setAppetiteRate(depAssessmentFormEntity.getAppetiteRate());
    depAssessmentDto.setFailureRate(depAssessmentFormEntity.getFailureRate());
    depAssessmentDto.setConcentrationRate(depAssessmentFormEntity.getConcentrationRate());
    depAssessmentDto.setRestlessnessRate(depAssessmentFormEntity.getRestlessnessRate());
    depAssessmentDto.setSuicideThought(depAssessmentFormEntity.getSuicideThought());
    return depAssessmentDto;
  }

  public static IntakeForm mapIntakeEntityToDto(IntakeFormEntity intakeFormEntity) {
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

  public static MoodDisorderAssessmentForm mapMoodDisorderEntityToDto(
      MoodDisorderAssessmentFormEntity mdafEntity) {
    MoodDisorderAssessmentForm dto = new MoodDisorderAssessmentForm();
    dto.setId(mdafEntity.getId());
    dto.setPatientId(UUID.fromString(mdafEntity.getPatientId()));
    dto.setHyperFeeling(mdafEntity.getHyperFeeling());
    dto.setIsIrritable(mdafEntity.getIsIrritable());
    dto.setIsOverConfident(mdafEntity.getIsOverConfident());
    dto.setLessSleep(mdafEntity.getLessSleep());
    dto.setTalkMore(mdafEntity.getTalkMore());
    dto.setPacedThoughts(mdafEntity.getPacedThoughts());
    dto.setEasyDistraction(mdafEntity.getEasyDistraction());
    dto.setOverEnergetic(mdafEntity.getOverEnergetic());
    dto.setOverActive(mdafEntity.getOverActive());
    dto.setOverSocial(mdafEntity.getOverSocial());
    dto.setSexaholic(mdafEntity.getSexaholic());
    dto.setOverFoolish(mdafEntity.getOverFoolish());
    dto.setOverSpending(mdafEntity.getOverSpending());
    dto.setSameTimeOccurrence(mdafEntity.getSameTimeOccurrence());
    dto.setInfluenceOnLife(mdafEntity.getInfluenceOnLife());
    dto.setIsRelativeWithBipolar(mdafEntity.getIsRelativeWithBipolar());
    dto.setIsBipolarDiagnosed(mdafEntity.getIsBipolarDiagnosed());
    return dto;
  }

  public static ScreeningForm mapScreeningEntityToDto(ScreeningFormEntity entity) {
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

  private static void setReferralToDto(ScreeningForm dto, ReferralEntity referralEntity) {
    Referral referralDto = new Referral();
    referralDto.setId(referralEntity.getId());
    referralEntity.setSource(referralEntity.getSource());
    referralDto.setTherapist(referralEntity.getTherapist());
    referralDto.setFirstName(referralEntity.getFirstName());
    referralDto.setMiddleName(referralEntity.getMiddleName());
    referralDto.setLastName(referralEntity.getLastName());
    referralDto.setPhone(referralEntity.getPhone());
    referralDto.setAddress(
        PatientDtoMapper.mapAddressEntityToAddressDto(referralEntity.getAddress()));
    dto.setReferral(referralDto);
  }

  private static void setAlcoholDrugHistory(AlcoholDrugHistoryEntity adhEntity,
      IntakeForm intakeFormDto) {
    AlcoholDrugHistory alcoholDrugHistoryDto = new AlcoholDrugHistory();
    alcoholDrugHistoryDto.setId(adhEntity.getId());
    alcoholDrugHistoryDto.setUsageFrequency(adhEntity.getUsageFrequency());
    alcoholDrugHistoryDto.setBrand(adhEntity.getBrand());
    alcoholDrugHistoryDto.setLastUsed(adhEntity.getLastUsed());
    alcoholDrugHistoryDto.setDrinkGuiltCheck(adhEntity.getDrinkGuiltCheck());
    alcoholDrugHistoryDto.setSubstanceUsages(
        adhEntity.getSubstanceUsages().stream().map(substanceUsageEntity -> {
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
    alcoholDrugHistoryDto.setPastTreatmentInfo(
        adhEntity.getPastTreatments().stream().map(pastTreatmentEntity -> {
          PastTreatmentInfo pastTreatment = new PastTreatmentInfo();
          pastTreatment.setId(pastTreatmentEntity.getId());
          pastTreatment.setDate(pastTreatmentEntity.getDate() != null ?
              pastTreatmentEntity.getDate().toLocalDate() : null
          );
          pastTreatment.setDrugTreated(pastTreatmentEntity.getDrugTreated());
          pastTreatment.setIsTreatmentCompleted(pastTreatmentEntity.getIsTreatmentCompleted());
          pastTreatment.setFacility(pastTreatmentEntity.getFacility());
          return pastTreatment;
        }).toList());
    alcoholDrugHistoryDto.setIsPastStepRecoveryParticipant(
        adhEntity.getIsPastStepRecoveryParticipant());
    alcoholDrugHistoryDto.setIsCurrentStepRecoveryParticipant(
        adhEntity.getIsCurrentStepRecoveryParticipant());
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

  private static void processDtoMedications(IntakeForm dto, List<MedicationEntity> medications) {
    List<Medication> currentMedicationDtos = new ArrayList<>();
    List<Medication> pastMedicationDtos = new ArrayList<>();
    for (MedicationEntity medicationEntity : medications) {
      Medication medicationDto = new Medication();
      setMedicationDtoProps(medicationDto, medicationEntity);
      if (medicationEntity.getIsCurrent()) {
        currentMedicationDtos.add(medicationDto);
      } else {
        pastMedicationDtos.add(medicationDto);
      }
    }
    dto.setCurrentMedications(currentMedicationDtos);
    dto.setPastMedications(pastMedicationDtos);
  }

  private static void setMedicationDtoProps(Medication medicationDto,
      MedicationEntity medicationEntity) {
    medicationDto.setMedication(medicationEntity.getMedication());
    medicationDto.setId(medicationEntity.getId());
    medicationDto.setCategory(
        medicationEntity.getIsCurrent() ? CategoryEnum.CURRENT : CategoryEnum.PAST);
    medicationDto.setPrescription(medicationEntity.getPrescription());
    medicationDto.setConditionTreated(medicationEntity.getConditionTreated());
    medicationDto.setUsageInstruction(medicationEntity.getInstruction());
  }

  private static Pharmacy mapPharmacyEntityToDto(PharmacyEntity pharmacyEntity) {
    Pharmacy pharmacyDto = new Pharmacy();
    pharmacyDto.setId(pharmacyEntity.getId());
    pharmacyDto.setName(pharmacyEntity.getName());
    pharmacyDto.setPhone(pharmacyEntity.getPhone());
    pharmacyDto.setAddress(
        PatientDtoMapper.mapAddressEntityToAddressDto(pharmacyEntity.getAddress()));
    return pharmacyDto;
  }

  private static PrimaryCarePhysician mapPrimaryCarePhysician(
      PrimaryCarePhysicianEntity pcpEntity) {
    PrimaryCarePhysician pcpDto = new PrimaryCarePhysician();
    pcpDto.setHavePcp(pcpEntity.getHavePcp());
    pcpDto.setId(pcpEntity.getId());
    pcpDto.setFax(pcpEntity.getFax());
    pcpDto.setName(pcpEntity.getName());
    pcpDto.setPhone(pcpEntity.getPhone());
    pcpDto.setAddress(PatientDtoMapper.mapAddressEntityToAddressDto(pcpEntity.getAddress()));
    return pcpDto;
  }
}
