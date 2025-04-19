package com.fronteers.controllers;

import com.fronteers.brightlife.api.PatientsApi;
import com.fronteers.brightlife.model.ADHDForm;
import com.fronteers.brightlife.model.AnxietyDisorderForm;
import com.fronteers.brightlife.model.ControlledSubstanceForm;
import com.fronteers.brightlife.model.DepressionAssessmentForm;
import com.fronteers.brightlife.model.InitialEvaluationForm;
import com.fronteers.brightlife.model.IntakeForm;
import com.fronteers.brightlife.model.MedicationConsentForm;
import com.fronteers.brightlife.model.MoodDisorderAssessmentForm;
import com.fronteers.brightlife.model.NoticeOfPrivacyPracticesForm;
import com.fronteers.brightlife.model.PaginatedPatients;
import com.fronteers.brightlife.model.Patient;
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
import com.fronteers.services.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PatientsController implements PatientsApi {

  final PatientService patientService;
  final AdhdService adhdService;
  final AnxietyDisorderService anxietyDisorderService;
  private final ControlledSubstanceService controlledSubstanceService;

  //  ADHD
  @Override
  public ResponseEntity<ADHDForm> getAdhd(Long id) {

    return ResponseEntity.ok(adhdService.getAdhd(id));
  }

  @Override
  public ResponseEntity<Success> submitAdhd(ADHDForm request) {
    log.info("Submitting Adhd form for patient {} with request payload {}", request.getPatientId(), request);
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
    log.info("Submitting Anxiety Disorder form for patient {} with request payload {}", request.getPatientId(), request);
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
    log.info("Submitting Controlled Substance form for patient {} with request payload {}", request.getPatientId(), request);
    Success response = controlledSubstanceService.submitControlled(request);
    log.info("Controlled Substance form submission response: {}", response);
    return ResponseEntity.ok(response);
  }

  //  DepressionAssessment
  @Override
  public ResponseEntity<DepressionAssessmentForm> getDepressionAssessment(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitDepressionAssessment(DepressionAssessmentForm request) {
    return null;
  }

  //  InitialEvaluation
  @Override
  public ResponseEntity<InitialEvaluationForm> getInitialEvaluation(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitInitialEvaluation(InitialEvaluationForm request) {
    return null;
  }

  //  IntakeForm
  @Override
  public ResponseEntity<IntakeForm> getIntake(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitIntake(IntakeForm request) {
    return null;
  }

  //  MedicationConsent
  @Override
  public ResponseEntity<MedicationConsentForm> getMedicationConsent(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitMedicationConsent(MedicationConsentForm request) {
    return null;
  }

  //  MoodDisorderAssessment
  @Override
  public ResponseEntity<MoodDisorderAssessmentForm> getMoodDisorderAssessment(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitMoodDisorderAssessment(MoodDisorderAssessmentForm request) {
    return null;
  }

  //  NoticeOfPrivacyPractices
  @Override
  public ResponseEntity<NoticeOfPrivacyPracticesForm> getNoticeOfPrivacyPractices(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitNoticeOfPrivacyPractices(
      NoticeOfPrivacyPracticesForm request) {
    return null;
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
    return null;
  }

  @Override
  public ResponseEntity<Success> submitPatientInformationConsentAndFinancialPolicy(
      PatientInformationConsentAndFinancialPolicyForm request) {
    return null;
  }

  //  ReleaseReceive
  @Override
  public ResponseEntity<ReleaseReceiveForm> getReleaseReceive(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitReleaseReceive(ReleaseReceiveForm request) {
    return null;
  }

  //  Screening
  @Override
  public ResponseEntity<ScreeningForm> getScreening(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitScreening(ScreeningForm request) {
    return null;
  }

  //  SelfPay
  @Override
  public ResponseEntity<SelfPayForm> getSelfPay(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitSelfPay(SelfPayForm request) {
    return null;
  }

  //  TerminationPolicy
  @Override
  public ResponseEntity<TerminationPolicyForm> getTerminationPolicy(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitTerminationPolicy(TerminationPolicyForm request) {
    return null;
  }

  //  TreatmentConsentTelehealthInPersonTreatmentConsent
  @Override
  public ResponseEntity<TreatmentConsentTelehealthInPersonTreatmentConsent> getTreatmentConsentTelehealthInPersonTreatmentConsent(
      Long id) {
    return null;
  }

  @Override
  public ResponseEntity<Success> submitTreatmentConsentTelehealthInPersonTreatmentConsent(
      TreatmentConsentTelehealthInPersonTreatmentConsent request) {
    return null;
  }
}
