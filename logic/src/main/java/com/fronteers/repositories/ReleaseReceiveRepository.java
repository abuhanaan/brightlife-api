package com.fronteers.repositories;

import com.fronteers.models.entity.forms.ReleaseReceiveFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseReceiveRepository extends JpaRepository<ReleaseReceiveFormEntity, Long> {

  Optional<ReleaseReceiveFormEntity> findOneById(Long id);
  ReleaseReceiveFormEntity findOneByPatientId(String patientId);
}
