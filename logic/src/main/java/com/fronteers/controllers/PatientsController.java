package com.fronteers.controllers;

import com.fronteers.brightlife.api.PatientsApi;
import com.fronteers.brightlife.model.ADHDForm;
import com.fronteers.brightlife.model.AnxietyDisorderForm;
import com.fronteers.brightlife.model.ControlledSubstanceForm;
import com.fronteers.brightlife.model.DepressionAssessmentForm;
import com.fronteers.brightlife.model.IdGenerationRequest;
import com.fronteers.brightlife.model.IdGenerationResponse;
import com.fronteers.brightlife.model.InitialEvaluationForm;
import com.fronteers.brightlife.model.IntakeForm;
import com.fronteers.brightlife.model.MedicationConsentForm;
import com.fronteers.brightlife.model.MoodDisorderAssessmentForm;
import com.fronteers.brightlife.model.NoticeOfPrivacyPracticesForm;
import com.fronteers.brightlife.model.PaginatedPatients;
import com.fronteers.brightlife.model.Patient;
import com.fronteers.brightlife.model.PatientIdValidationResponse;
import com.fronteers.brightlife.model.PatientInformationConsentAndFinancialPolicyForm;
import com.fronteers.brightlife.model.PatientRegistrationForm;
import com.fronteers.brightlife.model.PatientSearch;
import com.fronteers.brightlife.model.ReleaseReceiveForm;
import com.fronteers.brightlife.model.ScreeningForm;
import com.fronteers.brightlife.model.SelfPayForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TerminationPolicyForm;
import com.fronteers.brightlife.model.TreatmentConsentTelehealthInPersonTreatmentConsent;
import com.fronteers.services.AdhdService;
import com.fronteers.services.AnxietyDisorderService;
import com.fronteers.services.ControlledSubstanceService;
import com.fronteers.services.DepressionAssessmentService;
import com.fronteers.services.InitialEvaluationService;
import com.fronteers.services.IntakeService;
import com.fronteers.services.MedicationConsentService;
import com.fronteers.services.MoodDisorderService;
import com.fronteers.services.NoticeOfPrivacyService;
import com.fronteers.services.PatientInfoConsentAndFinPolicyService;
import com.fronteers.services.PatientService;
import com.fronteers.services.ReleaseReceiveService;
import com.fronteers.services.ScreeningService;
import com.fronteers.services.SelfPayService;
import com.fronteers.services.TerminationPolicyService;
import com.fronteers.services.TreatmentConsentTelehealthInPersonTreatmentConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PatientsController implements PatientsApi {

  private final PatientService patientService;
  private final AdhdService adhdService;
  private final AnxietyDisorderService anxietyDisorderService;
  private final ControlledSubstanceService controlledSubstanceService;
  private final DepressionAssessmentService depressionAssessmentService;
  private final InitialEvaluationService initialEvaluationService;
  private final IntakeService intakeService;
  private final MedicationConsentService medConsentService;
  private final MoodDisorderService moodDisorderService;
  private final NoticeOfPrivacyService noticeOfPrivacyService;
  private final PatientInfoConsentAndFinPolicyService patientInfoConsentAndFinPolicyService;
  private final ReleaseReceiveService releaseReceiveService;
  private final ScreeningService screeningService;
  private final SelfPayService selfPayService;
  private final TerminationPolicyService terminationPolicyService;
  private final TreatmentConsentTelehealthInPersonTreatmentConsentService tctInPersontcService;

  //  ADHD
  @Override
  public ResponseEntity<ADHDForm> getAdhd(Long id) {

    return ResponseEntity.ok(adhdService.getAdhd(id));
  }

  @Override
  public ResponseEntity<Success> submitAdhd(ADHDForm request) {
    log.info("Submitting Adhd form for patient {} with request payload {}", request.getPatientId(),
        request);
    Success response = adhdService.submitAdhd(request);
    log.info("Adhd form submission response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  AnxietyDisorder
  @Override
  public ResponseEntity<AnxietyDisorderForm> getAnxietyDisorder(Long id) {
    log.info("Anxiety Disorder with id {}", id);
    AnxietyDisorderForm response = anxietyDisorderService.getAnxietyDisorder(id);
    log.info("Anxiety Disorder form Fetched Successfully: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitAnxietyDisorder(AnxietyDisorderForm request) {
    log.info("Submitting Anxiety Disorder form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = anxietyDisorderService.submitAnxietyDisorder(request);
    log.info("Anxiety Disorder form submission response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  ControlledSubstance
  @Override
  public ResponseEntity<ControlledSubstanceForm> getControlledSubstance(Long id) {
    log.info("Fetching Controlled Substance with id {}", id);
    ControlledSubstanceForm response = controlledSubstanceService.getControlledSubstance(id);
    log.info("Controlled Substance form Fetched Successfully: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitControlledSubstance(ControlledSubstanceForm request) {
    log.info("Submitting Controlled Substance form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = controlledSubstanceService.submitControlled(request);
    log.info("Controlled Substance form submission response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  DepressionAssessment
  @Override
  public ResponseEntity<DepressionAssessmentForm> getDepressionAssessment(Long id) {
    log.info("Fetching Depression Assessment with id {}", id);
    DepressionAssessmentForm response = depressionAssessmentService.getDepAssessment(id);
    log.info("Depression Assessment form Fetched Successfully: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitDepressionAssessment(DepressionAssessmentForm request) {
    log.info("Submitting Depression Assessment form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = depressionAssessmentService.submitDepressionAssessment(request);
    log.info("Depression Assessment form submission response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  InitialEvaluation
  @Override
  public ResponseEntity<InitialEvaluationForm> getInitialEvaluation(Long id) {
    log.info("Fetching Initial Evaluation Form with id {}", id);
    InitialEvaluationForm response = initialEvaluationService.getInitialEvaluation(id);
    log.info("Initial Evaluation form fetched successfully: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitInitialEvaluation(InitialEvaluationForm request) {
    log.info("Submitting Initial Evaluation form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = initialEvaluationService.submitInitialEvaluation(request);
    log.info("Initial Evcaluation Form submitted successfully with response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  IntakeForm
  @Override
  public ResponseEntity<IntakeForm> getIntake(Long id) {
    log.info("Fetching Intake form with id {}", id);
    IntakeForm response = intakeService.getIntake(id);
    log.info("Intake form with id {} fetched successfully with response {}", id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitIntake(IntakeForm request) {
    log.info("Submitting Intake form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = intakeService.submitIntake(request);
    log.info("Intake form submitted successfully with response payload: {}", response);
    return ResponseEntity.ok(response);
  }

  //  MedicationConsent
  @Override
  public ResponseEntity<MedicationConsentForm> getMedicationConsent(Long id) {
    log.info("Fetching Medication Consent form with id {}", id);
    MedicationConsentForm response = medConsentService.getMedConsent(id);
    log.info("Medication Consent with id {} fetched successfully with response: {}", id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitMedicationConsent(MedicationConsentForm request) {
    log.info("Submitting Medication Consent form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = medConsentService.medConsent(request);
    log.info("Medication Consent form submitted successfully with response payload: {}", response);
    return ResponseEntity.ok(response);
  }

  //  MoodDisorderAssessment
  @Override
  public ResponseEntity<MoodDisorderAssessmentForm> getMoodDisorderAssessment(Long id) {
    log.info("Fetching Mood Disorder Assessment form with id {}", id);
    MoodDisorderAssessmentForm response = moodDisorderService.getMoodDisorder(id);
    log.info("Mood Disorder Assessment with id {} fetched successfully with response: {}", id,
        response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitMoodDisorderAssessment(MoodDisorderAssessmentForm request) {
    log.info("Submitting Mood Disorder Assessment form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = moodDisorderService.submitMoodDisorderAssessment(request);
    log.info("Mood Disorder Assessment form submitted successfully with response payload: {}",
        response);
    return ResponseEntity.ok(response);
  }

  //  NoticeOfPrivacyPractices
  @Override
  public ResponseEntity<NoticeOfPrivacyPracticesForm> getNoticeOfPrivacyPractices(Long id) {
    log.info("Fetching Notice Of Privacy Practice form with id {}", id);
    NoticeOfPrivacyPracticesForm response = noticeOfPrivacyService.getNoticeOfPrivacy(id);
    log.info("Notice Of Privacy Practice form with id {} fetched successfully with response: {}",
        id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitNoticeOfPrivacyPractices(
      NoticeOfPrivacyPracticesForm request) {
    log.info("Submitting Notice Of Privacy Practice form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = noticeOfPrivacyService.submitNoticeOfPrivacy(request);
    log.info("Notice Of Privacy Practice form submitted successfully with response payload: {}",
        response);
    return ResponseEntity.ok(response);
  }

  //  Patient
  @Override
  public ResponseEntity<Patient> getPatient(String patientId) {
    return null;
  }

  @Override
  public ResponseEntity<PaginatedPatients> listPatients(Integer pageNumber, Integer limit,
      PatientSearch searchCriteria) {
    return null;
  }

  @Override
  public ResponseEntity<PatientIdValidationResponse> validatePatient(String patientId) {
    log.info("Validating Patient with id: {}", patientId);
    PatientIdValidationResponse response = patientService.validatePatientId(patientId);
    log.info("Patient Validation Successful with response: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<IdGenerationResponse> generateId(IdGenerationRequest request) {
    log.info("Generating new patient id with request: {}", request);
    IdGenerationResponse response = patientService.generateId(request);
    log.info("Id generation succesful with response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  Registration
  @Override
  public ResponseEntity<PatientRegistrationForm> getRegistration(String patientId) {
    log.info("Fetching Patient Registartion Details with patientId: {}", patientId);
    PatientRegistrationForm response = patientService.getRegistrationDetails(patientId);
    log.info("Fetch Patient Response: {}", response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> register(PatientRegistrationForm request) {
    log.info("Patient Registration Request payload: {}", request.toString());
    Success response = patientService.submitRegistrationForm(request);
    log.info("Patient Registration Response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  PatientInformationConsentAndFinancialPolicy
  @Override
  public ResponseEntity<PatientInformationConsentAndFinancialPolicyForm> getPatientInformationConsentAndFinancialPolicy(
      Long id) {
    log.info("Patient Information Consent And Financial Policy form with id {}", id);
    PatientInformationConsentAndFinancialPolicyForm response = patientInfoConsentAndFinPolicyService.getNoticeOfPrivacy(
        id);
    log.info(
        "Patient Information Consent And Financial Policy form with id {} fetched successfully with response: {}",
        id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitPatientInformationConsentAndFinancialPolicy(
      PatientInformationConsentAndFinancialPolicyForm request) {
    log.info(
        "Submitting Patient Information Consent And Financial Policy form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = patientInfoConsentAndFinPolicyService.submitPatientInfoAndConsentFinPolicy(
        request);
    log.info(
        "Patient Information Consent And Financial Policy form submitted successfully with response payload: {}",
        response);
    return ResponseEntity.ok(response);
  }

  //  ReleaseReceive
  @Override
  public ResponseEntity<ReleaseReceiveForm> getReleaseReceive(Long id) {
    log.info("Fetching Release Receive form with id {}", id);
    ReleaseReceiveForm response = releaseReceiveService.getReleaseReceive(id);
    log.info("Release Receive form with id {} fetched successfully with response: {}", id,
        response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitReleaseReceive(ReleaseReceiveForm request) {
    log.info("Submitting Release Receive form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = releaseReceiveService.submitReleaseReceive(request);
    log.info("Release Receive form submitted successfully with response payload: {}", response);
    return ResponseEntity.ok(response);
  }

  //  Screening
  @Override
  public ResponseEntity<ScreeningForm> getScreening(Long id) {
    log.info("Fetching Screening form with id {}", id);
    ScreeningForm response = screeningService.getScreening(id);
    log.info("Screening form with id {} fetched successfully with response: {}", id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitScreening(ScreeningForm request) {
    log.info("Submitting Screening form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = screeningService.submitScreening(request);
    log.info("Screening form submitted successfully with response payload: {}", response);
    return ResponseEntity.ok(response);
  }

  //  SelfPay
  @Override
  public ResponseEntity<SelfPayForm> getSelfPay(Long id) {
    log.info("Fetching Self Pay form with id {}", id);
    SelfPayForm response = selfPayService.getSelfPay(id);
    log.info("Self Pay form with id {} fetched successfully with response: {}", id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitSelfPay(SelfPayForm request) {
    log.info("Submitting Self Pay form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = selfPayService.submitSelfPay(request);
    log.info("Self Pay form submitted successfully with response payload: {}", response);
    return ResponseEntity.ok(response);
  }

  //  TerminationPolicy
  @Override
  public ResponseEntity<TerminationPolicyForm> getTerminationPolicy(Long id) {
    log.info("Fetching Termination Policy form with id {}", id);
    TerminationPolicyForm response = terminationPolicyService.getTp(id);
    log.info("Termination Policy form with id {} fetched successfully with response: {}", id,
        response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitTerminationPolicy(TerminationPolicyForm request) {
    log.info("Submitting Termination Policy form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = terminationPolicyService.submit(request);
    log.info("Termination Policy form submitted successfully with response payload: {}", response);
    return ResponseEntity.ok(response);
  }

  //  TreatmentConsentTelehealthInPersonTreatmentConsent
  @Override
  public ResponseEntity<TreatmentConsentTelehealthInPersonTreatmentConsent> getTreatmentConsentTelehealthInPersonTreatmentConsent(
      Long id) {
    log.info("Fetching TreatmentConsentTelehealthInPersonTreatmentConsent form with id {}", id);
    TreatmentConsentTelehealthInPersonTreatmentConsent response = tctInPersontcService.fetch(id);
    log.info(
        "TreatmentConsentTelehealthInPersonTreatmentConsent form with id {} fetched successfully with response: {}",
        id, response);
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> submitTreatmentConsentTelehealthInPersonTreatmentConsent(
      TreatmentConsentTelehealthInPersonTreatmentConsent request) {
    log.info(
        "Submitting TreatmentConsentTelehealthInPersonTreatment form for patient {} with request payload {}",
        request.getPatientId(), request);
    Success response = tctInPersontcService.submit(request);
    log.info(
        "TreatmentConsentTelehealthInPersonTreatment form submitted successfully with response payload: {}",
        response);
    return ResponseEntity.ok(response);
  }
}
