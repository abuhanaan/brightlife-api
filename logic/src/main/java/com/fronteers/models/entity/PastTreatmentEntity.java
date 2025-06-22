package com.fronteers.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = "alcoholDrugHistory")
@ToString(exclude = "alcoholDrugHistory")
@Entity
@Table(name = "past_treatment")

public class PastTreatmentEntity extends BaseEntity {

  @Column(name = "facility")
  private String facility;

  @Column(name = "date")
  private Date date;

  @Column(name = "drug_treated")
  private String drugTreated;

  @Column(name = "is_treatment_completed")
  private Boolean isTreatmentCompleted;

  @ManyToOne
  @JoinColumn(name = "alcohol_drug_history_id", referencedColumnName = "id")
  private AlcoholDrugHistoryEntity alcoholDrugHistory;
}
