package com.fronteers.controllers;

import com.fronteers.brightlife.api.ReviewApi;
import com.fronteers.brightlife.model.PaginatedReviews;
import com.fronteers.brightlife.model.Review;
import com.fronteers.brightlife.model.ReviewSearch;
import com.fronteers.brightlife.model.Success;
import com.fronteers.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {
  private final ReviewService reviewService;

  @Override
  public ResponseEntity<PaginatedReviews> getDrafts(Integer pageNumber, Integer limit) {
    return ResponseEntity.ok(reviewService.getUnpublished(pageNumber, limit));
  }

  @Override
  public ResponseEntity<PaginatedReviews> getPublished(Integer pageNumber, Integer limit) {
    return ResponseEntity.ok(reviewService.getPublished(pageNumber, limit));
  }

  @Override
  public ResponseEntity<Review> getReview(Long reviewId) {
    return ResponseEntity.ok(reviewService.fetch(reviewId));
  }

  @Override
  public ResponseEntity<Success> publish(Long reviewId) {
    return ResponseEntity.ok(reviewService.publish(reviewId));
  }

  @Override
  public ResponseEntity<Success> unpublish(Long reviewId) {

    return ResponseEntity.ok(reviewService.unpublish(reviewId));
  }

  @Override
//  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Success> submitReview(Review request) {
    return ResponseEntity.ok(reviewService.submit(request));
  }

  @Override
  public ResponseEntity<PaginatedReviews> listReviews(Integer pageNumber, Integer limit,
      ReviewSearch searchCriteria) {
    return ResponseEntity.ok(reviewService.search(pageNumber, limit, searchCriteria));
  }
}
