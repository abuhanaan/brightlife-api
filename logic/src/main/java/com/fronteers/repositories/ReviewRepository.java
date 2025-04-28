package com.fronteers.repositories;

import com.fronteers.models.entity.ReviewEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>,
    QuerydslPredicateExecutor<ReviewEntity> {

  ReviewEntity findOneById(Long id);

  Page<ReviewEntity> findByPublishedTrue(Pageable pageable);

  Page<ReviewEntity> findByPublishedFalse(Pageable pageable);

  List<ReviewEntity> findTop10ByOrderByCreatedAtDesc();
}
