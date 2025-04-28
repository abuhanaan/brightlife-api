package com.fronteers.controllers;

import com.fronteers.brightlife.api.DashboardApi;
import com.fronteers.brightlife.model.Dashboard;
import com.fronteers.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class DashboardController implements DashboardApi {

  private final DashboardService dashboardService;

  @Override
  public ResponseEntity<Dashboard> getDashboard() {
    Dashboard response = dashboardService.getDashboard();
    return ResponseEntity.ok(response);
  }
}
