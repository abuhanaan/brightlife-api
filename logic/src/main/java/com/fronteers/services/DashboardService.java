package com.fronteers.services;

import com.fronteers.brightlife.model.AppointmentStatusEnum;
import com.fronteers.brightlife.model.Dashboard;
import com.fronteers.models.entity.AppointmentEntity;
import com.fronteers.models.entity.PatientEntity;
import com.fronteers.models.entity.ReviewEntity;
import com.fronteers.models.mappers.AppointmentMapper;
import com.fronteers.models.mappers.PatientDtoMapper;
import com.fronteers.models.mappers.ReviewMapper;
import com.fronteers.repositories.AppointmentRepository;
import com.fronteers.repositories.PatientRepository;
import com.fronteers.repositories.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DashboardService {

  private final PatientRepository patientRepository;
  private final AppointmentRepository appointmentRepository;
  private final ReviewRepository reviewRepository;

  public Dashboard getDashboard() {
    Dashboard dashboard = new Dashboard();
    List<PatientEntity> recentTenPatients = patientRepository.findTop10ByOrderByCreatedAtDesc();
    List<ReviewEntity> recentReviews = reviewRepository.findTop10ByOrderByCreatedAtDesc();
    List<AppointmentEntity> recentAppointments = appointmentRepository.findTop10ByStatusOrderByCreatedAtDesc(
        AppointmentStatusEnum.UPCOMING);
    dashboard.setRecentPatients(PatientDtoMapper.mapPatientListToBasicInfoDtos(recentTenPatients));
    dashboard.setRecentReviews(ReviewMapper.mapReviewEntitiesToDtos(recentReviews));
    dashboard.setUpcomingAppointments(
        AppointmentMapper.mapAppointmentEntitiesToDto(recentAppointments));
    dashboard.setAppointmentCount(appointmentRepository.count());
    dashboard.setPatientCount(patientRepository.count());
    dashboard.setReviewCount(reviewRepository.count());
    return dashboard;
  }
}
