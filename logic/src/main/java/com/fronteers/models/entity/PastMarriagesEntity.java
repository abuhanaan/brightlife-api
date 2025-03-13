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
@Table(name = "past_marriage")
public class PastMarriagesEntity extends BaseEntity {

  @Column(name = "description")
  private String description;

  @Column(name = "duration")
  private String duration;

  @Column(name = "divorce_reason")
  private String divorceReason;

  @ManyToOne
  @JoinColumn(name = "intake_form_id", referencedColumnName = "id")
  private IntakeFormEntity intakeForm;
}
