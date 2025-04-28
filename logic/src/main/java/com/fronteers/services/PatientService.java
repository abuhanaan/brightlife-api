package com.fronteers.services;

import com.fronteers.brightlife.model.BasicPatientInfo;
import com.fronteers.brightlife.model.Forms;
import com.fronteers.brightlife.model.IdGenerationRequest;
import com.fronteers.brightlife.model.IdGenerationResponse;
import com.fronteers.brightlife.model.PaginatedPatients;
import com.fronteers.brightlife.model.Patient;
import com.fronteers.brightlife.model.PatientIdValidationResponse;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PatientSearch;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.QPatientEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.models.mappers.PatientEntityMapper;
import com.fronteers.repositories.PatientRegistrationFormRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import com.querydsl.core.BooleanBuilder;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

  private final PatientRepository patientRepository;
  private final PatientEntityMapper patientEntityMapper;
  private final PatientRegistrationFormRepository patientRegistrationFormRepository;
  private final PatientUtils patientUtils;

  public Success submitRegistrationForm(PatientRegistrationForm request) {
    PatientEntity existingPatient = patientUtils.checkIfPatientExists(
        request.getPatientId().toString());
    String patientId = existingPatient.getPatientId();
    PatientRegistrationFormEntity patientRegistrationFormEntity = patientEntityMapper.mapRegFormToRegFormEntity(
        request, existingPatient);
    existingPatient.setPatientRegistrationForm(patientRegistrationFormEntity);
    patientRepository.save(existingPatient);
    //    TODO: send email
    return new Success(true, "Patient Registered Successfully",
        String.format("PatientId: %s", patientId));
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
    response.setPatientId(patientRegFormEntity.getPatientId());
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

  public IdGenerationResponse generateId(IdGenerationRequest request) {
    confirmPatientUniqueness(request.getEmail());
    PatientEntity newPatientEntity = new PatientEntity();
    String patientId = generatePatientId();
    newPatientEntity.setPatientId(patientId);
    newPatientEntity.setFullName(request.getFirstName() + " " + request.getLastName());
    newPatientEntity.setEmail(request.getEmail());
    patientRepository.save(newPatientEntity);
    IdGenerationResponse response = new IdGenerationResponse();
    response.setPatientId(patientId);
    response.setMessage("Patient Id generated successfully");
    response.setStatus(true);
    return response;
  }

  public Patient fetchPatient(String patientId) {
    PatientEntity patient = patientRepository.findOneByPatientId(patientId).orElseThrow(() ->
        new NotFoundException(String.format("Patient with id %s does not exist", patientId)));
    Patient dto = new Patient();
    dto.setId(patient.getId());
    dto.setPatientId(UUID.fromString(patientId));
    dto.setForms(setPatientForms(patient));
    dto.setReviews(null);
    dto.setAppointments(null);
    return dto;
  }

  private Forms setPatientForms(PatientEntity patient) {
    Forms patientForms = new Forms();
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

  private void confirmPatientUniqueness(String email) {
    PatientEntity patientEntity = patientRepository.findOneByEmail(email);
    if (patientEntity != null) {
      throw new ConflictException(String.format("Patient with email %s already exist", email));
    }
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
      predicate.and(qPatient.patientRegistrationForm.firstName.eq(searchCriteria.getFirstName()));
    }
    if (searchCriteria.getLastName() != null) {
      predicate.and(qPatient.patientRegistrationForm.firstName.eq(searchCriteria.getLastName()));
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
}
