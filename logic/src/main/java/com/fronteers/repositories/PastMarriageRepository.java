package com.fronteers.repositories;

import com.fronteers.models.entity.PastMarriageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastMarriageRepository extends JpaRepository<PastMarriageEntity, Long> {

}
