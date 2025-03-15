package com.fronteers.repositories;

import com.fronteers.models.entity.ParentGuardianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentGuardianRepository extends JpaRepository<ParentGuardianEntity, Long> {

}
