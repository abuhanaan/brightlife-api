package com.fronteers.services;

import com.fronteers.brightlife.model.AnxietyDisorderForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnxietyDisorderService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;

  public Success submitAnxietyDisorder(AnxietyDisorderForm request) {
    PatientEntity patientEntity = patientUtils.checkIfPatientExists(
        request.getPatientId().toString());
    return null;

  }
}
