package com.fronteers.controllers;

import com.fronteers.brightlife.api.PassportApi;
import com.fronteers.brightlife.model.PassportResponse;
import com.fronteers.services.PassportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class PassportController implements PassportApi {

  private final PassportService passportService;

  @Override
  public ResponseEntity<PassportResponse> uploadPassport(String name, Integer age,
      MultipartFile file) {
    return ResponseEntity.ok(passportService.upload(name, age, file));
  }
}
