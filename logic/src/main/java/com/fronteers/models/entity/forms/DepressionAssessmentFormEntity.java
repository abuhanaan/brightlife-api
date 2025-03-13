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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "depression_assessment_form")
public class DepressionAssessmentFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "pleasure_interest")
  private Integer pleasureInterest;

  @Column(name = "depression_rate")
  private Integer depressionRate;

  @Column(name = "sleep_rate")
  private Integer sleepRate;

  @Column(name = "fatigue_rate")
  private Integer fatigueRate;

  @Column(name = "appetite_rate")
  private Integer appetiteRate;

  @Column(name = "failure_rate")
  private Integer failureRate;

  @Column(name = "concentration_rate")
  private Integer concentrationRate;

  @Column(name = "restlessness_rate")
  private Integer restlessnessRate;

  @Column(name = "suicide_thought")
  private Integer suicideThought;
}
