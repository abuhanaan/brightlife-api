package com.fronteers.repositories;

import com.fronteers.models.entity.forms.AnxietyDisorderFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxietyDisorderRepository extends JpaRepository<AnxietyDisorderFormEntity, Long> {

  Optional<AnxietyDisorderFormEntity> findOneById(Long id);

  AnxietyDisorderFormEntity findOneByPatientId(String patientId);
}
