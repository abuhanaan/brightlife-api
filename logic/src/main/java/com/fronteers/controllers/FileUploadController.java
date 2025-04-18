package com.fronteers.controllers;

//import com.fronteers.brightlife.api.PassportApi;
//import com.fronteers.brightlife.model.PassportResponse;
//import com.fronteers.brightlife.api.FileApi;

import com.fronteers.brightlife.api.FileApi;
import com.fronteers.brightlife.model.FileUploadResponse;
import com.fronteers.exceptions.ProcessingException;
import com.fronteers.services.FileUploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FileUploadController implements FileApi {

  private final FileUploadService fileUploadService;

  @Override
  public ResponseEntity<FileUploadResponse> uploadFile(String fileType, String owner,
      MultipartFile file) {
    try {
      return ResponseEntity.ok(fileUploadService.upload(fileType, owner, file));
    } catch (IOException e) {
      log.error("File could not be uploaded {}", e.toString());
      throw new ProcessingException("file could not be uploaded " + e);
    }
  }
}
