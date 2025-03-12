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
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class PatientsController implements PatientsApi {

//  ADHD
  @Override
  public ResponseEntity<ADHDForm> getAdhd(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitAdhd(ADHDForm request){
    return null;
  }

//  AnxietyDisorder
  @Override
  public ResponseEntity<AnxietyDisorderForm> getAnxietyDisorder(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitAnxietyDisorder(AnxietyDisorderForm request){
    return null;
  }

  //  ControlledSubstance
  @Override
  public ResponseEntity<ControlledSubstanceForm> getControlledSubstance(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitControlledSubstance(ControlledSubstanceForm request){
    return null;
  }

  //  DepressionAssessment
  @Override
  public ResponseEntity<DepressionAssessmentForm> getDepressionAssessment(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitDepressionAssessment(DepressionAssessmentForm request){
    return null;
  }

  //  InitialEvaluation
  @Override
  public ResponseEntity<InitialEvaluationForm> getInitialEvaluation(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitInitialEvaluation(InitialEvaluationForm request){
    return null;
  }

  //  IntakeForm
  @Override
  public ResponseEntity<IntakeForm> getIntake(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitIntake(IntakeForm request){
    return null;
  }

  //  MedicationConsent
  @Override
  public ResponseEntity<MedicationConsentForm> getMedicationConsent(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitMedicationConsent(MedicationConsentForm request){
    return null;
  }

  //  MoodDisorderAssessment
  @Override
  public ResponseEntity<MoodDisorderAssessmentForm> getMoodDisorderAssessment(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitMoodDisorderAssessment(MoodDisorderAssessmentForm request){
    return null;
  }

  //  NoticeOfPrivacyPractices
  @Override
  public ResponseEntity<NoticeOfPrivacyPracticesForm> getNoticeOfPrivacyPractices(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitNoticeOfPrivacyPractices(NoticeOfPrivacyPracticesForm request){
    return null;
  }

  //  Patient
  @Override
  public ResponseEntity<Patient> getPatient(String patientId){
    return null;
  }

  @Override
  public ResponseEntity<PaginatedPatients> listPatients(Integer pageNumber, Integer limit, PatientSearch searchCriteria){
    return null;
  }

  //  Registration
  @Override
  public ResponseEntity<PatientRegistrationForm> getRegistration(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> register(PatientRegistrationForm request){
    return null;
  }

  //  PatientInformationConsentAndFinancialPolicy
  @Override
  public ResponseEntity<PatientInformationConsentAndFinancialPolicyForm> getPatientInformationConsentAndFinancialPolicy(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitPatientInformationConsentAndFinancialPolicy(PatientInformationConsentAndFinancialPolicyForm request){
    return null;
  }

  //  ReleaseReceive
  @Override
  public ResponseEntity<ReleaseReceiveForm> getReleaseReceive(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitReleaseReceive(ReleaseReceiveForm request){
    return null;
  }

  //  Screening
  @Override
  public ResponseEntity<ScreeningForm> getScreening(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitScreening(ScreeningForm request){
    return null;
  }

  //  SelfPay
  @Override
  public ResponseEntity<SelfPayForm> getSelfPay(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitSelfPay(SelfPayForm request){
    return null;
  }

  //  TerminationPolicy
  @Override
  public ResponseEntity<TerminationPolicyForm> getTerminationPolicy(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitTerminationPolicy(TerminationPolicyForm request){
    return null;
  }

//  TreatmentConsentTelehealthInPersonTreatmentConsent
  @Override
  public ResponseEntity<TreatmentConsentTelehealthInPersonTreatmentConsent> getTreatmentConsentTelehealthInPersonTreatmentConsent(Long id){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitTreatmentConsentTelehealthInPersonTreatmentConsent(TreatmentConsentTelehealthInPersonTreatmentConsent request){
    return null;
  }
}
