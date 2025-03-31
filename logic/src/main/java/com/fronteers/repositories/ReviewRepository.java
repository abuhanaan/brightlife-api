package com.fronteers.repositories;

import com.fronteers.models.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>,
    QuerydslPredicateExecutor<ReviewEntity> {

  ReviewEntity findOneById(Long id);
}
