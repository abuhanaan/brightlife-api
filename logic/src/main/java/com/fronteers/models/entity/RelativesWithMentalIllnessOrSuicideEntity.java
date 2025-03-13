package com.fronteers.models.entity;

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
@Table(name = "mental_suicide_relative")
public class RelativesWithMentalIllnessOrSuicideEntity extends BaseEntity {

  @Column(name = "relative")
  private String relative;

  @Column(name = "illness")
  private String illness;

  @ManyToOne
  @JoinColumn(name = "alcohol_drug_history_id", referencedColumnName = "id")
  private AlcoholDrugHistoryEntity alcoholDrugHistory;
}
