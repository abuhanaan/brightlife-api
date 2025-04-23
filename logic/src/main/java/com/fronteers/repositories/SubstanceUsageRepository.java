package com.fronteers.repositories;

import com.fronteers.models.entity.SubstanceUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstanceUsageRepository extends JpaRepository<SubstanceUsageEntity, Long> {

}
