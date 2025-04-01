package com.fronteers.repositories;

import com.fronteers.models.entity.forms.AdhdFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdhdRepository extends JpaRepository<AdhdFormEntity, Long> {

  AdhdFormEntity findOneById(Long id);
}
