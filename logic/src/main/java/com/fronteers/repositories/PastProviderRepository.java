package com.fronteers.repositories;

import com.fronteers.models.entity.PastProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PastProviderRepository extends JpaRepository<PastProviderEntity, Long> {


}
