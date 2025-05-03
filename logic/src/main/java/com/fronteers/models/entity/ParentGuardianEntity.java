package com.fronteers.models.entity;

import com.fronteers.brightlife.model.EmploymentStatusEnum;
import com.fronteers.brightlife.model.GenderEnum;
import com.fronteers.brightlife.model.MaritalStatusEnum;
import com.fronteers.brightlife.model.ParentGuardian.FamilyRoleEnum;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
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
@Table(name = "parent_guardian")
public class ParentGuardianEntity extends BaseEntity {

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private GenderEnum gender;

  @Enumerated(EnumType.STRING)
  @Column(name = "marital_status")
  private MaritalStatusEnum maritalStatus;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(name = "family_role")
  private FamilyRoleEnum familyRole;

  @Enumerated(EnumType.STRING)
  @Column(name = "employment_status")
  private EmploymentStatusEnum employmentStatus;

  @Column(name = "employer")
  private String employer;

  @Column(name = "occupation")
  private String occupation;

  @Column(name = "address_id", updatable = false, insertable = false)
  private Long addressId;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @OneToOne(mappedBy = "parentGuardian", cascade = CascadeType.ALL)
  private PatientRegistrationFormEntity patientRegistrationForm;
}
