package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import java.sql.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "guarantor")
public class GuarantorEntity extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

//    @Column(name = "")
//    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "address_id", updatable = false, insertable = false)
    private Long addressId;

    @OneToOne()
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "state_issued_id_file")
    private String stateIssuedIdFile;

    @Column(name = "insurance_card_file")
    private String insuranceCardFile;

    @OneToOne(mappedBy = "guarantor", cascade = CascadeType.ALL)
    private PatientRegistrationFormEntity patientRegistrationForm;

}
