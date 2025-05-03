package com.fronteers.models.entity;

import com.fronteers.brightlife.model.InsuranceNameEnum;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Date;
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
@Table(name = "insurance")
public class InsuranceEntity extends BaseEntity {

  @Column(name = "is_primary")
  private Boolean primary;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "relationship")
  private String relationship;

  @Column(name = "phone")
  private String phone;

  @Column(name = "dob")
  private Date dob;

  @Column(name = "provider_name")
  private InsuranceNameEnum providerName;

  @Column(name = "provider_phone")
  private String providerPhone;

  @Column(name = "policy_id")
  private String policyId;

  @Column(name = "group_number")
  private String groupNumber;

  @Column(name = "authorization_id")
  private String authorizationId;

  @Column(name = "co_pay")
  private String coPay;

  @Column(name = "coverage_start_date")
  private Date coverageStartDate;

  @Column(name = "coverage_end_date")
  private Date coverageEndDate;

  @Column(name = "address_id", updatable = false, insertable = false)
  private Long addressId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @ManyToOne()
  @JoinColumn(name = "patient_registration_form_id", referencedColumnName = "id")
  private PatientRegistrationFormEntity patientRegistrationForm;
}
