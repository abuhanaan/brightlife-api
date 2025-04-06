package com.fronteers.services;

import com.fronteers.brightlife.model.Address;
import com.fronteers.brightlife.model.EmergencyContact;
import com.fronteers.brightlife.model.Guarantor;
import com.fronteers.brightlife.model.Insurance;
import com.fronteers.brightlife.model.ParentGuardian;
import com.fronteers.brightlife.model.PassportResponse;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PaymentModeEnum;
import com.fronteers.brightlife.model.PaymentStructure;
import com.fronteers.brightlife.model.Success;
import com.fronteers.config.AwsConfig;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.repositories.AddressRepository;
import com.fronteers.repositories.EmergencyContactRepository;
import com.fronteers.repositories.GuarantorRepository;
import com.fronteers.repositories.ParentGuardianRepository;
import com.fronteers.repositories.PatientRegistrationFormRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.services.mappers.PatientMapper;
import com.fronteers.utils.FileUploadUtil;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientRegistrationFormRepository patientRegistrationFormRepository;
  private final AddressRepository addressRepository;
  private final GuarantorRepository guarantorRepository;
  private final ParentGuardianRepository parentGuardianRepository;
  private final EmergencyContactRepository emergencyContactRepository;
  private final AwsConfig awsConfig;
  private final S3Client s3Client;
  private final FileUploadUtil fileUploadUtil;


  public Success submitRegistrationForm(PatientRegistrationForm request, MultipartFile formFile,
      MultipartFile stateIssuedIdFile, MultipartFile insuranceCardFile) throws IOException {
    PatientEntity existingPatient = patientRepository.findOneByEmail(
        request.getPersonalInfo().getEmail());
    if (existingPatient != null) {
      throw new ConflictException(String.format("Patient with email: %s already exist",
          request.getPersonalInfo().getEmail()));
    }
    String patientId = generateUUID();
    PatientEntity newPatient = PatientEntity.builder()
        .patientId(patientId)
        .fullName(String.format("%s %s", request.getPersonalInfo().getLastName(),
            request.getPersonalInfo().getFirstName()))
        .email(request.getPersonalInfo().getEmail())
        .build();
    String fileUrl = fileUploadUtil.uploadFile(formFile, "regForm");
    assert stateIssuedIdFile != null;
    String stateIssuedIdUrl = fileUploadUtil.uploadFile(stateIssuedIdFile, "stateIssuedId");
    assert insuranceCardFile != null;
    String insuranceCardUrl = fileUploadUtil.uploadFile(insuranceCardFile, "insuranceCard");
    PatientRegistrationFormEntity patientRegistrationFormEntity = mapRegFormToRegFormEntity(request,
        newPatient,
        stateIssuedIdUrl, insuranceCardUrl);
    patientRegistrationFormEntity.setPatientRegFormFile(fileUrl);
    newPatient.setPatientRegistrationForm(patientRegistrationFormEntity);
    //    TODO: file upload
    //    TODO: save entity

    patientRepository.save(newPatient);
    //    TODO: send email

    return new Success(true, "Patient Registered Successfully",
        String.format("PatientId: %s", patientId));
  }

  private String generateUUID() {
    return UUID.randomUUID().toString();
  }

  private PatientRegistrationFormEntity mapRegFormToRegFormEntity(PatientRegistrationForm request,
      PatientEntity patientEntity, String stateIssuedIdUrl, String insuranceCardUrl) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = PatientRegistrationFormEntity.builder()
        .patientId(patientEntity.getPatientId())
        .patient(patientEntity)
        .firstName(request.getPersonalInfo().getFirstName())
        .lastName(request.getPersonalInfo().getLastName())
        .middleName(request.getPersonalInfo().getMiddleName())
        .gender(request.getPersonalInfo().getGender())
        .dob(Date.valueOf(request.getPersonalInfo().getDob()))
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
        .date(Date.valueOf(request.getDate()))
        .build();
    patientEntity = patientRepository.save(patientEntity);
    patientRegistrationFormEntity.setPatient(patientEntity);
    patientRegistrationFormEntity.setPatientId(patientEntity.getPatientId());
    AddressEntity addressEntity = mapAddressProperties(request.getPersonalInfo().getAddress());
    addressEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    patientRegistrationFormEntity.setAddress(addressEntity);
//    patientRegistrationFormEntity.getAddress().setPatientRegistrationForm(patientRegistrationFormEntity);
//    patientRegistrationFormEntity.setGuarantor(mapRegFormToGuarantorEntity(request.getGuarantor(), patientRegistrationFormEntity));
    GuarantorEntity guarantorEntity = mapRegFormToGuarantorEntity(request.getGuarantor(),
        patientRegistrationFormEntity, stateIssuedIdUrl, insuranceCardUrl);
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

  private List<InsuranceEntity> mapRegFormToInsuranceEntities(PaymentStructure paymentStructure,
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
          .dob(Date.valueOf(insurance.getPolicyHolder().getDob()))
          .providerName(insurance.getInsuranceProvider().getName())
          .providerPhone(insurance.getInsuranceProvider().getPhone())
          .policyId(insurance.getInsuranceProvider().getPolicyId())
          .groupNumber(insurance.getInsuranceProvider().getGroupNumber())
          .authorizationId(insurance.getInsuranceProvider().getAuthorizationId())
          .coPay(insurance.getInsuranceProvider().getCoPay())
          .coverageStartDate(Date.valueOf(insurance.getInsuranceProvider().getCoverageStartDate()))
          .coverageEndDate(Date.valueOf(insurance.getInsuranceProvider().getCoverageEndDate()))
          .address(mapAddressProperties(insurance.getInsuranceProvider().getAddress()))
          .patientRegistrationForm(patientRegistrationFormEntity)
          .build();
      insuranceEntity.getAddress().setInsurance(insuranceEntity);
      insuranceEntities.add(insuranceEntity);
    }
    return insuranceEntities;
  }

  private EmergencyContactEntity mapRegFormToEmergencyContactEntity(EmergencyContact emergency,
      PatientRegistrationFormEntity patientRegistrationFormEntity) {
    EmergencyContactEntity emergencyContactEntity = EmergencyContactEntity.builder()
        .firstName(emergency.getFirstName())
        .lastName(emergency.getLastName())
        .address(mapAddressProperties(emergency.getAddress()))
        .relationship(emergency.getRelationship())
        .homePhone(emergency.getHomePhone())
        .cellPhone(emergency.getCellPhone())
        .email(emergency.getEmail())
        .address(mapAddressProperties(emergency.getAddress()))
        .build();
    emergencyContactEntity.getAddress().setEmergencyContact(emergencyContactEntity);
    emergencyContactEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    emergencyContactRepository.save(emergencyContactEntity);
    return emergencyContactEntity;
  }

  private ParentGuardianEntity mapRegFormToParentGuardianEntity(ParentGuardian parentGuardian,
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
        .address(mapAddressProperties(parentGuardian.getAddress()))
        .build();
    parentGuardianEntity.getAddress().setParentGuardian(parentGuardianEntity);
    parentGuardianRepository.save(parentGuardianEntity);
    parentGuardianEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    return parentGuardianEntity;
  }

  private GuarantorEntity mapRegFormToGuarantorEntity(Guarantor guarantor,
      PatientRegistrationFormEntity patientRegistrationFormEntity, String stateIssuedIdUrl,
      String insuranceCardUrl) {
    GuarantorEntity guarantorEntity = GuarantorEntity.builder()
        .firstName(guarantor.getFirstName())
        .lastName(guarantor.getLastName())
        .dob(Date.valueOf(guarantor.getDob()))
        .relationship(guarantor.getRelationship())
        .address(mapAddressProperties(guarantor.getAddress()))
        .insuranceCardFile(insuranceCardUrl)
        .stateIssuedIdFile(stateIssuedIdUrl)
        .build();
    guarantorEntity.getAddress().setGuarantor(guarantorEntity);
    guarantorRepository.save(guarantorEntity);
    guarantorEntity.setPatientRegistrationForm(patientRegistrationFormEntity);
    return guarantorEntity;
  }

  public AddressEntity mapAddressProperties(Address address) {
    return addressRepository.save(AddressEntity.builder()
        .streetName(address.getStreetName())
        .city(address.getCity())
        .state(address.getState())
        .zipCode(address.getZipCode())
        .build());
  }

  public PatientRegistrationForm getRegistrationDetails(String patientId) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = checkIfPatientExist(patientId);
    return PatientMapper.mapPatientRegFormEntityToPatientRegFormDTO(patientRegistrationFormEntity);
  }

  public PatientRegistrationFormEntity checkIfPatientExist(String patientId) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = patientRegistrationFormRepository.findOneByPatientId(
        patientId);
    if (patientRegistrationFormEntity == null) {
      throw new NotFoundException(String.format("Patient with id %s does not exist", patientId));
    }
    return patientRegistrationFormEntity;
  }

  public PassportResponse upload(String name, Integer age, MultipartFile file) throws IOException {
    String bucketName = awsConfig.getBucketName();
    String fileUrl = fileUploadUtil.uploadFile(file, "RegForm");
    PassportResponse response = new PassportResponse();
    response.setAge(age);
    response.setName(name);
    response.setPassportUrl(fileUrl);
    return response;
  }
}
