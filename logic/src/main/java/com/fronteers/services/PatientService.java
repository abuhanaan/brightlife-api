package com.fronteers.services;

import com.fronteers.brightlife.model.BasicPatientInfo;
import com.fronteers.brightlife.model.ConsentForm;
import com.fronteers.brightlife.model.ConsentTypeEnum;
import com.fronteers.brightlife.model.EmergencyContact;
import com.fronteers.brightlife.model.FileUploadResponse;
import com.fronteers.brightlife.model.Forms;
import com.fronteers.brightlife.model.Guarantor;
import com.fronteers.brightlife.model.IdGenerationResponse;
import com.fronteers.brightlife.model.PaginatedPatients;
import com.fronteers.brightlife.model.ParentGuardian;
import com.fronteers.brightlife.model.Patient;
import com.fronteers.brightlife.model.PatientIdValidationResponse;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PatientSearch;
import com.fronteers.brightlife.model.PaymentStructure;
import com.fronteers.brightlife.model.PersonalInfo;
import com.fronteers.brightlife.model.ProgramTypeEnum;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.exceptions.ProcessingException;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.QPatientEntity;
import com.fronteers.models.entity.forms.ConsentFormEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.models.mappers.ConsentFormMapper;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.models.mappers.PatientEntityMapper;
import com.fronteers.repositories.ConsentFormRepository;
import com.fronteers.repositories.InsuranceRepository;
import com.fronteers.repositories.PatientRegistrationFormRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.UserRepository;
import com.fronteers.utils.CopyBeanUtil;
import com.fronteers.utils.PatientUtils;
import com.querydsl.core.BooleanBuilder;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientEntityMapper patientEntityMapper;
  private final PatientRegistrationFormRepository patientRegistrationFormRepository;
  private final InsuranceRepository insuranceRepository;
  private final PatientUtils patientUtils;
  private final FileUploadService fileUploadService;
  private final EmailService emailService;
  private final ConsentFormRepository consentRepository;
  private final UserRepository userRepository;
  @Value("${fe.base.url}")
  private String feBaseUrl;

  @Transactional
  public Success submitRegistrationForm(PatientRegistrationForm request) {
    patientUtils.confirmPatientUniqueness(request.getPersonalInfo().getEmail());
    PatientEntity newPatient = PatientEntity.builder()
        .patientId(request.getPatientId().toString())
        .email(request.getPersonalInfo().getEmail())
        .firstName(request.getPersonalInfo().getFirstName())
        .lastName(request.getPersonalInfo().getLastName())
        .middleName(request.getPersonalInfo().getMiddleName())
        .build();
    PatientRegistrationFormEntity patientRegistrationFormEntity = patientEntityMapper.mapRegFormToRegFormEntity(
        request, newPatient);
    Set<ProgramTypeEnum> programs = new HashSet<>();
    programs.add(ProgramTypeEnum.OMHC);
    newPatient.setPrograms(programs);
    patientRegistrationFormRepository.save(patientRegistrationFormEntity);
    Map<String, Object> variables = Map.of(
        "name", newPatient.getFirstName() + " " + newPatient.getLastName(),
        "email", newPatient.getEmail(),
        "omhcForm", feBaseUrl + "/forms/consent/omhc-consent/" + request.getPatientId()
    );
    try {
      emailService.sendEmail("fronteers.dev@gmail.com", "New Patient Registration", "admin-notification", variables);
      emailService.sendEmail(patientRegistrationFormEntity.getEmail(), "Patient Onboarding Message", "patient-onboarding", variables);
    } catch (MessagingException e) {
      log.warn(e.getMessage());
      throw new BadRequestException(e.getMessage());
    }
    return new Success(true, "Patient Registration Form Submitted Successfully",
        String.format("PatientId: %s", newPatient.getPatientId()));
  }

  public Success enrollProgram(String patientId, ProgramTypeEnum programType) {
    PatientEntity patient = patientUtils.checkIfPatientExists(patientId);
    if (patient.getPrograms().contains(programType)) {
      throw new ConflictException(String.format("Patient already enrolled in %s", programType));
    }
    patient.getPrograms().add(programType);
    patientRepository.save(patient);
    sendProgramConsentFormToPatient(patient, programType);
    return new Success(true, "Program Enrollment Successful",
        String.format("Patient enrolled in %s successfully", programType));
  }

  private void sendProgramConsentFormToPatient(PatientEntity patient, ProgramTypeEnum programType) {
    Map<String, Object> variables = new HashMap<>(Map.of(
        "name", patient.getFirstName() + " " + patient.getLastName(),
        "email", patient.getEmail(),
        "program", programType.getValue()
    ));
    String formLink = "";
    if (programType.equals(ProgramTypeEnum.PRP_ADULTS)){
      formLink = feBaseUrl + "/forms/consent/prp-consent/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.ASAM_0_5_EARLY_INTERVENTION)){
      formLink = feBaseUrl + "/forms/consent/asam-0.5-early-intervention/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.ASAM_LEVEL_1_0_OUTPATIENT_TREATMENT)){
      formLink = feBaseUrl + "/forms/consent/asam-1.0-outpatient-treatment/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.ASAM_OUTPATIENT_TREATMENT_LEVEL_2_1)){
      formLink = feBaseUrl + "/forms/consent/asam-2.1-outpatient-treatment/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.ASAM_LEVEL_OUTPATIENT_TREATMENT_2_5)){
      formLink = feBaseUrl + "/forms/consent/asam-2.5-outpatient-treatment/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum._3_1_COMMUNITY_HOUSING)){
      formLink = feBaseUrl + "/forms/consent/community-housing/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.DUI_DWI)){
      formLink = feBaseUrl + "/forms/consent/dui-dwi/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.SUPPORTED_EMPLOYMENT)){
      formLink = feBaseUrl + "/forms/consent/supported-employment/" + patient.getPatientId();
    }
    else if (programType.equals(ProgramTypeEnum.MEDICATION_ASSISTED_WEIGHTLOSS)){
      formLink = feBaseUrl + "/forms/consent/medication-assisted-weight-loss/" + patient.getPatientId();
    } else {
      throw new BadRequestException("Unsupported Program Type");
    }
    variables.put("formLink", formLink);
    try {
      emailService.sendEmail("fronteers.dev@gmail.com", "Patient Program Enrollment Notification", "admin-notification", variables);
      emailService.sendEmail(patient.getEmail(), "Patient Program Enrollment Notification", "patient-program-enrollment-template", variables);
    } catch (MessagingException e) {
      log.warn(e.getMessage());
      throw new BadRequestException(e.getMessage());
    }
  }

  public Success uploadConsentForm(String patientId, OffsetDateTime patientSignDate,
      ConsentTypeEnum consentType, MultipartFile file) throws IOException {
    PatientEntity patient = patientUtils.checkIfPatientExists(patientId);
    checkIfConsentFormAlreadyExists(patient, consentType);
    FileUploadResponse uploadResponse = fileUploadService.upload(consentType.name(), patientId, file);
    ConsentFormEntity newConsentForm = ConsentFormEntity.builder()
        .consentType(consentType)
        .file(uploadResponse.getFileUrl())
        .patient(patient)
        .patientSignDate(Timestamp.from(patientSignDate.toInstant()))
        .build();
    consentRepository.save(newConsentForm);
    Map<String, Object> variables = new HashMap<>(Map.of(
        "name", patient.getFirstName() + " " + patient.getLastName(),
        "email", patient.getEmail(),
        "formType", consentType,
        "formUrl", uploadResponse.getFileUrl(),
        "intakeForm", feBaseUrl + "/forms/intake-form/" + patient.getPatientId()
    ));
    notifyAdminAndPatientAboutConsentForm(patient, variables, consentType);
    if (consentType.getValue().equals(ConsentTypeEnum.OMHC_CONSENT.getValue())){
      sendIntakeFormToPatient(patient, variables);
    }
    return  new Success(true, "Form Uploaded Successfully",
        String.format(consentType + " Uploaded Successfully"));
  }

  public List<ConsentForm> getAllPatientConsents(String patientId) {
    PatientEntity patient = patientUtils.checkIfPatientExists(patientId);
    return ConsentFormMapper.mapConsentEntitiesToDtos(patient.getConsentForms());
  }

  public ConsentForm getConsentForm(String patientId, ConsentTypeEnum consentType) {
    ConsentFormEntity consentForm = consentRepository.findOneByPatient_PatientIdAndConsentType(patientId, consentType)
        .orElseThrow(() -> new NotFoundException(String.format("Consent form of type %s not found for patient %s",
            consentType, patientId)));
    return ConsentFormMapper.mapConsentEntityToDto(consentForm);
  }

  public Success updateRegForm(String patientId, OffsetDateTime date,
      String fileType, String owner, MultipartFile file) throws IOException {
    PatientEntity patient = patientUtils.checkIfPatientExists(patientId);
    PatientRegistrationFormEntity existingRegForm = patient.getPatientRegistrationForm();
    FileUploadResponse uploadResponse = fileUploadService.upload(fileType, owner, file);
    existingRegForm.setPatientRegFormFile(uploadResponse.getFileUrl());
    if (date == null) {
      throw new BadRequestException("Signature Date is Required");
    }
    existingRegForm.setDate(Timestamp.from(date.toInstant()));
    return new Success(true, "Reg Form Uploaded", "Completed Reg Form Uploaded Successfully");
  }

  private void notifyAdminAndPatientAboutConsentForm(PatientEntity patient, Map<String, Object> variables, ConsentTypeEnum consentType) {
    try {
      emailService.sendEmail("fronteers.dev@gmail.com", "Consent Form Submission Details",
          "admin-form-submission-notification", variables);
      emailService.sendEmail(patient.getEmail(), "Acknowledgement",
          "patient-consent-form-acknowledgement-template.html", variables);
    } catch (MessagingException e) {
      log.warn(e.getMessage());
      throw new BadRequestException(e.getMessage());
    }
  }

  private void sendIntakeFormToPatient(PatientEntity patient, Map<String, Object> variables) {
    try {
      emailService.sendEmail(patient.getEmail(), "Next Step",
          "patient-intake-template", variables);
    } catch (MessagingException e) {
      log.warn(e.getMessage());
      throw new BadRequestException(e.getMessage());
    }
  }

  private String generatePatientId() {
    return UUID.randomUUID().toString();
  }

  public PatientRegistrationForm getRegistrationDetails(String patientId) {
    PatientRegistrationFormEntity patientRegistrationFormEntity = checkIfPatientExist(patientId);
    return PatientDtoMapper.mapPatientRegFormEntityToPatientRegFormDTO(
        patientRegistrationFormEntity);
  }

  public PatientRegistrationFormEntity checkIfPatientExist(String patientId) {
    return patientRegistrationFormRepository.findOneByPatientId(patientId).orElseThrow(() ->
        new NotFoundException(String.format("Patient with id %s does not exist", patientId)));
  }

  public PatientIdValidationResponse validatePatientId(String patientId) {
    PatientRegistrationFormEntity patientRegFormEntity = checkIfPatientExist(patientId);
    PatientIdValidationResponse response = new PatientIdValidationResponse();
    response.setPatientId(UUID.fromString(patientRegFormEntity.getPatientId()));
    response.setFirstName(patientRegFormEntity.getFirstName());
    response.setLastName(patientRegFormEntity.getLastName());
    response.setMiddleName(patientRegFormEntity.getMiddleName());
    response.setGender(patientRegFormEntity.getGender());
    response.setDob(patientRegFormEntity.getDob() != null ?
        patientRegFormEntity.getDob().toLocalDate() : null);
    response.setPhone(patientRegFormEntity.getCellPhone());
    response.setEmail(patientRegFormEntity.getEmail());
    response.setAddress(patientRegFormEntity.getAddress() != null ?
        PatientDtoMapper.mapAddressEntityToAddressDto(patientRegFormEntity.getAddress()) : null);
    return response;
  }

  public IdGenerationResponse generateId() {
    String patientId;
    int attempts = 0;
    final int MAX_RETRIES = 5; // Increased retry limit for better reliability
    while (attempts < MAX_RETRIES) {
      patientId = generatePatientId();
      if (!patientRepository.existsByPatientId(patientId)) {
        IdGenerationResponse response = new IdGenerationResponse();
        response.setStatus(true);
        response.setMessage("Patient ID generated successfully");
        response.setPatientId(UUID.fromString(patientId));
        return response;
      }
      log.warn("Attempt {}: Generated Patient ID '{}' already exists.", attempts + 1, patientId);
      attempts++;
    }
    log.error("Failed to generate a unique Patient ID after {} attempts.", MAX_RETRIES);
    throw new ConflictException("ID could not be generated. Please try again.");
  }

  public Patient fetchPatient(String patientId) {
    PatientEntity patient = patientRepository.findOneByPatientId(patientId).orElseThrow(() ->
        new NotFoundException(String.format("Patient with id %s does not exist", patientId)));
    Patient dto = new Patient();
    dto.setId(patient.getId());
    dto.setPatientId(UUID.fromString(patientId));
    dto.setForms(setPatientForms(patient));
    dto.setPrograms(PatientDtoMapper.mapProgramEntitiesToDtos(patient));
    dto.setReviews(null);
    dto.setAppointments(null);
    return dto;
  }

  private void checkIfConsentFormAlreadyExists(PatientEntity patient, ConsentTypeEnum consentType) {
    List<ConsentFormEntity> patientConsentForms = patient.getConsentForms();
    Optional<ConsentFormEntity> currentConsentForm = patientConsentForms.stream().filter
        (consentFormEntity -> consentFormEntity.getConsentType().equals(consentType)).findFirst();
    if (currentConsentForm.isPresent()) {
      throw new ConflictException(String.format("Patient already submitted %s", consentType));
    }
  }

  private Forms setPatientForms(PatientEntity patient) {
    Forms patientForms = new Forms();
    patientForms.setConsentForms(ConsentFormMapper.mapConsentEntitiesToDtos(patient.getConsentForms()));
    patientForms.setAdhdForm(patient.getAdhdForm() != null ? PatientDtoMapper.mapAdhdEntityToDto(
        patient.getAdhdForm()) : null);
    patientForms.setAnxietyDisorderForm(patient.getAnxietyDisorderForm() != null ?
        PatientDtoMapper.mapAnxietyDisorderEntityToDto(patient.getAnxietyDisorderForm()) : null);
    patientForms.setControlledSubstanceForm(patient.getControlledSubstanceForm() != null ?
        PatientDtoMapper.mapControlledSubstanceEntityToDto(patient.getControlledSubstanceForm())
        : null);
    patientForms.setDepressionAssessmentForm(patient.getDepressionAssessmentForm() != null ?
        PatientDtoMapper.mapDepressionAssessmentEntityToDto(patient.getDepressionAssessmentForm())
        : null);
    patientForms.setInitialEvaluationForm(patient.getInitialEvaluationForm() != null ?
        PatientDtoMapper.mapInitialEvaluationEntityToDto(patient.getInitialEvaluationForm())
        : null);
    patientForms.setIntakeForm(patient.getIntakeForm() != null ?
        PatientDtoMapper.mapIntakeEntityToDto(patient.getIntakeForm()) : null);
    patientForms.setMedicationConsentForm(patient.getMedicationConsentForm() != null ?
        PatientDtoMapper.mapMedConsentEntityToDto(patient.getMedicationConsentForm()) : null);
    patientForms.setMoodDisorderAssessmentForm(patient.getMoodDisorderAssessmentForm() != null ?
        PatientDtoMapper.mapMoodDisorderEntityToDto(patient.getMoodDisorderAssessmentForm())
        : null);
    patientForms.setPatientRegistrationForm(patient.getPatientRegistrationForm() != null ?
        PatientDtoMapper.mapPatientRegFormEntityToPatientRegFormDTO(
            patient.getPatientRegistrationForm()) : null);
    patientForms.setNoticeOfPrivacyPracticesForm(patient.getNoticeOfPrivacyPracticesForm() != null ?
        PatientDtoMapper.mapNoticeOfPrivacyEntityToDto(patient.getNoticeOfPrivacyPracticesForm())
        : null);
    patientForms.setPatientInformationConsentAndFinancialPolicyForm(
        patient.getPatientInformationConsentAndFinancialPolicyForm() != null ?
            PatientDtoMapper.mapPatientInfoConsentAndFinPolicyFormEntityToDto(
                patient.getPatientInformationConsentAndFinancialPolicyForm()) : null);
    patientForms.setReleaseReceiveForm(patient.getReleaseReceiveForm() != null ?
        PatientDtoMapper.mapReleaseReceiveEntityToDto(patient.getReleaseReceiveForm()) : null);
    patientForms.setScreeningForm(patient.getScreeningForm() != null ?
        PatientDtoMapper.mapScreeningEntityToDto(patient.getScreeningForm()) : null);
    patientForms.setSelfPayForm(patient.getSelfPayForm() != null ?
        PatientDtoMapper.mapSelfPayEntityToDto(patient.getSelfPayForm()) : null);
    patientForms.setTerminationPolicy(patient.getTerminationPolicyForm() != null ?
        PatientDtoMapper.mapTerminationPolicyEntityToDto(patient.getTerminationPolicyForm())
        : null);
    patientForms.setTreatmentConsentTelehealthInPersonTreatmentConsent(
        patient.getTreatmentConsentTelehealth() != null ?
            PatientDtoMapper.mapTreatmentConsentTelehealthEntityToDto(
                patient.getTreatmentConsentTelehealth()) : null);
    return patientForms;
  }

  public PaginatedPatients searchPatients(Integer pageNumber, Integer limit,
      PatientSearch searchCriteria) {
    int maxLimit = (limit == null || limit > 100) ? 100 : limit;
    int currentPage =
        (pageNumber == null || pageNumber < 1) ? 0 : pageNumber - 1; // Adjust for 0-based indexing
    int safeLimit = Math.max(1, Math.min(maxLimit, 100));

    BooleanBuilder predicate = new BooleanBuilder();
    QPatientEntity qPatient = QPatientEntity.patientEntity;

    if (searchCriteria.getFirstName() != null) {
      predicate.and(qPatient.firstName.eq(searchCriteria.getFirstName()));
    }
    if (searchCriteria.getLastName() != null) {
      predicate.and(qPatient.lastName.eq(searchCriteria.getLastName()));
    }
    if (searchCriteria.getMiddleName() != null) {
      predicate.and(qPatient.patientRegistrationForm.middleName.eq(searchCriteria.getMiddleName()));
    }
    if (searchCriteria.getDob() != null) {
      predicate.and(qPatient.patientRegistrationForm.dob.eq(Date.valueOf(searchCriteria.getDob())));
    }
    if (searchCriteria.getPhone() != null) {
      predicate.and(qPatient.patientRegistrationForm.cellPhone.eq(searchCriteria.getPhone()));
    }
    if (searchCriteria.getEmail() != null) {
      predicate.and(qPatient.email.eq(searchCriteria.getEmail()));
    }
    if (searchCriteria.getGender() != null) {
      predicate.and((qPatient.patientRegistrationForm.gender.eq(searchCriteria.getGender())));
    }
    if (searchCriteria.getMaritalStatus() != null) {
      predicate.and(
          qPatient.patientRegistrationForm.maritalStatus.eq(searchCriteria.getMaritalStatus()));
    }
    if (searchCriteria.getCity() != null) {
      predicate.and(qPatient.patientRegistrationForm.address.city.eq(searchCriteria.getCity()));
    }

    // Fetch all records if no filters are applied
    if (predicate.getValue() == null) {
      log.info("No search filters applied, fetching all records");
      predicate.and(qPatient.id.isNotNull());
    }

    Pageable pageable = PageRequest.of(currentPage, safeLimit);
    Page<PatientEntity> patientPage = patientRepository.findAll(predicate, pageable);
    List<PatientEntity> patientList = patientPage.getContent();
    List<BasicPatientInfo> patientDtos = PatientDtoMapper.mapPatientListToBasicInfoDtos(
        patientList);

    PaginatedPatients response = new PaginatedPatients();
    response.setPatients(patientDtos);
    response.setCurrentPage(patientPage.getNumber() + 1);
    response.setItemsPerPage(patientPage.getSize());
    response.setTotalPages(patientPage.getTotalPages());
    return response;
  }

  public Success updatePersonalIfo(String patientId, PersonalInfo request) {
    PatientRegistrationFormEntity regFormEntity = checkIfPatientRegistrationFormExists(patientId);
    PatientEntity patientEntity = regFormEntity.getPatient();
    if (request.getEmail() != null) {
      validateEmail(regFormEntity, request.getEmail());
    }
    PatientEntityMapper.mapPersonalInfoForUpdate(request, regFormEntity);
    PatientEntityMapper.prepareAddressEntityForSave(regFormEntity.getAddress(),
        request.getAddress());
    PatientEntityMapper.updatePatientBasics(patientEntity, request);
    patientRegistrationFormRepository.save(regFormEntity);
    return new Success(true, "Personal Info Updated", "Patient Personal Info Updated Successfully");
  }

  public Success updateGuarantor(String patientId, Guarantor request) {
    PatientRegistrationFormEntity regFormEntity = checkIfPatientRegistrationFormExists(patientId);
    GuarantorEntity existingGuarantorEntity = regFormEntity.getGuarantor();
    GuarantorEntity updatedGuaratorEntity = patientEntityMapper.mapGuarantor(request, regFormEntity);
    updatedGuaratorEntity.getAddress().setGuarantor(null);
    updatedGuaratorEntity.getAddress().setId(null);
    updatedGuaratorEntity.setAddressId(null);
    updatedGuaratorEntity.setPatientRegistrationForm(null);
    updatedGuaratorEntity.setId(null);
    CopyBeanUtil.copyNonNullProperties(updatedGuaratorEntity, existingGuarantorEntity);
    patientRegistrationFormRepository.save(regFormEntity);
    return new Success(true, "Update Successful", "Guarantor Record Updated Successfully");
  }

  public Success updateParentGuardian(String patientId, ParentGuardian request) {
    PatientRegistrationFormEntity regFormEntity = checkIfPatientRegistrationFormExists(patientId);
    ParentGuardianEntity existingPgEntity = regFormEntity.getParentGuardian();
    ParentGuardianEntity updatedPgEntity = patientEntityMapper.mapParentGuardian(request, regFormEntity);
    updatedPgEntity.getAddress().setParentGuardian(null);
    updatedPgEntity.getAddress().setId(null);
    updatedPgEntity.setAddressId(null);
    updatedPgEntity.setPatientRegistrationForm(null);
    updatedPgEntity.setId(null);
    CopyBeanUtil.copyNonNullProperties(updatedPgEntity, existingPgEntity);
    patientRegistrationFormRepository.save(regFormEntity);
    return new Success(true, "Update Successful", "Parent/Guardian Record Updated Successfully");
  }

  public Success updateEmergencyContact(String patientId, EmergencyContact request) {
    PatientRegistrationFormEntity regFormEntity = checkIfPatientRegistrationFormExists(patientId);
    EmergencyContactEntity existingEcEntity = regFormEntity.getEmergencyContact();
    EmergencyContactEntity updatedEcEntity = patientEntityMapper.mapEmergencyContact(request, regFormEntity);
    updatedEcEntity.getAddress().setEmergencyContact(null);
    updatedEcEntity.getAddress().setId(null);
    updatedEcEntity.setAddressId(null);
    updatedEcEntity.setPatientRegistrationForm(null);
    updatedEcEntity.setId(null);
    CopyBeanUtil.copyNonNullProperties(updatedEcEntity, existingEcEntity);
    patientRegistrationFormRepository.save(regFormEntity);
    return new Success(true, "Update Successful", "EmergencyContact Record Updated Successfully");
  }

//  public Success updatePaymentStructure(String patientId, PaymentStructure request) {
//    PatientRegistrationFormEntity regFormEntity = checkIfPatientRegistrationFormExists(patientId);
//    ParentGuardianEntity existingPgEntity = regFormEntity.getParentGuardian();
//    regFormEntity.setPaymentMode(request.getPaymentMode());
//    List<InsuranceEntity> existingInsurances = regFormEntity.getInsurances();
//    if (existingInsurances.isEmpty()){
//      List<InsuranceEntity> newInsurances = patientEntityMapper.mapInsurances(request, regFormEntity);
//      regFormEntity.setInsurances(newInsurances);
//    } else {
//      // TODO: processupdate
//    }
//    patientRegistrationFormRepository.save(regFormEntity);
//    return new Success(true, "Update Successful", "Payment Structure Updated Successfully");
//  }

  @Transactional
  public Success updatePaymentStructure(String patientId, PaymentStructure request) {
    PatientRegistrationFormEntity regFormEntity = checkIfPatientRegistrationFormExists(patientId);
    List<InsuranceEntity> existingInsurances = new ArrayList<>(regFormEntity.getInsurances());
    regFormEntity.getInsurances().clear();
    insuranceRepository.deleteAll(existingInsurances);
    regFormEntity.setPaymentMode(request.getPaymentMode());
    List<InsuranceEntity> newInsurances = patientEntityMapper.mapInsurances(request, regFormEntity);
    regFormEntity.setInsurances(newInsurances);
    try {
      patientRegistrationFormRepository.save(regFormEntity);
    } catch (Exception e) {
      log.warn("Exception occurred while saving: ", e);
      throw new RuntimeException(e);
    }
    return new Success(true, "Update Successful", "Payment Structure Updated Successfully");
  }

  private void validateEmail(PatientRegistrationFormEntity regFormEntity, String incomingEmail) {
    if (!regFormEntity.getEmail().equals(incomingEmail)) {
      boolean alreadyExist = patientRepository.existsByEmail(incomingEmail);
      if (alreadyExist) {
        throw new BadRequestException("Email is already taken");
      }
    }
  }

  private PatientRegistrationFormEntity checkIfPatientRegistrationFormExists(String patientId) {
    return patientRegistrationFormRepository.findOneByPatientId(patientId).orElseThrow(() ->
        new BadRequestException("Registration Form Not Found"));
  }
}
