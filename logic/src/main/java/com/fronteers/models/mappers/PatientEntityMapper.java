package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.Address;
import com.fronteers.brightlife.model.EmergencyContact;
import com.fronteers.brightlife.model.Guarantor;
import com.fronteers.brightlife.model.ParentGuardian;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PaymentModeEnum;
import com.fronteers.brightlife.model.PaymentStructure;
import com.fronteers.brightlife.model.PersonalInfo;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PatientEntityMapper {

  private final PatientUtils patientUtils;

  public static void updatePatientBasics(PatientEntity patientEntity, PersonalInfo request) {
    if (request.getFirstName() != null) {
      patientEntity.setFirstName(request.getFirstName());
    }
    if (request.getLastName() != null) {
      patientEntity.setLastName(request.getLastName());
    }
    if (request.getMiddleName() != null) {
      patientEntity.setMiddleName(request.getMiddleName());
    }
    if (request.getEmail() != null) {
      patientEntity.setEmail(request.getEmail());
    }
  }

  public static void prepareAddressEntityForSave(AddressEntity entity, Address dto) {
    if (dto.getStreetName() != null) {
      entity.setStreetName(dto.getStreetName());
    }
    if (dto.getCity() != null) {
      entity.setCity(dto.getCity());
    }
    if (dto.getZipCode() != null) {
      entity.setZipCode(dto.getZipCode());
    }
    if (dto.getState() != null) {
      entity.setState(dto.getState());
    }
  }

  // Map personal info (used for update)
  public static void mapPersonalInfoForUpdate(PersonalInfo personalInfo,
      PatientRegistrationFormEntity form) {
    form.setFirstName(personalInfo.getFirstName());
    form.setLastName(personalInfo.getLastName());
    form.setMiddleName(personalInfo.getMiddleName());
    form.setGender(personalInfo.getGender());
    form.setDob(personalInfo.getDob() != null ? Date.valueOf(personalInfo.getDob()) : null);
    form.setMaritalStatus(personalInfo.getMaritalStatus());
    form.setSocialSecurityNumber(personalInfo.getSocialSecurityNumber());
    form.setHomePhone(personalInfo.getHomePhone());
    form.setCellPhone(personalInfo.getCellPhone());
    form.setWorkPhone(personalInfo.getWorkPhone());
    form.setPreferredPhone(personalInfo.getPreferredPhone());
    form.setAppointmentReminderMode(personalInfo.getAppointmentReminderMode());
    form.setEmail(personalInfo.getEmail());
    form.setSendMsgToHomePhone(personalInfo.getSendMsgToHomePhone());
    form.setSendMsgToRelative(personalInfo.getSendMsgToRelative());
    form.setSendMsgToWork(personalInfo.getSendMsgToWork());
    form.setSendMsgToCellPhone(personalInfo.getSendMsgToCellPhone());
    form.setHighestEduLevel(personalInfo.getHighestEduLevel());
    form.setEmploymentStatus(personalInfo.getEmploymentStatus());
    form.setEmployer(personalInfo.getEmployer());
    form.setOccupation(personalInfo.getOccupation());
    form.setReligion(personalInfo.getReligion());
    form.setEthnicity(personalInfo.getEthnicity());
    form.setRace(personalInfo.getRace());
    form.setPreferredLanguage(personalInfo.getPreferredLanguage());
  }

  // Map the entire registration form (used for create operations)
  public PatientRegistrationFormEntity mapRegFormToRegFormEntity(PatientRegistrationForm request,
      PatientEntity patientEntity) {
    PatientRegistrationFormEntity form = new PatientRegistrationFormEntity();

    // Map patient details
    mapPatientDetails(request, patientEntity, form);

    // Map address
    mapAddress(request, form);

    // Map guarantor
    if (request.getGuarantor() != null) {
      form.setGuarantor(mapGuarantor(request.getGuarantor(), form));
    }

    // Map parent/guardian
    if (request.getParentGuardian() != null) {
      form.setParentGuardian(mapParentGuardian(request.getParentGuardian(), form));
    }

    // Map emergency contact
    if (request.getEmergency() != null) {
      form.setEmergencyContact(mapEmergencyContact(request.getEmergency(), form));
    }

    // Map insurances
    if (request.getPaymentStructure() != null) {
      form.setInsurances(mapInsurances(request.getPaymentStructure(), form));
    }

    return form;
  }

  // Map only the fields that should be updated
  public void mapRegFormForUpdate(PatientRegistrationForm request,
      PatientRegistrationFormEntity existingForm) {
    // Update patient details
    if (request.getPersonalInfo() != null) {
      mapPersonalInfoForUpdate(request.getPersonalInfo(), existingForm);
    }

    // Update address
    if (request.getPersonalInfo() != null && request.getPersonalInfo().getAddress() != null) {
      mapAddressForUpdate(request.getPersonalInfo().getAddress(), existingForm);
    }

    // Update guarantor
    if (request.getGuarantor() != null) {
      existingForm.setGuarantor(mapGuarantor(request.getGuarantor(), existingForm));
    }

    // Update parent/guardian
    if (request.getParentGuardian() != null) {
      existingForm.setParentGuardian(mapParentGuardian(request.getParentGuardian(), existingForm));
    }

    // Update emergency contact
    if (request.getEmergency() != null) {
      existingForm.setEmergencyContact(mapEmergencyContact(request.getEmergency(), existingForm));
    }

    // Update insurances
    if (request.getPaymentStructure() != null) {
      existingForm.setInsurances(mapInsurances(request.getPaymentStructure(), existingForm));
    }
  }

  // Map patient details (used for create)
  private void mapPatientDetails(PatientRegistrationForm request, PatientEntity patientEntity,
      PatientRegistrationFormEntity form) {
    form.setPatientId(patientEntity.getPatientId());
    form.setPatient(patientEntity);
    form.setDate(request.getDate() != null ? Timestamp.from(request.getDate().toInstant()) : null);
    form.setPatientRegFormFile(request.getPatientRegForm());
    form.setPaymentMode(request.getPaymentStructure().getPaymentMode());
    mapPersonalInfoForUpdate(request.getPersonalInfo(), form);
  }

  // Map address (used for create)
  private void mapAddress(PatientRegistrationForm request, PatientRegistrationFormEntity form) {
    AddressEntity addressEntity = patientUtils.mapAddressProperties(
        request.getPersonalInfo().getAddress());
    addressEntity.setPatientRegistrationForm(form);
    form.setAddress(addressEntity);
  }

  // Map address (used for update)
  private void mapAddressForUpdate(Address address, PatientRegistrationFormEntity form) {
    AddressEntity addressEntity = patientUtils.mapAddressProperties(address);
    addressEntity.setPatientRegistrationForm(form);
    form.setAddress(addressEntity);
  }

  // Map guarantor
  public GuarantorEntity mapGuarantor(Guarantor guarantor, PatientRegistrationFormEntity form) {
    AddressEntity address = patientUtils.mapAddressProperties(guarantor.getAddress());
    GuarantorEntity guarantorEntity = GuarantorEntity.builder()
        .firstName(guarantor.getFirstName())
        .lastName(guarantor.getLastName())
        .dob(guarantor.getDob() != null ? Date.valueOf(guarantor.getDob()) : null)
        .relationship(guarantor.getRelationship())
        .phone(guarantor.getPhone())
        .email(guarantor.getEmail())
        .address(address)
        .insuranceCardFile(guarantor.getInsuranceCard())
        .stateIssuedIdFile(guarantor.getStateIssuedId())
        .build();
    address.setGuarantor(guarantorEntity);
    guarantorEntity.setPatientRegistrationForm(form);
    return guarantorEntity;
  }

  // Map parent/guardian
  public ParentGuardianEntity mapParentGuardian(ParentGuardian parentGuardian,
      PatientRegistrationFormEntity form) {
    AddressEntity address = patientUtils.mapAddressProperties(parentGuardian.getAddress());
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
        .address(address)
        .build();
    address.setParentGuardian(parentGuardianEntity);
    parentGuardianEntity.setPatientRegistrationForm(form);
    return parentGuardianEntity;
  }

  // Map emergency contact
  public EmergencyContactEntity mapEmergencyContact(EmergencyContact emergency,
      PatientRegistrationFormEntity form) {
    AddressEntity address = patientUtils.mapAddressProperties(emergency.getAddress());
    EmergencyContactEntity emergencyContactEntity = EmergencyContactEntity.builder()
        .firstName(emergency.getFirstName())
        .lastName(emergency.getLastName())
        .relationship(emergency.getRelationship())
        .homePhone(emergency.getHomePhone())
        .cellPhone(emergency.getCellPhone())
        .email(emergency.getEmail())
        .address(address)
        .build();
    address.setEmergencyContact(emergencyContactEntity);
    emergencyContactEntity.setPatientRegistrationForm(form);
    return emergencyContactEntity;
  }

  // Map insurances
  public List<InsuranceEntity> mapInsurances(PaymentStructure paymentStructure,
      PatientRegistrationFormEntity form) {
    if (paymentStructure.getPaymentMode().equals(PaymentModeEnum.INSURANCE_CARD) &&
        (paymentStructure.getInsurances() == null || paymentStructure.getInsurances().isEmpty())) {
      throw new BadRequestException("Insurance Data Is Required If Payment Mode Is Not Self Pay");
    }
    return paymentStructure.getInsurances().stream().map(insurance -> {
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
          .coverageStartDate(insurance.getInsuranceProvider().getCoverageStartDate() != null ?
              Date.valueOf(insurance.getInsuranceProvider().getCoverageStartDate()) : null)
          .coverageEndDate(insurance.getInsuranceProvider().getCoverageEndDate() != null ?
              Date.valueOf(insurance.getInsuranceProvider().getCoverageEndDate()) : null)
          .address(patientUtils.mapAddressProperties(insurance.getInsuranceProvider().getAddress()))
          .patientRegistrationForm(form)
          .build();
      insuranceEntity.getAddress().setInsurance(insuranceEntity);
      return insuranceEntity;
    }).collect(Collectors.toList());
  }
}
