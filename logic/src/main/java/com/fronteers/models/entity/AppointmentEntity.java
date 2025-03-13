package com.fronteers.models.entity;

import com.fronteers.brightlife.model.Appointment.AppointmentTypeEnum;
import com.fronteers.brightlife.model.Appointment.ServiceEnum;
import com.fronteers.brightlife.model.GenderEnum;
import com.fronteers.brightlife.model.InsuranceNameEnum;
import com.fronteers.brightlife.model.PaymentModeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "appointment")
public class AppointmentEntity extends BaseEntity{

  @ManyToOne
  @JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
  private PatientEntity patient;

  @Column(name = "is_new")
  private Boolean isNew;

  @Column(name = "verification_status")
  private String verificationStatus;

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

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @ManyToOne
  @JoinColumn(name = "address_id", referencedColumnName = "id")
  private AddressEntity address;

  @Enumerated(EnumType.STRING)
  @Column(name = "appointment_type")
  private AppointmentTypeEnum appointmentType;

  @Column(name = "service")
  private ServiceEnum service;

  @Column(name = "appointment_date_time")
  private Timestamp appointmentDateTime;

  @Column(name = "purpose")
  private String purpose;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method")
  private PaymentModeEnum paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "insurance_name")
  private InsuranceNameEnum insuranceName;

  @Column(name = "insurance_number")
  private String insuranceNumber;
}
