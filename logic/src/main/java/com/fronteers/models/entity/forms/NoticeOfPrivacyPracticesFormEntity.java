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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "notice_of_privacy_practices_form")
public class NoticeOfPrivacyPracticesFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "notice_effect_date")
  private Date noticeEffectDate;

  @Column(name = "date")
  private Date date;

  @Column(name = "notice_of_privacy_practices")
  private String noticeOfPrivacyPractices;
}
