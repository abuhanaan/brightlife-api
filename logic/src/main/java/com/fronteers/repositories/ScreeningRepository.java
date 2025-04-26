package com.fronteers.repositories;

import com.fronteers.models.entity.forms.ScreeningFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends JpaRepository<ScreeningFormEntity, Long> {

  Optional<ScreeningFormEntity> findOneById(Long id);

  ScreeningFormEntity findOneByPatientId(String patientId);
}
