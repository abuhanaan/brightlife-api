package com.fronteers.models.entity;

import com.fronteers.brightlife.model.FrequencyEnum;
import com.fronteers.brightlife.model.SubstanceUsage.SubstanceNameEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "substance_usage")
public class SubstanceUsageEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name")
  private SubstanceNameEnum name;

  @Column(name = "age_at_first_use")
  private Integer ageAtFirstUse;

  @Column(name = "qty_use")
  private String qtyUse;

  @Enumerated(EnumType.STRING)
  @Column(name = "frequent_usage")
  private FrequencyEnum frequentUsage;

  @Column(name = "last_used")
  private String lastUsed;

  @ManyToOne
  @JoinColumn(name = "alcohol_drug_history_id", referencedColumnName = "id")
  private AlcoholDrugHistoryEntity alcoholDrugHistory;
}
