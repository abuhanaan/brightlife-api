package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.IntakeFormEntity;
import jakarta.persistence.Column;
import java.sql.Date;

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
@Table(name = "past_provider")
public class PastProviderEntity extends BaseEntity {

  @Column(name = "provider")
  private String provider;

  @Column(name = "appointment_date")
  private Date appointmentDate;

  @ManyToOne
  @JoinColumn(name = "intake_form_id", referencedColumnName = "id")
  private IntakeFormEntity intakeForm;
}
