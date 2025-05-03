package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.ReleaseReceiveFormEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "party")
public class PartyEntity extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "phoneNumber")
  private String phoneNumber;

  @Column(name = "fax")
  private String fax;

  @Column(name = "is_receive")
  private Boolean isReceive;

  @Column(name = "address_id", updatable = false, insertable = false)
  private Long addressId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @ManyToOne()
  @JoinColumn(name = "release_receive_form_id", referencedColumnName = "id")
  private ReleaseReceiveFormEntity releaseReceiveForm;
}
