package com.fronteers.repositories;

import com.fronteers.models.entity.forms.AnxietyDisorderFormEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnxietyDisorderRepository extends JpaRepository<AnxietyDisorderFormEntity, Long> {

  AnxietyDisorderFormEntity findOneById(Long id);
}
