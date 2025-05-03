package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.InitialEvaluationFormEntity;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "primary_care_physician")
public class PrimaryCarePhysicianEntity extends BaseEntity {

  @Column(name = "have_pcp")
  private String havePcp;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Column(name = "phone")
  private String phone;

  @Column(name = "fax")
  private String fax;

  @Column(name = "address_id", updatable = false, insertable = false)
  private Long addressId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @OneToMany(mappedBy = "primaryCarePhysician", cascade = CascadeType.ALL)
  @Fetch(value = FetchMode.SUBSELECT)
  private List<InitialEvaluationFormEntity> initialEvaluationForms;
}
