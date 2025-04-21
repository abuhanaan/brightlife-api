package com.fronteers.repositories;

import com.fronteers.models.entity.PrimaryCarePhysicianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimaryCarePhysicianRepository extends JpaRepository<PrimaryCarePhysicianEntity, Long> {

  PrimaryCarePhysicianEntity findOneByName(String name);
}
