package com.fronteers.models.entity.forms;

import com.fronteers.brightlife.model.ConsentTypeEnum;
import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
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
@Table(name = "consent_form")
public class ConsentFormEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "consent_type", nullable = false)
  private ConsentTypeEnum consentType;

  @Column(name = "file", nullable = false)
  private String file;

  @ManyToOne
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "patient_sign_date", nullable = false)
  private Timestamp patientSignDate;
}
