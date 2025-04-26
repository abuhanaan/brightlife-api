package com.fronteers.services;

import com.fronteers.brightlife.model.Party;
import com.fronteers.brightlife.model.ReleaseReceiveForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PartyEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.ReleaseReceiveFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.PartyRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.ReleaseReceiveRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReleaseReceiveService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final ReleaseReceiveRepository releaseReceiveRepository;
  private final PartyRepository partyRepository;

  public Success submitReleaseReceive(ReleaseReceiveForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForReleaseReceiveUniqueness(patient.getPatientId());
    ReleaseReceiveFormEntity form = ReleaseReceiveFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .isMinor(request.getIsMinor())
        .releaseHealthInfo(request.getReleaseHealthInfo())
        .receiveHealthInfo(request.getReceiveHealthInfo())
        .exchangeHealthInfo(request.getExchangeHealthInfo())
        .disclosurePurposes(request.getDisclosurePurpose() != null ?
            new HashSet<>(request.getDisclosurePurpose()) : new HashSet<>())
        .infoTypeToRelease(request.getInfoTypeToRelease() != null ?
            new HashSet<>(request.getInfoTypeToRelease()) : new HashSet<>())
        .guardianName(request.getGuardianName())
        .relationship(request.getRelationship())
        .date(request.getDate() != null ? Date.valueOf(request.getDate()) : null)
        .releaseReceiveFile(request.getFile())
        .build();
    setParties(form, request.getParty());
    patient.setReleaseReceiveForm(form);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully", "Release Receive Form Submitted");
  }

  public ReleaseReceiveForm getReleaseReceive(Long id) {
    ReleaseReceiveFormEntity entity = checkIfReleaseReceiveExists(id);
    ReleaseReceiveForm dto = new ReleaseReceiveForm();
    dto.setId(entity.getId());
    dto.setPatientId(UUID.fromString(entity.getPatientId()));
    dto.setIsMinor(entity.getIsMinor());
    dto.setReceiveHealthInfo(entity.getReceiveHealthInfo());
    dto.setReceiveHealthInfo(entity.getReceiveHealthInfo());
    dto.setExchangeHealthInfo(entity.getExchangeHealthInfo());
    dto.setParty(entity.getParties() != null ? entity.getParties().stream().map(partyEntity -> {
      Party partyDto = new Party();
      partyDto.setId(partyEntity.getId());
      partyDto.setIsReceive(partyEntity.getIsReceive());
      partyDto.setName(partyEntity.getName());
      partyDto.setPhoneNumber(partyEntity.getPhoneNumber());
      partyDto.setFax(partyEntity.getFax());
      partyDto.setAddress(PatientDtoMapper.mapAddressEntityToAddressDto(partyEntity.getAddress()));
      return partyDto;
    }).toList() : null);
    dto.setDisclosurePurpose(entity.getDisclosurePurposes() != null ?
        entity.getDisclosurePurposes().stream().toList() : null);
    dto.setInfoTypeToRelease(entity.getInfoTypeToRelease() != null ?
        entity.getInfoTypeToRelease().stream().toList() : null);
    dto.setGuardianName(entity.getGuardianName());
    dto.setReleaseReceive(entity.getReleaseReceiveFile());
    dto.setDate(entity.getDate().toLocalDate());
    dto.setFile(entity.getReleaseReceiveFile());
    return dto;
  }

  private void setParties(ReleaseReceiveFormEntity form, List<Party> parties) {
    if (parties != null) {
      List<PartyEntity> partyEntities = new ArrayList<>();
      for (Party partyDto : parties) {
        PartyEntity partyEntity = PartyEntity.builder()
            .name(partyDto.getName())
            .phoneNumber(partyDto.getPhoneNumber())
            .fax(partyDto.getFax())
            .isReceive(partyDto.getIsReceive())
            .address(patientUtils.mapAddressProperties(partyDto.getAddress()))
            .build();
        partyEntity.setReleaseReceiveForm(form);
      }
      form.setParties(partyRepository.saveAll(partyEntities));
    }
  }

  private ReleaseReceiveFormEntity checkIfReleaseReceiveExists(Long id) {
    return releaseReceiveRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(String.format("Release Receive form with id %s does not exist", id)));
  }

  private void checkForReleaseReceiveUniqueness(String patientId) {
    ReleaseReceiveFormEntity entity = releaseReceiveRepository.findOneByPatientId(patientId);
    if (entity != null) {
      throw new ConflictException(
          String.format("Release Receive Has Already Been filled for patient with id %s",
              patientId));
    }
  }
}
