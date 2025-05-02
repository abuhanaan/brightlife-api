package com.fronteers.utils;

import com.fronteers.brightlife.model.Address;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.repositories.AddressRepository;
import com.fronteers.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientUtils {

  private final PatientRepository patientRepository;
  private final AddressRepository addressRepository;

  public PatientEntity checkIfPatientExists(String patientId) {
    return patientRepository.findOneByPatientId(patientId).orElseThrow(() ->
        new BadRequestException(String.format("Patient with id %s does not exist", patientId)));
  }

  public void confirmPatientUniqueness(String email) {
    PatientEntity patientEntity = patientRepository.findOneByEmail(email);
    if (patientEntity != null) {
      throw new ConflictException(String.format("Patient with email %s already exist", email));
    }
  }

  public AddressEntity mapAddressProperties(Address address) {
    return addressRepository.save(AddressEntity.builder()
        .streetName(address.getStreetName())
        .city(address.getCity())
        .state(address.getState())
        .zipCode(address.getZipCode())
        .build());
  }

}
