package com.fronteers.repositories;

import com.fronteers.models.entity.forms.AdhdFormEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdhdRepository extends JpaRepository<AdhdFormEntity, Long> {

  Optional<AdhdFormEntity> findOneById(Long id);
  AdhdFormEntity findOneByPatientId(String id);
}
