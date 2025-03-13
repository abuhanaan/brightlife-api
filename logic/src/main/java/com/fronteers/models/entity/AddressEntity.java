package com.fronteers.models.entity;

import com.fronteers.brightlife.model.Pharmacy;
import com.fronteers.models.entity.forms.AdhdFormEntity;
import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import jakarta.persistence.CascadeType;
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

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private PrimaryCarePhysicianEntity adhdForm;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private PharmacyEntity pharmacy;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private PatientRegistrationFormEntity patientRegistrationForm;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private ParentGuardianEntity parentGuardian;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private EmergencyContactEntity emergencyContact;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private InsuranceEntity insurance;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private ReferralEntity referral;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private PartyEntity party;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    private AppointmentEntity appointment;

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<AppointmentEntity> appointments;
}
