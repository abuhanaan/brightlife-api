package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.IntakeFormEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "medication")

public class MedicationEntity extends BaseEntity {

  @Column(name = "medication")
  private String medication;

  @Column(name = "instruction")
  private String instruction;

  @Column(name = "condition_treated")
  private String conditionTreated;

  @Column(name = "prescription")
  private String prescription;

  @Column(name = "is_current")
  private Boolean isCurrent;

  @ManyToOne
  @JoinColumn(name = "intake_form_id", referencedColumnName = "id")
  private IntakeFormEntity intakeForm;
}
