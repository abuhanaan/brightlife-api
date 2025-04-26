package com.fronteers.services;

import com.fronteers.brightlife.model.InitialEvaluationForm;
import com.fronteers.brightlife.model.Pharmacy;
import com.fronteers.brightlife.model.PrimaryCarePhysician;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.PharmacyEntity;
import com.fronteers.models.entity.PrimaryCarePhysicianEntity;
import com.fronteers.models.entity.forms.InitialEvaluationFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.InitialEvaluationRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.PharmacyRepository;
import com.fronteers.repositories.PrimaryCarePhysicianRepository;
import com.fronteers.utils.PatientUtils;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InitialEvaluationService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final InitialEvaluationRepository initialEvaluationRepository;
  private final PrimaryCarePhysicianRepository primaryCarePhysicianRepository;
  private final PharmacyRepository pharmacyRepository;

  @Transactional
  public Success submitInitialEvaluation(InitialEvaluationForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForInitialEvaluationUniqueness(patient.getPatientId());
    InitialEvaluationFormEntity newForm = new InitialEvaluationFormEntity();
    newForm.setPatient(patient);
    newForm.setPatientId(patient.getPatientId());
    newForm.setPharmacy(mapPharmacyEntity(request.getPharmacy(), newForm));
    newForm.setPrimaryCarePhysician(
        mapPrimaryCarePhysicianEmtity(request.getPrimaryCarePhysician(), newForm));
    newForm.setDate(
        request.getDate() != null ? Timestamp.from(request.getDate().toInstant()) : null);
    newForm.setInitialEvaluationFile(request.getFile());
    patient.setInitialEvaluationForm(newForm);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Initial Evaluation Form Submitted");
  }

  public InitialEvaluationForm getInitialEvaluation(Long id) {
    return PatientDtoMapper.mapInitialEvaluationEntityToDto(checkIfInitialEvaluationFormExists(id));
  }

  private PrimaryCarePhysicianEntity mapPrimaryCarePhysicianEmtity(PrimaryCarePhysician pcpDto,
      InitialEvaluationFormEntity initialEvaluationForm) {
    if (pcpDto == null || pcpDto.getName() == null) {
      throw new BadRequestException("Primary Care Physician details are missing");
    }
    PrimaryCarePhysicianEntity existingPcpEntity = primaryCarePhysicianRepository.findOneByName(
        pcpDto.getName());
    if (existingPcpEntity != null && !existingPcpEntity.getInitialEvaluationForms()
        .contains(initialEvaluationForm)) {
      existingPcpEntity.getInitialEvaluationForms().add(initialEvaluationForm);
      return existingPcpEntity;
    }
    PrimaryCarePhysicianEntity newPcpEntity = PrimaryCarePhysicianEntity.builder()
        .name(pcpDto.getName()).phone(pcpDto.getPhone()).havePcp(pcpDto.getHavePcp())
        .fax(pcpDto.getFax())
        .address(patientUtils.mapAddressProperties(pcpDto.getAddress())).build();
    newPcpEntity.setInitialEvaluationForms(new ArrayList<>());
    newPcpEntity.getInitialEvaluationForms().add(initialEvaluationForm);
    primaryCarePhysicianRepository.save(newPcpEntity);
    return newPcpEntity;
  }

  private PharmacyEntity mapPharmacyEntity(Pharmacy pharmacyDto,
      InitialEvaluationFormEntity initialEvaluationForm) {
    if (pharmacyDto == null || pharmacyDto.getName() == null) {
      throw new BadRequestException("Pharmacy details are missing");
    }
    PharmacyEntity existingPharmacyEntity = pharmacyRepository.findOneByName(pharmacyDto.getName());
    if (existingPharmacyEntity != null) {
      existingPharmacyEntity.getInitialEvaluationForms().add(initialEvaluationForm);
      return existingPharmacyEntity;
    }
    PharmacyEntity pharmacyEntity = PharmacyEntity.builder().name(pharmacyDto.getName())
        .phone(pharmacyDto.getPhone())
        .address(patientUtils.mapAddressProperties(pharmacyDto.getAddress())).build();
    pharmacyEntity.setInitialEvaluationForms(new ArrayList<>());
    pharmacyEntity.getInitialEvaluationForms().add(initialEvaluationForm);
    pharmacyRepository.save(pharmacyEntity);
    return pharmacyEntity;
  }

  private void checkForInitialEvaluationUniqueness(String patientId) {
    InitialEvaluationFormEntity initialEvaluationForm = initialEvaluationRepository.findOneByPatientId(
        patientId);
    if (initialEvaluationForm != null) {
      throw new ConflictException(
          String.format("Initial Evaluation form has already been filled for patient %s",
              patientId));
    }
  }

  private InitialEvaluationFormEntity checkIfInitialEvaluationFormExists(Long id) {
    return initialEvaluationRepository.findOneById(id).orElseThrow(() -> new NotFoundException(
        String.format("Initial Evaluation For with id %s does not exist", id)));
  }
}
