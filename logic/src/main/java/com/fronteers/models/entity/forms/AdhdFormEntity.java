package com.fronteers.models.entity.forms;

import com.fronteers.brightlife.model.AssessmentEnum;
import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "adhd_form")
public class AdhdFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Enumerated(EnumType.STRING)
  @Column(name = "project_completion_problem")
  private AssessmentEnum projectCompletionProblem;

  @Enumerated(EnumType.STRING)
  @Column(name = "organization_rate")
  private AssessmentEnum organizationRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "memory_rate")
  private AssessmentEnum memoryRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "attitude_to_challenge")
  private AssessmentEnum attitudeToChallenge;

  @Enumerated(EnumType.STRING)
  @Column(name = "fidget_rate_on_sit")
  private AssessmentEnum fidgetRateOnsit;

  @Enumerated(EnumType.STRING)
  @Column(name = "active_to_work")
  private AssessmentEnum activeToWork;

  @Enumerated(EnumType.STRING)
  @Column(name = "careless_mistakes")
  private AssessmentEnum carelessMistakes;

  @Enumerated(EnumType.STRING)
  @Column(name = "attention_to_boringWork")
  private AssessmentEnum attentionToBoringWork;

  @Enumerated(EnumType.STRING)
  @Column(name = "concentration_rate")
  private AssessmentEnum concentrationRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "misplace_rate")
  private AssessmentEnum misplaceRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "distraction_rate")
  private AssessmentEnum distractionRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "excuse_rate")
  private AssessmentEnum excuseRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "restless_rate")
  private AssessmentEnum restlessRate;

  @Enumerated(EnumType.STRING)
  @Column(name = "trouble_relaxing")
  private AssessmentEnum troubleRelaxing;

  @Enumerated(EnumType.STRING)
  @Column(name = "excessive_talks")
  private AssessmentEnum excessiveTalks;

  @Enumerated(EnumType.STRING)
  @Column(name = "people_sentence_completion")
  private AssessmentEnum peopleSentenceCompletion;

  @Enumerated(EnumType.STRING)
  @Column(name = "patience_on_queue")
  private AssessmentEnum patienceOnQueue;

  @Enumerated(EnumType.STRING)
  @Column(name = "interrupt_others")
  private AssessmentEnum interruptOthers;
}
