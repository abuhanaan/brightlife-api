package com.fronteers.services;

import com.fronteers.brightlife.model.FileUploadResponse;
import com.fronteers.utils.FileUploadUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileUploadService {

  private final FileUploadUtil fileUploadUtil;

  public FileUploadResponse upload(String fileType, String owner, MultipartFile file)
      throws IOException {
    String fileUrl = fileUploadUtil.uploadFile(file, fileType, owner);
    FileUploadResponse response = new FileUploadResponse();
    response.setFileType(fileType);
    response.setOwner(owner);
    response.setFileUrl(fileUrl);
    return response;
  }
}
