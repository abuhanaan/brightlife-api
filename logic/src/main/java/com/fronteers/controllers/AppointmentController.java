package com.fronteers.controllers;

import com.fronteers.brightlife.api.AppointmentApi;
import com.fronteers.brightlife.model.Appointment;
import com.fronteers.brightlife.model.AppointmentSearch;
import com.fronteers.brightlife.model.PaginatedAppointments;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TimeSlots;
import com.fronteers.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class AppointmentController implements AppointmentApi {

  final AppointmentService appointmentService;

  @Override
  public ResponseEntity<PaginatedAppointments> listAppointments(Integer pageNumber, Integer limit,
      AppointmentSearch searchCriteria) {

    return ResponseEntity.ok(
        appointmentService.searchAppointment(pageNumber, limit, searchCriteria));
  }

  @Override
  public ResponseEntity<Appointment> getAppointment(Long appointmentId) {
    return ResponseEntity.ok(appointmentService.getAppointment(appointmentId));
  }

  @Override
  public ResponseEntity<Success> submitAppointment(Appointment request) {

    return ResponseEntity.ok(appointmentService.submit(request));
  }

  @Override
  public ResponseEntity<Success> updateAppointment(Appointment request) {
    return null;
  }

  @Override
  public ResponseEntity<TimeSlots> getAppointmentSlots() {
    return ResponseEntity.ok(appointmentService.getAppointmentTimeSlots());
  }
}
