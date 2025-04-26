package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.Address;
import com.fronteers.brightlife.model.EmergencyContact;
import com.fronteers.brightlife.model.Guarantor;
import com.fronteers.brightlife.model.Insurance;
import com.fronteers.brightlife.model.InsuranceProvider;
import com.fronteers.brightlife.model.MedicationConsentForm;
import com.fronteers.brightlife.model.ParentGuardian;
import com.fronteers.brightlife.model.Party;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PaymentModeEnum;
import com.fronteers.brightlife.model.PaymentStructure;
import com.fronteers.brightlife.model.PersonalInfo;
import com.fronteers.brightlife.model.PolicyHolder;
import com.fronteers.brightlife.model.ReleaseReceiveForm;
import com.fronteers.brightlife.model.SelfPayForm;
import com.fronteers.brightlife.model.TerminationPolicyForm;
import com.fronteers.brightlife.model.TreatmentConsentTelehealthInPersonTreatmentConsent;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.forms.MedicationConsentFormEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.models.entity.forms.ReleaseReceiveFormEntity;
import com.fronteers.models.entity.forms.SelfPayFormEntity;
import com.fronteers.models.entity.forms.TerminationPolicyFormEntity;
import com.fronteers.models.entity.forms.TreatmentConsentTelehealthInPersonTreatmentConsentEntity;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientDtoMapper {


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
    return patientRegistrationForm;
  }

  private static PaymentStructure mapPaymentStructureDtoProps(
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    PaymentStructure paymentStructureDTO = new PaymentStructure();
    paymentStructureDTO.setPaymentMode(paymentStructureDTO.getPaymentMode());
    if (patientRegistrationFormEntity.getPaymentMode().equals(PaymentModeEnum.INSURANCE_CARD)) {
      paymentStructureDTO.setInsurances(
          mapInsuranceEntitiesToDTO(patientRegistrationFormEntity.getInsurances()));
    }
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
    policyHolder.setDob(insuranceEntity.getDob().toLocalDate());
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
    insuranceProvider.setPhone(insuranceProvider.getPhone());
    insuranceProvider.setAuthorizationId(insuranceEntity.getAuthorizationId());
    insuranceProvider.setCoverageEndDate(insuranceEntity.getCoverageEndDate().toLocalDate());
    insuranceProvider.setCoverageStartDate(insuranceEntity.getCoverageStartDate().toLocalDate());
    insuranceProvider.setGroupNumber(insuranceEntity.getGroupNumber());
    insuranceProvider.setPolicyId(insuranceEntity.getPolicyId());
    return insuranceProvider;
  }

  private static EmergencyContact mapEmergencyDtoProps(
      EmergencyContactEntity emergencyContactEntity) {
    EmergencyContact emergencyContactDTO = new EmergencyContact();
    emergencyContactDTO.setId(emergencyContactEntity.getId());
    emergencyContactDTO.setFirstName(emergencyContactDTO.getFirstName());
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
    parentGuardianEntity.setFirstName(parentGuardianEntity.getFirstName());
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
    guarantorDTO.setDob(guarantorEntity.getDob().toLocalDate());
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
    personalInfo.setDob(patientRegistrationFormEntity.getDob().toLocalDate());
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

  public static ReleaseReceiveForm mapReleaseReceiveEntityToDto(ReleaseReceiveFormEntity entity){
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

  public static SelfPayForm mapSelfPayEntityToDto(SelfPayFormEntity entity){
    SelfPayForm dto = new SelfPayForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setDate(entity.getDate() != null ? entity.getDate().toInstant().atOffset(ZoneOffset.UTC) : null);
    dto.setFile(entity.getSelfPayFile());
    return dto;
  }

  public static TerminationPolicyForm mapTerminationPolicyEntityToDto(TerminationPolicyFormEntity entity){
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
}
