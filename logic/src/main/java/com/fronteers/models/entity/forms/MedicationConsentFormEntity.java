package com.fronteers.models.entity.forms;

import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;
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
@Table(name = "medication_consent_form")
public class MedicationConsentFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "is_minor")
  private Boolean isMinor;

  @Column(name = "patient_sign_date")
  private Timestamp patientSignDate;

  @Column(name = "guardian_name")
  private String guardianName;

  @Column(name = "patient_guardian_relationship")
  private String patientGuardianRelationship;

  @Column(name = "guardian_sign_date")
  private Timestamp guardianSignDate;

  @Column(name = "medication_consent_file")
  private String medicationConsentFile;
}
