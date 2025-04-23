package com.fronteers.repositories;

import com.fronteers.models.entity.RelativesWithMentalIllnessOrSuicideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelativeWithMentalIllnessOrSuicideRepository extends JpaRepository<RelativesWithMentalIllnessOrSuicideEntity, Long> {

}
