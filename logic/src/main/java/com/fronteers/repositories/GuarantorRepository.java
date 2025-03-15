package com.fronteers.repositories;

import com.fronteers.models.entity.GuarantorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuarantorRepository extends JpaRepository<GuarantorEntity, Long> {

}
