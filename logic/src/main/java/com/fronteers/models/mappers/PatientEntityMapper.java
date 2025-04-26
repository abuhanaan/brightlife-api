package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.EmergencyContact;
import com.fronteers.brightlife.model.Guarantor;
import com.fronteers.brightlife.model.Insurance;
import com.fronteers.brightlife.model.ParentGuardian;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PaymentModeEnum;
import com.fronteers.brightlife.model.PaymentStructure;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.repositories.EmergencyContactRepository;
import com.fronteers.repositories.GuarantorRepository;
import com.fronteers.repositories.ParentGuardianRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor

@Component
public class PatientEntityMapper {

  private final PatientRepository patientRepository;
  private final EmergencyContactRepository emergencyContactRepository;
  private final ParentGuardianRepository parentGuardianRepository;
  private final GuarantorRepository guarantorRepository;
  private final PatientUtils patientUtils;

  public PatientRegistrationFormEntity mapRegFormToRegFormEntity(PatientRegistrationForm request,
      PatientEntity patientEntity) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = PatientRegistrationFormEntity.builder()
        .patientId(patientEntity.getPatientId())
        .patient(patientEntity)
        .firstName(request.getPersonalInfo().getFirstName())
        .lastName(request.getPersonalInfo().getLastName())
        .middleName(request.getPersonalInfo().getMiddleName())
        .gender(request.getPersonalInfo().getGender())
        .dob(request.getPersonalInfo().getDob() != null ? Date.valueOf(
            request.getPersonalInfo().getDob()) : null)
        .maritalStatus(request.getPersonalInfo().getMaritalStatus())
        .socialSecurityNumber(request.getPersonalInfo().getSocialSecurityNumber())
        .homePhone(request.getPersonalInfo().getHomePhone())
        .cellPhone(request.getPersonalInfo().getCellPhone())
        .workPhone(request.getPersonalInfo().getWorkPhone())
        .preferredPhone(request.getPersonalInfo().getPreferredPhone())
        .appointmentReminderMode(request.getPersonalInfo().getAppointmentReminderMode())
        .email(request.getPersonalInfo().getEmail())
        .sendMsgToHomePhone(request.getPersonalInfo().getSendMsgToHomePhone())
        .sendMsgToRelative(request.getPersonalInfo().getSendMsgToRelative())
        .sendMsgToWork(request.getPersonalInfo().getSendMsgToWork())
        .sendMsgToCellPhone(request.getPersonalInfo().getSendMsgToCellPhone())
        .highestEduLevel(request.getPersonalInfo().getHighestEduLevel())
        .employmentStatus(request.getPersonalInfo().getEmploymentStatus())
        .employer(request.getPersonalInfo().getEmployer())
        .occupation(request.getPersonalInfo().getOccupation())
        .religion(request.getPersonalInfo().getReligion())
        .ethnicity(request.getPersonalInfo().getEthnicity())
        .race(request.getPersonalInfo().getRace())
        .preferredLanguage(request.getPersonalInfo().getPreferredLanguage())
        .paymentMode(request.getPaymentStructure().getPaymentMode())
        .date(request.getDate() != null ? Timestamp.from(request.getDate().toInstant()) : null)
        .build();
    patientEntity = patientRepository.save(patientEntity);
    patientRegistrationFormEntity.setPatient(patientEntity);
    patientRegistrationFormEntity.setPatientId(patientEntity.getPatientId());
    AddressEntity addressEntity = patientUtils.mapAddressProperties(
        request.getPersonalInfo().getAddress());
    addressEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    patientRegistrationFormEntity.setAddress(addressEntity);
//    patientRegistrationFormEntity.getAddress().setPatientRegistrationForm(patientRegistrationFormEntity);
//    patientRegistrationFormEntity.setGuarantor(mapRegFormToGuarantorEntity(request.getGuarantor(), patientRegistrationFormEntity));
    GuarantorEntity guarantorEntity = mapRegFormToGuarantorEntity(request.getGuarantor(),
        patientRegistrationFormEntity);
    patientRegistrationFormEntity.setGuarantor(guarantorEntity);
    patientRegistrationFormEntity.setParentGuardian(
        mapRegFormToParentGuardianEntity(request.getParentGuardian(),
            patientRegistrationFormEntity));
    patientRegistrationFormEntity.setEmergencyContact(
        mapRegFormToEmergencyContactEntity(request.getEmergency(), patientRegistrationFormEntity));
    if (patientRegistrationFormEntity.getPaymentMode().equals(PaymentModeEnum.INSURANCE_CARD)) {
      patientRegistrationFormEntity.setInsurances(
          mapRegFormToInsuranceEntities(request.getPaymentStructure(),
              patientRegistrationFormEntity));
    }
    return patientRegistrationFormEntity;
  }

  public List<InsuranceEntity> mapRegFormToInsuranceEntities(PaymentStructure paymentStructure,
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    List<InsuranceEntity> insuranceEntities = new ArrayList<>();
    for (Insurance insurance : paymentStructure.getInsurances()) {
      InsuranceEntity insuranceEntity = InsuranceEntity.builder()
          .primary(insurance.getPrimary())
          .firstName(insurance.getPolicyHolder().getFirstName())
          .lastName(insurance.getPolicyHolder().getLastName())
          .middleName(insurance.getPolicyHolder().getMiddleName())
          .relationship(insurance.getPolicyHolder().getRelationship())
          .phone(insurance.getPolicyHolder().getPhone())
          .dob(insurance.getPolicyHolder().getDob() != null ? Date.valueOf(
              insurance.getPolicyHolder().getDob()) : null)
          .providerName(insurance.getInsuranceProvider().getName())
          .providerPhone(insurance.getInsuranceProvider().getPhone())
          .policyId(insurance.getInsuranceProvider().getPolicyId())
          .groupNumber(insurance.getInsuranceProvider().getGroupNumber())
          .authorizationId(insurance.getInsuranceProvider().getAuthorizationId())
          .coPay(insurance.getInsuranceProvider().getCoPay())
          .coverageStartDate(Date.valueOf(insurance.getInsuranceProvider().getCoverageStartDate()))
          .coverageEndDate(Date.valueOf(insurance.getInsuranceProvider().getCoverageEndDate()))
          .address(patientUtils.mapAddressProperties(insurance.getInsuranceProvider().getAddress()))
          .patientRegistrationForm(patientRegistrationFormEntity)
          .build();
      insuranceEntity.getAddress().setInsurance(insuranceEntity);
      insuranceEntities.add(insuranceEntity);
    }
    return insuranceEntities;
  }

  public EmergencyContactEntity mapRegFormToEmergencyContactEntity(EmergencyContact emergency,
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    EmergencyContactEntity emergencyContactEntity = EmergencyContactEntity.builder()
        .firstName(emergency.getFirstName())
        .lastName(emergency.getLastName())
        .address(patientUtils.mapAddressProperties(emergency.getAddress()))
        .relationship(emergency.getRelationship())
        .homePhone(emergency.getHomePhone())
        .cellPhone(emergency.getCellPhone())
        .email(emergency.getEmail())
        .address(patientUtils.mapAddressProperties(emergency.getAddress()))
        .build();
    emergencyContactEntity.getAddress().setEmergencyContact(emergencyContactEntity);
    emergencyContactEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    emergencyContactRepository.save(emergencyContactEntity);
    return emergencyContactEntity;
  }

  public ParentGuardianEntity mapRegFormToParentGuardianEntity(ParentGuardian parentGuardian,
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    ParentGuardianEntity parentGuardianEntity = ParentGuardianEntity.builder()
        .firstName(parentGuardian.getFirstName())
        .lastName(parentGuardian.getLastName())
        .gender(parentGuardian.getGender())
        .maritalStatus(parentGuardian.getMaritalStatus())
        .phone(parentGuardian.getPhone())
        .email(parentGuardian.getEmail())
        .familyRole(parentGuardian.getFamilyRole())
        .employmentStatus(parentGuardian.getEmploymentStatus())
        .employer(parentGuardian.getEmployer())
        .occupation(parentGuardian.getOccupation())
        .address(patientUtils.mapAddressProperties(parentGuardian.getAddress()))
        .build();
    parentGuardianEntity.getAddress().setParentGuardian(parentGuardianEntity);
    parentGuardianRepository.save(parentGuardianEntity);
    parentGuardianEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    return parentGuardianEntity;
  }

  public GuarantorEntity mapRegFormToGuarantorEntity(Guarantor guarantor,
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    GuarantorEntity guarantorEntity = GuarantorEntity.builder()
        .firstName(guarantor.getFirstName())
        .lastName(guarantor.getLastName())
        .dob(guarantor.getDob() != null ? Date.valueOf(guarantor.getDob()) : null)
        .relationship(guarantor.getRelationship())
        .address(patientUtils.mapAddressProperties(guarantor.getAddress()))
        .insuranceCardFile(guarantor.getInsuranceCard())
        .stateIssuedIdFile(guarantor.getStateIssuedId())
        .build();
    guarantorEntity.getAddress().setGuarantor(guarantorEntity);
    guarantorRepository.save(guarantorEntity);
    guarantorEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    return guarantorEntity;
  }
}
