package com.fronteers.models.entity.forms;

import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
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
@Table(name = "telehealth_consent_form")
public class TreatmentConsentTelehealthInPersonTreatmentConsentEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "patient_sign_date")
  private Date patientSignDate;

  @Column(name = "is_minor")
  private Boolean isMinor;

  @Column(name = "guardian_name")
  private String guardianName;

  @Column(name = "guardian_sign_date")
  private Date guardianSignDate;

  @Column(name = "treatment_consent_telehealth_file")
  private String treatmentConsentTelehealthInPersonFile;
}
