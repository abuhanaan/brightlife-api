package com.fronteers.controllers;

import com.fronteers.brightlife.api.ReviewApi;
import com.fronteers.brightlife.model.PaginatedReviews;
import com.fronteers.brightlife.model.Review;
import com.fronteers.brightlife.model.ReviewSearch;
import com.fronteers.brightlife.model.Success;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ReviewController implements ReviewApi {

  @Override
  public ResponseEntity<PaginatedReviews> getDrafts(Integer pageNumber, Integer limit){
    return null;
  }

  @Override
  public ResponseEntity<PaginatedReviews> getPublished(Integer pageNumber, Integer limit){
    return null;
  }

  @Override
  public ResponseEntity<Review> getReview(Long reviewId){
    return null;
  }

  @Override
  public ResponseEntity<Success> publish(Long reviewId){
    return null;
  }

  @Override
  public ResponseEntity<Success> unpublish(Long reviewId){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitReview(Review request){
    return null;
  }

  @Override
  public ResponseEntity<PaginatedReviews> listReviews(Integer pageNumber, Integer limit, ReviewSearch searchCriteria){
    return null;
  }
}
