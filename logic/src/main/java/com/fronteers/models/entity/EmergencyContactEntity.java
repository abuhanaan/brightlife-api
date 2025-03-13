package com.fronteers.models.entity;

import com.fronteers.models.entity.forms.PatientRegistrationFormEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "emergency_contact")
public class EmergencyContactEntity extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

//    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "relationship")
    private String relationship;

    @Column(name = "address_id", updatable = false, insertable = false)
    private Long addressId;

    @OneToOne()
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private AddressEntity address;

    @Column(name = "home_phone")
    private String homePhone;

    @Column(name = "cellPhone")
    private String cellPhone;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "emergencyContact", cascade = CascadeType.ALL)
    private PatientRegistrationFormEntity patientRegistrationForm;
}
