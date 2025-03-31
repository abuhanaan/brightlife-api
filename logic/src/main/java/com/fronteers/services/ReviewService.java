package com.fronteers.services;

import com.fronteers.brightlife.model.Review;
import com.fronteers.brightlife.model.Success;
import com.fronteers.exceptions.BadRequestException;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.ReviewEntity;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    return new Success(true, "Review Submitted Successfully", "Submitted Review will be published soon");
  }

  private PatientEntity checkIfPatientExist(String email) {
    PatientEntity patient = patientRepository.findOneByEmail(email);
    if (patient == null){
      throw new BadRequestException("The provided email is not connect to a patient record");
    }
    return patient;
  }
}
