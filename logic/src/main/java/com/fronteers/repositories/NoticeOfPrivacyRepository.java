package com.fronteers.repositories;

import com.fronteers.models.entity.forms.NoticeOfPrivacyPracticesFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeOfPrivacyRepository extends
    JpaRepository<NoticeOfPrivacyPracticesFormEntity, Long> {

  Optional<NoticeOfPrivacyPracticesFormEntity> findOneById(Long id);

  NoticeOfPrivacyPracticesFormEntity findOneByPatientId(String patientId);
}
