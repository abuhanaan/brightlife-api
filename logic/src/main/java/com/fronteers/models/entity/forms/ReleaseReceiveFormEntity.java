package com.fronteers.models.entity.forms;

import com.fronteers.brightlife.model.ReleaseReceiveForm.DisclosurePurposeEnum;
import com.fronteers.brightlife.model.ReleaseReceiveForm.InfoTypeToReleaseEnum;
import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.PartyEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Date;
import java.util.List;
import java.util.Set;
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
@Table(name = "release_receive_form")
public class ReleaseReceiveFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "is_minor")
  private Boolean isMinor;

  @Column(name = "release_health_info")
  private Boolean releaseHealthInfo;

  @Column(name = "receive_health_info")
  private Boolean receiveHealthInfo;

  @Column(name = "exchange_health_info")
  private Boolean exchangeHealthInfo;

  @OneToMany(mappedBy = "releaseReceiveForm", cascade = CascadeType.ALL)
  @Fetch(FetchMode.SELECT)
  private List<PartyEntity> parties;

  @Column(name = "disclosure_purposes")
  private Set<DisclosurePurposeEnum> disclosurePurposes;

  @Column(name = "info_type_to_release")
  private Set<InfoTypeToReleaseEnum> infoTypeToRelease;

  @Column(name = "guardian_name")
  private String guardianName;

  @Column(name = "relationship")
  private String relationship;

  @Column(name = "date")
  private Date date;

  @Column(name = "release_receive_file")
  private String releaseReceiveFile;
}
