package com.fronteers.repositories;

import com.fronteers.models.entity.PharmacyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends JpaRepository<PharmacyEntity, Long> {

  PharmacyEntity findOneByName(String name);
}
