package com.fronteers.utils;

import com.fronteers.brightlife.model.Address;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.models.entity.AddressEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.repositories.AddressRepository;
import com.fronteers.repositories.PatientRepository;
import java.beans.FeatureDescriptor;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PatientUtils {

  private final PatientRepository patientRepository;
  private final AddressRepository addressRepository;

  public void copyNonNullProperties(Object source, Object target) {
    try {
      BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    } catch (UnsupportedOperationException e) {
      log.info("Error copying properties: " + e.getMessage());
      throw e; // Re-throw the exception after logging
    }
  }

  private String[] getNullPropertyNames(Object source) {
    final BeanWrapper src = new BeanWrapperImpl(source);
    return Arrays.stream(src.getPropertyDescriptors())
        .map(FeatureDescriptor::getName)
        .filter(propertyName -> src.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }

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
    return AddressEntity.builder()
        .streetName(address.getStreetName())
        .city(address.getCity())
        .state(address.getState())
        .zipCode(address.getZipCode())
        .build();
  }

}
