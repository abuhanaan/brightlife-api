package com.fronteers.utils;

import com.fronteers.exceptions.BadRequestException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.repositories.PatientRepository;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientUtils {

  private final PatientRepository patientRepository;

  public PatientEntity checkIfPatientExists(String patientId) {
    PatientEntity patientEntity = patientRepository.findOneByPatientId(patientId);
    if (patientEntity == null){
      throw new BadRequestException(String.format("Patient with id %s does not exist", patientId));
    }
    return patientEntity;
  }

}
