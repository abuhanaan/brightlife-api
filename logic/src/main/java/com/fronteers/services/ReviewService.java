package com.fronteers.services;

import com.fronteers.brightlife.model.PaginatedReviews;
import com.fronteers.brightlife.model.Review;
import com.fronteers.brightlife.model.ReviewSearch;
import com.fronteers.brightlife.model.ReviewStatusEnum;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.QReviewEntity;
import com.fronteers.models.entity.ReviewEntity;
import com.fronteers.models.mappers.ReviewMapper;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.ReviewRepository;
import com.querydsl.core.BooleanBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final PatientRepository patientRepository;

  public Success submit(Review request) {
    PatientEntity patient = checkIfPatientExist(request.getEmail());
    ReviewEntity reviewEntity = new ReviewEntity();
    reviewEntity.setNickname(request.getNickname());
    reviewEntity.setEmail(request.getEmail());
    reviewEntity.setReferralWish(request.getReferralWish());
    reviewEntity.setRating(request.getRating());
    reviewEntity.setReviewMessage(request.getReviewMessage());
    reviewEntity.setPublished(false);
    reviewEntity.setPatient(patient);
    reviewRepository.save(reviewEntity);
    return new Success(true, "Review Submitted Successfully",
        "Submitted Review will be published soon");
  }

  public Review fetch(Long reviewId) {
    ReviewEntity reviewEntity = checkIfReviewExist(reviewId);
    return ReviewMapper.mapReviewEntityToDto(reviewEntity);
  }

  public Success publish(Long reviewId) {
    ReviewEntity reviewEntity = checkIfReviewExist(reviewId);
    if (reviewEntity.getPublished()) {
      throw new BadRequestException("Review Record Is Already Published");
    }
    reviewEntity.setPublished(true);
    reviewRepository.save(reviewEntity);
    return new Success(true, "Review Updated Successfully",
        "Review Record Has Been Successfully Published");
  }

  public Success unpublish(Long reviewId) {
    ReviewEntity reviewEntity = checkIfReviewExist(reviewId);
    if (!reviewEntity.getPublished()) {
      throw new BadRequestException("Review Record Is Already In Draft");
    }
    reviewEntity.setPublished(false);
    reviewRepository.save(reviewEntity);
    return new Success(true, "Review Updated Successfully",
        "Review Record Has Been Successfully Un-published");
  }

  private ReviewEntity checkIfReviewExist(Long reviewId) {
    ReviewEntity review = reviewRepository.findOneById(reviewId);
    if (review == null) {
      throw new BadRequestException(String.format("Review with id %s does not exist", reviewId));
    }
    return review;
  }

  private PatientEntity checkIfPatientExist(String email) {
    PatientEntity patient = patientRepository.findOneByEmail(email);
    if (patient == null) {
      throw new BadRequestException("The provided email is not connect to a patient record");
    }
    return patient;
  }

  public PaginatedReviews getPublished(Integer pageNumber, Integer limit) {
    return processPublishedAndDraft(pageNumber, limit, true);
  }

  public PaginatedReviews getUnpublished(Integer pageNumber, Integer limit) {
    return processPublishedAndDraft(pageNumber, limit, false);
  }

  private PaginatedReviews processPublishedAndDraft(Integer pageNumber, Integer limit,
      Boolean isPublished) {
    int maxLimit = (limit == null || limit > 100) ? 100 : limit;
    int currentPage =
        (pageNumber == null || pageNumber < 1) ? 0 : pageNumber - 1; // Adjust for 0-based indexing
    int safeLimit = Math.max(1, Math.min(maxLimit, 100));

    Pageable pageable = PageRequest.of(currentPage, safeLimit);

    Page<ReviewEntity> reviewPage = isPublished ? reviewRepository.findByPublishedTrue(pageable)
        : reviewRepository.findByPublishedFalse(pageable);
    List<ReviewEntity> reviewList = reviewPage.getContent();
    List<Review> reviewDtos = ReviewMapper.mapReviewEntitiesToDtos(reviewList);

    PaginatedReviews response = new PaginatedReviews();
    response.setReviews(reviewDtos);
    response.setCurrentPage(reviewPage.getNumber() + 1);
    response.setItemsPerPage(reviewPage.getSize());
    response.setTotalPages(reviewPage.getTotalPages());
    return response;
  }

  public PaginatedReviews search(Integer pageNumber, Integer limit, ReviewSearch searchCriteria) {
    int maxLimit = (limit == null || limit > 100) ? 100 : limit;
    int currentPage =
        (pageNumber == null || pageNumber < 1) ? 0 : pageNumber - 1; // Adjust for 0-based indexing
    int safeLimit = Math.max(1, Math.min(maxLimit, 100));

    BooleanBuilder predicate = new BooleanBuilder();
    QReviewEntity qReview = QReviewEntity.reviewEntity;

    if (searchCriteria.getPatientId() != null) {
      predicate.and(qReview.patient.patientId.eq(searchCriteria.getPatientId().toString()));
    }
    if (searchCriteria.getEmail() != null) {
      predicate.and(qReview.email.eq(searchCriteria.getEmail()));
    }
    if (searchCriteria.getNickName() != null) {
      predicate.and(qReview.nickname.eq(searchCriteria.getNickName()));
    }
    if (searchCriteria.getRating() != null) {
      predicate.and(qReview.rating.eq(searchCriteria.getRating()));
    }
    if (searchCriteria.getStatus() != null) {
      predicate.and(qReview.published.eq(
          searchCriteria.getStatus().equals(ReviewStatusEnum.PUBLISHED)));
    }

    // Fetch all records if no filters are applied
    if (predicate.getValue() == null) {
      log.info("No search filters applied, fetching all records");
      predicate.and(qReview.id.isNotNull());
    }

    Pageable pageable = PageRequest.of(currentPage, safeLimit);
    Page<ReviewEntity> reviewPage = reviewRepository.findAll(predicate, pageable);
    List<ReviewEntity> reviewList = reviewPage.getContent();
    List<Review> reviewDtos = ReviewMapper.mapReviewEntitiesToDtos(reviewList);

    PaginatedReviews response = new PaginatedReviews();
    response.setReviews(reviewDtos);
    response.setCurrentPage(reviewPage.getNumber() + 1);
    response.setItemsPerPage(reviewPage.getSize());
    response.setTotalPages(reviewPage.getTotalPages());
    return response;
  }
}
