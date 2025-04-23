package com.fronteers.repositories;

import com.fronteers.models.entity.AlcoholDrugHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlcoholDrugHistoryRepository extends JpaRepository<AlcoholDrugHistoryEntity, Long> {

}
