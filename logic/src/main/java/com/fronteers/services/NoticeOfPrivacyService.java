package com.fronteers.services;

import com.fronteers.brightlife.model.NoticeOfPrivacyPracticesForm;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.ConflictException;
import com.fronteers.exceptions.NotFoundException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.forms.NoticeOfPrivacyPracticesFormEntity;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.repositories.NoticeOfPrivacyRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.utils.PatientUtils;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NoticeOfPrivacyService {

  private final PatientRepository patientRepository;
  private final PatientUtils patientUtils;
  private final NoticeOfPrivacyRepository noticeOfPrivacyRepository;


  public Success submitNoticeOfPrivacy(NoticeOfPrivacyPracticesForm request) {
    PatientEntity patient = patientUtils.checkIfPatientExists(request.getPatientId().toString());
    checkForNoticeFormUniqueness(patient.getPatientId());
    NoticeOfPrivacyPracticesFormEntity newNotice = NoticeOfPrivacyPracticesFormEntity.builder()
        .patient(patient)
        .patientId(patient.getPatientId())
        .date(request.getDate() != null ? Timestamp.from(request.getDate().toInstant()) : null)
        .noticeEffectDate(Date.valueOf(request.getNoticeEffectDate()))
        .noticeOfPrivacyPractices(request.getFile())
        .build();
    patient.setNoticeOfPrivacyPracticesForm(newNotice);
    patientRepository.save(patient);
    return new Success(true, "Form Submitted Successfully",
        "Notice of Privacy Practices Form Submitted");
  }

  public NoticeOfPrivacyPracticesForm getNoticeOfPrivacy(Long id) {
    return PatientDtoMapper.mapNoticeOfPrivacyEntityToDto(checkIfNoticeOfPrivacyExists(id));
  }

  private NoticeOfPrivacyPracticesFormEntity checkIfNoticeOfPrivacyExists(Long id) {
    return noticeOfPrivacyRepository.findOneById(id).orElseThrow(() ->
        new NotFoundException(
            String.format("Notice Of Privacy Practices form with id %s does not exist", id)));
  }

  private void checkForNoticeFormUniqueness(String patientId) {
    NoticeOfPrivacyPracticesFormEntity notice = noticeOfPrivacyRepository.findOneByPatientId(
        patientId);
    if (notice != null) {
      throw new ConflictException(String.format(
          "Notice of Privacy Practices form has already been filled for patient %s", patientId));
    }
  }
}
