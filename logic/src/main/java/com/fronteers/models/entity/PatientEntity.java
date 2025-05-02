package com.fronteers.models.entity;

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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "patient")
public class PatientEntity extends BaseEntity {

  @Column(name = "patient_id", unique = true, nullable = false)
  private String patientId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
  @Fetch(FetchMode.SELECT)
  private List<ReviewEntity> reviews;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
  @Fetch(FetchMode.SELECT)
  private List<AppointmentEntity> appointments;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private AdhdFormEntity adhdForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private AnxietyDisorderFormEntity anxietyDisorderForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private ControlledSubstanceFormEntity controlledSubstanceForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private DepressionAssessmentFormEntity depressionAssessmentForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private InitialEvaluationFormEntity initialEvaluationForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private IntakeFormEntity intakeForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private MedicationConsentFormEntity medicationConsentForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private MoodDisorderAssessmentFormEntity moodDisorderAssessmentForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private NoticeOfPrivacyPracticesFormEntity noticeOfPrivacyPracticesForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private PatientInformationConsentAndFinancialPolicyFormEntity patientInformationConsentAndFinancialPolicyForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private PatientRegistrationFormEntity patientRegistrationForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private ReleaseReceiveFormEntity releaseReceiveForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private ScreeningFormEntity screeningForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private SelfPayFormEntity selfPayForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private TerminationPolicyFormEntity terminationPolicyForm;

  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
  private TreatmentConsentTelehealthInPersonTreatmentConsentEntity treatmentConsentTelehealth;

//  @PrePersist
//  public void generatePatientId() {
//    if (patientId == null) {
//      this.patientId = UUID.randomUUID().toString();
//    }
//  }

}
