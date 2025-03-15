package com.fronteers.models.entity.forms;

import com.fronteers.brightlife.model.EducationLevelEnum;
import com.fronteers.brightlife.model.EmploymentStatusEnum;
import com.fronteers.brightlife.model.EthnicityEnum;
import com.fronteers.brightlife.model.GenderEnum;
import com.fronteers.brightlife.model.MaritalStatusEnum;
import com.fronteers.brightlife.model.PaymentModeEnum;
import com.fronteers.brightlife.model.PersonalInfo.AppointmentReminderModeEnum;
import com.fronteers.brightlife.model.PersonalInfo.PreferredPhoneEnum;
import com.fronteers.brightlife.model.RaceEnum;
import com.fronteers.brightlife.model.ReligionEnum;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.BaseEntity;
import com.fronteers.models.entity.EmergencyContactEntity;
import com.fronteers.models.entity.GuarantorEntity;
import com.fronteers.models.entity.InsuranceEntity;
import com.fronteers.models.entity.ParentGuardianEntity;
import com.fronteers.models.entity.PatientEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Date;
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
@Table(name = "patient_registration_form")
public class PatientRegistrationFormEntity extends BaseEntity {

  @Column(name = "patient_id", updatable = false, insertable = false)
  private String patientId;

  @OneToOne()
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender")
  private GenderEnum gender;

  @Column(name = "dob")
  private Date dob;

  @Enumerated(EnumType.STRING)
  @Column(name = "marital_status")
  private MaritalStatusEnum maritalStatus;

  @Column(name = "social_security_number")
  private String socialSecurityNumber;

  @Column(name = "home_phone")
  private String homePhone;

  @Column(name = "cell_phone")
  private String cellPhone;

  @Column(name = "work_phone")
  private String workPhone;

  @Enumerated(EnumType.STRING)
  @Column(name = "preferred_phone")
  private PreferredPhoneEnum preferredPhone;

  @Enumerated(EnumType.STRING)
  @Column(name = "appointment_reminder_mode")
  private AppointmentReminderModeEnum appointmentReminderMode;

  @Column(name = "email")
  private String email;

  @Column(name = "send_msg_to_home_phone")
  private Boolean sendMsgToHomePhone;

  @Column(name = "send_msg_to_relative")
  private Boolean sendMsgToRelative;

  @Column(name = "send_msg_to_work")
  private Boolean sendMsgToWork;

  @Column(name = "send_msg_to_cell_phone")
  private Boolean sendMsgToCellPhone;

  @Column(name = "address_id", updatable = false, insertable = false)
  private Long addressId;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @Enumerated(EnumType.STRING)
  @Column(name = "highest_edu_level")
  private EducationLevelEnum highestEduLevel;

  @Enumerated(EnumType.STRING)
  @Column(name = "employment_status")
  private EmploymentStatusEnum employmentStatus;

  @Column(name = "employer")
  private String employer;

  @Column(name = "occupation")
  private String occupation;

  @Enumerated(EnumType.STRING)
  @Column(name = "religion")
  private ReligionEnum religion;

  @Enumerated(EnumType.STRING)
  @Column(name = "ethnicity")
  private EthnicityEnum ethnicity;

  @Enumerated(EnumType.STRING)
  @Column(name = "race")
  private RaceEnum race;

  @Column(name = "preferred_language")
  private String preferredLanguage;

  @Column(name = "guarantor_id", updatable = false, insertable = false)
  private Long guarantorId;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "guarantor_id", referencedColumnName = "id")
  private GuarantorEntity guarantor;

  @Column(name = "parent_guardian_id", updatable = false, insertable = false)
  private Long parentGuardianId;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "parent_guardian_id", referencedColumnName = "id")
  private ParentGuardianEntity parentGuardian;

  @Column(name = "emergency_contact_id", updatable = false, insertable = false)
  private Long emergencyContactId;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "emergency_contact_id", referencedColumnName = "id")
  private EmergencyContactEntity emergencyContact;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_mode")
  private PaymentModeEnum paymentMode;

  @OneToMany(mappedBy = "patientRegistrationForm", cascade = CascadeType.ALL)
  @Fetch(FetchMode.SELECT)
  private List<InsuranceEntity> insurances;

  @Column(name = "patient_reg_form_file")
  private String patientRegFormFile;

  @Column(name = "date")
  private Date date;
}
