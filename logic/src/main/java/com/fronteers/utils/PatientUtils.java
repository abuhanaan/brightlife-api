package com.fronteers.utils;

import com.fronteers.brightlife.model.Address;
import com.fronteers.exceptions.BadRequestException;
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
    PatientEntity patientEntity = patientRepository.findOneByPatientId(patientId);
    if (patientEntity == null) {
      throw new BadRequestException(String.format("Patient with id %s does not exist", patientId));
    }
    return patientEntity;
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
