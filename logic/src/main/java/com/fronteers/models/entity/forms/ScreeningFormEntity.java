package com.fronteers.models.entity.forms;

import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.ReferralEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "screening_form")
public class ScreeningFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "mh_bh_phone")
  private String mhBhPhone;

  @Column(name = "help_needs")
  private String helpNeeds;

  @Column(name = "in_crisis")
  private Boolean inCrisis;

  @Column(name = "currently_on_psych_med")
  private Boolean currentlyOnPsychMed;

  @Column(name = "stable_on_med")
  private Boolean stableOnMed;

  @Column(name = "is_psychiatrist_consult")
  private Boolean isPsychiatristConsult;

  @Column(name = "is_therapist_consult")
  private Boolean isTherapistConsult;

  @Column(name = "any_mental_health_treatment")
  private Boolean anyMentalHealthTreatment;

  @Column(name = "suicide_attempt_history")
  private Boolean suicideAttemptHistory;

  @Column(name = "harm_to_self_or_others")
  private Boolean harmToSelfOrOthers;

  @Column(name = "intent")
  private String intent;

  @Column(name = "health_symptoms")
  private Boolean healthSymptoms;

  @Column(name = "health_symptoms_frequency")
  private String healthSymptomsFrequency;

  @Column(name = "referral_id", updatable = false, insertable = false)
  private Long referralId;

  @OneToOne()
  @JoinColumn(name = "referral_id", referencedColumnName = "id")
  private ReferralEntity referral;

}
