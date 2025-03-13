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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "anxiety_disorder_form")
public class AnxietyDisorderFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "nervous_rate")
  private String nervousRate;

  @Column(name = "control_over_worry")
  private String controlOverWorry;

  @Column(name = "excessive_worry")
  private String excessiveWorry;

  @Column(name = "relax_trouble")
  private String relaxTrouble;

  @Column(name = "restlessness")
  private String restlessness;

  @Column(name = "annoyance_rate")
  private String annoyanceRate;

  @Column(name = "fright_rate")
  private String frightRate;

  @Column(name = "life_influence_summary_file")
  private String lifeInfluenceSummaryFile;
}
