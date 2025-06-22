package com.fronteers.models.entity;

import com.fronteers.brightlife.model.FrequencyEnum;
import com.fronteers.models.entity.forms.IntakeFormEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false, exclude = {
    "substanceUsages", "pastTreatments", "relativesWithMentalIllnessOrSuicide", "intakeForm"
})
@ToString(exclude = {
    "substanceUsages", "pastTreatments", "relativesWithMentalIllnessOrSuicide", "intakeForm"
})
@Entity
@Table(name = "alcohol_drug_history")
public class AlcoholDrugHistoryEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "usage_frequency")
  private FrequencyEnum usageFrequency;

  @Column(name = "brand")
  private String brand;

  @Column(name = "last_used")
  private String lastUsed;

  @Column(name = "drink_guilt_check")
  private String drinkGuiltCheck;

  @OneToMany(mappedBy = "alcoholDrugHistory", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<SubstanceUsageEntity> substanceUsages;

  @Column(name = "weekly_average_spending")
  private Double weeklyAverageSpending;

  @OneToMany(mappedBy = "alcoholDrugHistory", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<PastTreatmentEntity> pastTreatments;

  @Column(name = "is_past_step_recovery_participant")
  private Boolean isPastStepRecoveryParticipant;

  @Column(name = "is_current_step_recovery_participant")
  private Boolean isCurrentStepRecoveryParticipant;

  @Column(name = "birth_place")
  private String birthPlace;

  @Column(name = "growth_place")
  private String growthPlace;

  @Column(name = "raised_by")
  private String raisedBy;

  @Column(name = "siblings_count")
  private Integer siblingsCount;

  @Column(name = "child_hood_info")
  private String childhoodInfo;

  @Column(name = "was_physically_abused")
  private Boolean wasPhysicallyAbused;

  @Column(name = "was_emotionally_abused")
  private Boolean wasEmotionallyAbused;

  @Column(name = "was_sexually_abused")
  private Boolean wasSexuallyAbused;

  @Column(name = "has_medical_disability")
  private Boolean hasMedicalDisability;

  @Column(name = "past_medical_history")
  private Set<String> pastMedicalHistory;

  @Column(name = "past_surgical_history")
  private Set<String> pastSurgicalHistory;

  @Column(name = "allergies")
  private Set<String> allergies;

  @Column(name = "others")
  private String otherUsefulInfo;

  @OneToMany(mappedBy = "alcoholDrugHistory", cascade = CascadeType.ALL, orphanRemoval = true)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<RelativesWithMentalIllnessOrSuicideEntity> relativesWithMentalIllnessOrSuicide;

  @OneToOne(mappedBy = "alcoholDrugHistory", cascade = CascadeType.ALL)
  private IntakeFormEntity intakeForm;
}
