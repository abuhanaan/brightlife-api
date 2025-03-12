package com.fronteers.controllers;

import com.fronteers.brightlife.api.AppointmentApi;
import com.fronteers.brightlife.model.Appointment;
import com.fronteers.brightlife.model.AppointmentSearch;
import com.fronteers.brightlife.model.PaginatedAppointments;
import com.fronteers.brightlife.model.Success;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AppointmentController implements AppointmentApi {

  @Override
  public ResponseEntity<PaginatedAppointments> listAppointments(Integer pageNumber, Integer limit, AppointmentSearch searchCriteria){
    return null;
  }

  @Override
  public ResponseEntity<Appointment> getAppointment(Long appointmentId){
    return null;
  }

  @Override
  public ResponseEntity<Success> submitAppointment(Appointment request) {
    return null;
  }

  @Override
  public ResponseEntity<Success> updateAppointment(Appointment request){
    return null;
  }
}
