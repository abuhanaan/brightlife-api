package com.fronteers.models.mappers;

import com.fronteers.brightlife.model.Review;
import com.fronteers.brightlife.model.ReviewStatusEnum;
import com.fronteers.models.entity.ReviewEntity;
import java.time.ZoneOffset;
import java.util.List;

public class ReviewMapper {

  public static Review mapReviewEntityToDto(ReviewEntity reviewEntity) {
    Review reviewDto = new Review();
    reviewDto.setId(reviewEntity.getId());
    reviewDto.setNickname(reviewEntity.getNickname());
    reviewDto.setEmail(reviewEntity.getEmail());
    reviewDto.setReferralWish(reviewEntity.getReferralWish());
    reviewDto.setRating(reviewEntity.getRating());
    reviewDto.setRating(reviewEntity.getRating());
    reviewDto.setReviewMessage(reviewEntity.getReviewMessage());
    reviewDto.setStatus(
        reviewEntity.getPublished() ? ReviewStatusEnum.PUBLISHED : ReviewStatusEnum.DRAFT);
    reviewDto.setCreatedAt(reviewEntity.getCreatedAt().toInstant().atOffset(ZoneOffset.UTC));
    return reviewDto;
  }

  public static List<Review> mapReviewEntitiesToDtos(List<ReviewEntity> reviewEntities) {
    return reviewEntities.stream().map(ReviewMapper::mapReviewEntityToDto).toList();
  }
}
