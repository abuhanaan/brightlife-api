package com.fronteers.models.entity.forms;

import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.PharmacyEntity;
import com.fronteers.models.entity.PrimaryCarePhysicianEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Date;
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
@Table(name = "initial_evaluation_form")
public class InitialEvaluationFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @ManyToOne
  @JoinColumn(name = "pharmacy_id", referencedColumnName = "id")
  private PharmacyEntity pharmacy;

  @ManyToOne
  @JoinColumn(name = "primary_care_physician_id", referencedColumnName = "id")
  private PrimaryCarePhysicianEntity primaryCarePhysician;

  @Column(name = "date")
  private Date date;

  @Column(name = "initial_evaluation_file")
  private String initialEvaluationFile;
}
