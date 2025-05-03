package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "address")
public class AddressEntity extends BaseEntity {

  @Column(name = "street_name")
  private String streetName;

  @Column(name = "city")
  private String city;

  @Column(name = "state")
  private String state;

  @Column(name = "zip_code")
  private String zipCode;

  @OneToOne(mappedBy = "address")
  private PrimaryCarePhysicianEntity adhdForm;

  @OneToOne(mappedBy = "address")
  private PharmacyEntity pharmacy;

  @OneToOne(mappedBy = "address")
  private PatientRegistrationFormEntity patientRegistrationForm;

  @OneToOne(mappedBy = "address")
  private ParentGuardianEntity parentGuardian;

  @OneToOne(mappedBy = "address")
  private EmergencyContactEntity emergencyContact;

  @OneToOne(mappedBy = "address")
  private GuarantorEntity guarantor;

  @OneToOne(mappedBy = "address")
  private InsuranceEntity insurance;

  @OneToOne(mappedBy = "address")
  private ReferralEntity referral;

  @OneToOne(mappedBy = "address")
  private PartyEntity party;

  @OneToMany(mappedBy = "address")
  @Fetch(FetchMode.SELECT)
  private List<AppointmentEntity> appointments;
}
