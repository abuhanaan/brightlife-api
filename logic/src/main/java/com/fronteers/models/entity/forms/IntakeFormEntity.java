package com.fronteers.models.entity.forms;

import com.fronteers.models.entity.AlcoholDrugHistoryEntity;
import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.MedicationEntity;
import com.fronteers.models.entity.PastMarriageEntity;
import com.fronteers.models.entity.PastProviderEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "intake_form")
public class IntakeFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "do_you_share_home")
  private String doYouShareHome;

  @Column(name = "complaints")
  private String complaints;

  @Column(name = "sex_preference")
  private String sexPreference;

  @Column(name = "on_probation")
  private String onProbation;

  @Column(name = "in_lawsuit")
  private String inLawsuit;

  @Column(name = "children_count")
  private Integer childrenCount;

  @Column(name = "marriage_count")
  private Integer marriageCount;

  @OneToMany(mappedBy = "intakeForm", cascade = CascadeType.ALL)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<PastMarriageEntity> pastMarriagesInfo;

  @OneToMany(mappedBy = "intakeForm", cascade = CascadeType.ALL)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<PastProviderEntity> pastProviders;

  @OneToMany(mappedBy = "intakeForm", cascade = CascadeType.ALL)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<MedicationEntity> medications;

  @Column(name = "has_attempted_suicide")
  private Boolean hasAttemptedSuicide;

  @Column(name = "is_psych_hospitalized")
  private Boolean isPsychHospitalized;

  @Column(name = "alcohol_drugHistory_id", updatable = false, insertable = false)
  private Long alcoholDrugHistoryId;

  @OneToOne()
  @JoinColumn(name = "alcohol_drugHistory_id", referencedColumnName = "id")
  private AlcoholDrugHistoryEntity alcoholDrugHistory;
}
