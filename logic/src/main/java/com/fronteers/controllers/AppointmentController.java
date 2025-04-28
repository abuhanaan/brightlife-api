package com.fronteers.controllers;

import com.fronteers.brightlife.api.AppointmentApi;
import com.fronteers.brightlife.model.Appointment;
import com.fronteers.brightlife.model.AppointmentSearch;
import com.fronteers.brightlife.model.PaginatedAppointments;
import com.fronteers.brightlife.model.Success;
import com.fronteers.brightlife.model.TimeSlots;
import com.fronteers.brightlife.model.UpdateAppointmentStatus;
import com.fronteers.services.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Slf4j
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
    log.info("Updating Appointment {} with payload {}", request.getId(), request);
    Success response = appointmentService.updateAppointment(request);
    log.info("Appointment Updated Successfully");
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<Success> changeStatus(Long id, UpdateAppointmentStatus request){
    log.info("Changing Appointment {} to status {}", id, request.getStatus());
    Success response = appointmentService.changeAppointmentStatus(id, request);
    log.info("Appointment status successfully changed to {}", request.getStatus());
    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<TimeSlots> getAppointmentSlots() {
    return ResponseEntity.ok(appointmentService.getAppointmentTimeSlots());
  }
}
