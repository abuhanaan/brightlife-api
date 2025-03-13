package com.fronteers.models.entity;

import com.fronteers.brightlife.model.Referral.SourceEnum;
import com.fronteers.brightlife.model.Referral.TherapistEnum;
import com.fronteers.models.entity.forms.ScreeningFormEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "referral")
public class ReferralEntity extends BaseEntity {

  @OneToOne(mappedBy = "referral", cascade = CascadeType.ALL)
  private ScreeningFormEntity screeningForm;

  @Enumerated(EnumType.STRING)
  @Column(name = "source")
  private SourceEnum source;

  @Enumerated(EnumType.STRING)
  @Column(name = "therapist")
  private TherapistEnum therapist;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "phone")
  private String phone;

  @Column(name = "address_id", updatable = false, insertable = false)
  private Long addressId;

  @OneToOne()
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;
}
