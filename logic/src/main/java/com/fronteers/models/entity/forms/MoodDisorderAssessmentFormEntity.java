package com.fronteers.models.entity.forms;

import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "mood_disorder_assessment_form")
public class MoodDisorderAssessmentFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "hyper_feeling")
  private Boolean hyperFeeling;

  @Column(name = "is_irritable")
  private Boolean isIrritable;

  @Column(name = "is_over_confident")
  private Boolean isOverConfident;

  @Column(name = "less_sleep")
  private Boolean lessSleep;

  @Column(name = "talk_more")
  private Boolean talkMore;

  @Column(name = "paced_thoughts")
  private Boolean pacedThoughts;

  @Column(name = "easy_distraction")
  private Boolean easyDistraction;

  @Column(name = "over_energetic")
  private Boolean overEnergetic;

  @Column(name = "over_active")
  private Boolean overActive;

  @Column(name = "over_social")
  private Boolean overSocial;

  @Column(name = "sexaholic")
  private Boolean sexaholic;

  @Column(name = "over_foolish")
  private Boolean overFoolish;

  @Column(name = "over_spending")
  private Boolean overSpending;

  @Column(name = "same_time_occurrence")
  private Boolean sameTimeOccurrence;

  @Column(name = "influence_on_life")
  private String influenceOnLife;

  @Column(name = "is_relative_with_bipolar")
  private Boolean isRelativeWithBipolar;

  @Column(name = "is_bipolar_diagnosed")
  private Boolean isBipolarDiagnosed;
}
